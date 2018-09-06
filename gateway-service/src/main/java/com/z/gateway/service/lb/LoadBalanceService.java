/**
 * 
 */
package com.z.gateway.service.lb;

import java.util.List;

/**
 * @author Administrator
 *
 */
public interface LoadBalanceService {

    
    String chooseOne(LbKey key,List<String> set);
}
