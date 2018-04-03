package com.z.gateway.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/*import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
 import org.apache.commons.httpclient.HttpClient;
 import org.apache.commons.httpclient.HttpException;
 import org.apache.commons.httpclient.HttpStatus;
 import org.apache.commons.httpclient.NameValuePair;
 import org.apache.commons.httpclient.URI;
 import org.apache.commons.httpclient.methods.GetMethod;
 import org.apache.commons.httpclient.methods.PostMethod;
 import org.apache.commons.httpclient.params.HttpClientParams;
 import org.apache.commons.httpclient.params.HttpConnectionParams;
 import org.apache.commons.httpclient.params.HttpMethodParams;*/

/**
 * 
 * @author Administrator
 *
 */
@Deprecated
public class ApiHttpUtil {
    private static final int TIME_OUT_MILL_SEC = 15000;

    /**
     * 发送post请求工具方法
     * 
     * @Methods Name HttpPost
     * @Create In 2014年10月28日 By wangfei
     * @param url
     * @param method
     * @param paramMap
     * @return String
     */
    /*
     * @SuppressWarnings("rawtypes") public static String HttpPost(String url,
     * String method, Map paramMap) { return HttpPost(url + '/' + method,
     * paramMap); }
     */

    /*
     * public static String HttpPost(String webUrl, Map paramMap) { String
     * encoding = "UTF-8"; if (encoding == null || "".equals(encoding)) encoding
     * = "UTF-8"; StringBuffer sBuffer = new StringBuffer(); // 构造HttpClient的实例
     * HttpClient httpClient = new HttpClient(); // httpClient.set // 创建POS方法的实例
     * NameValuePair[] pairs = null; PostMethod postMethod = new
     * PostMethod(webUrl); if (paramMap != null) { pairs = new
     * NameValuePair[paramMap.size()]; Set set = paramMap.keySet(); Iterator it
     * = set.iterator(); int i = 0; while (it.hasNext()) { Object key =
     * it.next(); Object value = paramMap.get(key); if
     * (!ApiHttpUtil.checkNull(value)) { pairs[i] = new
     * NameValuePair(key.toString(), value.toString()); } i++; }
     * postMethod.setRequestBody(pairs); }
     * postMethod.getParams().setParameter(HttpMethodParams
     * .HTTP_CONTENT_CHARSET, encoding);
     * httpClient.getHttpConnectionManager().getParams
     * ().setConnectionTimeout(TIME_OUT_MILL_SEC);
     * httpClient.getHttpConnectionManager
     * ().getParams().setSoTimeout(TIME_OUT_MILL_SEC); HttpClientParams
     * httpClientParams = new HttpClientParams();
     * httpClientParams.setParameter(HttpMethodParams.RETRY_HANDLER, new
     * DefaultHttpMethodRetryHandler(0, false));
     * httpClientParams.setBooleanParameter
     * (HttpConnectionParams.STALE_CONNECTION_CHECK, true);
     * httpClient.setParams(httpClientParams); try { // 执行getMethod int
     * statusCode = httpClient.executeMethod(postMethod); if (statusCode !=
     * HttpStatus.SC_OK) { System.err.println("Method failed: " +
     * postMethod.getStatusLine()); sBuffer = new StringBuffer(); } else {
     * sBuffer = new StringBuffer(postMethod.getResponseBodyAsString() + ""); }
     * } catch (HttpException e) { // 发生致命的异常，可能是协议不对或者返回的内容有问题
     * System.out.println("Please check your provided http address!");
     * e.printStackTrace(); } catch (IOException e) { // 发生网络异常
     * e.printStackTrace(); } finally { // 释放连接 postMethod.releaseConnection();
     * } return sBuffer.toString(); }
     */

    /*
     * @SuppressWarnings({ "rawtypes", "unchecked" }) public static String
     * HttpGet(String webUrl, Map paramMap) {
     * 
     * // 设置编码格式 String encoding = "GBK"; if (encoding == null ||
     * "".equals(encoding)) encoding = "GBK"; String queryString =
     * createLinkString(paramMap); webUrl = webUrl + "?" + queryString;
     * StringBuffer sBuffer = new StringBuffer(); // 构造HttpClient的实例 HttpClient
     * httpClient = new HttpClient(); GetMethod gettMethod = null; //
     * httpClient.set try { URI uri = new URI(webUrl, false, encoding);
     * 
     * gettMethod = new GetMethod(uri.toString());
     * 
     * gettMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
     * encoding);
     * httpClient.getHttpConnectionManager().getParams().setConnectionTimeout
     * (5000); // 连接5秒超时
     * httpClient.getHttpConnectionManager().getParams().setSoTimeout(30000);//
     * 读取30秒超时
     * 
     * // 执行getMethod int statusCode = httpClient.executeMethod(gettMethod); if
     * (statusCode != HttpStatus.SC_OK) { System.err.println("Method failed: " +
     * gettMethod.getStatusLine()); sBuffer = new StringBuffer(); } else {
     * sBuffer = new StringBuffer(gettMethod.getResponseBodyAsString() + ""); }
     * } catch (HttpException e) { // 发生致命的异常，可能是协议不对或者返回的内容有问题
     * System.out.println("Please check your provided http address!");
     * e.printStackTrace(); } catch (IOException e) { // 发生网络异常
     * e.printStackTrace(); } finally { // 释放连接 // Rewriter // Rewrite
     * Date：2015-12-22 By ZouYongle Case gettMethod MayBe is // NULL Start1： //
     * gettMethod.releaseConnection(); // End1： // Added by // Add
     * date：2015-12-22 Start2： if (gettMethod != null) {
     * gettMethod.releaseConnection(); } // End2： } return sBuffer.toString(); }
     */

