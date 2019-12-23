/*
 * Copyright (c) 2008-2018 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package spec.rest.demo.rest

import com.haulmont.masquerade.restapi.ServiceGenerator
import spec.rest.demo.rest.service.NotFoundUrlService
import spock.lang.Specification

import static com.haulmont.masquerade.Connectors.RestApiHost
import static com.haulmont.masquerade.Connectors.restApi

class NotFoundUrlFT extends Specification {
    static final PORTAL_REST_API_BASE_URL = 'http://localhost:8080/app-portal/rest/v2/'

    def "REST-API returns 404 for incorrect URL"() {
        def host = new RestApiHost("admin", "admin", PORTAL_REST_API_BASE_URL)
        def notFoundUrlService = restApi(NotFoundUrlService.class, host)

        when:
        def notFoundResponse = notFoundUrlService.notFound().execute()

        then:
        notFoundResponse.code() == 404
    }

    def "REST-API returns 404 for incorrect URL if unauthorized"() {
        def unauthorizedService = ServiceGenerator.createService(PORTAL_REST_API_BASE_URL, NotFoundUrlService.class)

        when:
        def unauthorizedResponse = unauthorizedService.notFound().execute()

        then:
        unauthorizedResponse.code() == 404
    }
}