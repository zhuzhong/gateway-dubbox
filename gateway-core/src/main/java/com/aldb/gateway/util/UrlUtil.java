/**
 * 
 */
package com.aldb.gateway.util;

import java.util.Properties;

import org.springframework.util.PropertyPlaceholderHelper;

/**
 * url处理类
 * 
 * @author Administrator
 *
 */
public class UrlUtil {

    /**
     * 对于gateway请求后端restful服务，对于get方法提供的restful服务，
     * 在进行服务注册时，如果请求路径中需要参数，则一律使用${参数名}的形式出现,在此解析
     * 
     * @param targetUrl
     * @param params
     * @return
     */
    public static String dealUrl(String targetUrl, Properties properties) {
        if (properties == null) {
            return targetUrl;
        }

        return propertyPlaceHolder.replacePlaceholders(targetUrl, properties);

    }

    private static PropertyPlaceholderHelper propertyPlaceHolder = new PropertyPlaceholderHelper("{", "}");

    public static void main(String args[]) {
        String targetUrl = "/deptname/{deptId}/{user}/{userId}";
        Properties properties = new Properties();
        properties.put("deptId", "10");
        properties.put("user", "zhuzhong");
        properties.put("userId", "002");
        String result = dealUrl(targetUrl, properties);
        System.out.println(result);
    }
}
