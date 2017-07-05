package com.aldb.gateway.core.support;

import org.apache.commons.chain.Context;
import org.apache.commons.lang3.StringUtils;

import com.aldb.gateway.common.OpenApiHttpRequestBean;
import com.aldb.gateway.common.entity.ApiInterface;
import com.aldb.gateway.common.exception.OauthErrorEnum;
import com.aldb.gateway.common.exception.OpenApiException;
import com.aldb.gateway.common.util.CommonCodeConstants;
import com.aldb.gateway.core.AbstractOpenApiHandler;
import com.aldb.gateway.core.OpenApiHttpClientService;
import com.aldb.gateway.core.OpenApiRouteBean;
import com.aldb.gateway.protocol.OpenApiContext;
import com.aldb.gateway.protocol.OpenApiHttpSessionBean;
import com.aldb.gateway.service.ApiInterfaceService;
import com.aldb.gateway.service.CacheService;
import com.aldb.gateway.util.UrlUtil;

public class OpenApiReqHandler extends AbstractOpenApiHandler {

    private final int maxReqDataLth = 500;


    private ApiInterfaceService apiInterfaceService;

    private OpenApiHttpClientService apiHttpClientService;
    public void setApiInterfaceService(ApiInterfaceService apiInterfaceService) {
        this.apiInterfaceService = apiInterfaceService;
    }

