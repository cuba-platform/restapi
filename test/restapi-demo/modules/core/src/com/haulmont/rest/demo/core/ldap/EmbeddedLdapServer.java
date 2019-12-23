/*
 * Copyright (c) 2008-2018 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */
package com.haulmont.rest.demo.core.ldap;

import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.sys.events.AppContextStartedEvent;
import com.haulmont.cuba.core.sys.events.AppContextStoppedEvent;
import org.apache.commons.io.FileUtils;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.server.core.DefaultDirectoryService;
import org.apache.directory.server.core.api.DirectoryService;
import org.apache.directory.server.core.api.partition.Partition;
import org.apache.directory.server.core.factory.DefaultDirectoryServiceFactory;
import org.apache.directory.server.core.factory.JdbmPartitionFactory;
import org.apache.directory.server.core.factory.PartitionFactory;
import org.apache.directory.server.ldap.LdapServer;
import org.apache.directory.server.protocol.shared.transport.TcpTransport;
import org.slf4j.Logger;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.File;

/**
 * Embedded LDAP server.
 * Should be enabled with application property: <b>refapp.core.embeddedLdap.startTcpServer</b>
 * <br>
 * <b>URL</b>: ldap://localhost:10389
 * <br>
 * Users:
 * <ol>
 * <li>admin / admin</li>
 * <li>refapp / refapp</li>
 * <li>user / password</li>
 * </ol>
 */
@Component("refapp_EmbeddedADS")
public class EmbeddedLdapServer {
    private static final String ROOT_PARTITION_NAME = "refapp";

    private static final String ROOT_DN = "dc=example,dc=com";

    @Inject
    private Logger log;

    @Inject
    protected EmbeddedLdapConfig config;

    @Inject
    protected GlobalConfig globalConfig;

    protected DirectoryService service;
    protected LdapServer ldapService;
    protected PartitionFactory partitionFactory;

    protected Partition addPartition(String partitionId, String partitionDn) throws Exception {
        Partition partition = partitionFactory.createPartition(service.getSchemaManager(),
                service.getDnFactory(), partitionId,
                partitionDn, 100,
                service.getInstanceLayout().getPartitionsDirectory());

        service.addPartition(partition);

        return partition;
    }

    protected void addRootEntry(Partition partition) throws Exception {
        try {
            service.getAdminSession().lookup(partition.getSuffixDn());
        } catch (Exception e) {
            Dn dnBar = new Dn(ROOT_DN);
            Entry entryBar = service.newEntry(dnBar);
            entryBar.add("objectClass", "dcObject", "organization");
            entryBar.add("o", ROOT_PARTITION_NAME);
            entryBar.add("dc", ROOT_PARTITION_NAME);

            service.getAdminSession().add(entryBar);
        }
    }

    // Add a user to the server
    protected void addUser(String username, String password) throws Exception {
        Dn dn = new Dn("cn=" + username + "," + ROOT_DN);
        if (!service.getAdminSession().exists(dn)) {
            Entry e = service.newEntry(dn);
            e.add("objectClass", "person", "inetOrgPerson", "organizationalPerson");
            e.add("uid", username);
            e.add("displayName", username);
            e.add("userPassword", password.getBytes());
            e.add("sn", username);
            e.add("cn", username);
            e.add("givenName", username);

            service.getAdminSession().add(e);

            log.info("Registered embedded LDAP user: {} password: {}", username, password);
        }
    }


    protected void init() throws Exception {
        // Initialize the LDAP service
        partitionFactory = new JdbmPartitionFactory();
        service = new DefaultDirectoryService();
        DefaultDirectoryServiceFactory serviceFactory = new DefaultDirectoryServiceFactory(service, partitionFactory);

        File dataDir = new File(globalConfig.getTempDir(), "ldap");
        if (dataDir.exists()) {
            FileUtils.deleteDirectory(dataDir);
        }
        //noinspection ResultOfMethodCallIgnored
        dataDir.mkdirs();
        System.setProperty("workingDirectory", dataDir.getCanonicalPath());

        // Disable the ChangeLog system
        service.getChangeLog().setEnabled(false);
        service.setDenormalizeOpAttrsEnabled(true);

        serviceFactory.init("ldap");

        Partition refAppPartition = addPartition(ROOT_PARTITION_NAME, ROOT_DN);
        addRootEntry(refAppPartition);

        Dn peopleDn = new Dn(ROOT_DN);
        if (!service.getAdminSession().exists(peopleDn)) {
            Entry e = service.newEntry(peopleDn);
            e.add("objectClass", "organizationalUnit");
            e.add("ou", "system");
            service.getAdminSession().add(e);
        }

        // Add sample users
        addUser("refapp", "refapp");
        addUser("admin", "admin");
        addUser("user", "password");
    }

    @EventListener(AppContextStartedEvent.class)
    protected void run() {
        if (config.getStartTcpServer()) {
            try {
                init();

                log.info("Starting TCP LDAP server on {}:{}", config.getBindHost(), config.getBindPort());

                ldapService = new LdapServer();
                ldapService.setTransports(new TcpTransport(config.getBindHost(), config.getBindPort()));
                ldapService.setDirectoryService(service);

                ldapService.start();
            } catch (Exception e) {
                log.error("Error while start TCP LDAP server", e);
                throw new RuntimeException("Error while start TCP LDAP server", e);
            }
        }
    }

    @EventListener(AppContextStoppedEvent.class)
    protected void stop() throws Exception {
        if (ldapService != null) {
            ldapService.stop();
            service.shutdown();
            ldapService = null;
            service = null;
        }
    }
}