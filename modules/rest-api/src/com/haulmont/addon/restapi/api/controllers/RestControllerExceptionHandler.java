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

import com.haulmont.addon.restapi.api.exception.ConstraintViolationInfo;
import com.haulmont.addon.restapi.api.exception.ErrorInfo;
import com.haulmont.addon.restapi.api.exception.RestAPIException;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.RemoteException;
import com.haulmont.cuba.core.global.RowLevelSecurityException;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.core.global.validation.CustomValidationException;
import com.haulmont.cuba.core.global.validation.MethodParametersValidationException;
import com.haulmont.cuba.core.global.validation.MethodResultValidationException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.*;

@ControllerAdvice("com.haulmont.addon.restapi.api.controllers")
public class RestControllerExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(RestControllerExceptionHandler.class);

    protected static final Collection<Class> SERIALIZABLE_INVALID_VALUE_TYPES =
            Arrays.asList(String.class, Date.class, Number.class, Enum.class, UUID.class);

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    protected Metadata metadata;

    @ExceptionHandler(RestAPIException.class)
    @ResponseBody
    public ResponseEntity<ErrorInfo> handleRestAPIException(RestAPIException e) {
        if (e.getCause() == null) {
            log.info("RestAPIException: {}, {}", e.getMessage(), e.getDetails());
        } else {
            log.error("RestAPIException: {}, {}", e.getMessage(), e.getDetails(), e.getCause());
        }
        ErrorInfo errorInfo = new ErrorInfo(e.getMessage(), e.getDetails());
        return new ResponseEntity<>(errorInfo, e.getHttpStatus());
    }

    @ExceptionHandler(MethodResultValidationException.class)
    @ResponseBody
    public ResponseEntity<ErrorInfo> handleMethodResultValidationException(MethodResultValidationException e) {
        log.error("MethodResultValidationException in service", e);
        ErrorInfo errorInfo = new ErrorInfo("Server error", "");
        return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodParametersValidationException.class)
    @ResponseBody
    public ResponseEntity<List<ConstraintViolationInfo>> handleMethodParametersViolation(MethodParametersValidationException e) {
        log.debug("MethodParametersValidationException: {}, violations:\n{}", e.getMessage(), e.getConstraintViolations());

        List<ConstraintViolationInfo> violationInfos = getConstraintViolationInfos(e.getConstraintViolations());

        return new ResponseEntity<>(violationInfos, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity<List<ConstraintViolationInfo>> handleConstraintViolation(ConstraintViolationException e) {
        log.debug("ConstraintViolationException: {}, violations:\n{}", e.getMessage(), e.getConstraintViolations());

        List<ConstraintViolationInfo> violationInfos = getConstraintViolationInfos(e.getConstraintViolations());

        return new ResponseEntity<>(violationInfos, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomValidationException.class)
    @ResponseBody
    public ResponseEntity<List<ConstraintViolationInfo>> handleCustomValidationException(CustomValidationException e) {
        log.debug("CustomValidationException: {}", e.getMessage());

        ConstraintViolationInfo errorInfo = new ConstraintViolationInfo();
        errorInfo.setPath("");
        errorInfo.setInvalidValue(null);
        errorInfo.setMessage(e.getLocalizedMessage());
        errorInfo.setMessageTemplate("{com.haulmont.cuba.core.global.validation.CustomValidationException}");

        return new ResponseEntity<>(Collections.singletonList(errorInfo), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    public ResponseEntity<ErrorInfo> handleValidationException(ValidationException e) {
        log.error("ValidationException in service", e);
        ErrorInfo errorInfo = new ErrorInfo("Server error", "");
        return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RowLevelSecurityException.class)
    @ResponseBody
    public ResponseEntity<ErrorInfo> handleRowLevelSecurityException(RowLevelSecurityException e) {
        log.error("RowLevelSecurityException in service", e);
        ErrorInfo errorInfo = new ErrorInfo("Forbidden", e.getMessage());
        return new ResponseEntity<>(errorInfo, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ErrorInfo> handleException(Exception e) {
        log.error("Exception in REST controller", e);
        @SuppressWarnings("unchecked")
        List<Throwable> list = ExceptionUtils.getThrowableList(e);
        for (Throwable throwable : list) {
            if (throwable instanceof RemoteException) {
                RemoteException remoteException = (RemoteException) throwable;
                for (RemoteException.Cause cause : remoteException.getCauses()) {
                    if (Objects.equals("javax.persistence.OptimisticLockException", cause.getClassName())) {
                        ErrorInfo errorInfo = new ErrorInfo("Optimistic lock", cause.getMessage());
                        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
                    }
                }
            }
        }
        ErrorInfo errorInfo = new ErrorInfo("Server error", "");
        return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    protected List<ConstraintViolationInfo> getConstraintViolationInfos(Set<ConstraintViolation<?>> violations) {
        List<ConstraintViolationInfo> violationInfos = new ArrayList<>();

        for (ConstraintViolation<?> violation : violations) {
            ConstraintViolationInfo info = new ConstraintViolationInfo();

            info.setMessage(violation.getMessage());
            info.setMessageTemplate(violation.getMessageTemplate());

            Object invalidValue = violation.getInvalidValue();
            if (invalidValue != null) {
                Class<?> invalidValueClass = invalidValue.getClass();

                boolean serializable = false;
                for (Class serializableType : SERIALIZABLE_INVALID_VALUE_TYPES) {
                    //noinspection unchecked
                    if (serializableType.isAssignableFrom(invalidValueClass)) {
                        serializable = true;
                        break;
                    }
                }
                if (serializable) {
                    if (invalidValue instanceof Date) {
                        Datatype datatype = getDatatype(violation);
                        info.setInvalidValue(datatype.format(invalidValue, userSessionSource.getLocale()));
                    } else {
                        info.setInvalidValue(invalidValue);
                    }
                } else {
                    info.setInvalidValue(null);
                }
            }

            if (violation.getPropertyPath() != null) {
                info.setPath(violation.getPropertyPath().toString());
            }

            violationInfos.add(info);
        }
        return violationInfos;
    }

    protected Datatype getDatatype(ConstraintViolation<?> violation) {
        MetaClass metaClass = metadata.getClass(violation.getRootBeanClass());
        String propertyPath = violation.getPropertyPath().toString();
        MetaPropertyPath metaPropertyPath = metadata.getTools().resolveMetaPropertyPath(metaClass, propertyPath);
        return metaPropertyPath == null
                ? Datatypes.get(Date.class)
                : metaPropertyPath.getRange().asDatatype();
    }
}