/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.http.rest;

import com.haulmont.cuba.core.sys.encryption.BCryptEncryptionModule;
import com.haulmont.cuba.core.sys.encryption.EncryptionModule;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.entity.RoleType;
import com.haulmont.rest.demo.core.app.PortalTestService;
import com.haulmont.rest.demo.http.api.DataSet;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.ReadContext;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import static com.haulmont.rest.demo.http.rest.RestTestUtils.*;
import static org.junit.Assert.*;

/**
 *
 */
public class EntitiesControllerSecurityFT {

    private static final String DB_URL = "jdbc:hsqldb:hsql://localhost:9010/rest_demo";
    private static EncryptionModule encryption = new BCryptEncryptionModule();
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private Connection conn;
    private DataSet dirtyData = new DataSet();
    /**
     * Entitites ids
     */
    private String carUuidString;
    private String colourUuidString;
    private String driverUuidString;
    /**
     * User ids
     */
    private UUID colorReadUserId;
    private UUID colorUpdateUserId;
    private UUID colorCreateUserId;
    private UUID colorDeleteUserId;
    private UUID carReadUserId;
    /**
     * Logins
     */
    private String colorReadUserLogin = "colorReadUser";
    private String colorUpdateUserLogin = "colorUpdateUser";
    private String colorCreateUserLogin = "colorCreateUser";
    private String colorDeleteUserLogin = "colorDeleteUser";
    private String carReadUserLogin = "carReadUser";
    private String colorReadUserPassword = "colorReadUser";
    private String colorUpdateUserPassword = "colorUpdateUser";
    private String colorCreateUserPassword = "colorCreateUser";
    private String colorDeleteUserPassword = "colorDeleteUser";
    private String carReadUserPassword = "carReadUser";

    /**
     * Roles
     */
    private UUID colorReadRoleId;
    private UUID colorUpdateRoleId;
    private UUID colorCreateRoleId;
    private UUID colorDeleteRoleId;
    private UUID carReadRoleId;
    private UUID noColorReadRoleId;

    /**
     * User OAuth tokens
     */
    private String colorReadUserToken;
    private String colorUpdateUserToken;
    private String colorCreateUserToken;
    private String colorDeleteUserToken;
    private String carReadUserToken;

    private UUID groupUuid = UUID.fromString("0fa2b1a5-1d68-4d69-9fbd-dff348347f93");
    private String modelUuidString;

    @Before
    public void setUp() throws Exception {
        prepareDb();

        colorReadUserToken = getAuthToken(colorReadUserLogin, colorReadUserPassword);
        colorUpdateUserToken = getAuthToken(colorUpdateUserLogin, colorUpdateUserPassword);
        colorCreateUserToken = getAuthToken(colorCreateUserLogin, colorCreateUserPassword);
        colorDeleteUserToken = getAuthToken(colorDeleteUserLogin, colorDeleteUserPassword);
        carReadUserToken = getAuthToken(carReadUserLogin, carReadUserPassword);
    }

    @After
    public void tearDown() throws SQLException {
        dirtyData.cleanup(conn);

        if (conn != null)
            conn.close();
    }

    @Test
    public void findPermitted() throws Exception {
        //trying to get entity with permitted read access
        String url = "/entities/ref$Colour/" + colourUuidString;
        try (CloseableHttpResponse response = sendGet(url, colorReadUserToken, null)) {
            assertEquals(HttpStatus.SC_OK, statusCode(response));
            ReadContext ctx = parseResponse(response);
            assertEquals(colourUuidString, ctx.read("$.id"));
        }
    }

    @Test
    public void findForbidden() throws Exception {
        //trying to get entity with forbidden read access
        String url = "/entities/ref_Car/" + carUuidString;
        try (CloseableHttpResponse response = sendGet(url, colorReadUserToken, null)) {
            assertEquals(HttpStatus.SC_FORBIDDEN, statusCode(response));
            ReadContext ctx = parseResponse(response);
            assertEquals("Reading forbidden", ctx.read("$.error"));
        }
    }

    @Test
    public void findAttributes() throws Exception {
        //checks that forbidden attributes aren't included to the result JSON
        String url = "/entities/ref_Car/" + carUuidString +
                "?view=carEdit";
        try (CloseableHttpResponse response = sendGet(url, carReadUserToken, null)) {
            assertEquals(org.apache.http.HttpStatus.SC_OK, statusCode(response));
            ReadContext ctx = parseResponse(response);
            assertEquals(carUuidString, ctx.read("$.id"));
            assertNotNull(ctx.read("$.vin"));
            assertNotNull(ctx.read("$.model"));

            thrown.expect(PathNotFoundException.class);
            ctx.read("$.colour");

            thrown.expect(PathNotFoundException.class);
            ctx.read("$.driverAllocations");
        }
    }

