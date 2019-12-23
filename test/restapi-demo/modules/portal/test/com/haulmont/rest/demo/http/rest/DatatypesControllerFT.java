/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.http.rest;

import com.jayway.jsonpath.ReadContext;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.junit.Test;

import java.util.List;

import static com.haulmont.rest.demo.http.rest.RestTestUtils.parseResponse;
import static com.haulmont.rest.demo.http.rest.RestTestUtils.sendGet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DatatypesControllerFT extends AbstractRestControllerFT {
    @Test
    public void getDatatypes() throws Exception {
        String url = "/metadata/datatypes";
        try (CloseableHttpResponse response = sendGet(url, oauthToken, null)) {
            ReadContext ctx = parseResponse(response);
            assertTrue(ctx.read("$.length()", Integer.class) > 1);

            assertEquals("yyyy-MM-dd HH:mm:ss.SSS", ctx.read("$[?(@.id == 'dateTime')].format", List.class).get(0));
            assertEquals(0, ctx.read("$[?(@.id == 'string')].format", List.class).size());

            assertEquals("0.####", ctx.read("$[?(@.id == 'decimal')].format", List.class).get(0));
            assertEquals(".", ctx.read("$[?(@.id == 'decimal')].decimalSeparator", List.class).get(0));
        }
    }
}