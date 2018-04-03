/**
 * 
 */
package com.z.gateway.service.support;

import java.util.HashMap;
import java.util.Map;

import com.z.gateway.service.CacheService;

/**
 * @author Administrator
 *
 */

public class DefaultCacheServiceImpl implements CacheService {

    private Map<String, Object> m = new HashMap<String, Object>();

    @Override
    public void put(String key, Object obj) {
        m.put(key, obj);
    }

    @Override
    public Object get(String key) {
        return m.get(key);
    }

    @Override
    public void remove(String key) {
        m.remove(key);

    }

}
