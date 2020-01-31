/*
 * Copyright (c) 2008-2020 Haulmont.
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
 *
 */

package com.haulmont.addon.restapi.api.service.filter.data;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DefaultValuesInfo {

    private Integer entityCreate;
    private Integer entityRead;
    private Integer entityUpdate;
    private Integer entityDelete;
    private Integer entityAttribute;
    private Integer specific;

    public DefaultValuesInfo() {
    }

    public Integer getEntityCreate() {
        return entityCreate;
    }

    public void setEntityCreate(Integer entityCreate) {
        this.entityCreate = entityCreate;
    }

    public Integer getEntityRead() {
        return entityRead;
    }

    public void setEntityRead(Integer entityRead) {
        this.entityRead = entityRead;
    }

    public Integer getEntityUpdate() {
        return entityUpdate;
    }

    public void setEntityUpdate(Integer entityUpdate) {
        this.entityUpdate = entityUpdate;
    }

    public Integer getEntityDelete() {
        return entityDelete;
    }

    public void setEntityDelete(Integer entityDelete) {
        this.entityDelete = entityDelete;
    }

    public Integer getEntityAttribute() {
        return entityAttribute;
    }

    public void setEntityAttribute(Integer entityAttribute) {
        this.entityAttribute = entityAttribute;
    }

    public Integer getSpecific() {
        return specific;
    }

    public void setSpecific(Integer specific) {
        this.specific = specific;
    }
}
