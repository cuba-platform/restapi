/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.core.listeners;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.PersistenceTools;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.listener.BeforeAttachEntityListener;
import com.haulmont.cuba.core.listener.BeforeDetachEntityListener;
import com.haulmont.rest.demo.core.entity.Car;
import com.haulmont.rest.demo.core.entity.Currency;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component("refapp_CarDetachListener")
public class CarDetachListener implements BeforeDetachEntityListener<Car>, BeforeAttachEntityListener<Car> {

    @Inject
    private Persistence persistence;

    @Inject
    private PersistenceTools persistenceTools;

    @Override
    public void onBeforeDetach(Car entity, EntityManager entityManager) {
        // This is for testing the listener only. Usage of persistenceTools.getReferenceId() does not make sense here.
        PersistenceTools.RefId refId = persistenceTools.getReferenceId(entity, "currency");
        if (refId.isLoaded() && PersistenceHelper.isLoaded(entity, "currencyCode")) {
            entity.setCurrencyCode((String) refId.getValue());
        }
    }

    @Override
    public void onBeforeAttach(Car entity) {
        if (PersistenceHelper.isLoaded(entity, "currency") && entity.getCurrency() == null && entity.getCurrencyCode() != null) {
            Currency currency = persistence.getEntityManager().find(Currency.class, entity.getCurrencyCode());
            entity.setCurrency(currency);
        }
    }
}
