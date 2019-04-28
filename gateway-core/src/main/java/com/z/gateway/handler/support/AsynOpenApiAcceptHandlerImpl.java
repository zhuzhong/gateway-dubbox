/**
 * 
 */
package com.z.gateway.handler.support;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.alibaba.fastjson.JSON;
import com.z.gateway.common.OpenApiHttpRequestBean;
import com.z.gateway.common.util.CommonCodeConstants;
import com.z.gateway.handler.OpenApiAcceptHandler;
import com.z.gateway.handler.OpenApiHandlerExecuteTemplate;
import com.z.gateway.protocol.OpenApiContext;
import com.z.gateway.protocol.OpenApiHttpSessionBean;
import com.z.gateway.service.IdService;
import com.z.gateway.util.OpenApiResponseUtils;

/**
 * 异步处理请求
 * 
 * @author sunff
 * 
 */
public class AsynOpenApiAcceptHandlerImpl implements OpenApiAcceptHandler, ApplicationContextAware {

	private static Logger logger = LoggerFactory.getLogger(AsynOpenApiAcceptHandlerImpl.class);
	
    private ThreadPoolTaskExecutor taskExecutor;
    private IdService idService;

    public void setTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public void setIdService(IdService idService) {
        this.idService = idService;
    }

    

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
                  
                    logger.info("requestId={} request begin,reqeust={}",traceId,JSON.toJSONString(reqBean));
                    
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
                    logger.info("write content end...");
                } finally {
                    context.complete();
                    logger.info("request complete...");
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
