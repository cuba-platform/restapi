/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.core.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;

/**
 * Test entity for com.haulmont.refapp.core.OptionalFieldOrmMappingTest <br/>
 * Test NamePattern for com.haulmont.refapp.core.MinimalViewReferencesTest
 *
 */
@Entity(name = "ref$DriverLicense")
@Table(name = "REF_DRIVER_LICENSE")
@NamePattern("%s %s|driver,car")
public class DriverLicense extends StandardEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "DRIVER_ID")
    protected Driver driver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CAR_ID", nullable = false)
    protected Car car;

    @Column(name = "PRIORITY", nullable = false)
    protected Integer priority;

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}