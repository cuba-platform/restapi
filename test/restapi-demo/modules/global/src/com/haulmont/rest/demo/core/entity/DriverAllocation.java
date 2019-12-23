/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */
package com.haulmont.rest.demo.core.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.entity.Creatable;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "ref$DriverAllocation")
@Table(name = "REF_DRIVER_ALLOC")
@NamePattern("#getCaption|driver,car")
public class DriverAllocation extends BaseUuidEntity implements Creatable {

    private static final long serialVersionUID = 8101497971694305079L;

    @Column(name = "CREATE_TS")
    protected Date createTs;

    @Column(name = "CREATED_BY", length = 50)
    protected String createdBy;

    @ManyToOne
    @JoinColumn(name = "DRIVER_ID")
    @OnDeleteInverse(DeletePolicy.DENY)
    private Driver driver;

    @ManyToOne
    @JoinColumn(name = "CAR_ID")
    private Car car;

    @Override
    public Date getCreateTs() {
        return createTs;
    }

    @Override
    public void setCreateTs(Date createTs) {
        this.createTs = createTs;
    }

    @Override
    public String getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public String getCaption() {
        String str = driver == null ? "<no driver>" : driver.getInstanceName();
        str += " : ";
        str += car == null ? "<no car>" : car.getInstanceName();
        return str;
    }
}
