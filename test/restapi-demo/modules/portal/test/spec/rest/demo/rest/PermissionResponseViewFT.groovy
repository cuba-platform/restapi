package spec.rest.demo.rest

import com.haulmont.cuba.security.entity.PermissionType
import com.haulmont.rest.demo.http.api.DataSet
import groovy.sql.Sql
import spock.lang.Specification

import static spec.rest.demo.rest.DataUtils.*
import static spec.rest.demo.rest.DbUtils.getSql
import static spec.rest.demo.rest.RestSpecsUtils.createRequest
import static spec.rest.demo.rest.RestSpecsUtils.getAuthToken

class PermissionResponseViewFT extends Specification {

    private Sql sql
    private DataSet dirtyData

    private UUID carId
    private UUID insuranceCase1Id
    private UUID insuranceCase2Id
    private UUID insuranceCase3Id

    private String adminPassword = "admin"
    private String adminLogin = "admin"

    private String userLogin = "user"
    private String userPassword = "password"

    private String adminToken
    private String userToken

    final class EndPoints {
        public static final String ENTITIES_NAME = "/entities/{entityName}"
        public static final String ENTITIES_NAME_ID = "/entities/{entityName}/{entityId}"
    }


    void setup() {
        sql = getSql()
        dirtyData = new DataSet()

        carId = createCar(dirtyData, sql, '001')
        insuranceCase1Id = createInsuranceCase(dirtyData, sql, 'Insurance case', carId)
        insuranceCase2Id = createInsuranceCase(dirtyData, sql, 'Insurance case', carId)
        insuranceCase3Id = createInsuranceCase(dirtyData, sql, 'Insurance case', carId)

        createUser(dirtyData, sql)

        adminToken = getAuthToken(adminLogin, adminPassword)
        userToken = getAuthToken(userLogin, userPassword)
    }

    private void createUser(DataSet dirtyData, Sql sql) {
        def userId = createUser(dirtyData, sql, userLogin, userPassword, COMPANY_GROUP_ID)
        def roleId = createRole(dirtyData, sql, 'Limited role')

        createPermission(dirtyData, sql, roleId, PermissionType.ENTITY_ATTR, 'ref_Car:vin', 0)
        createUserRole(dirtyData, sql, userId, roleId)
    }

    void cleanup() {
        dirtyData.cleanup(sql.connection)
        if (sql != null) {
            sql.close()
        }
    }

    def "POST-request for add new insurance case with 'responseView' parameter"() {
        def newInsuranceCase = 'New insurance case'

        def body = [
                "description": newInsuranceCase,
                "car"        : [
                        "id": carId.toString()
                ]
        ]

        when:
        def response = createRequest(adminToken)
                .queryParam('responseView', 'insuranceCase-with-car')
                .body(body)
                .when()
                .pathParam('entityName', 'ref$InsuranceCase')
                .post(EndPoints.ENTITIES_NAME)

        then:
        response.statusCode() == 201
        response.path("description") == newInsuranceCase
        response.path("car.vin") == "001"

        def id = UUID.fromString(response.path("id"))
        dirtyData.addInsuranceCaseId(id)
    }

    def "POST-request for add new insurance case with 'responseView' parameter from the limited role"() {
        def newInsuranceCase = 'New insurance case'

        def body = [
                "description": newInsuranceCase,
                "car"        : [
                        "id": carId.toString()
                ]
        ]

        when:
        def response = createRequest(userToken)
                .queryParam('responseView', 'insuranceCase-with-car')
                .body(body)
                .when()
                .pathParam('entityName', 'ref$InsuranceCase')
                .post(EndPoints.ENTITIES_NAME)

        then:
        response.statusCode() == 201
        response.path("description") == newInsuranceCase
        response.path("car.vin") == null

        def id = UUID.fromString(response.path("id"))
        dirtyData.addInsuranceCaseId(id)
    }


    def "PUT-request for update new insurance case with 'responseView' parameter"() {
        def description = 'Updated insurance case'

        def body = [
                "description": description
        ]

        when:
        def response = createRequest(adminToken)
                .queryParam('responseView', 'insuranceCase-with-car')
                .body(body)
                .when()
                .pathParams(entityName: 'ref$InsuranceCase', entityId: insuranceCase1Id)
                .put(EndPoints.ENTITIES_NAME_ID)

        then:
        response.statusCode == 200
        response.path("description") == description
        response.path("car.vin") == "001"
    }

