/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */
package com.haulmont.rest.demo.core.entity;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.*;
import com.haulmont.cuba.core.entity.annotation.*;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity(name = "ref_Car")
@Table(name = "REF_CAR")
@TrackEditScreenHistory
@Listeners("refapp_CarDetachListener")
@NamePattern("%s|vin")
public class Car extends CategorizedEntity implements Versioned, Creatable, Updatable, SoftDelete {
    private static final long serialVersionUID = -7377186515184761381L;

    @Version
    @Column(name = "VERSION")
    protected Integer version;

    @Column(name = "CREATE_TS")
    protected Date createTs;

    @Column(name = "CREATED_BY", length = 50)
    protected String createdBy;

    @Column(name = "UPDATE_TS")
    protected Date updateTs;

    @Column(name = "UPDATED_BY", length = 50)
    protected String updatedBy;

    @Column(name = "DELETE_TS")
    protected Date deleteTs;

    @Column(name = "DELETED_BY", length = 50)
    protected String deletedBy;

    @Column(name = "VIN")
    private String vin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COLOUR_ID")
    private Colour colour;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MODEL_ID")
    @Lookup(type = LookupType.SCREEN)
    private Model model;

    @OneToMany(mappedBy = "car")
    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    private Set<DriverAllocation> driverAllocations;

    @OneToMany(mappedBy = "car")
    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    private Set<InsuranceCase> insuranceCases;

    @OneToMany(mappedBy = "car")
    @OrderBy("createTs")
    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    private List<Repair> repairs;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "car")
    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    protected CarDetails details;


    @OneToOne(fetch = FetchType.LAZY)
    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @JoinColumn(name = "CAR_DOCUMENTATION_ID")
    protected CarDocumentation carDocumentation;

    // for tests only
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TOKEN_ID")
    @OnDelete(DeletePolicy.CASCADE)
    protected CarToken token;

    // Test meta property enhancing in persistent entity

    @MetaProperty
    @Transient
    protected Integer repairCost;

    @Transient
    protected Integer repairPrice;

    @Transient
    protected Integer repairCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SELLER_ID")
    protected Seller seller;

    @Valid
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CURRENCY_CODE")
    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open"})
    protected Currency currency;

    // This attribute is set by CarDetachListener
    @Transient
    @MetaProperty
    protected String currencyCode;

    @Override
    public Integer getVersion() {
        return version;
    }

    @Override
    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public Date getCreateTs() {
        return createTs;
    }

    @Override
    public void setCreateTs(Date createTs) {
        this.createTs = createTs;
    }

    @Override
    public String getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public Date getUpdateTs() {
        return updateTs;
    }

    @Override
    public void setUpdateTs(Date updateTs) {
        this.updateTs = updateTs;
    }

    @Override
    public String getUpdatedBy() {
        return updatedBy;
    }

    @Override
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public Boolean isDeleted() {
        return deleteTs != null;
    }

    @Override
    public Date getDeleteTs() {
        return deleteTs;
    }

    @Override
    public void setDeleteTs(Date deleteTs) {
        this.deleteTs = deleteTs;
    }

    @Override
    public String getDeletedBy() {
        return deletedBy;
    }

    @Override
    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public Colour getColour() {
        return colour;
    }

    public void setColour(Colour colour) {
        this.colour = colour;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Set<DriverAllocation> getDriverAllocations() {
        return driverAllocations;
    }

    public void setDriverAllocations(Set<DriverAllocation> driverAllocations) {
        this.driverAllocations = driverAllocations;
    }

    public List<Repair> getRepairs() {
        return repairs;
    }

    public void setRepairs(List<Repair> repairs) {
        this.repairs = repairs;
    }

    public CarDetails getDetails() {
        return details;
    }

    public void setDetails(CarDetails details) {
        this.details = details;
    }

    public CarToken getToken() {
        return token;
    }

    public void setToken(CarToken token) {
        this.token = token;
    }

    // Test meta property enhancing in persistent entity

    public Integer getRepairCost() {
        return repairCost;
    }

    public void setRepairCost(Integer repairCost) {
        this.repairCost = repairCost;
    }

    public Integer getRepairPrice() {
        return repairPrice;
    }

    public void setRepairPrice(Integer repairPrice) {
        this.repairPrice = repairPrice;
    }

    @MetaProperty
    public Integer getRepairCount() {
        return repairCount;
    }

    @MetaProperty
    public void setRepairCount(Integer repairCount) {
        this.repairCount = repairCount;
    }

    // References with non-UUID primary keys

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Set<InsuranceCase> getInsuranceCases() {
        return insuranceCases;
    }

    public void setInsuranceCases(Set<InsuranceCase> insuranceCases) {
        this.insuranceCases = insuranceCases;
    }

    public CarDocumentation getCarDocumentation() {
        return carDocumentation;
    }

    public void setCarDocumentation(CarDocumentation carDocumentation) {
        this.carDocumentation = carDocumentation;
    }
}