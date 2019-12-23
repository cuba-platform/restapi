/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.core.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;

@Entity(name = "ref$DriverCallsign")
@Table(name = "REF_DRIVER_CALLSIGN")
@NamePattern("%s|callsign")
public class DriverCallsign extends StandardEntity {

    private static final long serialVersionUID = 4288372660891574760L;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "callsign")
    private Driver driver;

    @Column(name = "CALLSIGN", length = 50, unique = true)
    private String callsign;

    public String getCallsign() {
        return callsign;
    }

    public void setCallsign(String callsign) {
        this.callsign = callsign;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}