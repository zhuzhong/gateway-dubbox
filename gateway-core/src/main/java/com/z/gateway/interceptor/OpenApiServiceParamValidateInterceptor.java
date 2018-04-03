/**
 * 
 */
package com.z.gateway.interceptor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.z.gateway.common.OpenApiHttpRequestBean;
import com.z.gateway.common.util.CommonCodeConstants;
import com.z.gateway.common.util.NetworkUtil;

/**
 * @author Administrator
 *
 */
public class OpenApiServiceParamValidateInterceptor extends AbstractOpenApiValidateInterceptor {

    private static final Log log = LogFactory.getLog(OpenApiServiceParamValidateInterceptor.class);

    /**
     * 根据请求的协议进行解析
     */
    @Override
    protected OpenApiHttpRequestBean iniOpenApiHttpRequestBean(HttpServletRequest request) {
        String requestMethod = request.getMethod();

        OpenApiHttpRequestBean bean = new OpenApiHttpRequestBean();
        if (requestMethod.equalsIgnoreCase(CommonCodeConstants.REQUEST_METHOD.POST.name())) {
            try {
                parsePostMethod(request, bean);
            } catch (IOException e) {
                log.error("这个请求格式不是application/json的,我处理不了...");
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } else if (requestMethod.equalsIgnoreCase(CommonCodeConstants.REQUEST_METHOD.GET.name())) {
            parseGetMethod(request, bean);
            bean.setQueryString(request.getQueryString());
        }
        bean.setThdApiUrlParams(extractThdUrlParams(request));
        bean.setLocalAddr(request.getLocalAddr());
        bean.setLocalPort(request.getLocalPort());
        bean.setClientAddr(NetworkUtil.getClientIpAddr(request));
        bean.setReqHeader(getHeadersInfo(request)); // 获取请求头
        if (request.getContentType() != null)
            bean.getReqHeader().put("content-type", request.getContentType());
        bean.setOperationType(CommonCodeConstants.API_SERVICE_KEY);
        bean.setRequestMethod(requestMethod);
        if (bean.getSignMethod() == null)
            bean.setSignMethod("MD5");
        if (bean.getFormat() == null)
            bean.setFormat("json");
        return bean;
    }

    private void parseGetMethod(HttpServletRequest request, OpenApiHttpRequestBean bean) {

    	String requestUrl=request.getRequestURI(); // /test/tapi
    	bean.setRequestUrl(requestUrl);
  
    	
        Enumeration<String> enums = request.getParameterNames();
        while (enums.hasMoreElements()) {
            String mapJson = enums.nextElement();
            String value = (String) request.getParameter(mapJson);
            
            bean.addServiceGetReqData(mapJson, value);
        }
    }

    private void parsePostMethod(HttpServletRequest request, OpenApiHttpRequestBean bean) throws IOException {

    	
    	String requestUrl=request.getRequestURI(); // /test/tapi
    	bean.setRequestUrl(requestUrl);

    	 
    	int len = request.getContentLength();
        ServletInputStream iii = request.getInputStream();
        byte[] buffer = new byte[len];
        iii.read(buffer, 0, len);
        String bodyContent = new String(buffer, "UTF-8"); 
        bean.setServiceReqData(bodyContent); // 请求体

    }

    private Map<String, String> extractThdUrlParams(HttpServletRequest request) {
        Map<String, String> urlParams = new HashMap<String, String>();
        Map<String, String[]> orignalUrlParams = request.getParameterMap();
        String key = null;
        String[] values = null;
        if (null != orignalUrlParams) {
            Iterator entries = orignalUrlParams.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                key = (String) entry.getKey();
                values = (String[]) entry.getValue();
               /* if (key.equals(CommonCodeConstants.app_id) || key.equals(CommonCodeConstants.api_id)
                        || key.equals(CommonCodeConstants.version) || key.equals(CommonCodeConstants.app_token)
                        || key.equals(CommonCodeConstants.time_stamp) || key.equals(CommonCodeConstants.sign_method)
                        || key.equals(CommonCodeConstants.sign) || key.equals(CommonCodeConstants.device_token)
                        || key.equals(CommonCodeConstants.user_token) || key.equals(CommonCodeConstants.format)) {
                    continue;
                }*/
                String val = values[0];
                try {
                    val = java.net.URLEncoder.encode(val, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    log.error("exception on prceeding chinese char: " + val + " with " + e.getMessage());
                }

                urlParams.put(key, null != values ? val : "");
            }
        }
        return urlParams;
    }

    private Map<String, String> getHeadersInfo(HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }

        return map;
    }

    /*
     * private String getHttpRequestBodyString(HttpServletRequest request) { if
     * ("POST".equalsIgnoreCase(request.getMethod())) { Scanner s = null; try {
     * s = new Scanner(request.getInputStream(), "UTF-8").useDelimiter("\\A"); }
     * catch (IOException e) { log.error(e.getMessage()); } return s.hasNext() ?
     * s.next() : ""; } return ""; }
     */
}
