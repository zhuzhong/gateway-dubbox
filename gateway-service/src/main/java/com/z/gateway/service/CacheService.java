/**
 * 
 */
package com.z.gateway.service;

/**
 * 缓存接口,主要是对路由结果的缓存
 * @author Administrator
 *
 */
public interface CacheService {

    
    public void put(String key,Object obj);
    
    public Object get(String key);
    
    public void remove(String key);
}
