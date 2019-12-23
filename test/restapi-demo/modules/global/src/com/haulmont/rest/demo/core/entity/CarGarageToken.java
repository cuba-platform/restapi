/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.core.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.BaseUuidEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity(name = "ref$CarGarageToken")
@Table(name = "REF_CAR_GARAGE_TOKEN")
@NamePattern("%s|title")
public class CarGarageToken extends BaseUuidEntity {

    private static final long serialVersionUID = -3020931348846190506L;

    @Column(name = "TITLE", length = 50, nullable = false)
    protected String title;

    @Column(name = "TOKEN", length = 20, nullable = false)
    protected String token;

    @Column(name = "DESCRIPTION", length = 200)
    protected String description;

    @Column(name = "LAST_USAGE")
    protected Date lastUsage;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getLastUsage() {
        return lastUsage;
    }

    public void setLastUsage(Date lastUsage) {
        this.lastUsage = lastUsage;
    }
}