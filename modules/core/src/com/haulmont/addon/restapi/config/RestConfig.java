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

package com.haulmont.addon.restapi.config;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.DefaultBoolean;
import com.haulmont.cuba.core.config.type.Factory;
import com.haulmont.cuba.core.config.type.UuidTypeFactory;

import javax.annotation.Nullable;
import java.util.UUID;

public interface RestConfig extends Config {

    /**
     * @return whether to store REST API OAuth tokens in the database
     */
    @Property("cuba.rest.storeTokensInDb")
    @Source(type = SourceType.DATABASE)
    @DefaultBoolean(false)
    boolean getRestStoreTokensInDb();

    /**
     * @return whether newly created tokens should be sent to the cluster synchronously
     */
    @Property("cuba.rest.syncTokenReplication")
    @Source(type = SourceType.APP)
    @DefaultBoolean(false)
    boolean getSyncTokenReplication();

    @Property("cuba.rest.anonymousSessionId")
    @Factory(factory = UuidTypeFactory.class)
    @Nullable
    UUID getRestAnonymousSessionId();

}
