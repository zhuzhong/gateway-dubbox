/**
 * 
 */
package com.aldb.gateway.service;

import java.util.List;

/**
 * @author Administrator
 *
 */
public interface LoadBalanceService {

    
    String chooseOne(String apiId,String version,List<String> set);
}
