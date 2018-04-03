/**
 * 
 */
package com.z.gateway.util;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.RateLimiter;

/**
 * @author Administrator
 *
 */
public class SystemConfigParaUtil implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 5575683586852012994L;

    private static final Logger logger = LoggerFactory.getLogger(SystemConfigParaUtil.class);

    // -----------并发量----------------------------
    public static final String concurrentLimit_Key = "concurrentLimit";

    private static ConcurrentHashMap<String, String> paraContainer = new ConcurrentHashMap<String, String>();

    private SystemConfigParaUtil() {
    }

    public static String getValue(String key) {
        return paraContainer.get(key);
    }

    public static void set(String key, String value) {
        paraContainer.put(key, value);
    }

    public static Integer getConcurrentLimit() {
        try {
            return Integer.valueOf(paraContainer.get(concurrentLimit_Key));
        } catch (Exception e) {
            return Integer.MAX_VALUE; //不进行并发量限制
        }
    }

    public static void setConcurrentLimit(Integer value) {
        try {
            paraContainer.put(concurrentLimit_Key, value.toString());
        } catch (Exception e) {
            logger.error("设置并发量出错,value={},e={}", value, e);
        }
    }

    // -------限流----------------
    private static final ConcurrentHashMap<String, RateLimiter> container = new ConcurrentHashMap<>();

    private static final String ratelimit_key = "ratelimit_key";

    public static double acquire() {
        return container.get(ratelimit_key) == null ? 0d : container.get(ratelimit_key).acquire();

    }

    public static void setPermitsOfRateLimiter(double permitsPerSecond) {
        RateLimiter rateLimiter = RateLimiter.create(permitsPerSecond);
        container.put(ratelimit_key, rateLimiter);
    }
    
    
}
