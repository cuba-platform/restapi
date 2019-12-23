/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.http.rest;

import com.jayway.jsonpath.ReadContext;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.*;

import static com.haulmont.rest.demo.http.rest.RestTestUtils.parseResponse;
import static com.haulmont.rest.demo.http.rest.RestTestUtils.statusCode;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PortalRestApiFT {

    protected static final String CLIENT_ID = "client";
    protected static final String CLIENT_SECRET = "secret";
    protected static final String PORTAL_URI_BASE = "http://localhost:8080/app-portal/rest/v2";

    /**
     * Checks that entities controller in the portal app is available
     */
    @Test
    public void loadEntitiesList() throws Exception {
        String oauthToken = getAuthToken(PORTAL_URI_BASE + "/oauth/token", "admin", "admin");
        assertNotNull(oauthToken);

        String url = "/entities/ref$Colour";
        Map<String, String> params = new HashMap<>();
        try (CloseableHttpResponse response = sendGetPortal(url, oauthToken, params)) {
            assertEquals(HttpStatus.SC_OK, statusCode(response));
        }
    }

    private String getAuthToken(String tokenUrl, String login, String password) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String encoding = Base64.getEncoder().encodeToString((CLIENT_ID + ":" + CLIENT_SECRET).getBytes());
        HttpPost httpPost = new HttpPost(tokenUrl);
        httpPost.setHeader("Authorization", "Basic " + encoding);

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("grant_type", "password"));
        urlParameters.add(new BasicNameValuePair("username", login));
        urlParameters.add(new BasicNameValuePair("password", password));

        httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != SC_OK) {
                throw new RestTestUtils.AuthException(statusCode);
            }
            ReadContext ctx = parseResponse(response);
            return ctx.read("$.access_token");
        }
    }

    private CloseableHttpResponse sendGetPortal(String url, String token, @Nullable Map<String, String> params) throws Exception {
        URIBuilder uriBuilder = new URIBuilder(PORTAL_URI_BASE + url);
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                uriBuilder.addParameter(entry.getKey(), entry.getValue());
            }
        }
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.setHeader("Authorization", "Bearer " + token);
        return httpClient.execute(httpGet);
    }
}