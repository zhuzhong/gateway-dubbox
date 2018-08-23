/**
 * 
 */
package com.z.gateway.core;

import java.io.Serializable;
import java.util.Map;
import java.util.Properties;

/**
 * @author Administrator
 *
 */
public class OpenApiRouteBean implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = -5676073409823449078L;
    private String traceId; // 内部定义的请求id
    private String requestUrl;
    
    private String targetUrl;
    
    
    
    private String apiId;
    private String requestMethod;
    private String version; // api_version
    private String timeStamp;

    private Map<String, String> reqHeader;

    private String operationType;

    private String serviceReqData;// post请求方法参数
    // private String queryString; // 请求string
    private Map<String, String> serviceGetReqData; // get请求参数
    private Properties thdApiUrlParams;// 第三方接口所需传入的url参数

   // private String serviceRsp; // 后端服务返回值

   /* public String getServiceRsp() {
        return serviceRsp;
    }

    public void setServiceRsp(String serviceRsp) {
        this.serviceRsp = serviceRsp;
    }*/

    public void setThdApiUrlParams(Properties thdApiUrlParams) {
        this.thdApiUrlParams = thdApiUrlParams;
    }

    public Map<String, String> getServiceGetReqData() {
        return serviceGetReqData;
    }

    public void setServiceGetReqData(Map<String, String> serviceGetReqData) {
        this.serviceGetReqData = serviceGetReqData;
    }

    public Properties getThdApiUrlParams() {
        return this.thdApiUrlParams;
    }

    public void addThdApiUrlParams(String key, String value) {
        if (thdApiUrlParams == null) {
            this.thdApiUrlParams = new Properties();
        }
        this.thdApiUrlParams.put(key, value);
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getServiceReqData() {
        return serviceReqData;
    }

    public void setServiceReqData(String serviceReqData) {
        this.serviceReqData = serviceReqData;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Map<String, String> getReqHeader() {
        return reqHeader;
    }

    public void setReqHeader(Map<String, String> reqHeader) {
        this.reqHeader = reqHeader;
    }

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	
	private String returnContent;// 后端服务返回值

    public String getReturnContent() {
        return returnContent;
    }

    public void setReturnContent(String returnContent) {
        this.returnContent = returnContent;
    }

  
	
}
