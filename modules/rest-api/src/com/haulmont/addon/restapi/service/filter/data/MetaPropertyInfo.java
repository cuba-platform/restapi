/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haulmont.addon.restapi.service.filter.data;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Metadata;

public class MetaPropertyInfo {
    public String name;
    public MetaProperty.Type attributeType;
    public String type;
    public Range.Cardinality cardinality;
    public boolean mandatory;
    public boolean readOnly;
    boolean isPersistent;
    public String description;
    @Deprecated
    boolean isTransient;

    public MetaPropertyInfo(MetaProperty metaProperty) {
        Messages messages = AppBeans.get(Messages.class);
        Metadata metadata = AppBeans.get(Metadata.class);
        this.name = metaProperty.getName();
        this.attributeType = metaProperty.getType();
        switch (attributeType) {
            case DATATYPE:
                Datatype<Object> datatype = metaProperty.getRange().asDatatype();
                try {
                    this.type = metadata.getDatatypes().getId(datatype);
                } catch (Exception e) {
                    this.type = datatype.toString();
                }
                break;
            case ASSOCIATION:
            case COMPOSITION:
                this.type = metaProperty.getRange().asClass().getName();
                break;
            case ENUM:
                this.type = metaProperty.getRange().asEnumeration().getJavaClass().getName();
                break;
        }
        this.cardinality = metaProperty.getRange().getCardinality();
        this.readOnly = metaProperty.isReadOnly();
        this.mandatory = metaProperty.isMandatory();
        this.isPersistent = metadata.getTools().isPersistent(metaProperty);
        this.isTransient = metadata.getTools().isNotPersistent(metaProperty);
        this.description = messages.getTools().getPropertyCaption(metaProperty);
    }

    public boolean getPersistent() {
        return isPersistent;
    }

    @Deprecated
    public boolean getTransient() {
        return isTransient;
    }
}
