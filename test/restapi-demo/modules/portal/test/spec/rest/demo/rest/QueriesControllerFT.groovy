package spec.rest.demo.rest


import com.haulmont.rest.demo.core.entity.DriverStatus
import com.haulmont.rest.demo.http.api.DataSet
import groovy.sql.Sql
import spock.lang.Specification

import static spec.rest.demo.rest.DataUtils.createDriver
import static spec.rest.demo.rest.DbUtils.getSql
import static spec.rest.demo.rest.RestSpecsUtils.createRequest
import static spec.rest.demo.rest.RestSpecsUtils.getAuthToken

class QueriesControllerFT extends Specification {

    private DataSet dirtyData = new DataSet()
    private Sql sql

    private String userLogin = 'admin'
    private String userPassword = 'admin'
    private String userToken

    private UUID driver1Id
    private UUID driver2Id

    void setup() {
        userToken = getAuthToken(userLogin, userPassword)

        sql = getSql() as Sql

        driver1Id = createDriver(dirtyData, sql, "Bob", DriverStatus.ACTIVE)
        driver2Id = createDriver(dirtyData, sql, "John", DriverStatus.RETIRED)
    }

    void cleanup() {
        dirtyData.cleanup(sql.connection)
        if (sql != null) {
            sql.close()
        }
    }

    def "Execute a query with an enumeration parameter"() {

        when:
        def request = createRequest(userToken).param('status', 'ACTIVE')
        def response = request.with().get("/queries/ref\$Driver/getDriversByStatus")

        then:
        response.statusCode() == 200
        response.jsonPath().getList("").size() == 1

        def driver = response.jsonPath().getList("").first()

        driver['id'] == driver1Id.toString()
        driver['name'] == 'Bob'
        driver['status'] == 'ACTIVE'
    }
}
