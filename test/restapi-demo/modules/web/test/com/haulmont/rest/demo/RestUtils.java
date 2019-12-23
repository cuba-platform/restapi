package com.haulmont.rest.demo;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.http.MediaType;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;

public final class RestUtils {

    private static final String CLIENT_ID = "client";
    private static final String CLIENT_SECRET = "secret";

    private static CloseableHttpResponse sendGet(String url, String token, @Nullable Map<String, String> params) throws Exception {
        URIBuilder uriBuilder = new URIBuilder(url);
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                uriBuilder.addParameter(entry.getKey(), entry.getValue());
            }
        }
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.setHeader("Authorization", "Bearer " + token);
        httpGet.setHeader("Accept-Language", "en");
        return httpClient.execute(httpGet);
    }

    private static String responseContentType(CloseableHttpResponse response) {
        return response.getEntity().getContentType().getValue();
    }

    private static int statusCode(CloseableHttpResponse response) {
        return response.getStatusLine().getStatusCode();
    }

    private static ReadContext parseResponse(CloseableHttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        String s = EntityUtils.toString(entity);
        return JsonPath.parse(s);
    }

    public static String getAuthToken(String urlBase) throws Exception {
        String uri = urlBase + "/oauth/token";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        String encoding = Base64.getEncoder().encodeToString((CLIENT_ID + ":" + CLIENT_SECRET).getBytes());
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setHeader("Authorization", "Basic " + encoding);
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("grant_type", "password"));
        urlParameters.add(new BasicNameValuePair("username", "admin"));
        urlParameters.add(new BasicNameValuePair("password", "admin"));

        httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            ReadContext ctx = parseResponse(response);
            return ctx.read("$.access_token");
        }
    }

    public static void loadSomeList(String url, String oauthToken) throws Exception {
        try (CloseableHttpResponse response = sendGet(url, oauthToken, null)) {
            assertEquals(HttpStatus.SC_OK, statusCode(response));
            ReadContext ctx = parseResponse(response);
            assertEquals(2, (int) ctx.read("$.length()", Integer.class));
        }
    }

    public static void executeQuery(String url, String oauthToken) throws Exception {
        Map<String, String> params = new HashMap<>();
        try (CloseableHttpResponse response = sendGet(url, oauthToken, params)) {
            assertEquals(HttpStatus.SC_OK, statusCode(response));
            assertEquals("application/json;charset=utf-8", responseContentType(response).toLowerCase());
            ReadContext ctx = parseResponse(response);
            assertEquals(1, ctx.<Collection>read("$").size());
            assertEquals("admin", ctx.read("$.[0].login"));
        }
    }

    public static void executeService(String url, String oauthToken) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("number1", "2");
        params.put("number2", "3");
        try (CloseableHttpResponse response = sendGet(url, oauthToken, params)) {
            assertEquals("text/plain;charset=utf-8", responseContentType(response).toLowerCase());
            assertEquals(HttpStatus.SC_OK, statusCode(response));
            assertEquals("5", EntityUtils.toString(response.getEntity()));
        }
    }

}
