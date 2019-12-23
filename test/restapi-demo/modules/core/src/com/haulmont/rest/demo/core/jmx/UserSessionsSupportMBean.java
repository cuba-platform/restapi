/*
 * Copyright (c) 2008-2018 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.core.jmx;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource(description = "Performs operations for integration testing")
public interface UserSessionsSupportMBean {

    @ManagedOperation(description = "Terminates session by user logins")
    @ManagedOperationParameters(value = {
            @ManagedOperationParameter(name = "logins", description = "list of user logins")
    })
    String killSessions(String[] logins);

    @ManagedOperation(description = "Terminates all non-system sessions")
    String killAllSessions();
}