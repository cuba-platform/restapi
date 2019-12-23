/*
 * Copyright (c) 2008-2018 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.http.rest.jmx;

import com.haulmont.masquerade.jmx.JmxName;

/**
 * @author birin
 * @version $Id$
 */
@JmxName("app-core.refapp:type=Sample")
public interface ImportReportsService {
    String importReports(byte[] data);
    String removeReport(String id);
}
