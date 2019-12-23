/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.core.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "ref$Plant")
@Table(name = "REF_PLANT")
@NamePattern("%s|name")
public class Plant extends StandardEntity {

    @Column(name = "NAME")
    protected String name;

    @ManyToMany
    @JoinTable(name = "REF_PLANT_MODEL_LINK",
        joinColumns = @JoinColumn(name = "PLANT_ID"),
        inverseJoinColumns = @JoinColumn(name = "MODEL_ID"))
    protected Set<Model> models;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DOC_ID")
    protected Doc doc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Model> getModels() {
        return models;
    }

    public void setModels(Set<Model> models) {
        this.models = models;
    }

    public Doc getDoc() {
        return doc;
    }

    public void setDoc(Doc doc) {
        this.doc = doc;
    }
}
