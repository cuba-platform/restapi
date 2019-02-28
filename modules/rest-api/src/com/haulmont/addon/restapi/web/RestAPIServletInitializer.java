package com.haulmont.addon.restapi.web;

import com.haulmont.addon.restapi.rest.RestAPIDispatcherServlet;
import com.haulmont.cuba.core.sys.AbstractWebAppContextLoader;
import com.haulmont.cuba.core.sys.servlet.ServletRegistrationManager;
import com.haulmont.cuba.core.sys.servlet.events.ServletContextInitializedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.FrameworkServlet;

import javax.inject.Inject;
import javax.servlet.*;
import java.util.EnumSet;

@Component(RestAPIServletInitializer.NAME)
public class RestAPIServletInitializer {

    private static final Logger log = LoggerFactory.getLogger(RestAPIServletInitializer.class);

    public static final String NAME = "restapi_RestAPIServletInitializer";

    protected static final String SERVLET_NAME = "rest_api";
    protected static final String SERVLET_MAPPING = "/restapi/*";

    @Inject
    protected ServletRegistrationManager servletRegistrationManager;

    @EventListener
    protected void init(ServletContextInitializedEvent event) {
        ApplicationContext appCtx = event.getApplicationContext();
        Servlet servlet = servletRegistrationManager.createServlet(appCtx, RestAPIDispatcherServlet.class.getName());

        ServletContext servletCtx = event.getSource();
        try {
            servlet.init(new AbstractWebAppContextLoader.CubaServletConfig(SERVLET_NAME, servletCtx));
        } catch (ServletException e) {
            throw new RuntimeException("An error occurred while initializing " + SERVLET_NAME + " servlet", e);
        }

        servletCtx.addServlet(SERVLET_NAME, servlet).addMapping(SERVLET_MAPPING);

        DelegatingFilterProxy springSecurityFilterChain = new DelegatingFilterProxy();
        springSecurityFilterChain.setContextAttribute(FrameworkServlet.SERVLET_CONTEXT_PREFIX + SERVLET_NAME);
        springSecurityFilterChain.setTargetBeanName("springSecurityFilterChain");

        FilterRegistration.Dynamic springSecurityFilterChainReg =
                servletCtx.addFilter("restapiSpringSecurityFilterChain", springSecurityFilterChain);

        springSecurityFilterChainReg.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, SERVLET_MAPPING);
    }
}
