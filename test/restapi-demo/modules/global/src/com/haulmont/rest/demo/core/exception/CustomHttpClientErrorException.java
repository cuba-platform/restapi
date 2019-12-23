package com.haulmont.rest.demo.core.exception;

import com.haulmont.cuba.core.global.SupportedByClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

@SupportedByClient
public class CustomHttpClientErrorException extends HttpClientErrorException {

    public CustomHttpClientErrorException(HttpStatus statusCode, String statusText) {
        super(statusCode, statusText);
    }
}