/*
 * Copyright (c) 2008-2018 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package spec.rest.demo.rest

import groovy.sql.Sql

class DbUtils {
    static Sql getSql() {
        return Sql.newInstance('jdbc:hsqldb:hsql://localhost:9010/rest_demo',
                'sa', '', 'org.hsqldb.jdbc.JDBCDriver')
    }
}
