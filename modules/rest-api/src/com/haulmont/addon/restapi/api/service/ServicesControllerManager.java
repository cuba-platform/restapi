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

import com.haulmont.addon.restapi.api.common.RestControllerUtils;
import com.haulmont.addon.restapi.api.common.RestParseUtils;
import com.haulmont.addon.restapi.api.config.RestServicesConfiguration;
import com.haulmont.addon.restapi.api.controllers.ServicesController;
import com.haulmont.addon.restapi.api.exception.RestAPIException;
import com.haulmont.addon.restapi.api.transform.JsonTransformationDirection;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.app.serialization.EntitySerializationAPI;
import com.haulmont.cuba.core.app.serialization.EntitySerializationOption;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Class that executes business logic required by the {@link ServicesController}. It
 * performs middleware services invocations.
 */
@Component("restapi_ServicesControllerManager")
public class ServicesControllerManager {

    @Inject
    protected RestServicesConfiguration restServicesConfiguration;

    @Inject
    protected EntitySerializationAPI entitySerializationAPI;

    @Inject
    protected RestParseUtils restParseUtils;

    @Inject
    protected RestControllerUtils restControllerUtils;

    @Inject
    protected Metadata metadata;

    private static final Logger log = LoggerFactory.getLogger(ServicesControllerManager.class);

    @Nullable
    public ServiceCallResult invokeServiceMethodGet(String serviceName,
                                                    String methodName,
                                                    Map<String, String> paramsMap,
                                                    String modelVersion) throws Throwable {
        paramsMap.remove("modelVersion");
        List<String> paramNames = new ArrayList<>(paramsMap.keySet());
        List<String> paramValuesStr = new ArrayList<>(paramsMap.values());
        return _invokeServiceMethod(serviceName, methodName, paramNames, paramValuesStr, modelVersion);
    }

    @Nullable
    public ServiceCallResult invokeServiceMethodPost(String serviceName,
                                                     String methodName,
                                                     String paramsJson,
                                                     String modelVersion) throws Throwable {
        Map<String, String> paramsMap = restParseUtils.parseParamsJson(paramsJson);
        List<String> paramNames = new ArrayList<>(paramsMap.keySet());
        List<String> paramValuesStr = new ArrayList<>(paramsMap.values());
        return _invokeServiceMethod(serviceName, methodName, paramNames, paramValuesStr, modelVersion);
    }

    public Collection<RestServicesConfiguration.RestServiceInfo> getServiceInfos() {
        return restServicesConfiguration.getServiceInfos();
    }

    public RestServicesConfiguration.RestServiceInfo getServiceInfo(String serviceName) {
        RestServicesConfiguration.RestServiceInfo serviceInfo = restServicesConfiguration.getServiceInfo(serviceName);
        if (serviceInfo == null) {
            throw new RestAPIException("Service not found",
                    String.format("Service %s not found", serviceName),
                    HttpStatus.NOT_FOUND);
        }
        return serviceInfo;
    }

