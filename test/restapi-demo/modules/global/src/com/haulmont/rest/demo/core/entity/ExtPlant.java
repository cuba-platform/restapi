/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.core.entity;

import com.haulmont.cuba.core.entity.annotation.Extends;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "ref$ExtPlant")
@Extends(Plant.class)
public class ExtPlant extends Plant {

    @Column(name = "NUMBER_IN_1C")
    private String numberIn1C;

    public String getNumberIn1C() {
        return numberIn1C;
    }

    public void setNumberIn1C(String numberIn1C) {
        this.numberIn1C = numberIn1C;
    }
}