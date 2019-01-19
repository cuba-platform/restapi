package com.haulmont.addon.restv2.web;

import com.haulmont.addon.restv2.restapi.Restv2DispatcherServlet;
import com.haulmont.cuba.core.sys.AbstractWebAppContextLoader;
import com.haulmont.cuba.core.sys.servlet.ServletRegistrationManager;
import com.haulmont.cuba.core.sys.servlet.events.ServletContextInitializedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.servlet.*;

@Component(Restv2ServletInitializer.NAME)
public class Restv2ServletInitializer {

    private static final Logger log = LoggerFactory.getLogger(Restv2ServletInitializer.class);

    public static final String NAME = "restv2_Restv2ServletInitializer";

    protected static final String SERVLET_NAME = "restv2";
    protected static final String SERVLET_MAPPING = "/restv2/*";

    @Inject
    protected ServletRegistrationManager servletRegistrationManager;

    @EventListener
    protected void init(ServletContextInitializedEvent event) {
        log.debug("init - start");
        ApplicationContext appCtx = event.getApplicationContext();
        Servlet servlet = servletRegistrationManager.createServlet(appCtx, Restv2DispatcherServlet.class.getName());

        ServletContext servletCtx = event.getSource();
        try {
            servlet.init(new AbstractWebAppContextLoader.CubaServletConfig(SERVLET_NAME, servletCtx));
        } catch (ServletException e) {
            throw new RuntimeException("An error occurred while initializing " + SERVLET_NAME + " servlet", e);
        }

        servletCtx.addServlet(SERVLET_NAME, servlet).addMapping(SERVLET_MAPPING);

//        DelegatingFilterProxy jpaWebApiSpringSecurityFilterChain = new DelegatingFilterProxy();
//        jpaWebApiSpringSecurityFilterChain.setContextAttribute(FrameworkServlet.SERVLET_CONTEXT_PREFIX + SERVLET_NAME);
//        jpaWebApiSpringSecurityFilterChain.setTargetBeanName("springSecurityFilterChain");
//
//        FilterRegistration.Dynamic springSecurityFilterChainReg =
//                servletCtx.addFilter("jpaWebApiSpringSecurityFilterChain", jpaWebApiSpringSecurityFilterChain);
//
//        springSecurityFilterChainReg.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, SERVLET_MAPPING);
    }
}
