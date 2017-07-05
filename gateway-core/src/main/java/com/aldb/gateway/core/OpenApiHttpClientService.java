/**
 * 
 */
package com.aldb.gateway.core;

import java.util.Map;


/**
 * @author Administrator
 *
 */
public interface OpenApiHttpClientService {

    // get请求
    public String doGet(String webUrl,String traceId);
    public String doGet(String webUrl, Map<String,String> paramMap,String traceId);
    public String doHttpsGet(String webUrl,String traceId);
    
    public String doHttpsGet(String webUrl, Map<String,String> paramMap,String traceId);
    

    public String doHttpsPost(String url, String content, String contentType,String traceId);

    public String doPost(String url, String reqData, String contentType,String traceId);
    
    /*public Map<String, String> HttpGet(String webUrl, Map paramMap);

    public Map<String, String> HttpGet(String url, String method, Map paramMap);
    */
    /*public String doPost(String url, String reqData, String contentType, String params);
    public String HttpPost(String webUrl, Map paramMap);

    public String HttpPost(String url, String method, Map paramMap);
*/
}