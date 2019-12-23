/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */
package com.haulmont.rest.demo.core.entity;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.DeletePolicy;


import javax.persistence.*;
import java.sql.Date;
import java.util.Set;

@Entity(name = "ref$Repair")
@Table(name = "REF_REPAIR")
@NamePattern("%s|description")
public class Repair extends StandardEntity {

    private static final long serialVersionUID = 1785737195382529798L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CAR_ID")
    private Car car;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INSURANCE_CASE_ID")
    private InsuranceCase insuranceCase;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "REPAIR_DATE")
    private Date date;

    @OneToMany(mappedBy = "repair")
    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    private Set<CarToken> carTokens;

//    @Transient
//    @MetaProperty(related = "db1CustomerId")
//    private Db1Customer db1Customer;

    @SystemLevel
    @Column(name = "DB1_CUSTOMER_ID")
    private Long db1CustomerId;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Set<CarToken> getCarTokens() {
        return carTokens;
    }

    public void setCarTokens(Set<CarToken> carTokens) {
        this.carTokens = carTokens;
    }

    public InsuranceCase getInsuranceCase() {
        return insuranceCase;
    }

    public void setInsuranceCase(InsuranceCase insuranceCase) {
        this.insuranceCase = insuranceCase;
    }

//    public Db1Customer getDb1Customer() {
//        return db1Customer;
//    }
//
//    public void setDb1Customer(Db1Customer db1Customer) {
//        this.db1Customer = db1Customer;
//    }

    public Long getDb1CustomerId() {
        return db1CustomerId;
    }

    public void setDb1CustomerId(Long db1CustomerId) {
        this.db1CustomerId = db1CustomerId;
    }
}
