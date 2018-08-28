package com.z.gateway.service.support;

import com.z.gateway.common.OpenApiRouteBean;

/**
 * 单个服务的校验,权限验证,以及你想要作的与业务不那么紧密的一切的一切
 * @author sunff
 *
 */
public interface AuthenticationSingelService {
    /**
     *  对于apiId 对应的服务，
     *  如果能够处理，则返回true，并同时将业务返回值写入returnContent;
     *  
     *  如果不能处理则返回false
     *  
     *  
     * @param requestBean
     * @return
     */
    public boolean doAuthOpenApiHttpRequestBean(OpenApiRouteBean requestBean);

}
