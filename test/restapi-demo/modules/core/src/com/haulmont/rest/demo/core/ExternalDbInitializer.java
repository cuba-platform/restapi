/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.core;

import com.google.common.base.Splitter;
import com.haulmont.bali.db.QueryRunner;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.InputStream;

@Component
public class ExternalDbInitializer implements AppContext.Listener {

    private static final Logger log = LoggerFactory.getLogger(ExternalDbInitializer.class);

    public ExternalDbInitializer() {
        AppContext.addListener(this);
    }

    @Override
    public void applicationStarted() {
        createDbSchema();
    }

    @Override
    public void applicationStopped() {
    }

    public void createDbSchema() {
        try {
            QueryRunner queryRunner = new QueryRunner(AppBeans.get("cubaDataSource_db1", DataSource.class));

            InputStream stream = getClass().getResourceAsStream("/com/haulmont/rest/demo/core/entity/multidb/create-db.sql");
            String content = IOUtils.toString(stream);
            for (String sql : Splitter.on("^").omitEmptyStrings().trimResults().split(content)) {
                log.debug("Executing SQL on DB1:\n" + sql);
                queryRunner.update(sql);
            }
        } catch (Exception e) {
            log.error("Cannot create external DB schema", e);
        }
    }
}