    @Test
    public void unavailableAttributesMustBeHiddenInQueryResult() throws Exception {
        String url = "/queries/ref_Car/carByVin?vin=VWV000";

        try (CloseableHttpResponse response = sendGet(url, carReadUserToken, null)) {
            assertEquals(org.apache.http.HttpStatus.SC_OK, statusCode(response));
            ReadContext ctx = parseResponse(response);
            assertEquals(carUuidString, ctx.read("$.[0].id"));
            assertNotNull(ctx.read("$.[0].vin"));
            assertNotNull(ctx.read("$.[0].model"));

            thrown.expect(PathNotFoundException.class);
            ctx.read("$.[0].colour");
        }
    }

    @Test
    public void unavailableAttributesMustBeHiddenInServiceResult() throws Exception {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("carId", carUuidString);
        params.put("viewName", "carEdit");
        try (CloseableHttpResponse response = sendGet("/services/" + PortalTestService.NAME + "/findCar", carReadUserToken, params)) {
            assertEquals(HttpStatus.SC_OK, statusCode(response));
            assertEquals("application/json;charset=UTF-8", responseContentType(response));
            ReadContext ctx = parseResponse(response);
            assertEquals(carUuidString, ctx.read("$.id"));
            assertEquals("ref_Car", ctx.read("$._entityName"));
            assertNotNull(ctx.read("$.model"));

            thrown.expect(PathNotFoundException.class);
            ctx.read("$.colour");
        }
    }

    @Test
    public void createForbidden() throws Exception {
        String url = "/entities/ref$Colour";
        String json = getFileContent("colour.json", null);
        try (CloseableHttpResponse response = sendPost(url, colorReadUserToken, json, null)) {
            assertEquals(HttpStatus.SC_FORBIDDEN, statusCode(response));
        }
    }

    @Test
    public void updateForbiddenEntity() throws Exception {
        String url = "/entities/ref$Colour/" + colourUuidString;
        String json = getFileContent("colour.json", null);
        try (CloseableHttpResponse response = sendPut(url, colorReadUserToken, json, null)) {
            assertEquals(HttpStatus.SC_FORBIDDEN, statusCode(response));
        }
    }

    @Test
    public void updateForbiddenAttribute() throws Exception {
        //checks that forbidden attribute won't be updated
        String url = "/entities/ref$Colour/" + colourUuidString;
        Map<String, String> replacements = new HashMap<>();
        replacements.put("$COLOUR_ID$", colourUuidString);
        String json = getFileContent("updateColorWithForbiddenAttr.json", replacements);
        try (CloseableHttpResponse response = sendPut(url, colorUpdateUserToken, json, null)) {
            assertEquals(HttpStatus.SC_OK, statusCode(response));
        }

        try (PreparedStatement stmt = conn.prepareStatement("select DESCRIPTION from REF_COLOUR where ID = ?")) {
            stmt.setObject(1, UUID.fromString(colourUuidString));
            ResultSet rs = stmt.executeQuery();
            assertTrue(rs.next());
            String description = rs.getString("DESCRIPTION");
            assertEquals("Description", description);
        }
    }

    @Test
    public void updatePermittedAttribute() throws Exception {
        //checks that permitted attribute will be updated
        String url = "/entities/ref$Colour/" + colourUuidString;
        Map<String, String> replacements = new HashMap<>();
        replacements.put("$COLOUR_ID$", colourUuidString);
        String json = getFileContent("updateColorWithPermittedAttr.json", replacements);

        try (CloseableHttpResponse response = sendPut(url, colorUpdateUserToken, json, null)) {
            assertEquals(HttpStatus.SC_OK, statusCode(response));
        }

        try (PreparedStatement stmt = conn.prepareStatement("select NAME from REF_COLOUR where ID = ?")) {
            stmt.setObject(1, UUID.fromString(colourUuidString));
            ResultSet rs = stmt.executeQuery();
            assertTrue(rs.next());
            String name = rs.getString("NAME");
            assertEquals("Red 2", name);
        }
    }

