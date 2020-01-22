package com.haulmont.rest.demo.http.rest;

import com.haulmont.masquerade.Connectors;
import com.haulmont.rest.demo.core.app.PortalTestService;
import com.haulmont.rest.demo.http.rest.jmx.UserSessionsJmxService;
import com.haulmont.rest.demo.http.rest.jmx.WebConfigStorageJmxService;
import com.jayway.jsonpath.ReadContext;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.haulmont.rest.demo.http.rest.RestTestUtils.*;
import static org.junit.Assert.assertEquals;

public class AnonymousAccessFT {

    protected Map<String, String> serviceParams;

    private static final String DB_URL = "jdbc:hsqldb:hsql://localhost:9010/rest_demo";
    private static final UUID anonymousUserId = UUID.fromString("a405db59-e674-4f63-8afe-269dda788fe8");
    private Connection conn;

    @Before
    public void setUp() throws Exception {
        serviceParams = new HashMap<>();

        serviceParams.put("number1", "2");
        serviceParams.put("number2", "3");

        prepareDb();
        Connectors.jmx(UserSessionsJmxService.class)
                .initializeAnonymousSessions();
    }

    @After
    public void tearDown() throws SQLException {
        Connectors.jmx(WebConfigStorageJmxService.class)
                .setAppProperty("cuba.rest.anonymousEnabled", "false");

        deleteUserRoles(conn, anonymousUserId);
        if (conn != null)
            conn.close();
    }

    @Test
    public void executeServiceMethodWithAnonymousEnabled() throws Exception {
        Connectors.jmx(WebConfigStorageJmxService.class)
                .setAppProperty("cuba.rest.anonymousEnabled", "true");

        try (CloseableHttpResponse response = sendGet("/services/" + PortalTestService.NAME + "/sum", serviceParams)) {
            assertEquals("text/plain;charset=UTF-8", responseContentType(response));
            assertEquals(HttpStatus.SC_OK, statusCode(response));
            assertEquals("5", responseToString(response));
        }
    }

    @Test
    public void executeServiceMethodWithAnonymousAllowed() throws Exception {
        Connectors.jmx(WebConfigStorageJmxService.class)
                .setAppProperty("cuba.rest.anonymousEnabled", "false");

        try (CloseableHttpResponse response = sendGet("/services/" + PortalTestService.NAME + "/sum", serviceParams)) {
            assertEquals("text/plain;charset=UTF-8", responseContentType(response));
            assertEquals(HttpStatus.SC_OK, statusCode(response));
            assertEquals("5", responseToString(response));
        }
    }

    @Test
    public void executeServiceWithoutAnonymousAllowed() throws Exception {
        Connectors.jmx(WebConfigStorageJmxService.class)
                .setAppProperty("cuba.rest.anonymousEnabled", "false");

        try (CloseableHttpResponse response = sendGet("/services/" + PortalTestService.NAME + "/emptyMethod", null)) {
            assertEquals(HttpStatus.SC_UNAUTHORIZED, statusCode(response));
        }
    }

    @Test
    public void executeQueryWithAnonymousEnabled() throws Exception {
        Connectors.jmx(WebConfigStorageJmxService.class)
                .setAppProperty("cuba.rest.anonymousEnabled", "true");

        try (CloseableHttpResponse response = sendGet("/queries/sec$User/currentUser", null)) {
            assertEquals(HttpStatus.SC_OK, statusCode(response));
            assertEquals("application/json;charset=utf-8", responseContentType(response).toLowerCase());
            ReadContext ctx = parseResponse(response);
            assertEquals(1, ctx.<Collection>read("$").size());
            assertEquals("anonymous", ctx.read("$.[0].login"));
        }
    }

    @Test
    public void executeQueryWithAnonymousAllowed() throws Exception {
        Connectors.jmx(WebConfigStorageJmxService.class)
                .setAppProperty("cuba.rest.anonymousEnabled", "false");

        try (CloseableHttpResponse response = sendGet("/queries/sec$User/currentUser", null)) {
            assertEquals(HttpStatus.SC_OK, statusCode(response));
            assertEquals("application/json;charset=utf-8", responseContentType(response).toLowerCase());
            ReadContext ctx = parseResponse(response);
            assertEquals(1, ctx.<Collection>read("$").size());
            assertEquals("anonymous", ctx.read("$.[0].login"));
        }
    }

    @Test
    public void executeQueryWithoutAnonymousAllowed() throws Exception {
        Connectors.jmx(WebConfigStorageJmxService.class)
                .setAppProperty("cuba.rest.anonymousEnabled", "false");

        try (CloseableHttpResponse response = sendGet("/queries/sec$User/userByLogin", null)) {
            assertEquals(HttpStatus.SC_UNAUTHORIZED, statusCode(response));
        }
    }

    @Test
    public void loadEntitiesWithPermissionAnonymous() throws Exception {
        Connectors.jmx(WebConfigStorageJmxService.class)
                .setAppProperty("cuba.rest.anonymousEnabled", "true");

        String url = "/entities/sec$User";
        try (CloseableHttpResponse response = sendGet(url, null)) {
            assertEquals(HttpStatus.SC_OK, statusCode(response));
            ReadContext ctx = parseResponse(response);

            assertEquals(2, (int) ctx.read("$.length()", Integer.class));
        }
    }

    @Test
    public void loadEntitiesWithoutPermissionAnonymous() throws Exception {
        Connectors.jmx(WebConfigStorageJmxService.class)
                .setAppProperty("cuba.rest.anonymousEnabled", "true");

        String url = "/entities/sec$Group";
        try (CloseableHttpResponse response = sendGet(url, null)) {
            assertEquals(HttpStatus.SC_FORBIDDEN, statusCode(response));
        }
    }

    public static CloseableHttpResponse sendGet(String url, @Nullable Map<String, String> params) throws Exception {
        URIBuilder uriBuilder = new URIBuilder(URI_BASE + url);
        if (params != null) {
            params.forEach(uriBuilder::addParameter);
        }
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.setHeader("Accept-Language", "en");
        return httpClient.execute(httpGet);
    }

    private void prepareDb() throws Exception {
        Class.forName("org.hsqldb.jdbc.JDBCDriver");
        conn = DriverManager.getConnection(DB_URL, "sa", "");
        createDbUserRoles();
    }

    private void createDbUserRoles() throws SQLException {
        UUID id = UUID.randomUUID();
        //colorReadUser has colorReadRole role (read-only)
        executePrepared("insert into sec_user_role(id, user_id, role_name) values(?, ?, ?)",
                id,
                anonymousUserId,
                "rest-test-anonymous"
        );
    }

    private void executePrepared(String sql, Object... params) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            stmt.executeUpdate();
        }
    }

    private void deleteUserRoles(Connection conn, UUID userId) throws SQLException {
        PreparedStatement stmt;
        stmt = conn.prepareStatement("delete from sec_user_role where user_id = ?");
        try {
            stmt.setObject(1, userId);
            stmt.executeUpdate();
        } finally {
            stmt.close();
        }
    }
}
