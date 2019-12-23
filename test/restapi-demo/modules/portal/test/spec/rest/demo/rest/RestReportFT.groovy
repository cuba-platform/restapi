/*
 * Copyright (c) 2008-2018 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package spec.rest.demo.rest

import com.haulmont.masquerade.Connectors
import com.haulmont.rest.demo.http.api.DataSet
import com.haulmont.rest.demo.http.rest.jmx.ImportReportsService
import groovy.sql.Sql
import org.apache.commons.io.IOUtils
import org.apache.http.HttpStatus
import spock.lang.Specification

import static spec.rest.demo.rest.DataUtils.*
import static spec.rest.demo.rest.DbUtils.getSql
import static spec.rest.demo.rest.RestSpecsUtils.createRequest
import static spec.rest.demo.rest.RestSpecsUtils.getAuthToken

class RestReportFT extends Specification {
    private Sql sql
    private DataSet dirtyData = new DataSet()

    private String report1Id = '065fe01e-4049-e2c6-55d3-37b46c41a697',
                   report2Id = 'd32eeceb-18f3-0211-5f1e-c6c686ce317d',
                   report3Id = 'e609a0f1-1dab-7367-6a8a-664a467106b7';
    private String userPassword = 'password'
    private String userLogin = 'user1'
    private String userToken
    private UUID userId

    void setup() {
        //import reports using jmx bean
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        IOUtils.copy(RestReportFT.class.getResourceAsStream('/spec/rest/demo/rest/Reports.zip'), outputStream)
        Connectors.jmx(ImportReportsService.class).importReports(outputStream.toByteArray())

        sql = getSql() as Sql

        def groupId = createGroup(dirtyData, sql, 'Group')

        userId = createUser(dirtyData, sql,
                userLogin, userPassword, groupId)

        createRole(dirtyData, sql, 'ReportRole')

        userToken = getAuthToken(userLogin, userPassword)

        RestSpecsUtils.setReportsBasePath()
    }

    void cleanup() {
        RestSpecsUtils.clearBasePath()
        def importReportsService = Connectors.jmx(ImportReportsService.class)
        importReportsService.removeReport(report1Id)
        importReportsService.removeReport(report2Id)
        importReportsService.removeReport(report3Id)
        dirtyData.cleanup(sql.connection)
        if (sql != null) {
            sql.close()
        }
    }

//    def """Get reports"""() {
//        when:
//
//        def request = createRequest(userToken)
//
//        def response = request.with().get("/report")
//
//        then:
//
//        response.then().statusCode(HttpStatus.SC_OK)
//                .body('name', hasItems('User Report #1'))
//                .body('name', not(hasItems('User Report #2', 'User Report #3')))
//    }

//    def "Get single report"() {
//        when:
//
//        def request = createRequest(userToken)
//
//        def response = request.with().get("/report/$report1Id")
//
//        then:
//
//        response.then().statusCode(HttpStatus.SC_OK)
//                .body('name', equalTo('User Report #1'))
//                .body('templates.code', hasItem('DEFAULT'))
//                .body('inputParameters.name', hasItem('User'))
//                .body('inputParameters.type', hasItem('ENTITY_LIST'))
//                .body('inputParameters.entityMetaClass', hasItem('sec$User'))
//                .body('inputParameters.alias', hasItem('entities'))
//    }

//    def "Get single report without user access rights"() {
//        when:
//
//        def request = createRequest(userToken)
//
//        def response = request.with().get("/report/$report2Id")
//
//        then:
//
//        response.then().statusCode(HttpStatus.SC_NOT_FOUND)
//
//        when:
//
//        request = createRequest(userToken)
//
//        response = request.with().get("/report/$report3Id")
//
//        then:
//
//        response.then().statusCode(HttpStatus.SC_NOT_FOUND)
//    }

//    def "Run report"() {
//        when:
//
//        def body = ['parameters':
//                            [
//                                    [
//                                            'name' : 'entities',
//                                            'values': [userId.toString()]
//                                    ]
//                            ]
//
//        ]
//
//        def request = createRequest(userToken).body(body)
//
//        def response = request.with().post("/run/$report1Id")
//
//        then:
//
//        response.then().statusCode(HttpStatus.SC_OK)
//                .assertThat().header("Content-Disposition", containsString("filename=\"" + URLEncodeUtils.encodeUtf8("Report for entity \"User\".docx") + "\""))
//        response.then().statusCode(HttpStatus.SC_OK)
//                .assertThat().header("Content-Disposition", containsString("inline"))
//
//        when:
//
//        body = ['attachment' : 'true',
//                'parameters':
//                            [
//                                    [
//                                            'name' : 'entities',
//                                            'values': [userId.toString()]
//                                    ]
//                            ]
//        ]
//
//        request = createRequest(userToken).body(body)
//
//        response = request.with().post("/run/$report1Id")
//
//        then:
//
//        response.then().statusCode(HttpStatus.SC_OK)
//                .assertThat().header("Content-Disposition", containsString("filename=\"" + URLEncodeUtils.encodeUtf8("Report for entity \"User\".docx") + "\""))
//        response.then().statusCode(HttpStatus.SC_OK)
//                .assertThat().header("Content-Disposition", containsString("attachment"))
//    }
//
//    def "Run report without required params"() {
//        when:
//
//        def body = ['attachment' : 'true',
//                'parameters': []
//        ]
//
//        def request = createRequest(userToken).body(body)
//
//        def response = request.with().post("/run/$report1Id")
//
//        then:
//
//        response.then().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
//                .assertThat().body("details", containsString("Required report parameter \"entities\" not found"))
//    }

    def "Run report without user access rights"() {
        when:

        def request = createRequest(userToken).body([])

        def response = request.with().post("/run/$report2Id")

        then:

        response.then().statusCode(HttpStatus.SC_NOT_FOUND)

        when:

        request = createRequest(userToken).body([])

        response = request.with().post("/run/$report3Id")

        then:

        response.then().statusCode(HttpStatus.SC_NOT_FOUND)
    }
}
