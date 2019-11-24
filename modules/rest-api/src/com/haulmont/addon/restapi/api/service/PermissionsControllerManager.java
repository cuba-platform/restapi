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
import com.haulmont.addon.restapi.api.service.filter.data.PermissionInfo;
import com.haulmont.addon.restapi.api.service.filter.data.RoleInfo;
import com.haulmont.addon.restapi.api.service.filter.data.RolesInfo;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.entity.User;
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

    public RolesInfo getRolesInfo() {

        RolesInfo rolesInfo = new RolesInfo();
        rolesInfo.roles = new ArrayList<>();
        User user = userSessionSource.getUserSession().getCurrentOrSubstitutedUser();

        if (user == null || user.getUserRoles() == null) return rolesInfo;

        rolesInfo.permissions = getPermissionInfos();

        user.getUserRoles().forEach(userRole -> {
            RoleInfo roleInfo = new RoleInfo();
            roleInfo.roleType = userRole == null || userRole.getRole() == null ? null : userRole.getRole().getType();
            rolesInfo.roles.add(roleInfo);
        });

        return rolesInfo;
    }
}
