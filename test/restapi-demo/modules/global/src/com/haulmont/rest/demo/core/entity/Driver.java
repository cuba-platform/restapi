/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */
package com.haulmont.rest.demo.core.entity;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.EmbeddedParameters;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

import static com.haulmont.cuba.core.global.PersistenceHelper.isLoaded;

@Entity(name = "ref$Driver")
@Table(name = "REF_DRIVER")
@NamePattern("%s|name")
public class Driver extends StandardEntity {

    private static final long serialVersionUID = -3978805138573255022L;

    @Column(name = "NAME")
    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @OnDeleteInverse(DeletePolicy.DENY)
    @JoinColumn(name = "CALLSIGN_ID")
    protected DriverCallsign callsign;

    @Embedded
    @EmbeddedParameters(nullAllowed = false)
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DRIVER_GROUP_ID")
    private DriverGroup driverGroup;

    @Column(name = "STATUS")
    private Integer status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PLATFORM_ENTITY_ID")
    protected SamplePlatformEntity platformEntity;

    @OneToMany(mappedBy = "driver")
    @Composition
    private Set<DriverAllocation> allocations;

    @MetaProperty
    public SamplePlatformEntity getPlatformEntityName() {
        if (!isLoaded(this, "platformEntity")
                || !isLoaded(this, "status")) {
            return null;
        }

        return !Objects.equals(status, DriverStatus.ACTIVE.getId()) ? platformEntity : null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DriverCallsign getCallsign() {
        return callsign;
    }

    public void setCallsign(DriverCallsign callsign) {
        this.callsign = callsign;
    }

    public DriverGroup getDriverGroup() {
        return driverGroup;
    }

    public void setDriverGroup(DriverGroup driverGroup) {
        this.driverGroup = driverGroup;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public DriverStatus getStatus() {
        return status == null ? null : DriverStatus.fromId(status);
    }

    public void setStatus(DriverStatus status) {
        this.status = status == null ? null : status.getId();
    }

    public SamplePlatformEntity getPlatformEntity() {
        return platformEntity;
    }

    public void setPlatformEntity(SamplePlatformEntity platformEntity) {
        this.platformEntity = platformEntity;
    }

    public Set<DriverAllocation> getAllocations() {
        return allocations;
    }

    public void setAllocations(Set<DriverAllocation> allocations) {
        this.allocations = allocations;
    }
}
