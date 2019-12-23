/*
 * Copyright (c) 2008-2018 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.core.app.attributeaccess;


import com.haulmont.cuba.core.app.SetupAttributeAccessHandler;
import com.haulmont.cuba.core.app.events.SetupAttributeAccessEvent;
import com.haulmont.rest.demo.core.entity.Place;
import org.springframework.stereotype.Component;

@Component("refapp_RefAppPlaceAttributeAccess")
public class RefAppPlaceAttributeAccess implements SetupAttributeAccessHandler<Place> {

    protected volatile boolean enabled;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setupAccess(SetupAttributeAccessEvent<Place> event) {
        if (enabled) {
            event.addRequired("name");
        }
    }

    @Override
    public boolean supports(Class clazz) {
        return enabled && Place.class.isAssignableFrom(clazz);
    }
}
