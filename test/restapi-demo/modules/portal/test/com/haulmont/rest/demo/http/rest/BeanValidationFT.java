/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.http.rest;

import com.haulmont.rest.demo.core.app.PortalTestService;
import com.haulmont.rest.demo.http.api.DataSet;
import com.jayway.jsonpath.ReadContext;
import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.haulmont.rest.demo.http.rest.RestTestUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BeanValidationFT extends AbstractRestControllerFT {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private static final String DB_URL = "jdbc:hsqldb:hsql://localhost:9010/rest_demo";

    private Connection conn;
    private DataSet dirtyData = new DataSet();
    private SimpleDateFormat dateFormatRequest = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat dateFormatResponse = new SimpleDateFormat("dd/MM/yyyy");

    @Before
    public void setUp() throws Exception {
        oauthToken = getAuthToken("admin", "admin");
        Class.forName("org.hsqldb.jdbc.JDBCDriver");
        conn = DriverManager.getConnection(DB_URL, "sa", "");
        prepareDb();
    }

    @After
    public void tearDown() throws Exception {
        //todo artamonov logout
        dirtyData.cleanup(conn);
        if (conn != null) {
            conn.close();
        }
    }

    @Test
    public void commitValidData() throws Exception {
        String json = getFileContent("currency-valid.json", null);
        String url = "/entities/ref$Currency";

        try (CloseableHttpResponse response = sendPost(url, oauthToken, json, null)) {
            assertEquals(HttpStatus.SC_CREATED, statusCode(response));
            Header[] locationHeaders = response.getHeaders("Location");
            assertEquals(1, locationHeaders.length);
            String location = locationHeaders[0].getValue();
            assertTrue(location.startsWith("http://localhost:8080/app/rest/v2/entities/ref$Currency"));
            String idString = location.substring(location.lastIndexOf("/") + 1);

            dirtyData.addCurrencyId(idString);
        }
    }

    @Test
    public void commitInvalidData() throws Exception {
        String json = getFileContent("currency-invalid-name.json", null);
        String url = "/entities/ref$Currency";

        try (CloseableHttpResponse response = sendPost(url, oauthToken, json, null)) {
            assertEquals(HttpStatus.SC_BAD_REQUEST, statusCode(response));

            ReadContext ctx = parseResponse(response);

            assertEquals("may not be null", ctx.read("$[0].message"));
            assertEquals("name", ctx.read("$[0].path"));
        }
    }

    @Test
    public void updateWithMissingRequiredFields() throws Exception {
        String currencyCode = "USD";
        executePrepared("insert into REF_CURRENCY (CODE, NAME, UUID, VERSION) values (?, ?, ?, 1)",
                currencyCode,
                "Dollar-1",
                UUID.randomUUID());

        dirtyData.addCurrencyId(currencyCode);

        String json = getFileContent("currency-missing-name.json", null);
        String url = "/entities/ref$Currency/" + currencyCode;

        Map<String, String> params = new HashMap<>();
        params.put("responseView", "currencyWithName");

        try (CloseableHttpResponse response = sendPut(url, oauthToken, json, params)) {

            assertEquals(HttpStatus.SC_OK, statusCode(response));
            ReadContext ctx = parseResponse(response);
            assertEquals("ref$Currency", ctx.read("$._entityName"));
            assertEquals(currencyCode, ctx.read("$.id"));
            assertEquals("Dollar-1", ctx.read("$.name"));
        }
    }

    @Test
    public void commitInvalidCustomValidationMessage() throws Exception {
        String json = getFileContent("currency-invalid-name-length.json", null);
        String url = "/entities/ref$Currency";

        try (CloseableHttpResponse response = sendPost(url, oauthToken, json, null)) {
            assertEquals(HttpStatus.SC_BAD_REQUEST, statusCode(response));

            ReadContext ctx = parseResponse(response);

            assertEquals("Epic fail", ctx.read("$[0].message"));
            assertEquals("name", ctx.read("$[0].path"));
            assertEquals("O", ctx.read("$[0].invalidValue"));
        }
    }

    @Test
    public void commitInvalidClassLevelValidators() throws Exception {
        String json = getFileContent("currency-invalid-code-ban.json", null);
        String url = "/entities/ref$Currency";

        try (CloseableHttpResponse response = sendPost(url, oauthToken, json, null)) {
            assertEquals(HttpStatus.SC_BAD_REQUEST, statusCode(response));

            ReadContext ctx = parseResponse(response);

            assertEquals("Invalid currency", ctx.read("$[0].message"));
            assertEquals("", ctx.read("$[0].path"));
        }
    }

    @Test
    public void commitValidClassLevelValidators() throws Exception {
        String json = getFileContent("currency-valid-code.json", null);
        String url = "/entities/ref$Currency";

        try (CloseableHttpResponse response = sendPost(url, oauthToken, json, null)) {
            assertEquals(HttpStatus.SC_CREATED, statusCode(response));
            Header[] locationHeaders = response.getHeaders("Location");
            assertEquals(1, locationHeaders.length);
            String location = locationHeaders[0].getValue();
            assertTrue(location.startsWith("http://localhost:8080/app/rest/v2/entities/ref$Currency"));
            String idString = location.substring(location.lastIndexOf("/") + 1);

            dirtyData.addCurrencyId(idString);
        }
    }

    @Test
    public void callValidService() throws Exception {
        String requestBody = getFileContent("service-valid-call.json", null);
        try (CloseableHttpResponse response = sendPost("/services/" + PortalTestService.NAME + "/validatedMethod",
                oauthToken, requestBody, null)) {
            assertEquals(HttpStatus.SC_OK, statusCode(response));
            ReadContext ctx = parseResponse(response);
            assertEquals(Integer.valueOf(0), ctx.read("$"));
        }
    }

    @Test
    public void callInvalidService() throws Exception {
        String requestBody = getFileContent("service-invalid-call.json", null);
        try (CloseableHttpResponse response = sendPost("/services/" + PortalTestService.NAME + "/validatedMethod",
                oauthToken, requestBody, null)) {
            assertEquals(HttpStatus.SC_BAD_REQUEST, statusCode(response));
            ReadContext ctx = parseResponse(response);

            assertEquals("must match \"\\d+\"", ctx.read("$[0].message"));
            assertEquals("validatedMethod.arg0", ctx.read("$[0].path"));
            assertEquals("AA", ctx.read("$[0].invalidValue"));
        }
    }

    @Test
    public void callInvalidServiceResult() throws Exception {
        String requestBody = getFileContent("service-valid-call.json", null);
        try (CloseableHttpResponse response = sendPost("/services/" + PortalTestService.NAME + "/validatedMethodResult",
                oauthToken, requestBody, null)) {
            assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, statusCode(response));
            ReadContext ctx = parseResponse(response);

            assertEquals("Server error", ctx.read("$.error"));
        }
    }

    @Test
    public void callInvalidServiceCustomException() throws Exception {
        String requestBody = getFileContent("service-custom-invalid-call.json", null);
        try (CloseableHttpResponse response = sendPost("/services/" + PortalTestService.NAME + "/validatedMethodResult",
                oauthToken, requestBody, null)) {
            assertEquals(HttpStatus.SC_BAD_REQUEST, statusCode(response));
            ReadContext ctx = parseResponse(response);

            assertEquals("{com.haulmont.cuba.core.global.validation.CustomValidationException}", ctx.read("$[0].messageTemplate"));
            assertEquals("Epic fail!", ctx.read("$[0].message"));
        }
    }


    @Test
    public void createSellerWithInvalidPastDate() throws Exception {
        String url = "/entities/ref$Seller";
        Date futureDate = getDateWithDifferentDay(5);

        Map<String, String> replacement = new HashMap<>();
        replacement.put("$NAME$", "Bob");
        replacement.put("$CONTRACT_START_DATE$", dateFormatRequest.format(futureDate));
        replacement.put("$CONTRACT_END_DATE$", dateFormatRequest.format(futureDate));

        String json = getFileContent("createNewSeller.json", replacement);
        try (CloseableHttpResponse response = sendPost(url, oauthToken, json, null)) {
            assertEquals(HttpStatus.SC_BAD_REQUEST, statusCode(response));

            ReadContext ctx = parseResponse(response);

            assertEquals(1, (int) ctx.read("$.length()"));
            assertEquals("Must be in the past", ctx.read("$[0].message"));
            assertEquals("contractStartDate", ctx.read("$[0].path"));
            assertEquals(dateFormatResponse.format(futureDate), ctx.read("$[0].invalidValue"));
        }
    }

    @Test
    public void createSellerWithInvalidFutureDate() throws Exception {
        String url = "/entities/ref$Seller";
        Date pastDate = getDateWithDifferentDay(-5);

        Map<String, String> replacement = new HashMap<>();
        replacement.put("$NAME$", "Dorian Green");
        replacement.put("$CONTRACT_START_DATE$", dateFormatRequest.format(pastDate));
        replacement.put("$CONTRACT_END_DATE$", dateFormatRequest.format(pastDate));

        String json = getFileContent("createNewSeller.json", replacement);
        try (CloseableHttpResponse response = sendPost(url, oauthToken, json, null)) {
            assertEquals(HttpStatus.SC_BAD_REQUEST, statusCode(response));

            ReadContext ctx = parseResponse(response);

            assertEquals(1, (int) ctx.read("$.length()"));
            assertEquals("Must be in the future", ctx.read("$[0].message"));
            assertEquals("contractEndDate", ctx.read("$[0].path"));
            assertEquals(dateFormatResponse.format(pastDate), ctx.read("$[0].invalidValue"));
        }
    }

    private Date getDateWithDifferentDay(int differentDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, differentDay);
        return calendar.getTime();
    }

    private void executePrepared(String sql, Object... params) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            stmt.executeUpdate();
        }
    }
}