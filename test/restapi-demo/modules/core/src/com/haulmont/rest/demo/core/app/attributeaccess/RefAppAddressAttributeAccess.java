/*
 * Copyright (c) 2008-2018 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.core.app.attributeaccess;


import com.haulmont.cuba.core.app.SetupAttributeAccessHandler;
import com.haulmont.cuba.core.app.events.SetupAttributeAccessEvent;
import com.haulmont.rest.demo.core.entity.Address;
import org.springframework.stereotype.Component;

@Component("refapp_RefAppAddressAttributeAccess")
public class RefAppAddressAttributeAccess implements SetupAttributeAccessHandler<Address> {

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

    public void setupAccess(SetupAttributeAccessEvent<Address> event) {
        if (enabled || enabledForRest) {
            event.addReadOnly("city");
            event.addHidden("country");
            event.addRequired("state");
        }
    }

    @Override
    public boolean supports(Class clazz) {
        return (enabled || enabledForRest) && Address.class.isAssignableFrom(clazz);
    }
}
