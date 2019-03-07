package com.haulmont.addon.restapi.security.checks;

import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.security.auth.AbstractClientCredentials;
import com.haulmont.cuba.security.auth.AuthenticationDetails;
import com.haulmont.cuba.security.auth.Credentials;
import com.haulmont.cuba.security.auth.checks.AbstractUserAccessChecker;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.addon.restapi.exception.RestApiAccessDeniedException;
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
    public void check(Credentials credentials, AuthenticationDetails authenticationDetails) throws LoginException {
        if (credentials instanceof AbstractClientCredentials) {
            AbstractClientCredentials clientCredentials = (AbstractClientCredentials) credentials;

            if (clientCredentials.isCheckClientPermissions()
                    && clientCredentials.getClientType() == ClientType.REST_API
                    && !authenticationDetails.getSession().isSpecificPermitted("cuba.restApi.enabled")) {
                throw new RestApiAccessDeniedException(messages.getMessage(MSG_PACK, "LoginException.restApiAccessDenied"));
            }
        }
    }

    @Override
    public int getOrder() {
        return HIGHEST_PLATFORM_PRECEDENCE + 30;
    }
}