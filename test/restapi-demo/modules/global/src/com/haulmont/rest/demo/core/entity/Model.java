/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */
package com.haulmont.rest.demo.core.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.annotation.PostConstruct;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity(name = "ref$Model")
@Table(name = "REF_MODEL")
@NamePattern("%s: %s|manufacturer,name")
public class Model extends StandardEntity {

    private static final long serialVersionUID = -645455241169768567L;

    @Column(name = "NAME")
    private String name;

    @Column(name = "MANUFACTURER")
    private String manufacturer;

    @ManyToMany(mappedBy = "models")
    @OnDeleteInverse(DeletePolicy.DENY)
    protected Set<Plant> plants;

    @Column(name = "NUMBER_OF_SEATS")
    private Integer numberOfSeats;

    @PostConstruct
    public void init() {
        setNumberOfSeats(4);
    }

    @PostConstruct
    public void initName() {
        setName("Default Model name");
    }

    public Integer getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(Integer numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Set<Plant> getPlants() {
        return plants;
    }

    public void setPlants(Set<Plant> plants) {
        this.plants = plants;
    }
}
