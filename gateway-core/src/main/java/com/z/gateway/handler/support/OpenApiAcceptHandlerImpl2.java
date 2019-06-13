/**
 * 
 */
package com.z.gateway.handler.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.alibaba.fastjson.JSON;
import com.z.gateway.common.OpenApiHttpRequestBean;
import com.z.gateway.common.util.CommonCodeConstants;
import com.z.gateway.handler.OpenApiAcceptHandler;
import com.z.gateway.handler.OpenApiHandlerExecuteTemplate;
import com.z.gateway.protocol.OpenApiContext;
import com.z.gateway.protocol.OpenApiHttpSessionBean;
import com.z.gateway.service.IdService;
import com.z.gateway.util.OpenApiResponseUtils;

/**在使用netty的情况，这个不需要再次异步处理，所以同步即可
 * @author sunff
 *
 */
public class OpenApiAcceptHandlerImpl2 implements OpenApiAcceptHandler, ApplicationContextAware {

    private static Logger logger = LoggerFactory.getLogger(OpenApiAcceptHandlerImpl2.class);
    private IdService idService;
    
    public void setIdService(IdService idService) {
        this.idService = idService;
    }


    @Override
    public void acceptRequest(HttpServletRequest request, HttpServletResponse response) {
        OpenApiHttpRequestBean reqBean = (OpenApiHttpRequestBean) request.getAttribute(CommonCodeConstants.REQ_BEAN_KEY);
        String traceId = idService.genInnerRequestId();
        reqBean.setTraceId(traceId);
        request.setAttribute(CommonCodeConstants.REQ_BEAN_KEY, reqBean); // 重新设置bean
      
        logger.info("requestId={} request begin,reqeust={}",traceId, JSON.toJSONString(reqBean));
        addTask2Pool(response, new OpenApiHttpSessionBean(reqBean));
    }

    

    private void addTask2Pool(HttpServletResponse response, OpenApiHttpSessionBean sessionBean) {
        long currentTime = System.currentTimeMillis();
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("begin deal_sessionbean,current_time=%d,sessionbean=%s ", currentTime,
                    sessionBean));
        }
        logger.info("added one task to thread pool");
        String operationType = sessionBean.getRequest().getOperationType();

        OpenApiHandlerExecuteTemplate handlerExecuteTemplate = applicationContext.getBean(operationType,
                OpenApiHandlerExecuteTemplate.class);
        
        OpenApiContext context = new OpenApiContext();
        context.setOpenApiHttpSessionBean(sessionBean);
        try {
			handlerExecuteTemplate.execute(context);
		} catch (Exception e) {
			e.printStackTrace();
		}
        OpenApiHttpSessionBean tmp=  context.getOpenApiHttpSessionBean();
        
        // 写入响应
        OpenApiResponseUtils.writeRsp(response, tmp.getRequest());
        logger.debug(
                    "end deal_sessionbean,current_time=%d,elapase_time={} milseconds,sessionbean={}",
                    System.currentTimeMillis(), (System.currentTimeMillis() - currentTime), tmp);
    }

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}

