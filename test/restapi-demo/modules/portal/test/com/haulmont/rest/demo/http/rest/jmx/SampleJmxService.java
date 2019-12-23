/*
 * Copyright (c) 2008-2018 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.http.rest.jmx;

import com.haulmont.masquerade.jmx.JmxName;

@JmxName("app-core.refapp:type=Sample")
public interface SampleJmxService {
    String setAttributeAccessEnabledForRest(boolean enabled);
    String setEntityAttributePermissionChecking(boolean enabled);
    String setRestRequiresSecurityToken(boolean enabled);
}