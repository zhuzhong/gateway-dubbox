/**
 * 
 */
package com.z.gateway.service;

import com.z.gateway.common.OpenApiRouteBean;

/**认证服务类
 * @author Administrator
 *
 */
public interface AuthenticationService {

    public void doAuthOpenApiHttpRequestBean(OpenApiRouteBean requestBean);
}
