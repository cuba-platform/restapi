/*
 * Copyright (c) 2008-2017 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.core.entity;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;

@Entity(name = "ref$CarDetailsItem")
@Table(name = "REF_CAR_DETAILS_ITEM")
public class CarDetailsItem extends StandardEntity {

    private static final long serialVersionUID = 8201548746103223718L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CAR_DETAILS_ID")
    protected CarDetails carDetails;

    @Column(name = "INFO")
    protected String info;

    public CarDetails getCarDetails() {
        return carDetails;
    }

    public void setCarDetails(CarDetails carDetails) {
        this.carDetails = carDetails;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
