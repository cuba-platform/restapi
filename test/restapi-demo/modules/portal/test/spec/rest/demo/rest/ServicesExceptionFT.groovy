package spec.rest.demo.rest

import spock.lang.Specification

import static spec.rest.demo.rest.RestSpecsUtils.createRequest
import static spec.rest.demo.rest.RestSpecsUtils.getAuthToken

class ServicesExceptionFT extends Specification {

    private String adminUser = 'admin'
    private String adminPassword = 'admin'

    private String adminToken

    void setup() {
        adminToken = getAuthToken(adminUser, adminPassword)
    }

    def "GET-request for the call service with a custom exception"() {
        when:
        def response = createRequest(adminToken)
                .when()
                .pathParam('serviceName', 'restdemo_PortalTestService')
                .pathParam('methodName', 'methodWithCustomException')
                .get('services/{serviceName}/{methodName}')

        then:
        response.statusCode() == 418
        response.path('error') == "I'm a teapot"
        response.path('details') == 'Server is not a coffee machine'
    }

    def "GET-request for the call service with a exception"() {
        when:
        def response = createRequest(adminToken)
                .when()
                .pathParam('serviceName', 'restdemo_PortalTestService')
                .pathParam('methodName', 'methodWithException')
                .get('services/{serviceName}/{methodName}')

        then:
        response.statusCode() == 500
        response.path('error') == "Server error"
        response.path('details') == ''
    }
}