/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.core.app;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

import java.util.Date;

/**
 *
 */
@ManagedResource(description = "Sample JMX interface for middleware bean")
public interface SampleMBean {

    @ManagedOperation(description = "Execute test script with parameter")
    @ManagedOperationParameters({
            @ManagedOperationParameter(name = "input", description = "Script input parameter")
    })
    String jmxExecuteScript(String input);

    /* Getter method with @ManagedOperation annotation becomes an operation, not attribute getter */
    @ManagedOperation(description = "Count cars")
    String getCarCount();

    @ManagedOperation(description = "Count cars created after parameter")
    String getCarCount(Date creationDate);

    @ManagedOperation(description = "Test nested authentication")
    String testAuthentication();

    String testDataManagerWithoutSecurityContext();

    @ManagedOperation(description = "Enable/disable attribute access")
    String setAttributeAccessEnabledForRest(boolean enabled);

    @ManagedOperation(description = "Enable/disable cuba.entityAttributePermissionChecking")
    String setEntityAttributePermissionChecking(boolean enabled);

    @ManagedOperation(description = "Enable/disable cuba.rest.requiresSecurityToken")
    String setRestRequiresSecurityToken(boolean enabled);

    @ManagedOperation(description = "Import reports")
    String importReports(byte[] data);

    @ManagedOperation(description = "Remove report by id")
    String removeReport(String id);
}
