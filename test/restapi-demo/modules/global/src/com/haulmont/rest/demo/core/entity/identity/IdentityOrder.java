/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.core.entity.identity;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.cuba.core.entity.BaseIdentityIdEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity(name = "ref$IdentityOrder")
@Table(name = "REF_IK_ORDER")
public class IdentityOrder extends BaseIdentityIdEntity {

    @Column(name = "ORDER_DATE")
    private Date orderDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_ID")
    private IdentityCustomer customer;

    @OneToMany(mappedBy = "order")
    @OrderBy("id")
    @Composition
    private List<IdentityOrderLine> orderLines;

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public IdentityCustomer getCustomer() {
        return customer;
    }

    public void setCustomer(IdentityCustomer customer) {
        this.customer = customer;
    }

    public List<IdentityOrderLine> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(List<IdentityOrderLine> orderLines) {
        this.orderLines = orderLines;
    }
}
