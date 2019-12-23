/*
 * Copyright (c) 2008-2018 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.core.app.attributeaccess;


import com.haulmont.cuba.core.app.SetupAttributeAccessHandler;
import com.haulmont.cuba.core.app.events.SetupAttributeAccessEvent;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.EntityStates;
import com.haulmont.rest.demo.core.entity.Car;
import org.springframework.stereotype.Component;

@Component("refapp_RefAppCarAttributeAccess")
public class RefAppCarAttributeAccess implements SetupAttributeAccessHandler<Car> {

    protected volatile boolean enabled;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setupAccess(SetupAttributeAccessEvent<Car> event) {
        if (enabled) {
            Car car = event.getEntity();
            if (!AppBeans.get(EntityStates.class).isManaged(car))
                throw new IllegalStateException("entity is not managed");
            if (car.getColour() != null) {
                event.addHidden("seller");
                event.addReadOnly("model");
                event.addRequired("vin");
            }
        }
    }

    @Override
    public boolean supports(Class clazz) {
        return enabled && Car.class.isAssignableFrom(clazz);
    }
}
