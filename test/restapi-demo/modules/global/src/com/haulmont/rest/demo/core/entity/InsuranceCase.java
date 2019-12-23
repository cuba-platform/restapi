/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.core.entity;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;

@Entity(name = "ref$InsuranceCase")
@Table(name = "REF_INSURANCE_CASE")
public class InsuranceCase extends StandardEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CAR_ID")
    private Car car;

    @Column(name = "DESCRIPTION")
    private String description;

    @OneToOne(mappedBy = "insuranceCase", fetch = FetchType.LAZY)
    @OnDeleteInverse(DeletePolicy.UNLINK)
    private Repair repair;

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Repair getRepair() {
        return repair;
    }

    public void setRepair(Repair repair) {
        this.repair = repair;
    }
}
