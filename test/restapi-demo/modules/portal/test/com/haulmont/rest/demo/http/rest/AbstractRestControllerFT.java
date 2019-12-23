/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.http.rest;

import com.haulmont.rest.demo.http.api.DataSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;

import java.sql.Connection;
import java.sql.DriverManager;

import static com.haulmont.rest.demo.http.rest.RestTestUtils.getAuthToken;

@Ignore
public abstract class AbstractRestControllerFT {

    protected static final String DB_URL = "jdbc:hsqldb:hsql://localhost:9010/rest_demo";

    protected Connection conn;
    protected DataSet dirtyData = new DataSet();
    protected String oauthToken;

    @Before
    public void setUp() throws Exception {
        oauthToken = getAuthToken("admin", "admin");
        Class.forName("org.hsqldb.jdbc.JDBCDriver");
        conn = DriverManager.getConnection(DB_URL, "sa", "");
        prepareDb();
    }

    @After
    public void tearDown() throws Exception {
        dirtyData.cleanup(conn);
        if (conn != null) {
            conn.close();
        }
    }

    public void prepareDb() throws Exception {}
}
