/*
 * Copyright (c) 2008-2018 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.core.ldap;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.DefaultBoolean;
import com.haulmont.cuba.core.config.defaults.DefaultInt;
import com.haulmont.cuba.core.config.defaults.DefaultString;

/**
 * Configuration of the embedded LDAP server for integration testing.
 */
@Source(type = SourceType.APP)
public interface EmbeddedLdapConfig extends Config {
    @Property("refapp.core.embeddedLdap.host")
    @DefaultString("0.0.0.0")
    String getBindHost();

    @Property("refapp.core.embeddedLdap.port")
    @DefaultInt(10389)
    int getBindPort();

    @Property("refapp.core.embeddedLdap.startTcpServer")
    @DefaultBoolean(false)
    boolean getStartTcpServer();
}