package com.haulmont.rest.demo.http.rest;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.junit.Test;

import static com.haulmont.rest.demo.http.rest.RestTestUtils.*;
import static org.junit.Assert.*;

public class VersionControllerFT extends AbstractRestControllerFT {

    @Test
    public void getApiVersion() throws Exception {
        String url = "/version";
        try (CloseableHttpResponse response = sendGet(url, oauthToken, null)) {
            assertEquals(HttpStatus.SC_OK, statusCode(response));

            String version = responseToString(response);
            assertNotNull(version);
            assertTrue(version.length() > 0);
        }
    }
}
