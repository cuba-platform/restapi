/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.core.entity.identity;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.cuba.core.entity.BaseIdentityIdEntity;

import javax.persistence.*;
import java.util.List;

@Entity(name = "ref$IdentityOrderLine")
@Table(name = "REF_IK_ORDER_LINE")
public class IdentityOrderLine extends BaseIdentityIdEntity {

    private static final long serialVersionUID = -7952999998304700636L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    private IdentityOrder order;

    @Column(name = "PRODUCT")
    private String product;

    @OneToMany(mappedBy = "orderLine")
    @OrderBy("id")
    @Composition
    private List<IdentityOrderLineTag> orderLineTags;

    public IdentityOrder getOrder() {
        return order;
    }

    public void setOrder(IdentityOrder order) {
        this.order = order;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public List<IdentityOrderLineTag> getOrderLineTags() {
        return orderLineTags;
    }

    public void setOrderLineTags(List<IdentityOrderLineTag> orderLineTags) {
        this.orderLineTags = orderLineTags;
    }
}
