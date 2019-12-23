/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.http.rest;

import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.ReadContext;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.haulmont.rest.demo.http.rest.RestTestUtils.*;
import static org.junit.Assert.assertEquals;

/**
 */
public class MessagesControllerFT extends AbstractRestControllerFT {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testLocalizationForEntity() throws Exception {
        String url = "/messages/entities/sec$User";
        try (CloseableHttpResponse response = sendGet(url, oauthToken, null)) {
            ReadContext ctx = parseResponse(response);
            assertEquals("User", ctx.read("$['sec$User']"));
            assertEquals("Login", ctx.read("$['sec$User.login']"));
            thrown.expect(PathNotFoundException.class);
            ctx.read("$['sec$Role']");
        }
    }

    @Test
    public void testLocalizationForEntityUsingRuLanguage() throws Exception {
        String url = "/messages/entities/sec$User";
        Map<String, String> headers = new HashMap<>();
        headers.put(HttpHeaders.ACCEPT_LANGUAGE, "ru");
        try (CloseableHttpResponse response = sendGetWithHeaders(url, oauthToken, null, headers)) {
            ReadContext ctx = parseResponse(response);
            assertEquals("Пользователь", ctx.read("$['sec$User']"));
            assertEquals("Логин", ctx.read("$['sec$User.login']"));

            thrown.expect(PathNotFoundException.class);
            ctx.read("$['sec$Role']");
        }
    }

    @Test
    public void testLocalizationForEntityWhenNoAcceptLanguageSpecified() throws Exception {
        String url = "/messages/entities/sec$User";
        Map<String, String> headers = new HashMap<>();
        try (CloseableHttpResponse response = sendGetWithHeaders(url, oauthToken, null, headers)) {
            ReadContext ctx = parseResponse(response);
            assertEquals("User", ctx.read("$['sec$User']"));
            assertEquals("Login", ctx.read("$['sec$User.login']"));
        }
    }

    @Test
    public void testLocalizationUsingLocaleFromLogin() throws Exception {
        String url = "/messages/entities/sec$User";

        String ruToken = getAuthToken("admin", "admin", Collections.singletonMap("Accept-Language", "ru"));
        try (CloseableHttpResponse response = sendGetWithHeaders(url, ruToken, null, Collections.emptyMap())) {
            ReadContext ctx = parseResponse(response);
            assertEquals("Пользователь", ctx.read("$['sec$User']"));
            assertEquals("Логин", ctx.read("$['sec$User.login']"));
        }

        String enToken = getAuthToken("admin", "admin", Collections.singletonMap("Accept-Language", "en"));
        try (CloseableHttpResponse response = sendGetWithHeaders(url, enToken, null, Collections.emptyMap())) {
            ReadContext ctx = parseResponse(response);
            assertEquals("User", ctx.read("$['sec$User']"));
            assertEquals("Login", ctx.read("$['sec$User.login']"));
        }
    }

    @Test
    public void testLocalizationForAllEntities() throws Exception {
        String url = "/messages/entities";
        try (CloseableHttpResponse response = sendGet(url, oauthToken, null)) {
            ReadContext ctx = parseResponse(response);
            assertEquals("User", ctx.read("$['sec$User']"));
            assertEquals("Login", ctx.read("$['sec$User.login']"));
            assertEquals("Role", ctx.read("$['sec$Role']"));
        }
    }

    @Test
    public void testLocalizationForEnum() throws Exception {
        String url = "/messages/enums/com.haulmont.cuba.security.entity.RoleType";
        try (CloseableHttpResponse response = sendGet(url, oauthToken, null)) {
            ReadContext ctx = parseResponse(response);
            assertEquals("Role Type", ctx.read("$['com.haulmont.cuba.security.entity.RoleType']"));
            assertEquals("Standard", ctx.read("$['com.haulmont.cuba.security.entity.RoleType.STANDARD']"));

            thrown.expect(PathNotFoundException.class);
            ctx.read("$['com.haulmont.cuba.security.entity.ConstraintOperationType.CREATE']");
        }
    }

    @Test
    public void testLocalizationForAllEnums() throws Exception {
        String url = "/messages/enums";
        try (CloseableHttpResponse response = sendGet(url, oauthToken, null)) {
            ReadContext ctx = parseResponse(response);
            assertEquals("Role Type", ctx.read("$['com.haulmont.cuba.security.entity.RoleType']"));
            assertEquals("Standard", ctx.read("$['com.haulmont.cuba.security.entity.RoleType.STANDARD']"));
            assertEquals("Create", ctx.read("$['com.haulmont.cuba.security.entity.ConstraintOperationType.CREATE']"));
        }
    }
}
