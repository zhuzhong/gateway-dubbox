package com.z.gateway.core.support;

import org.apache.commons.chain.Context;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.z.gateway.common.OpenApiHttpRequestBean;
import com.z.gateway.common.exception.OpenApiException;
import com.z.gateway.common.exception.OpenApiServiceErrorEnum;
import com.z.gateway.core.AbstractOpenApiHandler;
import com.z.gateway.core.OpenApiRouteBean;
import com.z.gateway.protocol.OpenApiContext;
import com.z.gateway.protocol.OpenApiHttpSessionBean;

public class OpenApiRspHandler extends AbstractOpenApiHandler {
	private static final Log logger = LogFactory
			.getLog(OpenApiRspHandler.class);

	/*
	 * @Override public boolean execute(Context context) {
	 * logger.info("step1----"); return doExcuteBiz(context); }
	 */

	@Override
	public boolean doExcuteBiz(Context context) {
		logger.info("step2----");
		OpenApiContext blCtx = (OpenApiContext) context;
		OpenApiHttpSessionBean httpSessionBean = (OpenApiHttpSessionBean) blCtx
				.getOpenApiHttpSessionBean();
		OpenApiHttpRequestBean request = httpSessionBean.getRequest();
		long currentTime = System.currentTimeMillis();
		if (logger.isDebugEnabled()) {
			logger.info(String.format(
					"begin run doExecuteBiz,currentTime=%d,httpSessonBean=%s",
					currentTime, httpSessionBean));
		}
	/*	String printStr = this.executePrint(request,blCtx);
		request.setPrintStr(printStr);
	*/	
		
	
		
		OpenApiRouteBean routeBean = (OpenApiRouteBean) blCtx
                .get(request.getRouteBeanKey());
		request.setReqHeader(routeBean.getReqHeader()); //把头给还回去了，为了支持获取图形验证码之类的需求
		request.setReturnContent(routeBean.getReturnContent());
		
		if (logger.isDebugEnabled()) {
			logger.info(String
					.format("end run doExecuteBiz,currentTime=%d,elapase_time=%d milseconds,httpSessonBean=%s",
							System.currentTimeMillis(),
							(System.currentTimeMillis() - currentTime),
							httpSessionBean));
		}

		return false;
	}

	
   /* private  CacheService cacheService;

    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }*/
    
    
	private String executePrint(OpenApiHttpRequestBean request,OpenApiContext blCtx) {
		logger.info("step3...");
		try {
			return this.getResponseBody(request,blCtx);
		} catch (Exception e) {
			OpenApiException ex = null;
			if (e instanceof OpenApiException) {
				ex = (OpenApiException) e;
			} else {
				ex = new OpenApiException(OpenApiServiceErrorEnum.SYSTEM_BUSY,
						e.getCause());
			}
			logger.error("executePrint error, " + ex.getMessage());
			// return XmlUtils.bean2xml((ex.getShortMsg("unknow")));
			return "error";
		} finally {
			// 从redis移除当前routebean
			/*String routeBeanKey = request.getRouteBeanKey();
			if (StringUtils.isNotBlank(routeBeanKey)) {
				cacheService.remove(routeBeanKey);
			}*/

			/*
			 * // 设置同步信号unlock redisKey =
			 * request.getUserTokenSyncSingalRedisKey(); if
			 * (StringUtils.isNotBlank(redisKey)) { Cache redisCache =
			 * this.cacheManager.getCache(openApiCacheName);
			 * redisCache.put(request.getUserTokenSyncSingalRedisKey(),
			 * CommonCodeConstants.SyncSingalType.SingalUnLock.getCode()); }
			 */
		}

	}

	private String getResponseBody(OpenApiHttpRequestBean bean,OpenApiContext blCtx) {
		logger.info("step4....");
		String routeBeanKey = bean.getRouteBeanKey();
		OpenApiRouteBean routeBean = (OpenApiRouteBean) blCtx
				.get(routeBeanKey);
		Object body = (Object) routeBean.getServiceRsp();
		if (body instanceof String) {
			return body.toString();
		} else {
			throw new RuntimeException("返回内容格式不对...");
		}

	}
}
