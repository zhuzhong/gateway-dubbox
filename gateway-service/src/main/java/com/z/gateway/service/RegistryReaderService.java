package com.z.gateway.service;

import java.util.List;

import com.z.gateway.common.entity.ApiServerInfo;

/**
 * 注册器内容读取器服务
 */
public interface RegistryReaderService {

    List<ApiServerInfo> queryApiInterfaceByApiId(ApiServerInfoReq req); 
}