    @Nullable
    protected ServiceCallResult _invokeServiceMethod(String serviceName,
                                                     String methodName,
                                                     List<String> paramNames,
                                                     List<String> paramValuesStr,
                                                     String modelVersion) throws Throwable {
        Object service = AppBeans.get(serviceName);
        RestServicesConfiguration.RestMethodInfo restMethodInfo = restServicesConfiguration.getRestMethodInfo(serviceName, methodName, paramNames);
        if (restMethodInfo == null) {
            throw new RestAPIException("Service method not found",
                    serviceName + "." + methodName + "(" + paramNames.stream().collect(Collectors.joining(",")) + ")",
                    HttpStatus.NOT_FOUND);
        }
        Method serviceMethod = restMethodInfo.getMethod();
        List<Object> paramValues = new ArrayList<>();
        Type[] types = restMethodInfo.getMethod().getGenericParameterTypes();
        for (int i = 0; i < types.length; i++) {
            int idx = i;
            try {
                idx = paramNames.indexOf(restMethodInfo.getParams().get(i).getName());
                paramValues.add(restParseUtils.toObject(types[i], paramValuesStr.get(idx), modelVersion));
            } catch (Exception e) {
                log.error("Error on parsing service param value", e);
                throw new RestAPIException("Invalid parameter value",
                        "Invalid parameter value for " + paramNames.get(idx),
                        HttpStatus.BAD_REQUEST,
                        e);
            }
        }

        Object methodResult;
        try {
            methodResult = serviceMethod.invoke(service, paramValues.toArray());
        } catch (InvocationTargetException | IllegalAccessException ex) {
            throw ex.getCause();
        }

        if (methodResult == null) {
            return null;
        }

        Class<?> methodReturnType = serviceMethod.getReturnType();
        if (Entity.class.isAssignableFrom(methodReturnType)) {
            Entity entity = (Entity) methodResult;
            restControllerUtils.applyAttributesSecurity(entity);
            String entityJson = entitySerializationAPI.toJson(entity,
                    null,
                    EntitySerializationOption.SERIALIZE_INSTANCE_NAME);
            entityJson = restControllerUtils.transformJsonIfRequired(entity.getMetaClass().getName(),
                    modelVersion, JsonTransformationDirection.TO_VERSION, entityJson);
            return new ServiceCallResult(entityJson, true);
        } else if (Collection.class.isAssignableFrom(methodReturnType)) {
            Type returnTypeArgument = getMethodReturnTypeArgument(serviceMethod);
            if ((returnTypeArgument instanceof Class && Entity.class.isAssignableFrom((Class) returnTypeArgument))
                    || isEntitiesCollection((Collection) methodResult)) {
                Collection<? extends Entity> entities = (Collection<? extends Entity>) methodResult;
                entities.forEach(entity -> restControllerUtils.applyAttributesSecurity(entity));
                String entitiesJson = entitySerializationAPI.toJson(entities,
                        null,
                        EntitySerializationOption.SERIALIZE_INSTANCE_NAME);
                if (returnTypeArgument != null) {
                    MetaClass metaClass = metadata.getClass((Class<?>) returnTypeArgument);
                    if (metaClass != null) {
                        entitiesJson = restControllerUtils.transformJsonIfRequired(metaClass.getName(), modelVersion,
                                JsonTransformationDirection.TO_VERSION, entitiesJson);
                    } else {
                        log.error("MetaClass for the returned collection type parameter (or the appropriate wildcard/type " +
                                        "variable upper bound) {} of service method is not found",
                                returnTypeArgument);
                    }
                }
                return new ServiceCallResult(entitiesJson, true);
            } else {
                return new ServiceCallResult(restParseUtils.serialize(methodResult), true);
            }
        } else {
            Datatype<?> datatype = Datatypes.get(methodReturnType);
            if (datatype != null) {
                return new ServiceCallResult(datatype.format(methodResult), false);
            } else {
                return new ServiceCallResult(restParseUtils.serialize(methodResult), true);
            }
        }
    }

    @Nullable
    protected Type getMethodReturnTypeArgument(Method serviceMethod) {
        TypeVariable<Method>[] typeParameters = serviceMethod.getTypeParameters();

        Type parameterizedReturnType = getParameterizedReturnType(serviceMethod);

        if (parameterizedReturnType == null) {
            return null;
        }

        if (parameterizedReturnType instanceof WildcardType) {
            return ((WildcardType) parameterizedReturnType).getUpperBounds()[0];
        } else {
            if (typeParameters.length != 0) {
                TypeVariable<Method> typeParameter = typeParameters[0];
                Type bound = typeParameter.getBounds()[0];
                if (parameterizedReturnType.getTypeName().equals(typeParameter.getTypeName())) {
                    return bound;
                }
            }
            return parameterizedReturnType;
        }
    }

    @Nullable
    protected Type getParameterizedReturnType(Method serviceMethod) {
        Type returnTypeArgument = null;

        Type genericReturnType = serviceMethod.getGenericReturnType();
        if (genericReturnType instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) genericReturnType).getActualTypeArguments();
            if (actualTypeArguments.length > 0) {
                returnTypeArgument = actualTypeArguments[0];
            }
        }

        return returnTypeArgument;
    }

    protected boolean isEntitiesCollection(Collection collection) {
        if (collection.isEmpty()) return false;
        for (Object item : collection) {
            if (!(item instanceof Entity)) {
                return false;
            }
        }
        return true;
    }

    public static class ServiceCallResult {
        protected String stringValue;
        protected boolean validJson;

        public ServiceCallResult(String stringValue, boolean validJson) {
            this.stringValue = stringValue;
            this.validJson = validJson;
        }

        public boolean isValidJson() {
            return validJson;
        }

        public String getStringValue() {
            return stringValue;
        }
    }
}
