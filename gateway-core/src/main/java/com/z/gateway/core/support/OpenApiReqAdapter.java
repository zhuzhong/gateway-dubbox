package com.z.gateway.core.support;

import java.util.Map;

import org.apache.commons.chain.Context;

import com.z.gateway.common.OpenApiHttpRequestBean;
import com.z.gateway.core.AbstractOpenApiHandler;
import com.z.gateway.core.OpenApiRouteBean;
import com.z.gateway.protocol.OpenApiContext;
import com.z.gateway.protocol.OpenApiHttpSessionBean;
import com.z.gateway.service.AuthenticationService;

public class OpenApiReqAdapter extends AbstractOpenApiHandler {

    public OpenApiReqAdapter() {

    }

    private void initRouteBean(OpenApiHttpRequestBean request, OpenApiContext openApiContext) {
        OpenApiRouteBean routeBean = null;
        logger.info("iniApiRouteBean，这一步可以校验token,当然这个根据我们的实际情况去实现");
        /*
         * String accessToken = request.getAppToken(); if
         * (StringUtils.isBlank(accessToken)) { throw new
         * OpenApiException(OauthErrorEnum.ACCESSTOKEN.getErrCode(),
         * OauthErrorEnum.ACCESSTOKEN.getErrMsg()); }
         */
        logger.info("init 路由bean ");
        routeBean = new OpenApiRouteBean();
        routeBean.setTraceId(request.getTraceId()); // 内部请求id,利于跟踪
        routeBean.setApiId(request.getApiId());// 请求api_id
        routeBean.setVersion(request.getVersion());// api_version
        routeBean.setReqHeader(request.getReqHeader());// 请求头
        routeBean.setTimeStamp(request.getTimeStamp());// 请求时间

        routeBean.setOperationType(request.getOperationType()); // 请求操作类型
        routeBean.setRequestMethod(request.getRequestMethod());// 请求方法
        routeBean.setServiceReqData(request.getServiceReqData());// post请求参数
        // routeBean.setQueryString(request.getQueryString());// get请求参数
        routeBean.setServiceGetReqData(request.getServiceGetReqData()); // get请求参数
        routeBean.setRequestUrl(request.getRequestUrl());
        if (request.getThdApiUrlParams() != null) {
            for (Map.Entry<String, String> maps : request.getThdApiUrlParams().entrySet()) {
                routeBean.addThdApiUrlParams(maps.getKey(), maps.getValue());
            }
        }
        setRouteBeanApiId(request, routeBean);// 计算apiid
        openApiContext.put(request.getRouteBeanKey(), routeBean);

    }

    private void setRouteBeanApiId(OpenApiHttpRequestBean request, OpenApiRouteBean routeBean) {
        String requestUrl = request.getRequestUrl();
        if (this.contextPath != null) {
            String subString = requestUrl.substring(requestUrl.indexOf(contextPath) + contextPath.length() + 1);
            routeBean.setTargetUrl(subString);
            int index = subString.indexOf("/");
            if (index != -1) {
                String apiId = subString.substring(0, index);
                routeBean.setApiId(apiId);
            } else {
                routeBean.setApiId(subString);
            }

        } else {
            // /test
            String subString = requestUrl.substring(1);
            routeBean.setTargetUrl(subString);
            int index = subString.indexOf("/");
            if (index != -1) {
                String apiId = subString.substring(0, index);
                routeBean.setApiId(apiId);
            } else {
                routeBean.setApiId(subString);
            }
        }
    }
    /*
     * private CacheService cacheService;
     * 
     * public void setCacheService(CacheService cacheService) {
     * this.cacheService = cacheService; }
     */

    private void validateParam(OpenApiHttpRequestBean requestBean) {
        /*
         * String appId = requestBean.getAppId(); if
         * (StringUtils.isBlank(appId)) { // appId为空
         * setError(OauthErrorEnum.APP_ID.getErrCode(),
         * OauthErrorEnum.APP_ID.getErrMsg(), requestBean); return; } String
         * appToken = requestBean.getAppToken();
         * 
         * if (StringUtils.isBlank(appToken)) {// appToken为空
         * setError(OauthErrorEnum.APP_TOKEN.getErrCode(),
         * OauthErrorEnum.APP_TOKEN.getErrMsg(), requestBean); return; } if
         * (StringUtils.isBlank(requestBean.getApiId())) {
         * setError(OauthErrorEnum.API_ID.getErrCode(),
         * OauthErrorEnum.API_ID.getErrMsg(), requestBean); return; }
         */

    }

    // step1
    private void authRequestBean(OpenApiHttpRequestBean requestBean) {
        // 对于请求信息进行审计
        logger.info("authRequestBean权限校验...");
        if (this.authenticationService != null) {
            this.authenticationService.doAuthOpenApiHttpRequestBean(requestBean);
        }
    }

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

        // 参数校验
        validateParam(request);
        // 权限校验
        authRequestBean(request);

        initRouteBean(httpSessionBean.getRequest(), openApiContext); // 初始化路由bean
        if (logger.isDebugEnabled()) {
            logger.info(
                    String.format("end run doExecuteBiz,currentTime=%d,elapase_time=%d milseconds,httpSessonBean=%s",
                            System.currentTimeMillis(), (System.currentTimeMillis() - currentTime), httpSessionBean));
        }

        if (request.getReturnContent() != null) {
            return true;
        }
        return false;
    }

    // 外部依赖的服务--------------------------------------
    private AuthenticationService authenticationService;

    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    private String contextPath;

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

}
