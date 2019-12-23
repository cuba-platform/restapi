/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.core.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.Extends;

import javax.persistence.*;

@Entity(name = "ref$ExtDriver")
@NamePattern("%s:%s|name,info")
@Extends(Driver.class)
public class ExtDriver extends Driver {

    private static final long serialVersionUID = 5271478633053259678L;

    @Column(name = "INFO", length = 50)
    protected String info;

    // the field is so large that we don't want it to be loaded automatically
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "NOTES")
    @Lob
    protected String notes;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}