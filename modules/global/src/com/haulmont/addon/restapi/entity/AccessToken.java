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

package com.haulmont.addon.restapi.entity;

import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity(name = "restapi$AccessToken")
@Table(name = "RESTAPI_ACCESS_TOKEN")
@SystemLevel
public class AccessToken extends BaseUuidEntity {

    @Column(name = "CREATE_TS")
    protected Date createTs;

    @Column(name = "TOKEN_VALUE")
    protected String tokenValue;

    @Column(name = "TOKEN_BYTES")
    protected byte[] tokenBytes;

    @Column(name = "AUTHENTICATION_KEY")
    protected String authenticationKey;

    @Column(name = "AUTHENTICATION_BYTES")
    protected byte[] authenticationBytes;

    @Column(name = "EXPIRY")
    protected Date expiry;

    @Column(name = "USER_LOGIN")
    protected String userLogin;

    @Column(name = "LOCALE")
    protected String locale;

    @Column(name = "REFRESH_TOKEN_VALUE")
    protected String refreshTokenValue;

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    public byte[] getTokenBytes() {
        return tokenBytes;
    }

    public void setTokenBytes(byte[] tokenBytes) {
        this.tokenBytes = tokenBytes;
    }

    public String getAuthenticationKey() {
        return authenticationKey;
    }

    public void setAuthenticationKey(String authenticationKey) {
        this.authenticationKey = authenticationKey;
    }

    public byte[] getAuthenticationBytes() {
        return authenticationBytes;
    }

    public void setAuthenticationBytes(byte[] authenticationBytes) {
        this.authenticationBytes = authenticationBytes;
    }

    public Date getExpiry() {
        return expiry;
    }

    public void setExpiry(Date expiry) {
        this.expiry = expiry;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Date getCreateTs() {
        return createTs;
    }

    public void setCreateTs(Date createTs) {
        this.createTs = createTs;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getRefreshTokenValue() {
        return refreshTokenValue;
    }

    public void setRefreshTokenValue(String refreshTokenValue) {
        this.refreshTokenValue = refreshTokenValue;
    }
}
