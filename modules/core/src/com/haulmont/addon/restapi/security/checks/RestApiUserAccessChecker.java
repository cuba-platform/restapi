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

package com.haulmont.addon.restapi.security.checks;

import com.haulmont.addon.restapi.exception.RestApiAccessDeniedException;
import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.security.auth.AbstractClientCredentials;
import com.haulmont.cuba.security.auth.AuthenticationDetails;
import com.haulmont.cuba.security.auth.Credentials;
import com.haulmont.cuba.security.auth.checks.AbstractUserAccessChecker;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Checks if login to REST API is permitted for user.
 */
@Component("restapi_RestApiUserAccessChecker")
public class RestApiUserAccessChecker extends AbstractUserAccessChecker implements Ordered {

    @Inject
    public RestApiUserAccessChecker(Messages messages) {
        super(messages);
    }

    @Override
    public void check(Credentials credentials, AuthenticationDetails authenticationDetails)
            throws LoginException {
        if (credentials instanceof AbstractClientCredentials) {
            AbstractClientCredentials clientCredentials = (AbstractClientCredentials) credentials;
            UserSession session = authenticationDetails.getSession();

            if (clientCredentials.isCheckClientPermissions()
                    && clientCredentials.getClientType() == ClientType.REST_API
                    && !session.isSpecificPermitted("cuba.restApi.enabled")) {
                throw new RestApiAccessDeniedException(
                        messages.getMessage(MSG_PACK, "LoginException.restApiAccessDenied"));
            }
        }
    }

    @Override
    public int getOrder() {
        return HIGHEST_PLATFORM_PRECEDENCE + 30;
    }
}