package com.haulmont.rest.demo.web.exception;

import com.haulmont.addon.restapi.api.exception.ErrorInfo;
import com.haulmont.rest.demo.core.exception.CustomHttpClientErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice("com.haulmont.addon.restapi.api.controllers")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomRestControllerExceptionHandler {

    private Logger log = LoggerFactory.getLogger(CustomRestControllerExceptionHandler.class);

    @ExceptionHandler(CustomHttpClientErrorException.class)
    @ResponseBody
    public ResponseEntity<ErrorInfo> handleException(Exception e) {
        log.error("Exception in REST controller", e);
        CustomHttpClientErrorException ex = (CustomHttpClientErrorException) e;
        ErrorInfo errorInfo = new ErrorInfo(ex.getStatusCode().getReasonPhrase(), ex.getStatusText());
        return new ResponseEntity<>(errorInfo, ex.getStatusCode());
    }
}