    /**
     * 发送Get请求工具方法
     * 
     * @Methods Name HttpGet
     * @Create In Dec 30, 2014 By lihongfei
     * @param url
     * @param method
     * @param paramMap
     * @return String
     */
    /*
     * @SuppressWarnings({ "rawtypes", "unchecked" }) public static String
     * HttpGet(String url, String method, Map paramMap) {
     * 
     * String webUrl = url + "/" + method;
     * 
     * return HttpGet(webUrl, paramMap); }
     */

    /**
     * 发送Get请求工具方法,处理参数有中文字符
     * 
     * @Methods Name HttpGet
     * @Create In Dec 30, 2014 By songw
     * @param url
     * @param method
     * @param paramMap
     * @return String
     */
    /*
     * @SuppressWarnings({ "rawtypes", "unchecked" }) public static String
     * HttpGetByUtf(String url, String method, Map paramMap) { // 设置编码格式 String
     * encoding = "UTF-8"; String webUrl = url + "/" + method; if (encoding ==
     * null || "".equals(encoding)) encoding = "UTF-8"; String queryString =
     * createLinkString(paramMap); webUrl = webUrl + "?" + queryString;
     * StringBuffer sBuffer = new StringBuffer(); // 构造HttpClient的实例 HttpClient
     * httpClient = new HttpClient(); GetMethod gettMethod = null; //
     * httpClient.set try { URI uri = new URI(webUrl, false, encoding);
     * 
     * gettMethod = new GetMethod(uri.toString());
     * gettMethod.setRequestHeader("Content-type", "application/json");
     * gettMethod
     * .getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
     * encoding);
     * httpClient.getHttpConnectionManager().getParams().setConnectionTimeout
     * (5000); // 连接5秒超时
     * httpClient.getHttpConnectionManager().getParams().setSoTimeout(30000);//
     * 读取30秒超时
     * 
     * // 执行getMethod int statusCode = httpClient.executeMethod(gettMethod); if
     * (statusCode != HttpStatus.SC_OK) { System.err.println("Method failed: " +
     * gettMethod.getStatusLine()); sBuffer = new StringBuffer(); } else {
     * sBuffer = new StringBuffer(gettMethod.getResponseBodyAsString() + ""); }
     * } catch (HttpException e) { // 发生致命的异常，可能是协议不对或者返回的内容有问题
     * System.out.println("Please check your provided http address!");
     * e.printStackTrace(); } catch (IOException e) { // 发生网络异常
     * e.printStackTrace(); } finally { // 释放连接 // Rewriter // Rewrite
     * Date：2015-12-22 By ZouYongle Case gettMethod MayBe is // NULL Start1： //
     * gettMethod.releaseConnection(); // End1： // Added by // Add
     * date：2015-12-22 Start2： if (gettMethod != null) {
     * gettMethod.releaseConnection(); } // End2： } return sBuffer.toString(); }
     */

    /**
     * 执行一个HTTP POST请求，返回请求响应的HTML
     * 
     * @param url
     *            请求的URL地址
     * @param params
     *            请求的查询参数,可以为null
     * @return 返回请求响应的HTML
     */
    /*
     * @SuppressWarnings("deprecation") public static String doPost(String url,
     * String reqData, String contentType) { String response = null; HttpClient
     * client = new HttpClient();
     * client.getHttpConnectionManager().getParams().setConnectionTimeout
     * (TIME_OUT_MILL_SEC);
     * client.getHttpConnectionManager().getParams().setSoTimeout
     * (TIME_OUT_MILL_SEC); HttpClientParams httpClientParams = new
     * HttpClientParams();
     * httpClientParams.setParameter(HttpMethodParams.RETRY_HANDLER, new
     * DefaultHttpMethodRetryHandler(0, false));
     * httpClientParams.setBooleanParameter
     * (HttpConnectionParams.STALE_CONNECTION_CHECK, true);
     * client.setParams(httpClientParams); PostMethod method = new
     * PostMethod(url);
     * method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
     * "UTF-8"); // 设置Http Post数据 try { method.setRequestBody(reqData);
     * method.setRequestHeader("Content-type", contentType);
     * client.executeMethod(method); // if (method.getStatusCode() ==
     * HttpStatus.SC_OK) { response = method.getResponseBodyAsString(); // } }
     * catch (IOException e) { e.printStackTrace();
     * response="errorMessage："+e.getMessage(); } finally {
     * method.releaseConnection(); } return response; }
     */
    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * 
     * @param params
     *            需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }

        return prestr;
    }

    public static boolean checkNull(Object target) {
        if (target == null || "".equals(target.toString().trim()) || "null".equalsIgnoreCase(target.toString().trim())) {
            return true;
        }
        return false;
    }
}
