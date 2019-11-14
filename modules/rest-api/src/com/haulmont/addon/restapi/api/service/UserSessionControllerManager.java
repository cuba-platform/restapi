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

import com.haulmont.addon.restapi.api.common.RestAuthUtils;
import com.haulmont.addon.restapi.api.exception.RestAPIException;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.app.UserSessionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.UUID;

@Component("restapi_UserSessionControllerManager")
public class UserSessionControllerManager {

    @Inject
    protected RestAuthUtils restAuthUtils;
    @Inject
    protected UserSessionService userSessionService;

    public void setSessionLocale(HttpServletRequest request) {
        Locale locale = restAuthUtils.extractLocaleFromRequestHeader(request);

        if (locale != null) {
            SecurityContext securityContext = AppContext.getSecurityContext();
            if (securityContext != null) {
                UUID sessionId = securityContext.getSessionId();
                if (sessionId != null) {
                    userSessionService.setSessionLocale(sessionId, locale);
                    return;
                }
            }
        }

        throw new RestAPIException("Could not change user session locale", null, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
