/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */
package com.haulmont.rest.demo.core.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import java.util.List;

@Entity(name = "ref$DriverGroup")
@Table(name = "REF_DRIVER_GROUP")
@NamePattern("%s|name")
public class DriverGroup extends StandardEntity {

    private static final long serialVersionUID = -1289004849888519750L;

    @Column(name = "NAME")
    private String name;

    @Column(name = "IN_USE")
    private Boolean inUse;

    @OneToMany(mappedBy = "driverGroup")
    @OrderBy("name")
    private List<Driver> drivers;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getInUse() {
        return inUse;
    }

    public void setInUse(Boolean inUse) {
        this.inUse = inUse;
    }

    public List<Driver> getDrivers() {
        return drivers;
    }

    public void setDrivers(List<Driver> drivers) {
        this.drivers = drivers;
    }
}
