/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.core.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.Extends;

import javax.annotation.PostConstruct;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "ref$CustomExtPlant")
@Extends(ExtPlant.class)
@NamePattern("%s|additionalInfo")
public class CustomExtPlant extends ExtPlant {

    @Column(name = "ADDITIONAL_INFO")
    protected String additionalInfo;

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    @PostConstruct
    protected void init() {
        setAdditionalInfo("Some info");
    }
}