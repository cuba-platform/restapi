/*
 * Copyright (c) 2008-2018 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.core.app.attributeaccess;


import com.haulmont.cuba.core.app.SetupAttributeAccessHandler;
import com.haulmont.cuba.core.app.events.SetupAttributeAccessEvent;
import com.haulmont.rest.demo.core.entity.*;
import org.springframework.stereotype.Component;

@Component("refapp_RefAppDriverAttributeAccess")
public class RefAppDriverAttributeAccess implements SetupAttributeAccessHandler<Driver> {

    protected volatile boolean enabled;
    protected volatile boolean enabledForRest;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabledForRest() {
        return enabledForRest;
    }

    public void setEnabledForRest(boolean enableForRest) {
        this.enabledForRest = enableForRest;
    }

    public void setupAccess(SetupAttributeAccessEvent<Driver> event) {
        if (enabled) {
            Driver driver = event.getEntity();
            if (driver.getStatus() == DriverStatus.ACTIVE) {
                event.addReadOnly("name");
            }
        }
        if (enabledForRest) {
            Driver driver = event.getEntity();
            if (driver.getStatus() == DriverStatus.ACTIVE) {
                event.addHidden("name");
                event.addRequired("status");
            }
        }
    }

    @Override
    public boolean supports(Class clazz) {
        return (enabled || enabledForRest) && Driver.class.isAssignableFrom(clazz);
    }
}
