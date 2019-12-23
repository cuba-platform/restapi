/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.core.entity.identity;

import com.haulmont.cuba.core.entity.BaseIdentityIdEntity;

import javax.persistence.*;

@Entity(name = "ref$IdentityOrderLineTag")
@Table(name = "REF_IK_ORDER_LINE_TAG")
public class IdentityOrderLineTag extends BaseIdentityIdEntity {

    private static final long serialVersionUID = 2233487701425675803L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_LINE_ID")
    private IdentityOrderLine orderLine;

    @Column(name = "NAME")
    private String name;

    public IdentityOrderLine getOrderLine() {
        return orderLine;
    }

    public void setOrderLine(IdentityOrderLine orderLine) {
        this.orderLine = orderLine;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
