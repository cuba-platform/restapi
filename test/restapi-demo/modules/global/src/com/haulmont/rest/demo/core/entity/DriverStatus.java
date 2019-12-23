/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.core.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

public enum DriverStatus implements EnumClass<Integer> {

    ACTIVE(10),
    RETIRED(20);

    private Integer id;

    DriverStatus(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public static DriverStatus fromId(Integer status) {
        for (DriverStatus driverStatus : DriverStatus.values()) {
            if (driverStatus.getId().equals(status))
                return driverStatus;
        }
        return null;
    }
}
