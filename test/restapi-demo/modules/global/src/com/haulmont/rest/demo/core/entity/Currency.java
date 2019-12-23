/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.core.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.*;
import com.haulmont.cuba.core.global.UuidProvider;
import com.haulmont.cuba.core.global.validation.groups.RestApiChecks;
import com.haulmont.rest.demo.core.validation.TestCurrencyClassConstraint;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.UUID;

@TestCurrencyClassConstraint(groups = RestApiChecks.class)
@Entity(name = "ref$Currency")
@Table(name = "REF_CURRENCY")
@NamePattern("%s|code")
public class Currency extends BaseStringIdEntity implements Versioned, Creatable, Updatable, SoftDelete, HasUuid {
    private static final long serialVersionUID = 6912352294666689769L;

    @Id
    @Column(name = "CODE")
    @Size(min = 3)
    @NotNull(message = "{msg://com.haulmont.rest.demo.core.entity/Currency.code.empty}")
    protected String code;

    @Column(name = "UUID")
    private UUID uuid = UuidProvider.createUuid();

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

    @Column(name = "NAME")
    @Size(min = 2, message = "Epic fail")
    @NotNull
    protected String name;

    @Override
    public String getId() {
        return code;
    }

    @Override
    public void setId(String id) {
        code = id;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}