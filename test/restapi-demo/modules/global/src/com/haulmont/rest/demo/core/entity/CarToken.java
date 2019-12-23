/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.core.entity;

import com.haulmont.cuba.core.entity.BaseUuidEntity;

import javax.persistence.*;

@Entity(name = "ref$CarToken")
@Table(name = "REF_CAR_TOKEN")
public class CarToken extends BaseUuidEntity {

    private static final long serialVersionUID = -3020931348846190506L;

    @Column(name = "TOKEN")
    protected String token;

    @JoinColumn(name = "REPAIR_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    protected Repair repair;

    @JoinColumn(name = "GARAGE_TOKEN_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    protected CarGarageToken garageToken;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Repair getRepair() {
        return repair;
    }

    public void setRepair(Repair repair) {
        this.repair = repair;
    }

    public CarGarageToken getGarageToken() {
        return garageToken;
    }

    public void setGarageToken(CarGarageToken garageToken) {
        this.garageToken = garageToken;
    }
}
