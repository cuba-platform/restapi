/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */
package com.haulmont.rest.demo.core.entity;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity(name = "ref$Doc")
@Table(name = "REF_DOC")
@DiscriminatorValue("100")
@PrimaryKeyJoinColumn(name = "CARD_ID", referencedColumnName = "ID")
@NamePattern("%s|description")
public class Doc extends Card {

    private static final long serialVersionUID = -3158618141974583321L;

    @Column(name = "DOC_NUMBER", length = 50)
    protected String number = "";

    @Column(name = "AMOUNT")
    protected BigDecimal amount;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "doc")
    @Composition
    protected List<Plant> plants;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public List<Plant> getPlants() {
        return plants;
    }

    public void setPlants(List<Plant> plants) {
        this.plants = plants;
    }
}
