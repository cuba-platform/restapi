/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.core.entity;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import java.util.List;

@Entity(name = "ref$CarDetails")
@Table(name = "REF_CAR_DETAILS")
@NamePattern("%s|details")
public class CarDetails extends StandardEntity {

    private static final long serialVersionUID = 8201548746103223718L;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CAR_ID")
    protected Car car;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "carDetails")
    @Composition
    protected List<CarDetailsItem> items;

    @Column(name = "DETAILS")
    protected String details;

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public List<CarDetailsItem> getItems() {
        return items;
    }

    public void setItems(List<CarDetailsItem> items) {
        this.items = items;
    }
}
