/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.core.entity;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "ref$PricingRegion")
@Table(name = "REF_PRICING_REGION")
@NamePattern("%s|name")
public class PricingRegion extends StandardEntity {

    @Column(name = "NAME", nullable = false, length = 50)
    protected String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    protected PricingRegion parent;

    @OneToMany(mappedBy = "parent")
    @Composition
    protected Set<PricingRegion> children;

    public Set<PricingRegion> getChildren() {
        return children;
    }

    public void setChildren(Set<PricingRegion> children) {
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PricingRegion getParent() {
        return parent;
    }

    public void setParent(PricingRegion parent) {
        this.parent = parent;
    }
}