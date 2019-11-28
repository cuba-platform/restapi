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
 *
 */

package com.haulmont.addon.restapi.api.service;

import com.haulmont.addon.restapi.api.controllers.VersionController;
import com.haulmont.addon.restapi.api.exception.RestAPIException;
import com.haulmont.cuba.core.global.BuildInfo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * Class that is used by {@link VersionController} for getting REST API version
 */
@Component("restapi_VersionControllerManager")
public class VersionControllerManager {

    private String apiVersion;

    @Inject
    protected BuildInfo buildInfo;

    public String getApiVersion() {
        if (apiVersion != null && apiVersion.length() > 0) {
            return apiVersion;
        } else {
            throw new RestAPIException("Could not determine REST API version", null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostConstruct
    private void determineApiVersion() {
        BuildInfo.Content content = buildInfo.getContent();

        if (content.getAppName().equals("restapi") && content.getArtifactGroup().equals("com.haulmont.addon.restapi")) {
            // Standalone REST API
            apiVersion = content.getVersion();
        } else {
            // REST API as part of a Cuba application
            for (String component : content.getAppComponents()) {
                if (component.trim().startsWith("com.haulmont.addon.restapi:")) {
                    apiVersion = component.split(":")[1];
                    break;
                }
            }
        }
    }
}