    @Test
    public void deleteForbidden() throws Exception {
        String url = "/entities/ref$Colour/" + colourUuidString;

        try (CloseableHttpResponse response = sendDelete(url, colorUpdateUserToken, null)) {
            assertEquals(HttpStatus.SC_FORBIDDEN, statusCode(response));
        }

        try (PreparedStatement stmt = conn.prepareStatement("select NAME from REF_COLOUR where ID = ?")) {
            stmt.setObject(1, UUID.fromString(colourUuidString));
            ResultSet rs = stmt.executeQuery();
            assertTrue(rs.next());
        }
    }

    private void prepareDb() throws Exception {
        Class.forName("org.hsqldb.jdbc.JDBCDriver");
        conn = DriverManager.getConnection(DB_URL, "sa", "");
        createDbData();
        createDbUsers();
        createDbRoles();
        createDbUserRoles();
        createDbPermissions();
    }

    private void createDbPermissions() throws SQLException {
        createEntityOpPermissions();
        createEntityAttrPermissions();
    }

    private void createEntityOpPermissions() throws SQLException {
        Integer PERMIT = 1;
        Integer FORBID = 0;

        UUID canReadColorPrmsId = dirtyData.createPermissionUuid();
        //colourReadRole allows to read colours
        executePrepared("insert into sec_permission(id, role_id, permission_type, target, value_) values(?, ?, ?, ?, ?)",
                canReadColorPrmsId,
                colorReadRoleId,
                PermissionType.ENTITY_OP.getId(),
                "ref$Colour:read",
                PERMIT
        );

        UUID cantReadCarPrmsID = dirtyData.createPermissionUuid();
        //colorReadRole prohibits to read cars
        executePrepared("insert into sec_permission(id, role_id, permission_type, target, value_) values(?, ?, ?, ?, ?)",
                cantReadCarPrmsID,
                colorReadRoleId,
                PermissionType.ENTITY_OP.getId(),
                "ref_Car:read",
                FORBID
        );

        UUID canUpdateColorPrmsId = dirtyData.createPermissionUuid();
        //colorUpdateRole allows to update colours
        executePrepared("insert into sec_permission(id, role_id, permission_type, target, value_) values(?, ?, ?, ?, ?)",
                canUpdateColorPrmsId,
                colorUpdateRoleId,
                PermissionType.ENTITY_OP.getId(),
                "ref$Colour:update",
                PERMIT
        );

        UUID canCreateColorPrmsId = dirtyData.createPermissionUuid();
        //colorCreateRole allows to create colours
        executePrepared("insert into sec_permission(id, role_id, permission_type, target, value_) values(?, ?, ?, ?, ?)",
                canCreateColorPrmsId,
                colorCreateRoleId,
                PermissionType.ENTITY_OP.getId(),
                "ref$Colour:create",
                PERMIT
        );

        UUID canDeleteColorPrmsId = dirtyData.createPermissionUuid();
        //colorDeleteRole allows to delete colours
        executePrepared("insert into sec_permission(id, role_id, permission_type, target, value_) values(?, ?, ?, ?, ?)",
                canDeleteColorPrmsId,
                colorDeleteRoleId,
                PermissionType.ENTITY_OP.getId(),
                "ref$Colour:delete",
                PERMIT
        );

        UUID cantUpdateCarPrmsId = dirtyData.createPermissionUuid();
        //colorUpdateRole prohibits to update cars
        executePrepared("insert into sec_permission(id, role_id, permission_type, target, value_) values(?, ?, ?, ?, ?)",
                cantUpdateCarPrmsId,
                colorUpdateRoleId,
                PermissionType.ENTITY_OP.getId(),
                "ref_Car:update",
                FORBID
        );

        UUID cantReadColorPrmsId = dirtyData.createPermissionUuid();
        //noColorReadRole prohibits to view colors
        executePrepared("insert into sec_permission(id, role_id, permission_type, target, value_) values(?, ?, ?, ?, ?)",
                cantReadColorPrmsId,
                noColorReadRoleId,
                PermissionType.ENTITY_OP.getId(),
                "ref$Colour:read",
                FORBID
        );
    }

