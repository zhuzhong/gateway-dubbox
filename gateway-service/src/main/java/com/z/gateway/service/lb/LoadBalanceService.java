/**
 * 
 */
package com.z.gateway.service.lb;

import java.util.List;

import com.z.gateway.common.entity.ApiServerInfo;

/**
 * @author Administrator
 *
 */
public interface LoadBalanceService {

    
    ApiServerInfo chooseOne(LbKey key,List<ApiServerInfo> set);
}
