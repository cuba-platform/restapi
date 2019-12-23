/*
 * Copyright (c) 2008-2018 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.core.jmx;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource(description = "Generates random data for entities")
public interface WorkLoadGeneratorMBean {

    @ManagedOperation(description = "Generates users")
    @ManagedOperationParameters({@ManagedOperationParameter(name = "count", description = "")})
    String generateUsers(Integer count);

    @ManagedOperation(description = "Generates colours")
    @ManagedOperationParameters({@ManagedOperationParameter(name = "count", description = "")})
    String generateColours(Integer count);

    @ManagedOperation(description = "Generates repairs for existing cars")
    @ManagedOperationParameters({@ManagedOperationParameter(name = "count", description = "")})
    String generateRepairs(Integer count);

    @ManagedOperation(description = "Generates plants using existing models")
    @ManagedOperationParameters({@ManagedOperationParameter(name = "count", description = "")})
    String generatePlants(Integer count);

    @ManagedOperation(description = "Generates drivers")
    @ManagedOperationParameters({@ManagedOperationParameter(name = "count", description = "")})
    String generateDrivers(Integer count);

    @ManagedOperation(description = "Generates pricing regions")
    @ManagedOperationParameters({@ManagedOperationParameter(name = "count", description = "")})
    String generatePricingRegions(Integer count);

    @ManagedOperation(description = "Generates models")
    @ManagedOperationParameters({@ManagedOperationParameter(name = "count", description = "")})
    String generateModels(Integer count);

    @ManagedOperation(description = "Generates cars using existing colours, models, drivers and sellers")
    @ManagedOperationParameters({@ManagedOperationParameter(name = "count", description = "")})
    String generateCars(Integer count);

    @ManagedOperation(description = "Generates sellers")
    @ManagedOperationParameters({@ManagedOperationParameter(name = "count", description = "")})
    String generateSellers(Integer count);
}
