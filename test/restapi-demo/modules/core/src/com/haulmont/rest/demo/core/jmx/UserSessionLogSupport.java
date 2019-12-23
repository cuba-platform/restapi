/*
 * Copyright (c) 2008-2018 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.core.jmx;

import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.LoadContext.Query;
import com.haulmont.cuba.security.app.Authenticated;
import com.haulmont.cuba.security.app.UserSessionLog;
import com.haulmont.cuba.security.entity.SessionAction;
import com.haulmont.cuba.security.entity.SessionLogEntry;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

@Component("refapp_UserSessionLogSupport")
public class UserSessionLogSupport implements UserSessionLogSupportMBean {
    @Inject
    protected Configuration configuration;
    @Inject
    protected UserSessionLog userSessionLog;
    @Inject
    protected DataManager dataManager;
    @Inject
    protected Persistence persistence;

    @Authenticated
    @Override
    public void changeUserSessionLogEnabled(boolean enabled) {
        GlobalConfig config = configuration.getConfig(GlobalConfig.class);
        config.setUserSessionLogEnabled(enabled);
    }

    @Authenticated
    @Override
    public String findLastLoggedSessionId() {
        List<SessionLogEntry> sessionLogEntries = dataManager.loadList(
                LoadContext.create(SessionLogEntry.class)
                        .setQuery(
                                new Query("select e from sec$SessionLogEntry e order by e.startedTs desc")
                                        .setMaxResults(1)
                        ));

        return sessionLogEntries.stream()
                .findFirst()
                .map(entry -> entry.getSessionId().toString())
                .orElse(null);
    }

    @Authenticated
    @Override
    public String getLastLoggedSessionAction(String sessionId) {
        SessionLogEntry lastSessionLogRecord = userSessionLog.getLastSessionLogRecord(UUID.fromString(sessionId));
        SessionAction lastAction = lastSessionLogRecord.getLastAction();
        return lastAction != null ? lastAction.name() : null;
    }

    @Authenticated
    @Override
    public String cleanupLogEntries() {
        return persistence.callInTransaction(em -> {
            em.setSoftDeletion(false);

            int rows = em.createQuery("delete from sec$SessionLogEntry e")
                    .executeUpdate();

            return String.valueOf(rows);
        });
    }
}