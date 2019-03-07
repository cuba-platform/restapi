package com.haulmont.addon.restapi.exception;

import com.haulmont.cuba.core.global.Logging;
import com.haulmont.cuba.core.global.SupportedByClient;
import com.haulmont.cuba.security.global.LoginException;

/**
 * Exception that is thrown when REST API user that does not have permission to use REST API.
 */
@SupportedByClient
@Logging(Logging.Type.BRIEF)
public class RestApiAccessDeniedException extends LoginException {
    public RestApiAccessDeniedException(String message) {
        super(message);
    }

    public RestApiAccessDeniedException(String template, Object... params) {
        super(template, params);
    }
}