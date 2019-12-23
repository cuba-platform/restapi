package spec.rest.demo.rest

import com.haulmont.cuba.security.entity.PermissionType
import com.haulmont.cuba.security.entity.RoleType
import com.haulmont.rest.demo.http.api.DataSet
import groovy.sql.Sql
import org.apache.http.HttpStatus
import spock.lang.Specification

import static org.hamcrest.CoreMatchers.notNullValue
import static org.hamcrest.CoreMatchers.nullValue
import static spec.rest.demo.rest.DataUtils.*
import static spec.rest.demo.rest.DbUtils.getSql
import static spec.rest.demo.rest.RestSpecsUtils.createRequest
import static spec.rest.demo.rest.RestSpecsUtils.getAuthToken

class StrictlyDenyingRoleEntityControllerFT extends Specification {

    private Sql sql
    private DataSet dirtyData = new DataSet()

    private String userPassword = "password"
    private String userLogin = "user1"
    private String userToken

    private UUID carId

    void setup() {
        sql = getSql() as Sql

        def groupId = createGroup(dirtyData, sql, 'Group')

        UUID userId = createUser(dirtyData, sql,
                userLogin, userPassword, groupId)

        UUID roleId = createRole(dirtyData, sql, 'TestStrictDenyingRole', RoleType.STRICTLY_DENYING)

        //access REST API
        createPermission(dirtyData, sql, roleId, PermissionType.SPECIFIC, 'cuba.restApi.enabled', 1)

        //read ref_Car
        createPermission(dirtyData, sql, roleId, PermissionType.ENTITY_OP, 'ref_Car:read', 1)

        //read ref_Car vin attribute
        createPermission(dirtyData, sql, roleId, PermissionType.ENTITY_ATTR, 'ref_Car:vin', 1)

        createUserRole(dirtyData, sql, userId, roleId)

        UUID colorId = createColor(dirtyData, sql, 'Black')

        carId = createCarWithColour(dirtyData, sql, 'carVin1', colorId)

        userToken = getAuthToken(userLogin, userPassword)
    }

    void cleanup() {
        dirtyData.cleanup(sql.connection)
        if (sql != null) {
            sql.close()
        }
    }

    def "load car with restricted attributes. only vin attribute is allowed"() {
        when:

        def request = createRequest(userToken).param('view', 'carEdit')
        def response = request.with().get("/entities/ref_Car/$carId")

        then:

        response.then().statusCode(HttpStatus.SC_OK)
                .body('vin', notNullValue())
                .body("id", notNullValue())
                .body("createTs", nullValue())
                .body("colour", nullValue())
    }
}
