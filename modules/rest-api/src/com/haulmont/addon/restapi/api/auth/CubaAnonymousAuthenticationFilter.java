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
 */

package com.haulmont.addon.restapi.api.auth;

import com.haulmont.addon.restapi.api.common.RestParseUtils;
import com.haulmont.addon.restapi.api.config.RestApiConfig;
import com.haulmont.addon.restapi.api.config.RestQueriesConfiguration;
import com.haulmont.addon.restapi.api.config.RestServicesConfiguration;
import com.haulmont.addon.restapi.api.sys.CachingHttpServletRequestWrapper;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.app.TrustedClientService;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This filter is used for anonymous access to CUBA REST API. If no Authorization header presents in the request and if
 * {@link RestApiConfig#getRestAnonymousEnabled()} is true, then the anonymous user session will be set to the {@link
 * SecurityContext} and the request will be authenticated. This filter must be invoked after the {@link
 * org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationProcessingFilter}
 */
public class CubaAnonymousAuthenticationFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(CubaAnonymousAuthenticationFilter.class);

    protected static final String GET = "GET";
    protected static final String POST = "POST";

    protected static final String QUERIES = "queries";
    protected static final String SERVICES = "services";

    protected static final String SERVICE_URL_REGEX = "/v2/(services)/([a-zA-Z_][a-zA-Z\\d_$]*)/([a-zA-Z_][a-zA-Z\\d_]*)";
    protected static final String QUERY_URL_REGEX = "/v2/(queries)/([a-zA-Z_][a-zA-Z\\d_$]*)/([a-zA-Z_][a-zA-Z\\d_]*)(/count)?";
    protected static final Pattern REGEX_PATTERN = Pattern.compile("^" + SERVICE_URL_REGEX + "|" + QUERY_URL_REGEX + "$");

    @Inject
    protected RestApiConfig restApiConfig;

    @Inject
    protected RestServicesConfiguration restServicesConfiguration;

    @Inject
    protected RestQueriesConfiguration restQueriesConfiguration;

    @Inject
    protected TrustedClientService trustedClientService;

    @Inject
    protected RestParseUtils restParseUtils;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // do nothing
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        ServletRequest nextRequest = request;
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            if (restApiConfig.getRestAnonymousEnabled()) {
                populateSecurityContextWithAnonymousSession();
            } else {
                //anonymous service method or query may be invoked
                String pathInfo = ((HttpServletRequest) request).getPathInfo();
                String methodType = ((HttpServletRequest) request).getMethod();
                Matcher matcher = REGEX_PATTERN.matcher(pathInfo);
                if (matcher.matches()) {
                    if (SERVICES.equals(matcher.group(1))) {
                        List<String> methodParamNames;
                        String serviceName = matcher.group(2);
                        String methodName = matcher.group(3);
                        if (GET.equals(methodType)) {
                            methodParamNames = Collections.list(request.getParameterNames());
                        } else if (POST.equals(methodType)) {
                            //wrap the request using content caching request wrapper because we need to access the
                            //request body
                            nextRequest = new CachingHttpServletRequestWrapper((HttpServletRequest) request);
                            try {
                                String json = IOUtils.toString(nextRequest.getReader());
                                Map<String, String> paramsMap = restParseUtils.parseParamsJson(json);
                                methodParamNames = new ArrayList<>(paramsMap.keySet());
                            } catch (IOException e) {
                                log.error("Error on reading request body", e);
                                throw e;
                            }
                        } else {
                            chain.doFilter(nextRequest, response);
                            return;
                        }
                        RestServicesConfiguration.RestMethodInfo restMethodInfo = restServicesConfiguration
                                .getRestMethodInfo(serviceName, methodName, methodParamNames);
                        if (restMethodInfo != null && restMethodInfo.isAnonymousAllowed()) {
                            populateSecurityContextWithAnonymousSession();
                        }
                    } else if (QUERIES.equals(matcher.group(4))) {
                        String entityName = matcher.group(5);
                        String queryName = matcher.group(6);
                        if (GET.equals(methodType) || POST.equals(methodType)) {
                            RestQueriesConfiguration.QueryInfo restQueryInfo = restQueriesConfiguration.getQuery(entityName, queryName);
                            if (restQueryInfo != null && restQueryInfo.isAnonymousAllowed()) {
                                populateSecurityContextWithAnonymousSession();
                            }
                        }
                    }
                }
            }
        } else {
            log.debug("SecurityContextHolder not populated with cuba anonymous token, as it already contained: '{}'",
                    SecurityContextHolder.getContext().getAuthentication());
        }
        chain.doFilter(nextRequest, response);
    }

    protected void populateSecurityContextWithAnonymousSession() {
        UserSession anonymousSession;
        try {
            anonymousSession = trustedClientService.getAnonymousSession(restApiConfig.getTrustedClientPassword());
        } catch (LoginException e) {
            throw new RuntimeException("Unable to obtain anonymous session for REST", e);
        }

        CubaAnonymousAuthenticationToken anonymousAuthenticationToken =
                new CubaAnonymousAuthenticationToken("anonymous", AuthorityUtils.createAuthorityList("ROLE_CUBA_ANONYMOUS"));
        SecurityContextHolder.getContext().setAuthentication(anonymousAuthenticationToken);
        AppContext.setSecurityContext(new SecurityContext(anonymousSession));
    }

    @Override
    public void destroy() {
    }
}