    private void createEntityAttrPermissions() throws SQLException {
        Integer MODIFY = 2;
        Integer VIEW = 1;
        Integer DENY = 0;

        UUID canReadCarModelPrmsId = dirtyData.createPermissionUuid();
        //carReadRole allows to view car's model
        executePrepared("insert into sec_permission(id, role_id, permission_type, target, value_) values(?, ?, ?, ?, ?)",
                canReadCarModelPrmsId,
                carReadRoleId,
                PermissionType.ENTITY_ATTR.getId(),
                "ref_Car:model",
                VIEW
        );

        UUID canReadCarColorPrmsId = dirtyData.createPermissionUuid();
        //carReadRole cannot read or modify car's color
        executePrepared("insert into sec_permission(id, role_id, permission_type, target, value_) values(?, ?, ?, ?, ?)",
                canReadCarColorPrmsId,
                carReadRoleId,
                PermissionType.ENTITY_ATTR.getId(),
                "ref_Car:colour",
                DENY
        );

        UUID canModifyCarVinPrmsId = dirtyData.createPermissionUuid();
        //carReadRole allows to modify car's vin
        executePrepared("insert into sec_permission(id, role_id, permission_type, target, value_) values(?, ?, ?, ?, ?)",
                canModifyCarVinPrmsId,
                carReadRoleId,
                PermissionType.ENTITY_ATTR.getId(),
                "ref_Car:vin",
                MODIFY
        );

        UUID denyCarDriverAllocsPrmsId = dirtyData.createPermissionUuid();
        //carReadRole cannot read or modify car's driver allocations
        executePrepared("insert into sec_permission(id, role_id, permission_type, target, value_) values(?, ?, ?, ?, ?)",
                denyCarDriverAllocsPrmsId,
                carReadRoleId,
                PermissionType.ENTITY_ATTR.getId(),
                "ref_Car:driverAllocations",
                DENY
        );


        UUID denyColourDescriptionUpdatePrmsId = dirtyData.createPermissionUuid();
        //carUpdateRole cannot update colour property
        executePrepared("insert into sec_permission(id, role_id, permission_type, target, value_) values(?, ?, ?, ?, ?)",
                denyColourDescriptionUpdatePrmsId,
                colorUpdateRoleId,
                PermissionType.ENTITY_ATTR.getId(),
                "ref$Colour:description",
                DENY
        );
    }

    private void createDbUserRoles() throws SQLException {
        UUID id = UUID.randomUUID();
        //colorReadUser has colorReadRole role (read-only)
        executePrepared("insert into sec_user_role(id, user_id, role_id) values(?, ?, ?)",
                id,
                colorReadUserId,
                colorReadRoleId
        );

        id = UUID.randomUUID();
        //colorUpdateUser has colorUpdateRole (read-only)
        executePrepared("insert into sec_user_role(id, user_id, role_id) values(?, ?, ?)",
                id,
                colorUpdateUserId,
                colorUpdateRoleId
        );

        id = UUID.randomUUID();
        //colorCreateUser has colorCreateRole (read-only)
        executePrepared("insert into sec_user_role(id, user_id, role_id) values(?, ?, ?)",
                id,
                colorCreateUserId,
                colorCreateRoleId
        );

        id = UUID.randomUUID();
        //colorDeleteUser has colorDeleteRole (read-only)
        executePrepared("insert into sec_user_role(id, user_id, role_id) values(?, ?, ?)",
                id,
                colorDeleteUserId,
                colorDeleteRoleId
        );

        id = UUID.randomUUID();
        //carReadUser has carReadRole (read-only)
        executePrepared("insert into sec_user_role(id, user_id, role_id) values(?, ?, ?)",
                id,
                carReadUserId,
                carReadRoleId
        );

        id = UUID.randomUUID();
        //carReadUser has noColorReadRole (read-only)
        executePrepared("insert into sec_user_role(id, user_id, role_id) values(?, ?, ?)",
                id,
                carReadUserId,
                noColorReadRoleId
        );
    }

    private void createDbRoles() throws SQLException {
        //read-only role. can read colours, can't read cars
        colorReadRoleId = dirtyData.createRoleUuid();
        executePrepared("insert into sec_role(id, role_type, name) values(?, ?, ?)",
                colorReadRoleId,
                RoleType.READONLY.getId(),
                "colorReadRole"
        );

        //read_only role. can update colours, can't update cars
        colorUpdateRoleId = dirtyData.createRoleUuid();
        executePrepared("insert into sec_role(id, role_type, name) values(?, ?, ?)",
                colorUpdateRoleId,
                RoleType.READONLY.getId(),
                "colorUpdateRole"
        );

        //read-only role. can create colours
        colorCreateRoleId = dirtyData.createRoleUuid();
        executePrepared("insert into sec_role(id, role_type, name) values(?, ?, ?)",
                colorCreateRoleId,
                RoleType.READONLY.getId(),
                "colorCreateRole"
        );

        //read-only role. can delete colours
        colorDeleteRoleId = dirtyData.createRoleUuid();
        executePrepared("insert into sec_role(id, role_type, name) values(?, ?, ?)",
                colorDeleteRoleId,
                RoleType.READONLY.getId(),
                "colorDeleteRole"
        );

        //read-only role for attributes access tests
        carReadRoleId = dirtyData.createRoleUuid();
        executePrepared("insert into sec_role(id, role_type, name) values(?, ?, ?)",
                carReadRoleId,
                RoleType.READONLY.getId(),
                "carReadRole"
        );

        //read-only role, prohibiting viewing the colors
        noColorReadRoleId = dirtyData.createRoleUuid();
        executePrepared("insert into sec_role(id, role_type, name) values(?, ?, ?)",
                noColorReadRoleId,
                RoleType.READONLY.getId(),
                "noColorReadRole"
        );
    }

