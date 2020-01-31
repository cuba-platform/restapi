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

package com.haulmont.addon.restapi.api.controllers;

import com.haulmont.addon.restapi.api.service.filter.data.EffectiveRoleInfo;
import com.haulmont.addon.restapi.api.service.filter.data.PermissionInfo;
import com.haulmont.addon.restapi.api.service.PermissionsControllerManager;
import com.haulmont.addon.restapi.api.service.filter.data.RolesInfo;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.Collection;

/**
 * Controller that is used for getting current user permissions
 */
@RestController("restapi_PermissionsController")
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PermissionsController {

    @Inject
    protected PermissionsControllerManager permissionsControllerManager;

    @GetMapping("/v2/permissions")
    public Collection<PermissionInfo> getPermissions() {
        return permissionsControllerManager.getPermissionInfos();
    }

    @GetMapping("/v2/permissions/effective")
    public EffectiveRoleInfo getEffectiveRole(@RequestParam(required = false) boolean entities,
                                              @RequestParam(required = false) boolean entityAttributes,
                                              @RequestParam(required = false) boolean specific) {
        PermissionsControllerManager.EffectiveRoleRequestParams params =
                new PermissionsControllerManager.EffectiveRoleRequestParams();
        params.setEntities(entities);
        params.setEntityAttributes(entityAttributes);
        params.setSpecific(specific);
        return permissionsControllerManager.getEffectiveRole(params);
    }

    @GetMapping("/v2/roles")
    public RolesInfo getRoles() {
        return permissionsControllerManager.getRolesInfo();
    }
}