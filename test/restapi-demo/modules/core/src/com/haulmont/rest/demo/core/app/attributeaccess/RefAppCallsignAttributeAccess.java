/*
 * Copyright (c) 2008-2018 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.core.app.attributeaccess;


import com.haulmont.cuba.core.app.SetupAttributeAccessHandler;
import com.haulmont.cuba.core.app.events.SetupAttributeAccessEvent;
import com.haulmont.rest.demo.core.entity.DriverCallsign;
import org.springframework.stereotype.Component;

@Component("refapp_RefAppCallsignAttributeAccess")
public class RefAppCallsignAttributeAccess implements SetupAttributeAccessHandler<DriverCallsign> {

    protected volatile boolean enabled;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setupAccess(SetupAttributeAccessEvent<DriverCallsign> event) {
        if (enabled) {
            event.addReadOnly("callsign");
            event.addRequired("callsign");
        }
    }

    @Override
    public boolean supports(Class clazz) {
        return enabled && DriverCallsign.class.isAssignableFrom(clazz);
    }
}