    public void setApiHttpClientService(OpenApiHttpClientService apiHttpClientService) {
        this.apiHttpClientService = apiHttpClientService;
    }
    private  CacheService cacheService;

    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }
    
    // step2
    @Override
    public boolean doExcuteBiz(Context context) {
        OpenApiContext openApiContext = (OpenApiContext) context;
        OpenApiHttpSessionBean httpSessionBean = (OpenApiHttpSessionBean) openApiContext.getOpenApiHttpSessionBean();
        OpenApiHttpRequestBean request = httpSessionBean.getRequest();
        long currentTime = System.currentTimeMillis();
        if (logger.isDebugEnabled()) {
            logger.info(String.format("begin run doExecuteBiz,currentTime=%d,httpSessonBean=%s", currentTime,
                    httpSessionBean));
        }
        String routeBeanKey = request.getRouteBeanKey();
        OpenApiRouteBean routeBean = (OpenApiRouteBean) cacheService.get(routeBeanKey);

        routeBean.setServiceRsp(doInvokeBackService(routeBean)); // 返回值
        if (logger.isDebugEnabled()) {
            logger.info(String.format("end run doExecuteBiz,currentTime=%d,elapase_time=%d milseconds,httpSessonBean=%s",
                    System.currentTimeMillis(), (System.currentTimeMillis() - currentTime) , httpSessionBean));
        }
        return false;
    }

    /**
     * 根据routeBean信息，通过httpclient调用后端信息，然后将返回值构建成string
     * 
     * @param bean
     * @return
     */
    private String doInvokeBackService(OpenApiRouteBean bean) {
        logger.info("step5...");
        String serviceRspData = null; // 后端服务返回值
        String operationType = bean.getOperationType();
        String requestMethod = bean.getRequestMethod();

        if (operationType.equals(CommonCodeConstants.API_SYSERVICE_KEY)) {
            /*
             * Map<String, String> map = new TreeMap<String, String>();
             * map.put("code", "200"); map.put("message",
             * "The request has been accepted, the processing number is :" +
             * bean.getDataId()); serviceRspData = JSON.toJSONString(map);
             */
        } else if (CommonCodeConstants.API_GETDATA_KEY.equals(operationType)) {
            /*
             * ListOperations<String, String> lop = null; lop =
             * redisTemplate.opsForList(); while
             * (StringUtil.isNotEmpty(serviceRspData =
             * lop.rightPop(bean.getDataId()))) {
             * 
             * return JSON.parseObject(serviceRspData,
             * OpenApiRouteBeanVO.class).getServiceReqData(); }
             */
        } else if (CommonCodeConstants.API_SERVICE_KEY.equals(operationType)) {
            logger.info(String.format("{serviceId:%s ,version:%s }", bean.getApiId(), bean.getVersion()));
            ApiInterface apiInfo = apiInterfaceService.findOne(bean.getApiId(), bean.getVersion());
            
            if (apiInfo == null) {
                return String.format("this apiId=%s,version=%s has off line,please use another one", bean.getApiId(), bean.getVersion());
            }
            apiInfo.setTargetUrl(bean.getTargetUrl());
            apiInfo.setRequestMethod(bean.getRequestMethod());
            if (CommonCodeConstants.REQUEST_METHOD.GET.name().equalsIgnoreCase(requestMethod)) { // get请求
                String url = apiInfo.getUrl();
                url = UrlUtil.dealUrl(url, bean.getThdApiUrlParams());
                /*
                 * if (StringUtils.isNotBlank(bean.getQueryString())) { url +=
                 * "?" + bean.getQueryString(); // 串好url 地址 }
                 */
                logger.info(String.format("{service url:%s} ", url));
                // String contentType =
                // bean.getReqHeader().get(CONTENT_TYPE_KEY);
                if (url.startsWith("https")) {
                    if (bean.getServiceGetReqData() == null) {
                        serviceRspData = apiHttpClientService.doHttpsGet(url,bean.getTraceId());
                    } else {
                        serviceRspData = apiHttpClientService.doHttpsGet(url, bean.getServiceGetReqData(),bean.getTraceId());
                    }
                } else {
                    if (bean.getServiceGetReqData() == null) {
                        serviceRspData = apiHttpClientService.doGet(url,bean.getTraceId());
                    } else {
                        serviceRspData = apiHttpClientService.doGet(url, bean.getServiceGetReqData(),bean.getTraceId());
                    }
                }

            } else if (CommonCodeConstants.REQUEST_METHOD.POST.name().equalsIgnoreCase(requestMethod)) {// post请求
                String url = apiInfo.getUrl();
                logger.info(String.format("{service url:%s} ", url));
                String reqData = bean.getServiceReqData(); // 请求的json格式数据参数
                if (StringUtils.isNotBlank(reqData) && reqData.length() > this.maxReqDataLth) {
                    reqData = reqData.substring(0, this.maxReqDataLth - 1);
                }
                logger.info(String.format("{serviceId:%s ,reqData:%s }", bean.getApiId(), reqData));

                String contentType = bean.getReqHeader().get(CONTENT_TYPE_KEY);
                if (url.startsWith("https://")) {
                    serviceRspData = apiHttpClientService.doHttpsPost(url, bean.getServiceReqData(), contentType,bean.getTraceId());
                } else {
                    serviceRspData = apiHttpClientService.doPost(url, bean.getServiceReqData(), contentType,bean.getTraceId());
                }
                if ("timeout".equals(serviceRspData)) {
                    logger.error("invoke service: response is null!");
                    throw new OpenApiException(OauthErrorEnum.ERROR.getErrCode(), OauthErrorEnum.ERROR.getErrMsg());
                }
            }
        } else {
            /*
             * String reqData = bean.getServiceReqData(); // 请求的json格式数据参数 if
             * (StringUtils.isNotBlank(reqData) && reqData.length() >
             * this.maxReqDataLth) { reqData = reqData.substring(0,
             * this.maxReqDataLth - 1); }
             * log.info(String.format("{serviceId:%s ,reqData:%s }",
             * bean.getApiId(), reqData));
             * 
             * ApiInterface apiInfo =
             * apiInterfaceService.findOne(bean.getApiId(), bean.getVersion());
             * String contentType = bean.getReqHeader().get(CONTENT_TYPE_KEY);
             * 
             * try { String url = apiInfo.getTargetUrl();
             * log.info("{service url: " + url); if (url.startsWith("https://"))
             * { serviceRspData = apiHttpClientService.doHttpsPost(url,
             * bean.getServiceReqData(), contentType); } else { if
             * (bean.getApiId().equals("bl.kd100.tms.backstatus") ||
             * bean.getApiId().equals("bl.order.dc.tmsexpress")) {
             * 
             * url = this.generatePassUrl(apiInfo.getTargetUrl(),
             * user.getSalt(), bean.getAccessToken(), bean.getTimeStamp());
             * String params = this.getParams(user.getSalt(),
             * bean.getAccessToken(), bean.getTimeStamp(),
             * bean.getThdApiUrlParams()); serviceRspData =
             * mutiHttpClientUtil.doPost(url, bean.getServiceReqData(),
             * contentType, params);
             * 
             * } else { serviceRspData = apiHttpClientService.doPost(url,
             * bean.getServiceReqData(), contentType); } } if
             * ("timeout".equals(serviceRspData)) {
             * log.error("invoke service: response is null!"); throw new
             * OpenApiException(OauthErrorEnum.ERROR.getErrCode(),
             * OauthErrorEnum.ERROR.getErrMsg()); } } catch (Exception e) {
             * log.error("invoke service: " + e.getMessage()); throw new
             * OpenApiException(OauthErrorEnum.ERROR.getErrCode(),
             * OauthErrorEnum.ERROR.getErrMsg()); }
             */
        }
        return serviceRspData;
    }
}
