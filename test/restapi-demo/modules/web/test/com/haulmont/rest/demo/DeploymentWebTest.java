package com.haulmont.rest.demo;

import com.haulmont.rest.demo.core.app.PortalTestService;
import org.junit.Before;
import org.junit.Test;

import static com.haulmont.rest.demo.RestUtils.*;
import static org.junit.Assert.assertNotNull;

public class DeploymentWebTest {

    protected static final String URI_BASE_WEB = "http://localhost:8080/app/rest/v2";

    protected String oauthTokenWeb;

    @Before
    public void beforeEach() throws Exception {
        oauthTokenWeb = getAuthToken(URI_BASE_WEB);
    }

    @Test
    public void getTokenWeb() {
        assertNotNull(oauthTokenWeb);
    }

    @Test
    public void loadSomeListWeb() throws Exception {
        String url = URI_BASE_WEB + "/entities/sec$User";
        loadSomeList(url, oauthTokenWeb);
    }

    @Test
    public void executeQueryWeb() throws Exception {
        String url = URI_BASE_WEB + "/queries/sec$User/currentUser";
        executeQuery(url, oauthTokenWeb);
    }

    @Test
    public void executeServiceWeb() throws Exception {
        String url = URI_BASE_WEB + "/services/" + PortalTestService.NAME + "/sum";
        executeService(url, oauthTokenWeb);
    }
}
