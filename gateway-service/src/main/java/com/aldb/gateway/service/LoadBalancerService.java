/**
 * 
 */
package com.aldb.gateway.service;

import java.util.Set;

/**
 * @author Administrator
 *
 */
public interface LoadBalancerService {

    
    String chooseOne(Set<String> set);
}
