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

public class EffectiveRoleInfo {

    private ExplicitPermissionsInfo explicitPermissions = new ExplicitPermissionsInfo();

    private DefaultValuesInfo defaultValues = new DefaultValuesInfo();

    private String undefinedPermissionPolicy = "DENY";

    public EffectiveRoleInfo() {
    }

    public ExplicitPermissionsInfo getExplicitPermissions() {
        return explicitPermissions;
    }

    public void setExplicitPermissions(ExplicitPermissionsInfo explicitPermissions) {
        this.explicitPermissions = explicitPermissions;
    }

    public DefaultValuesInfo getDefaultValues() {
        return defaultValues;
    }

    public void setDefaultValues(DefaultValuesInfo defaultValues) {
        this.defaultValues = defaultValues;
    }

    public String getUndefinedPermissionPolicy() {
        return undefinedPermissionPolicy;
    }

    public void setUndefinedPermissionPolicy(String undefinedPermissionPolicy) {
        this.undefinedPermissionPolicy = undefinedPermissionPolicy;
    }
}