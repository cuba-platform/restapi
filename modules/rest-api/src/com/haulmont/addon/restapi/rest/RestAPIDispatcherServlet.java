/*
 * Copyright (c) 2008-2019 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.addon.restapi.rest;

import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.CubaXmlWebApplicationContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.annotation.Nonnull;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.io.File;

public class RestAPIDispatcherServlet extends DispatcherServlet {

    private static final Logger log = LoggerFactory.getLogger(RestAPIDispatcherServlet.class);

    private static final long serialVersionUID = -4884517938479910144L;

    public static final String SPRING_CONTEXT_CONFIG = "cuba.restSpringContextConfig";

    /*
     * The field is used to prevent double initialization of the servlet.
     * Double initialization might occur during single WAR deployment when we call the method from initializer.
     */
    protected volatile boolean initialized = false;

    @Override
    public String getContextConfigLocation() {
        log.debug("getContextConfigLocation - start:");
        String configProperty = AppContext.getProperty(SPRING_CONTEXT_CONFIG);
        if (StringUtils.isBlank(configProperty)) {
            throw new IllegalStateException("Missing " + SPRING_CONTEXT_CONFIG + " application property");
        }
        File baseDir = new File(AppContext.getProperty("cuba.confDir"));

        StringTokenizer tokenizer = new StringTokenizer(configProperty);
        String[] tokenArray = tokenizer.getTokenArray();
        StringBuilder locations = new StringBuilder();
        for (String token : tokenArray) {
            String location;
            if (ResourceUtils.isUrl(token)) {
                location = token;
            } else {
                if (token.startsWith("/"))
                    token = token.substring(1);
                File file = new File(baseDir, token);
                if (file.exists()) {
                    location = file.toURI().toString();
                } else {
                    location = "classpath:" + token;
                }
            }
            locations.append(location).append(" ");
        }
        return locations.toString();
    }

    @Nonnull
    @Override
    protected WebApplicationContext initWebApplicationContext() {
        WebApplicationContext wac = findWebApplicationContext();
        if (wac == null) {
            ApplicationContext parent = AppContext.getApplicationContext();
            wac = createWebApplicationContext(parent);
        }

        onRefresh(wac);

        // Publish the context as a servlet context attribute.
        String attrName = getServletContextAttributeName();
        getServletContext().setAttribute(attrName, wac);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Published WebApplicationContext of servlet '" + getServletName() +
                    "' as ServletContext attribute with name [" + attrName + "]");
        }

        return wac;
    }

    @Nonnull
    @Override
    public Class<?> getContextClass() {
        return CubaXmlWebApplicationContext.class;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        if (!initialized) {
            super.init(config);
            initialized = true;
        }
    }
}