package spec.rest.demo.rest


import spock.lang.Specification

import static spec.rest.demo.rest.RestSpecsUtils.createRequest
import static spec.rest.demo.rest.RestSpecsUtils.getAuthToken

class EntitiesControllerFT extends Specification {

    private String userPassword = "admin"
    private String userLogin = "admin"
    private String userToken


    void setup() {
        userToken = getAuthToken(userLogin, userPassword)
    }

    def "GET-request with filter for obtaining the count of entities"() {
        def param = [
                'conditions': [
                        [
                                'property': 'login',
                                'operator': 'notEmpty'
                        ]
                ]
        ]

        when:
        def request = createRequest(userToken).param("filter", param)
        def response = request.with().get("/entities/sec\$User/search/count")

        then:
        response.statusCode() == 200
        response.body.as(Integer) == 2
    }

    def "POST-request with filter for obtaining the count of entities"() {
        def body = [
                'filter': [
                        'conditions': [
                                [
                                        'property': 'login',
                                        'operator': '=',
                                        'value'   : "admin"
                                ]
                        ]
                ]
        ]

        when:
        def request = createRequest(userToken).body(body)
        def response = request.with().post("/entities/sec\$User/search/count")

        then:
        response.statusCode() == 200
        response.body.as(Integer) == 1
    }
}
