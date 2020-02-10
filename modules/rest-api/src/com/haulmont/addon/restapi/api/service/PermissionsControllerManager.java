/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.addon.restapi.api.service;

import com.haulmont.addon.restapi.api.controllers.PermissionsController;
import com.haulmont.addon.restapi.api.exception.RestAPIException;
import com.haulmont.addon.restapi.api.service.filter.data.*;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.role.RoleDefinition;
import com.haulmont.cuba.security.role.RolesService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Class is used for getting current user permissions for the REST API. It contains a business logic required by the
 * {@link PermissionsController}
 */
@Component("restapi_PermissionsControllerManager")
public class PermissionsControllerManager {

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    private RolesService rolesService;

    public Collection<PermissionInfo> getPermissionInfos() {
        Collection<PermissionInfo> result = new ArrayList<>();
        for (PermissionType permissionType : PermissionType.values()) {
            Map<String, Integer> permissionsMap = userSessionSource.getUserSession().getPermissionsByType(permissionType);
            for (Map.Entry<String, Integer> entry : permissionsMap.entrySet()) {
                String target = entry.getKey();
                Integer value = entry.getValue();
                PermissionInfo permissionInfo = new PermissionInfo(permissionType.name(),
                        target,
                        getPermissionValueStr(permissionType, value),
                        value);
                result.add(permissionInfo);
            }
        }
        return result;
    }

    protected String getPermissionValueStr(PermissionType permissionType, int value) {
        switch (permissionType) {
            case SCREEN:
            case SPECIFIC:
            case ENTITY_OP:
                return value == 1 ? "ALLOW" : "DENY";
            case ENTITY_ATTR:
                switch (value) {
                    case 0:
                        return "DENY";
                    case 1:
                        return "VIEW";
                    case 2:
                        return "MODIFY";
                }
            case UI:
                switch (value) {
                    case 0:
                        return "HIDE";
                    case 1:
                        return "READ_ONLY";
                    case 2:
                        return "SHOW";
                }
        }
        throw new RestAPIException("Cannot evaluate permission value", "", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public EffectiveRoleInfo getEffectiveRole(EffectiveRoleRequestParams params) {
        RoleDefinition joinedRole = userSessionSource.getUserSession().getJoinedRole();
        return createEffectiveRoleInfo(joinedRole, params);
    }

    public EffectiveRoleInfo createEffectiveRoleInfo(RoleDefinition role, EffectiveRoleRequestParams params) {
        EffectiveRoleInfo roleInfo = new EffectiveRoleInfo();
        ExplicitPermissionsInfo explicitPermissionsInfo = roleInfo.getExplicitPermissions();
        if (params.isEntities()) {
            explicitPermissionsInfo.setEntities(new ArrayList<>());
            role.entityPermissions().getExplicitPermissions().forEach((key, value) ->
                    explicitPermissionsInfo.getEntities().add(new ShortPermissionInfo(key, value)));
        }
        if (params.isEntityAttributes()) {
            explicitPermissionsInfo.setEntityAttributes(new ArrayList<>());
            role.entityAttributePermissions().getExplicitPermissions().forEach((key, value) ->
                    explicitPermissionsInfo.getEntityAttributes().add(new ShortPermissionInfo(key, value)));
        }
        if (params.isSpecific()) {
            explicitPermissionsInfo.setSpecific(new ArrayList<>());
            role.specificPermissions().getExplicitPermissions().forEach((key, value) ->
                    explicitPermissionsInfo.getSpecific().add(new ShortPermissionInfo(key, value)));
        }

        roleInfo.setUndefinedPermissionPolicy(rolesService.getPermissionUndefinedAccessPolicy().name());

        return roleInfo;
    }

    public static class EffectiveRoleRequestParams {

        private boolean entities;
        private boolean entityAttributes;
        private boolean specific;

        public boolean isEntities() {
            return entities;
        }

        public void setEntities(boolean entities) {
            this.entities = entities;
        }

        public boolean isEntityAttributes() {
            return entityAttributes;
        }

        public void setEntityAttributes(boolean entityAttributes) {
            this.entityAttributes = entityAttributes;
        }

        public boolean isSpecific() {
            return specific;
        }

        public void setSpecific(boolean specific) {
            this.specific = specific;
        }
    }

}
