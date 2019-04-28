package com.z.gateway.core.support;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.z.gateway.common.OpenApiRouteBean;
import com.z.gateway.common.entity.ApiServerInfo;
import com.z.gateway.common.util.CommonCodeConstants;
import com.z.gateway.core.OpenApiHttpClientService;
import com.z.gateway.service.ApiServerInfoReq;
import com.z.gateway.service.ApiServerInfoService;

import com.z.gateway.util.UrlUtil;

/**
 * @author sunff
 *
 */
public class HystrixServiceCommand extends HystrixCommand<String> {

    protected static Log logger = LogFactory.getLog(HystrixServiceCommand.class);

    private OpenApiHttpClientService apiHttpClientService;
    private ApiServerInfoService apiInterfaceService;
    private OpenApiRouteBean routeBean;

    public HystrixServiceCommand(OpenApiHttpClientService apiHttpClientService,
            ApiServerInfoService apiInterfaceService, OpenApiRouteBean routeBean) {
        super(setter());
        this.apiHttpClientService = apiHttpClientService;
        this.apiInterfaceService = apiInterfaceService;
        this.routeBean = routeBean;
    }

    private static Setter setter() {
        HystrixCommandGroupKey groupKey = HystrixCommandGroupKey.Factory.asKey("httpclient");
        HystrixCommandKey commandKey = HystrixCommandKey.Factory.asKey("requestRemoteServer");
        HystrixThreadPoolKey threadPoolKey = HystrixThreadPoolKey.Factory.asKey("httpclient-pool");

        HystrixThreadPoolProperties.Setter threadPoolProerties = HystrixThreadPoolProperties.Setter().withCoreSize(10)
                .withKeepAliveTimeMinutes(5).withMaxQueueSize(Integer.MAX_VALUE).withQueueSizeRejectionThreshold(10000)
                // 最大并发统计采样
                .withMetricsRollingStatisticalWindowInMilliseconds(1000).withMetricsRollingStatisticalWindowBuckets(10)

        ;

        HystrixCommandProperties.Setter commandProperties = HystrixCommandProperties.Setter()
                // 隔离参数
                .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)
                // 降级参数
                .withFallbackEnabled(true).withFallbackIsolationSemaphoreMaxConcurrentRequests(100)

                // .withExecutionIsolationThreadInterruptOnFutureCancel(true)
                .withExecutionTimeoutEnabled(true)

                .withExecutionIsolationThreadInterruptOnTimeout(true)

                .withExecutionTimeoutInMilliseconds(1000)
                // 熔断参数
                .withCircuitBreakerEnabled(true).withCircuitBreakerForceClosed(false).withCircuitBreakerForceOpen(false)
                .withCircuitBreakerErrorThresholdPercentage(50).withCircuitBreakerRequestVolumeThreshold(20)
                .withCircuitBreakerSleepWindowInMilliseconds(5000)
                // 最大并发采样参数
                .withMetricsRollingPercentileWindowInMilliseconds(10000).withMetricsRollingPercentileWindowBuckets(10);

        return HystrixCommand.Setter.withGroupKey(groupKey).andCommandKey(commandKey).andThreadPoolKey(threadPoolKey)
                .andThreadPoolPropertiesDefaults(threadPoolProerties).andCommandPropertiesDefaults(commandProperties);
    }

    @Override
    protected String getFallback() {
        return "please try again";
    }

    @Override
    protected String run() throws Exception {
        String serviceRspData = null; // 后端服务返回值
        String operationType = routeBean.getOperationType();
        String requestMethod = routeBean.getRequestMethod();

        if (operationType.equals(CommonCodeConstants.API_SYSERVICE_KEY)) {

        } else if (CommonCodeConstants.API_GETDATA_KEY.equals(operationType)) {

        } else if (CommonCodeConstants.API_SERVICE_KEY.equals(operationType)) {
            logger.info(String.format("{serviceId:%s ,version:%s }", routeBean.getApiId(), routeBean.getVersion()));
            ApiServerInfo apiInfo = apiInterfaceService
                    .queryApiInterfaceByApiId(new ApiServerInfoReq(routeBean.getApiId(), routeBean.getVersion()));

            if (apiInfo == null) {
//                return StringResponseUtil
//                        .encodeResp(String.format("this apiId=%s,version=%s has off line,please use another one",
//                                routeBean.getApiId(), routeBean.getVersion()).getBytes());
                
               return String.format("this apiId=%s,version=%s has off line,please use another one",
                        routeBean.getApiId(), routeBean.getVersion());
            }
            apiInfo.setTargetUrl(routeBean.getTargetUrl());
            apiInfo.setRequestMethod(routeBean.getRequestMethod());
            if (CommonCodeConstants.REQUEST_METHOD.GET.name().equalsIgnoreCase(requestMethod)) { // get请求
                String url = apiInfo.getUrl();
                url = UrlUtil.dealUrl(url, routeBean.getThdApiUrlParams());

                logger.info(String.format("{service url:%s} ", url));
                // String contentType =
                // bean.getReqHeader().get(CONTENT_TYPE_KEY);
                if (url.startsWith(CommonCodeConstants.HTTPS)) {
                    if (routeBean.getServiceGetReqData() == null) {
                        return apiHttpClientService.doHttpsGet(url, routeBean.getReqHeader());
                    } else {
                        return apiHttpClientService.doHttpsGet(url, routeBean.getServiceGetReqData(),
                                routeBean.getReqHeader());
                    }
                } else {
                    if (routeBean.getServiceGetReqData() == null) {
                        return apiHttpClientService.doGet(url, routeBean.getReqHeader());
                    } else {
                        return apiHttpClientService.doGet(url, routeBean.getServiceGetReqData(),
                                routeBean.getReqHeader());
                    }
                }

            } else if (CommonCodeConstants.REQUEST_METHOD.POST.name().equalsIgnoreCase(requestMethod)) {// post请求
                String url = apiInfo.getUrl();
                logger.info(String.format("{service url:%s} ", url));
                String reqData = routeBean.getServiceReqData(); // 请求的json格式数据参数
                if (StringUtils.isNotBlank(reqData) && reqData.length() > this.maxReqDataLth) {
                    reqData = reqData.substring(0, this.maxReqDataLth - 1);
                }
                logger.info(String.format("{serviceId:%s ,reqData:%s }", routeBean.getApiId(), reqData));

                // String contentType =
                // bean.getReqHeader().get(CONTENT_TYPE_KEY);
                if (url.startsWith(CommonCodeConstants.HTTPS)) {
                    return apiHttpClientService.doHttpsPost(url, routeBean.getServiceReqData(),
                            routeBean.getReqHeader());
                } else {
                    return apiHttpClientService.doPost(url, routeBean.getServiceReqData(), routeBean.getReqHeader());
                }
            }
        }
        return serviceRspData;
    }

    private final int maxReqDataLth = 500;

}
