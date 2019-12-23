/*
 * Copyright (c) 2008-2018 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package spec.rest.demo.rest.jmx;

import com.haulmont.masquerade.jmx.JmxName;

@JmxName("app-core.refapp:type=UserSessionLogSupport")
public interface UserSessionLogService {
    void changeUserSessionLogEnabled(boolean enabled);

    String findLastLoggedSessionId();

    String getLastLoggedSessionAction(String sessionId);

    String cleanupLogEntries();
}