/**
 * 
 */
package com.z.gateway.service;

import com.z.gateway.common.OpenApiHttpRequestBean;

/**认证服务类
 * @author Administrator
 *
 */
public interface AuthenticationService {

    public void doAuthOpenApiHttpRequestBean(OpenApiHttpRequestBean requestBean);
}
