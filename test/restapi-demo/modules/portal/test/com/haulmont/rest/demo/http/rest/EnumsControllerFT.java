/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.http.rest;

import com.jayway.jsonpath.ReadContext;
import net.minidev.json.JSONArray;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static com.haulmont.rest.demo.http.rest.RestTestUtils.parseResponse;
import static com.haulmont.rest.demo.http.rest.RestTestUtils.sendGet;
import static com.haulmont.rest.demo.http.rest.RestTestUtils.statusCode;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 */
public class EnumsControllerFT extends AbstractRestControllerFT {

    @Test
    public void getAllEnums() throws Exception {
        String url = "/metadata/enums";
        try (CloseableHttpResponse response = sendGet(url, oauthToken, null)) {
            assertEquals(HttpStatus.SC_OK, statusCode(response));
            ReadContext ctx = parseResponse(response);
            assertTrue(ctx.read("$.length()", Integer.class) > 1);

            assertEquals(2, (int) ctx.read("$[?(@.name == 'com.haulmont.rest.demo.core.entity.DriverStatus')].values.length()", List.class).get(0));
            Map<String, Object> value1 = (Map<String, Object>) ctx.read("$[?(@.name == 'com.haulmont.rest.demo.core.entity.DriverStatus')].values[0]", JSONArray.class).get(0);
            assertEquals("ACTIVE", value1.get("name"));
            assertEquals("Active", value1.get("caption"));
            assertEquals(10, value1.get("id"));
        }
    }

    @Test
    public void getEnum() throws Exception {
        String url = "/metadata/enums/com.haulmont.rest.demo.core.entity.DriverStatus";
        try (CloseableHttpResponse response = sendGet(url, oauthToken, null)) {
            assertEquals(HttpStatus.SC_OK, statusCode(response));
            ReadContext ctx = parseResponse(response);

            assertEquals("com.haulmont.rest.demo.core.entity.DriverStatus", ctx.read("$.name"));
            assertEquals(2, (int) ctx.read("$.values.length()", Integer.class));
            assertEquals("ACTIVE", ctx.read("$.values[0].name"));
            assertEquals("Active", ctx.read("$.values[0].caption"));
            assertEquals(10, (int) ctx.read("$.values[0].id", Integer.class));
        }
    }

    @Test
    public void getNonExistingEnum() throws Exception {
        String url = "/metadata/enums/com.haulmont.rest.demo.core.entity.NonExistingEnum";
        try (CloseableHttpResponse response = sendGet(url, oauthToken, null)) {
            assertEquals(HttpStatus.SC_NOT_FOUND, statusCode(response));
            ReadContext ctx = parseResponse(response);
            assertEquals("Enum not found", ctx.read("$.error"));
        }
    }
}
