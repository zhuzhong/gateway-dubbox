/**
 * 
 */
package com.z.gateway.service;

import com.z.gateway.common.entity.ApiServerInfo;

/**后端服务api server信息
 * @author Administrator
 *
 */
public interface ApiServerInfoService {

    /**
     * 根据apiId及版本号，获取一个后端服务api接口服务的信息
     * @param apiId
     * @param version
     * @return
     */
    ApiServerInfo queryApiInterfaceByApiId(ApiServerInfoReq req); //这个接口方法暂定两个
    
    
}