    def "POST-request for multiple add new insurance case with 'responseView' parameter from the limited role"() {
        def newInsuranceCase = 'New insurance case'

        def body = [
                [
                        "description": newInsuranceCase,
                        "car"        : [
                                "id": carId.toString()
                        ]
                ],
                [
                        "description": newInsuranceCase,
                        "car"        : [
                                "id": carId.toString()
                        ]
                ],
                [
                        "description": newInsuranceCase,
                        "car"        : [
                                "id": carId.toString()
                        ]
                ]
        ]

        when:
        def response = createRequest(userToken)
                .queryParam('responseView', 'insuranceCase-with-car')
                .body(body)
                .when()
                .pathParam('entityName', 'ref$InsuranceCase')
                .post(EndPoints.ENTITIES_NAME)

        then:
        response.statusCode() == 201
        response.path("description").findAll() { it == newInsuranceCase }.size() == 3
        response.path("car.vin").findAll() { it == null }.size() == 3

        response.path("id").each {
            dirtyData.addInsuranceCaseId(UUID.fromString(it.toString()))
        }
    }

    def "POST-request for multiple add new insurance case with 'responseView' parameter"() {
        def newInsuranceCase = 'New insurance case'

        def body = [
                [
                        "description": newInsuranceCase,
                        "car"        : [
                                "id": carId.toString()
                        ]
                ],
                [
                        "description": newInsuranceCase,
                        "car"        : [
                                "id": carId.toString()
                        ]
                ],
                [
                        "description": newInsuranceCase,
                        "car"        : [
                                "id": carId.toString()
                        ]
                ]
        ]

        when:
        def response = createRequest(adminToken)
                .queryParam('responseView', 'insuranceCase-with-car')
                .body(body)
                .when()
                .pathParam('entityName', 'ref$InsuranceCase')
                .post(EndPoints.ENTITIES_NAME)

        then:
        response.statusCode() == 201
        response.path("description").findAll() { it == newInsuranceCase }.size() == 3
        response.path("car.vin").findAll() { it == '001' }.size() == 3

        response.path("id").each {
            dirtyData.addInsuranceCaseId(UUID.fromString(it.toString()))
        }
    }

    def "PUT-request for update new insurance case with 'responseView' parameter from the limited role"() {
        def description = 'Updated insurance case'

        def body = [
                "description": description
        ]

        when:
        def response = createRequest(userToken)
                .queryParam('responseView', 'insuranceCase-with-car')
                .body(body)
                .when()
                .pathParams(entityName: 'ref$InsuranceCase', entityId: insuranceCase1Id)
                .put(EndPoints.ENTITIES_NAME_ID)

        then:
        response.statusCode == 200
        response.path("description") == description
        response.path("car.vin") == null
    }

    def "PUT-request for multiple update new insurance case with 'responseView' parameter from the limited role"() {
        def description = 'Updated insurance case'

        def body = [
                [
                        "id"         : insuranceCase1Id,
                        "description": description
                ],
                [
                        "id"         : insuranceCase2Id,
                        "description": description
                ],
                [
                        "id"         : insuranceCase3Id,
                        "description": description
                ]
        ]

        when:
        def response = createRequest(userToken)
                .queryParam('responseView', 'insuranceCase-with-car')
                .body(body)
                .when()
                .pathParams(entityName: 'ref$InsuranceCase')
                .put(EndPoints.ENTITIES_NAME)

        then:
        response.statusCode == 200
        response.path("description").findAll() { it == description }.size() == 3
        response.path("car.vin").findAll() { it == null }.size() == 3
    }

    def "PUT-request for multiple update new insurance case with 'responseView' parameter"() {
        def description = 'Updated insurance case'

        def body = [
                [
                        "id"         : insuranceCase1Id,
                        "description": description
                ],
                [
                        "id"         : insuranceCase2Id,
                        "description": description
                ],
                [
                        "id"         : insuranceCase3Id,
                        "description": description
                ]
        ]

        when:
        def response = createRequest(adminToken)
                .queryParam('responseView', 'insuranceCase-with-car')
                .body(body)
                .when()
                .pathParams(entityName: 'ref$InsuranceCase')
                .put(EndPoints.ENTITIES_NAME)

        then:
        response.statusCode == 200
        response.path("description").findAll() { it == description }.size() == 3
        response.path("car.vin").findAll() { it == '001' }.size() == 3
    }
}