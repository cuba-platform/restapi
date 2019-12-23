package com.haulmont.rest.demo.http.rest;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.junit.Test;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static com.haulmont.rest.demo.http.rest.RestTestUtils.*;
import static org.junit.Assert.*;

public class UserSessionControllerFT extends AbstractRestControllerFT {

    @Test
    public void setSessionLocale() throws Exception {
        setSessionLocale(
            "en",
            oauthToken,
            response -> assertEquals(HttpStatus.SC_OK, statusCode(response))
        );
    }

    @Test
    public void setSessionLocaleUnauthorized() throws Exception {
        setSessionLocale(
            "en",
            null,
            response -> assertEquals(HttpStatus.SC_UNAUTHORIZED, statusCode(response))
        );
    }

    @Test
    public void setSessionLocaleUnsupported() throws Exception {
        setSessionLocale(
            "a string representing unsupported locale",
            oauthToken,
            response -> assertEquals(HttpStatus.SC_UNPROCESSABLE_ENTITY, statusCode(response))
        );
    }

    private void setSessionLocale(
        String locale, @Nullable String token, Consumer<CloseableHttpResponse> assertionsCallback
    ) throws Exception {
        String url = "/user-session/locale";

        Map<String, String> headers = new HashMap<>();
        headers.put(HttpHeaders.ACCEPT_LANGUAGE, locale);

        try (CloseableHttpResponse response = sendPutWithHeaders(url, token, "", null, headers)) {
            assertionsCallback.accept(response);
        }
    }
}
