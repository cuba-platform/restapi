package com.haulmont.addon.restapi.config;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.DefaultBoolean;

public interface RestConfig extends Config {

    /**
     * @return whether to store REST API OAuth tokens in the database
     */
    @Property("cuba.rest.storeTokensInDb")
    @Source(type = SourceType.DATABASE)
    @DefaultBoolean(false)
    boolean getRestStoreTokensInDb();
}