    private void createDbData() throws SQLException {
        UUID colourUuid = dirtyData.createColourUuid();
        colourUuidString = colourUuid.toString();
        executePrepared("insert into ref_colour(id, version, name, description) values(?, ?, ?, ?)",
                colourUuid,
                1L,
                "Red",
                "Description"
        );

        UUID modelUuid = dirtyData.createModelUuid();
        modelUuidString = modelUuid.toString();
        executePrepared("insert into ref_model(id, version, name) values(?, ?, ?)",
                modelUuid,
                1L,
                "Audi"
        );

        UUID carUuid = dirtyData.createCarUuid();
        carUuidString = carUuid.toString();
        executePrepared("insert into ref_car(id, version, vin, colour_id, model_id) values(?, ?, ?, ?, ?)",
                carUuid,
                1l,
                "VWV000",
                colourUuid,
                modelUuid
        );

        UUID driverUuid = dirtyData.createDriverUuid();
        driverUuidString = driverUuid.toString();
        executePrepared("insert into ref_driver(id, version, name, DTYPE) values(?, ?, ?, 'ref$ExtDriver')",
                driverUuid,
                1l,
                "Driver"
        );
    }

    private void createDbUsers() throws SQLException {
        //can read colours, cant read cars
        colorReadUserId = dirtyData.createUserUuid();
        String pwd = encryption.getPasswordHash(colorReadUserId, colorReadUserPassword);
        executePrepared("insert into sec_user(id, version, login, password, password_encryption, group_id, login_lc) " +
                        "values(?, ?, ?, ?, ?, ?, ?)",
                colorReadUserId,
                1l,
                colorReadUserLogin,
                pwd,
                encryption.getHashMethod(),
                groupUuid, //"Company" group
                "colorreaduser"
        );

        //can update colours
        colorUpdateUserId = dirtyData.createUserUuid();
        pwd = encryption.getPasswordHash(colorUpdateUserId, colorUpdateUserPassword);
        executePrepared("insert into sec_user(id, version, login, password, password_encryption, group_id, login_lc) " +
                        "values(?, ?, ?, ?, ?, ?, ?)",
                colorUpdateUserId,
                1l,
                colorUpdateUserLogin,
                pwd,
                encryption.getHashMethod(),
                groupUuid, //"Company" group
                "colorupdateuser"
        );

        //can create colours
        colorCreateUserId = dirtyData.createUserUuid();
        pwd = encryption.getPasswordHash(colorCreateUserId, colorCreateUserPassword);
        executePrepared("insert into sec_user(id, version, login, password, password_encryption, group_id, login_lc) " +
                        "values(?, ?, ?, ?, ?, ?, ?)",
                colorCreateUserId,
                1l,
                colorCreateUserLogin,
                pwd,
                encryption.getHashMethod(),
                groupUuid, //"Company" group
                "colorcreateuser"
        );

        //can delete colours
        colorDeleteUserId = dirtyData.createUserUuid();
        pwd = encryption.getPasswordHash(colorDeleteUserId, colorDeleteUserPassword);
        executePrepared("insert into sec_user(id, version, login, password, password_encryption, group_id, login_lc) " +
                        "values(?, ?, ?, ?, ?, ?, ?)",
                colorDeleteUserId,
                1l,
                colorDeleteUserLogin,
                pwd,
                encryption.getHashMethod(),
                groupUuid, //"Company" group
                "colordeleteuser"
        );

        //can read cars, used for attributes access testing
        carReadUserId = dirtyData.createUserUuid();
        pwd = encryption.getPasswordHash(carReadUserId, carReadUserPassword);
        executePrepared("insert into sec_user(id, version, login, password, password_encryption, group_id, login_lc) " +
                        "values(?, ?, ?, ?, ?, ?, ?)",
                carReadUserId,
                1l,
                carReadUserLogin,
                pwd,
                encryption.getHashMethod(),
                groupUuid, //"Company" group
                "carreaduser"
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

}
