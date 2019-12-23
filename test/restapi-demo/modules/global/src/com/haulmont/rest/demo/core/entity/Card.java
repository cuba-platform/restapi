/*
 * Copyright (c) 2008-2018 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */
package com.haulmont.rest.demo.core.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.*;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.DeletePolicy;
import com.haulmont.cuba.security.entity.User;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.*;

@Entity(name = "ref$Card")
@Table(name = "REF_CARD")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "CARD_TYPE", discriminatorType = DiscriminatorType.INTEGER)
@DiscriminatorValue("0")
@NamePattern("%s|description")
@SystemLevel
public class Card extends CategorizedEntity implements Creatable, Updatable, SoftDelete, Versioned {

    private static final long serialVersionUID = -6180254942462308853L;

    @Version
    @Column(name = "VERSION")
    protected Integer version;

    @Column(name = "CREATE_TS")
    protected Date createTs;

    @Column(name = "CREATED_BY", length = 50)
    protected String createdBy;

    @Column(name = "UPDATE_TS")
    protected Date updateTs;

    @Column(name = "UPDATED_BY", length = 50)
    protected String updatedBy;

    @Column(name = "DELETE_TS")
    protected Date deleteTs;

    @Column(name = "DELETED_BY", length = 50)
    protected String deletedBy;

    @Column(name = "STATE", length = 255)
    protected String state;

    @Column(name = "DESCRIPTION", length = 1000)
    protected String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATOR_ID")
    protected User creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SUBSTITUTED_CREATOR_ID")
    protected User substitutedCreator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_CARD_ID")
    @OnDeleteInverse(DeletePolicy.DENY)
    protected Card parentCard;

    @OneToMany(mappedBy = "parentCard")
    protected Set<Card> subCards;

   @Column(name = "HAS_ATTACHMENTS")
    protected Boolean hasAttachments = false;

    @Column(name = "HAS_ATTRIBUTES")
    protected Boolean hasAttributes = false;

    @Column(name = "PARENT_CARD_ACCESS")
    protected Boolean parentCardAccess = false;

    @Override
    public Integer getVersion() {
        return version;
    }

    @Override
    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public Date getCreateTs() {
        return createTs;
    }

    @Override
    public void setCreateTs(Date createTs) {
        this.createTs = createTs;
    }

    @Override
    public String getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public Date getUpdateTs() {
        return updateTs;
    }

    @Override
    public void setUpdateTs(Date updateTs) {
        this.updateTs = updateTs;
    }

    @Override
    public String getUpdatedBy() {
        return updatedBy;
    }

    @Override
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public Boolean isDeleted() {
        return deleteTs != null;
    }

    @Override
    public Date getDeleteTs() {
        return deleteTs;
    }

    @Override
    public void setDeleteTs(Date deleteTs) {
        this.deleteTs = deleteTs;
    }

    @Override
    public String getDeletedBy() {
        return deletedBy;
    }

    @Override
    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Card getParentCard() {
        return parentCard;
    }

    public void setParentCard(Card parentCard) {
        this.parentCard = parentCard;
    }

    public Set<Card> getSubCards() {
        return subCards;
    }

    public void setSubCards(Set<Card> subCards) {
        this.subCards = subCards;
    }

    public User getSubstitutedCreator() {
        return substitutedCreator;
    }

    public void setSubstitutedCreator(User substitutedCreator) {
        this.substitutedCreator = substitutedCreator;
    }

    public Boolean getHasAttachments() {
        return hasAttachments;
    }

    public void setHasAttachments(Boolean hasAttachments) {
        this.hasAttachments = hasAttachments;
    }

    public Boolean getHasAttributes() {
        return hasAttributes;
    }

    public void setHasAttributes(Boolean hasAttributes) {
        this.hasAttributes = hasAttributes;
    }

    public Boolean getParentCardAccess() {
        return parentCardAccess;
    }

    public void setParentCardAccess(Boolean parentCardAccess) {
        this.parentCardAccess = parentCardAccess;
    }
}
