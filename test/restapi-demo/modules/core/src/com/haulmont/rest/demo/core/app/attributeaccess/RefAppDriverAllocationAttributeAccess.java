/*
 * Copyright (c) 2008-2018 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.core.app.attributeaccess;


import com.haulmont.cuba.core.app.SetupAttributeAccessHandler;
import com.haulmont.cuba.core.app.events.SetupAttributeAccessEvent;
import com.haulmont.rest.demo.core.entity.DriverAllocation;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component("refapp_RefAppDriverAllocationAttributeAccess")
public class RefAppDriverAllocationAttributeAccess implements SetupAttributeAccessHandler<DriverAllocation> {

    protected volatile boolean enabledForRest;

    public boolean isEnabledForRest() {
        return enabledForRest;
    }

    public void setEnabledForRest(boolean enableForRest) {
        this.enabledForRest = enableForRest;
    }

    public void setupAccess(SetupAttributeAccessEvent<DriverAllocation> event) {
        if (enabledForRest) {
            DriverAllocation allocation = event.getEntity();
            if (Objects.equals(allocation.getDriver().getName(), "Driver#1")) {
                event.addHidden("car");
            }
        }
    }

    @Override
    public boolean supports(Class clazz) {
        return enabledForRest && DriverAllocation.class.isAssignableFrom(clazz);
    }
}
