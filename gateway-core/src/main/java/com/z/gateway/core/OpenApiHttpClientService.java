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

    
    /*
         返回值string 使用base64编码，使用时需要base64解码
            之前使用 String 的utf-8 编解码，对于文本类型是没有问题;但是对于image 类型的支持就会有问题，原因是它是错误的编码，后面再解码就不正确了，
            后来该这些接口改为使用字码码byte[]的形式作为返回值，但是总觉得不太好，所以最终使用base64进行字符串的编解码.对于字节码的编码的细节看来还有很多需要学习
     
     */
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
    
   // public byte[] doGet2(String webUrl, Map<String, String> requestHeader) ;
}