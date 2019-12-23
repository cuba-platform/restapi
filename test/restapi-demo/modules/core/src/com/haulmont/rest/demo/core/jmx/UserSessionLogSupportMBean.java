/*
 * Copyright (c) 2008-2018 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.core.jmx;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource(description = "Performs operations for integration testing with UserSessionLog")
public interface UserSessionLogSupportMBean {
    @ManagedOperation(description = "Enables / disables UserSessionLog")
    @ManagedOperationParameters(value = {
            @ManagedOperationParameter(name = "enabled", description = "enabled flag")
    })
    void changeUserSessionLogEnabled(boolean enabled);

    @ManagedOperation(description = "Returns the last logged session id")
    String findLastLoggedSessionId();

    @ManagedOperation(description = "Returns the last logged action by session id")
    String getLastLoggedSessionAction(String sessionId);

    @ManagedOperation(description = "Returns the last logged action by session id")
    String cleanupLogEntries();
}