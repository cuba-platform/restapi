/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.core.entity;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Table(name = "DEBT_DEBTOR")
@Entity(name = "debt$Debtor")
@NamePattern("%s|title")
public class Debtor extends StandardEntity {
    @Column(name = "TITLE", nullable = false, length = 50)
    protected String title;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "debtor")
    protected Set<Case> cases;

    private static final long serialVersionUID = -1813396990976305229L;

    public void setCases(Set<Case> cases) {
        this.cases = cases;
    }

    public Set<Case> getCases() {
        return cases;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}