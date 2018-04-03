/**
 * 
 */
package com.z.gateway.common;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.z.gateway.common.util.CommonCodeConstants;

/**
 * 
 * 
 * @author Administrator
 * 
 */
public class OpenApiHttpRequestBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Map<String, String> reqHeader;//
    private String requestUrl;
    
    private String operationType;//

    private String clientAddr; //
    private String localAddr;//
    private int localPort;//

    private Map<String, String> thdApiUrlParams;//
    private String serviceReqData; //
    private String requestMethod;

    private Map<String, String> serviceGetReqData; //
    private String queryString; //

    private Date requestTime; //
    private Date responseTime;//
    private Long elapsedTime;//

    private String traceId; //

    private String printStr; //
    // private String serviceRsp; // ֵ
    // begin ---
    private String appId;
    private String appToken;
    private String apiId;
    private String version;

    private String timeStamp;
    private String signMethod;
    private String sign;
    private String deviceToken;
    private String userToken;

    private String format;

    // private String request_data;

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public Date getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Date responseTime) {
        this.responseTime = responseTime;
    }

    public Long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(Long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    /*
     * public void setServiceGetReqData(Map<String, Object> serviceGetReqData) {
     * this.serviceGetReqData = serviceGetReqData; }
     */

    public Map<String, String> getServiceGetReqData() {
        return serviceGetReqData;
    }

    public void addServiceGetReqData(String key, String value) {
        if (this.serviceGetReqData == null) {
            this.serviceGetReqData = new HashMap<String, String>();
        }
        this.serviceGetReqData.put(key, value);
    }

    /*
     * public void setServiceGetReqData(Map<String, Object> serviceGetReqData) {
     * this.serviceGetReqData = serviceGetReqData; }
     */

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getPrintStr() {
        return printStr;
    }

    public void setPrintStr(String printStr) {
        this.printStr = printStr;
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

    public String getClientAddr() {
        return clientAddr;
    }

    public void setClientAddr(String clientAddr) {
        this.clientAddr = clientAddr;
    }

    public String getLocalAddr() {
        return localAddr;
    }

    public void setLocalAddr(String localAddr) {
        this.localAddr = localAddr;
    }

    public int getLocalPort() {
        return localPort;
    }

    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public Map<String, String> getThdApiUrlParams() {
        return thdApiUrlParams;
    }

    public void setThdApiUrlParams(Map<String, String> thdApiUrlParams) {
        this.thdApiUrlParams = thdApiUrlParams;
    }

    public Map<String, String> getReqHeader() {
        return reqHeader;
    }

    public void setReqHeader(Map<String, String> reqHeader) {
        this.reqHeader = reqHeader;
    }

    public String getAppToken() {
        return appToken;
    }

    public void setAppToken(String appToken) {
        this.appToken = appToken;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    /*
     * public String getRequest_data() { return request_data; }
     * 
     * public void setRequest_data(String request_data) { this.request_data =
     * request_data; }
     */

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getRouteBeanKey() {
        if (this.operationType.equals(CommonCodeConstants.API_SERVICE_KEY)) {
            return CommonCodeConstants.getRouteBeanRedisKey(traceId);
        }

        return CommonCodeConstants.getRouteBeanRedisKey("");
    }

    /*
     * public String getServiceRsp() { return serviceRsp; }
     * 
     * public void setServiceRsp(String serviceRsp) { this.serviceRsp =
     * serviceRsp; }
     */

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSignMethod() {
        return signMethod;
    }

    public void setSignMethod(String signMethod) {
        this.signMethod = signMethod;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	@Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
