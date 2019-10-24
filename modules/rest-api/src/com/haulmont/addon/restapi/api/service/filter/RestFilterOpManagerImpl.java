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

package com.haulmont.addon.restapi.api.service.filter;

import com.google.common.collect.ImmutableList;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Metadata;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.time.*;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

import static com.haulmont.addon.restapi.api.service.filter.RestFilterOp.*;

@Component(RestFilterOpManager.NAME)
public class RestFilterOpManagerImpl implements RestFilterOpManager {

    @Inject
    protected Metadata metadata;

    protected static final List<Class> dateTimeClasses = ImmutableList.of(Date.class, LocalDate.class, LocalDateTime.class,
            OffsetDateTime.class);
    protected static final List<Class> timeClasses = ImmutableList.of(LocalTime.class, OffsetTime.class);

    @Override
    public EnumSet<RestFilterOp> availableOps(Class javaClass) {
        if (String.class.equals(javaClass))
            return EnumSet.of(EQUAL, IN, NOT_IN, NOT_EQUAL, CONTAINS, DOES_NOT_CONTAIN, NOT_EMPTY, STARTS_WITH, ENDS_WITH, IS_NULL);

        else if (dateTimeClasses.contains(javaClass))
            return EnumSet.of(EQUAL, IN, NOT_IN, NOT_EQUAL, GREATER, GREATER_OR_EQUAL, LESSER, LESSER_OR_EQUAL, NOT_EMPTY, DATE_INTERVAL, IS_NULL);

        else if (timeClasses.contains(javaClass))
            return EnumSet.of(EQUAL, NOT_EQUAL, GREATER, GREATER_OR_EQUAL, LESSER, LESSER_OR_EQUAL, NOT_EMPTY, DATE_INTERVAL, IS_NULL);

        else if (Number.class.isAssignableFrom(javaClass))
            return EnumSet.of(EQUAL, IN, NOT_IN, NOT_EQUAL, GREATER, GREATER_OR_EQUAL, LESSER, LESSER_OR_EQUAL, NOT_EMPTY, IS_NULL);

        else if (Boolean.class.equals(javaClass))
            return EnumSet.of(EQUAL, NOT_EQUAL, NOT_EMPTY, IS_NULL);

        else if (UUID.class.equals(javaClass)
                || Enum.class.isAssignableFrom(javaClass)
                || Entity.class.isAssignableFrom(javaClass))
            return EnumSet.of(EQUAL, IN, NOT_IN, NOT_EQUAL, NOT_EMPTY, IS_NULL);

        else
            throw new UnsupportedOperationException("Unsupported java class: " + javaClass);
    }
}