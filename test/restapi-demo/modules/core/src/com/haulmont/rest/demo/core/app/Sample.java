/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.core.app;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.app.ConfigStorage;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.Scripting;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.security.app.Authenticated;
import com.haulmont.cuba.security.app.Authentication;
import com.haulmont.rest.demo.core.app.attributeaccess.*;
import com.haulmont.rest.demo.core.entity.Car;
import com.haulmont.reports.app.service.ReportService;
import com.haulmont.reports.entity.Report;
import com.haulmont.reports.entity.ReportImportResult;
import groovy.lang.Binding;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Date;
import java.util.UUID;

@Component(SampleAPI.NAME)
public class Sample implements SampleAPI, SampleMBean {

    @Inject
    protected Scripting scripting;

    @Inject
    protected Persistence persistence;

    @Inject
    protected Authentication authentication;

    @Inject
    protected DataManager dataManager;

    @Inject
    protected ConfigStorage configStorage;

    @Inject
    protected ReportService reportService;

    @Override
    public Object executeScript(Object input) {
        Binding binding = new Binding();
        binding.setVariable("input", input);
        scripting.runGroovyScript("/com/haulmont/addon/demo/core/scripts/sampleScript.groovy", binding);
        return binding.getVariable("output");
    }

    @Override
    public String jmxExecuteScript(String input) {
        Object result;
        try {
            result = executeScript(input);
        } catch (Exception e) {
            return e.toString();
        }
        return result == null ? "<null>" : result.toString();
    }

    @Override
    @Transactional
    public String getCarCount() {
        try {
            int count = countCars();
            return String.valueOf(count);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @Override
    @Transactional
    public String getCarCount(Date creationDate) {
        try {
            int count = countCars(creationDate);
            return String.valueOf(count);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @Override
    public String testAuthentication() {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("Before: ").append(AppContext.getSecurityContext()).append("\n");
            authentication.begin();
            try {
                sb.append("Outer begin: ").append(AppContext.getSecurityContext()).append("\n");
                Transaction tx = persistence.createTransaction();
                try {
                    testAuthenticationInner(sb);
                    tx.commit();
                } finally {
                    tx.end();
                }
            } finally {
                authentication.end();
                sb.append("Outer end: ").append(AppContext.getSecurityContext()).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        }
    }

    private void testAuthenticationInner(StringBuilder sb) {
        authentication.begin();
        try {
            sb.append("Inner begin: ").append(AppContext.getSecurityContext()).append("\n");
            Transaction tx = persistence.createTransaction();
            try {
                tx.commit();
            } finally {
                tx.end();
            }
        } finally {
            authentication.end();
            sb.append("Inner end: ").append(AppContext.getSecurityContext()).append("\n");
        }
    }

    @Override
    public int countCars() {
        EntityManager em = persistence.getEntityManager();
        Query query = em.createQuery("select c from ref_Car c");
        return query.getResultList().size();
    }

    public int countCars(Date creationDate) {
        EntityManager em = persistence.getEntityManager();
        Query query = em.createQuery("select c from ref_Car c where c.createTs > ?1");
        query.setParameter(1, creationDate);
        return query.getResultList().size();
    }

    @Override
    public String testDataManagerWithoutSecurityContext() {
        LoadContext<Car> lc = new LoadContext<>(Car.class);
        long count = dataManager.getCount(lc);
        return "" + count;
    }

    @Override
    public String setAttributeAccessEnabledForRest(boolean enabled) {
        AppBeans.get(RefAppAddressAttributeAccess.class).setEnabledForRest(enabled);
        AppBeans.get(RefAppDriverAllocationAttributeAccess.class).setEnabledForRest(enabled);
        AppBeans.get(RefAppDriverAttributeAccess.class).setEnabledForRest(enabled);
        return "OK";
    }

    @Override
    @Authenticated
    public String setEntityAttributePermissionChecking(boolean enabled) {
        configStorage.setDbProperty("cuba.entityAttributePermissionChecking", enabled ? "true" : "false");
        configStorage.clearCache();
        return "OK";
    }

    @Override
    @Authenticated
    public String setRestRequiresSecurityToken(boolean enabled) {
        configStorage.setDbProperty("cuba.rest.requiresSecurityToken", enabled ? "true" : "false");
        configStorage.clearCache();
        return "OK";
    }


    @Override
    @Authenticated
    public String importReports(byte[] data) {
        ReportImportResult reportImportResult = reportService.importReportsWithResult(data, null);
        if (reportImportResult != null) {
            return "Reports imported: " + reportImportResult.getImportedReports().size();
        }
        return "";
    }

    @Override
    @Authenticated
    public String removeReport(String id) {
        LoadContext<Report> lc = new LoadContext<>(Report.class).setId(UUID.fromString(id));
        Report report = dataManager.load(lc);
        dataManager.remove(report);
        return "Removed";
    }
}