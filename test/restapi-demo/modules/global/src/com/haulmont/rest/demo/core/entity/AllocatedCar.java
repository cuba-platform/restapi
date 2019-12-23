/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */
package com.haulmont.rest.demo.core.entity;

import com.haulmont.cuba.core.entity.BaseUuidEntity;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "ref$AllocatedCar")
@Table(name = "REF_ALLOCATED_CAR")
public class AllocatedCar extends BaseUuidEntity {

    private static final long serialVersionUID = -1412897491565744100L;

    @Column(name = "VIN")
    private String vin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MODEL_ID")
    private Model model;

    @Column(name = "COLOUR_NAME")
    private String colourName;

    @Column(name = "DRIVER_NAME")
    private String driverName;

    @Column(name = "ALLOC_TS")
    private Date allocTs;

    public String getVin() {
        return vin;
    }

    public Model getModel() {
        return model;
    }

    public String getColourName() {
        return colourName;
    }

    public String getDriverName() {
        return driverName;
    }

    public Date getAllocTs() {
        return allocTs;
    }
}
