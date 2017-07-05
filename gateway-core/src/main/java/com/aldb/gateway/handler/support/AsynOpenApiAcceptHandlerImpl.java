/**
 * 
 */
package com.aldb.gateway.handler.support;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.aldb.gateway.common.OpenApiHttpRequestBean;
import com.aldb.gateway.common.util.CommonCodeConstants;
import com.aldb.gateway.handler.OpenApiAcceptHandler;
import com.aldb.gateway.handler.OpenApiHandlerExecuteTemplate;
import com.aldb.gateway.protocol.OpenApiContext;
import com.aldb.gateway.protocol.OpenApiHttpSessionBean;
import com.aldb.gateway.service.IdService;
import com.aldb.gateway.util.OpenApiResponseUtils;
import com.alibaba.fastjson.JSON;

/**
 * 异步处理请求
 * 
 * @author sunff
 * 
 */
public class AsynOpenApiAcceptHandlerImpl implements OpenApiAcceptHandler, ApplicationContextAware {

    private ThreadPoolTaskExecutor taskExecutor;
    private IdService idService;

    public void setTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public void setIdService(IdService idService) {
        this.idService = idService;
    }

    private static Log logger = LogFactory.getLog(AsynOpenApiAcceptHandlerImpl.class);

    @Override
    public void acceptRequest(HttpServletRequest request, HttpServletResponse response) {

        final AsyncContext context = request.startAsync(request, response);
        taskExecutor.submit(new Runnable() {

            @Override
            public void run() {
                try {
                    HttpServletRequest asynRequest = (HttpServletRequest) context.getRequest();

                    OpenApiHttpRequestBean reqBean = (OpenApiHttpRequestBean) asynRequest
                            .getAttribute(CommonCodeConstants.REQ_BEAN_KEY);
                    String traceId = idService.genInnerRequestId();
                    reqBean.setTraceId(traceId);
                    asynRequest.setAttribute(CommonCodeConstants.REQ_BEAN_KEY, reqBean); // 重新设置bean
                    if (logger.isInfoEnabled()) {
                        logger.info(String.format("requestId=%s request begin,reqeust=%s", traceId,
                                JSON.toJSONString(reqBean)));
                    }

                    OpenApiHttpSessionBean sessionBean = new OpenApiHttpSessionBean(reqBean);
                    String operationType = sessionBean.getRequest().getOperationType();
                    OpenApiHandlerExecuteTemplate handlerExecuteTemplate = applicationContext.getBean(operationType,
                            OpenApiHandlerExecuteTemplate.class);
                    OpenApiContext openApiContext = new OpenApiContext();
                    openApiContext.setOpenApiHttpSessionBean(sessionBean);
                    try {
                        handlerExecuteTemplate.execute(openApiContext);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // 写入响应
                    OpenApiResponseUtils.writeRsp((HttpServletResponse) context.getResponse(), sessionBean.getRequest());
                } finally {
                    context.complete();
                }

            }
        });
    }

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
