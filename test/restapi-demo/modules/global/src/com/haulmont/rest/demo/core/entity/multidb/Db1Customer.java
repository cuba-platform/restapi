/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.core.entity.multidb;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.BaseDbGeneratedIdEntity;

import javax.persistence.*;
import java.util.List;

@Entity(name = "ref$Db1Customer")
@Table(name = "CUSTOMER")
@NamePattern("%s|name")
public class Db1Customer extends BaseDbGeneratedIdEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ref$Db1Customer")
    @SequenceGenerator(name = "ref$Db1Customer", sequenceName = "customer_sequence", allocationSize = 1)
    @Column(name = "ID")
    protected Long id;

    @OneToMany(mappedBy = "customer")
    protected List<Db1Order> orders;

    @Column(name = "NAME")
    private String name;

    @Override
    protected void setDbGeneratedId(Long id) {
        this.id = id;
    }

    @Override
    protected Long getDbGeneratedId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Db1Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Db1Order> orders) {
        this.orders = orders;
    }
}
