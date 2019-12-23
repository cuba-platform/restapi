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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static com.haulmont.rest.demo.http.rest.RestTestUtils.*;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OAuthTokenFT {

    protected static final String URI_BASE = "http://localhost:8080/app/rest/v2";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getToken() throws IOException {
        String token = getAuthToken("admin", "admin");
        assertNotNull(token);
    }

    @Test
    public void requestTokenWithoutAuthentication() throws Exception {
        String uri = URI_BASE + "/oauth/token";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(uri);

        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("grant_type", "password"));
        urlParameters.add(new BasicNameValuePair("username", "admin"));
        urlParameters.add(new BasicNameValuePair("password", "admin"));

        httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            int statusCode = statusCode(response);
            assertEquals(HttpStatus.SC_UNAUTHORIZED, statusCode);
            ReadContext ctx = parseResponse(response);
            assertEquals("unauthorized", ctx.read("$.error"));
        }
    }

    @Test
    public void requestTokenWithInvalidClientCredentials() throws Exception {
        String uri = URI_BASE + "/oauth/token";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        String encoding = Base64.getEncoder().encodeToString(("invalidClient:invalidPassword").getBytes());
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setHeader("Authorization", "Basic " + encoding);

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("grant_type", "password"));
        urlParameters.add(new BasicNameValuePair("username", "admin"));
        urlParameters.add(new BasicNameValuePair("password", "admin"));

        httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            int statusCode = statusCode(response);
            assertEquals(HttpStatus.SC_UNAUTHORIZED, statusCode);
            ReadContext ctx = parseResponse(response);
            assertEquals("unauthorized", ctx.read("$.error"));
//            assertEquals("Bad credentials", ctx.read("$.error_description"));
        }
    }

    @Test
    public void requestTokenWithInvalidUserCredentials() throws Exception {
        String uri = URI_BASE + "/oauth/token";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        String encoding = Base64.getEncoder().encodeToString(("cuba:cuba").getBytes());
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setHeader("Authorization", "Basic " + encoding);

        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("grant_type", "password"));
        urlParameters.add(new BasicNameValuePair("username", "admin"));
        urlParameters.add(new BasicNameValuePair("password", "admin1"));

        httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            int statusCode = statusCode(response);
            assertEquals(HttpStatus.SC_UNAUTHORIZED, statusCode);
            ReadContext ctx = parseResponse(response);
            assertEquals("unauthorized", ctx.read("$.error"));
            assertEquals("Bad credentials", ctx.read("$.error_description"));
        }
    }

    @Test
    public void revokeToken() throws Exception {
        String oauthToken = getAuthToken("admin", "admin");
        String resourceUrl = "/entities/ref_Car";
        try (CloseableHttpResponse response = sendGet(resourceUrl, oauthToken, null)) {
            assertEquals(HttpStatus.SC_OK, statusCode(response));
        }

        String revokeUrl = URI_BASE + "/oauth/revoke";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        String encoding = Base64.getEncoder().encodeToString((CLIENT_ID + ":" + CLIENT_SECRET).getBytes());
        HttpPost httpPost = new HttpPost(revokeUrl);
        httpPost.setHeader("Authorization", "Basic " + encoding);

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("token", oauthToken));

        httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            int statusCode = statusCode(response);
            assertEquals(SC_OK, statusCode);
        }

        try (CloseableHttpResponse response = sendGet(resourceUrl, oauthToken, null)) {
            assertEquals(HttpStatus.SC_UNAUTHORIZED, statusCode(response));
        }
    }

    @Test
    public void revokeTokenWithoutAuthorization() throws Exception {
        String oauthToken = getAuthToken("admin", "admin");
        String resourceUrl = "/entities/ref_Car";
        try (CloseableHttpResponse response = sendGet(resourceUrl, oauthToken, null)) {
            assertEquals(HttpStatus.SC_OK, statusCode(response));
        }

        String revokeUrl = URI_BASE + "/oauth/revoke";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPostRevoke = new HttpPost(revokeUrl);

        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("token", oauthToken));

        httpPostRevoke.setEntity(new UrlEncodedFormEntity(urlParameters));

        try (CloseableHttpResponse response = httpClient.execute(httpPostRevoke)) {
            int statusCode = statusCode(response);
            assertEquals(SC_UNAUTHORIZED, statusCode);
        }

        try (CloseableHttpResponse response = sendGet(resourceUrl, oauthToken, null)) {
            assertEquals(HttpStatus.SC_OK, statusCode(response));
        }
    }
}