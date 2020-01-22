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

package com.haulmont.rest.demo.core.roles;

import com.haulmont.cuba.security.app.role.AnnotatedRoleDefinition;
import com.haulmont.cuba.security.app.role.annotation.DefaultEntityAttributeAccess;
import com.haulmont.cuba.security.app.role.annotation.EntityAccess;
import com.haulmont.cuba.security.app.role.annotation.Role;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.role.EntityAttributePermissionsContainer;
import com.haulmont.cuba.security.role.EntityPermissionsContainer;

/**
 * The role should be assigned to the anonymous user to run functional tests.
 */
@Role(name = RestTestAnonymousRole.NAME, securityScope = "REST")
public class RestTestAnonymousRole extends AnnotatedRoleDefinition {
    public static final String NAME = "rest-test-anonymous";


    @Override
    @EntityAccess(target = User.class, allow = {EntityOp.READ})
    public EntityPermissionsContainer entityPermissions() {
        return super.entityPermissions();
    }

    @Override
    @DefaultEntityAttributeAccess(value = EntityAttrAccess.VIEW)
    public EntityAttributePermissionsContainer entityAttributePermissions() {
        return super.entityAttributePermissions();
    }
}
