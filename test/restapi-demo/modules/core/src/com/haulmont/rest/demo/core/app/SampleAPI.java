/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.core.app;

public interface SampleAPI {

    String NAME = "refapp_Sample";

    Object executeScript(Object input);

    int countCars();
}
