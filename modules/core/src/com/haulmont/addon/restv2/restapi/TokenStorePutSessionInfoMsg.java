/*
 * Copyright (c) 2008-2016 Haulmont.
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

package com.haulmont.addon.restv2.restapi;

import java.io.Serializable;

/**
 * Cluster message containing an information about the mapping between token value and middleware session
 */
public class TokenStorePutSessionInfoMsg implements Serializable {
    protected String tokenValue;
    protected RestUserSessionInfo sessionInfo;

    public TokenStorePutSessionInfoMsg(String tokenValue, RestUserSessionInfo sessionInfo) {
        this.tokenValue = tokenValue;
        this.sessionInfo = sessionInfo;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public RestUserSessionInfo getSessionInfo() {
        return sessionInfo;
    }
}
