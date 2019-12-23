/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.core.entity;

import com.haulmont.cuba.core.entity.annotation.Extends;

import javax.annotation.PostConstruct;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "ref$ExtModel")
@Extends(Model.class)
public class ExtModel extends Model {

    @Column(name = "LAUNCH_YEAR")
    protected Integer launchYear;

    public Integer getLaunchYear() {
        return launchYear;
    }

    public void setLaunchYear(Integer launchYear) {
        this.launchYear = launchYear;
    }

    @PostConstruct
    @Override
    public void initName() {
        setLaunchYear(2000);
    }
}
