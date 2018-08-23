/**
 * 
 */
package com.z.gateway.core;

import java.util.Map;


/**
 * @author Administrator
 *
 */
public interface OpenApiHttpClientService {

    // get请求
    public String doGet(String webUrl,Map<String,String> requestHeader);
    public String doGet(String webUrl, Map<String,String> paramMap,Map<String,String> requestHeader);
    public String doHttpsGet(String webUrl,Map<String,String> requestHeader);
    
    public String doHttpsGet(String webUrl, Map<String,String> paramMap,Map<String,String> requestHeader);
    

    public String doHttpsPost(String url, String reqData, Map<String,String> requestHeader);

    public String doPost(String url, String reqData, Map<String,String> requestHeader);
    
    /*public Map<String, String> HttpGet(String webUrl, Map paramMap);

    public Map<String, String> HttpGet(String url, String method, Map paramMap);
    */
    /*public String doPost(String url, String reqData, String contentType, String params);
    public String HttpPost(String webUrl, Map paramMap);

    public String HttpPost(String url, String method, Map paramMap);
*/
    
    public byte[] doGet2(String webUrl, Map<String, String> requestHeader) ;
}