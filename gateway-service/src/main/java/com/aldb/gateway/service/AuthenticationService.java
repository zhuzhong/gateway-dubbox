/**
 * 
 */
package com.aldb.gateway.service;

import com.aldb.gateway.common.OpenApiHttpRequestBean;

/**认证服务类
 * @author Administrator
 *
 */
public interface AuthenticationService {

    public void doAuthOpenApiHttpRequestBean(OpenApiHttpRequestBean requestBean);
}
