/*
 * Copyright (c) 2008-2018 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package spec.rest.demo.rest.service;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UserInfoService {
    @GET("userInfo")
    Call<UserInfo> getUserInfo();
}