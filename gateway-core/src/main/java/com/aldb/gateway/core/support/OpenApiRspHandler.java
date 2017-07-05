package com.aldb.gateway.core.support;

import org.apache.commons.chain.Context;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.aldb.gateway.common.OpenApiHttpRequestBean;
import com.aldb.gateway.common.exception.OpenApiException;
import com.aldb.gateway.common.exception.OpenApiServiceErrorEnum;
import com.aldb.gateway.core.AbstractOpenApiHandler;
import com.aldb.gateway.core.OpenApiRouteBean;
import com.aldb.gateway.protocol.OpenApiContext;
import com.aldb.gateway.protocol.OpenApiHttpSessionBean;
import com.aldb.gateway.service.CacheService;

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
		String printStr = this.executePrint(request);
		request.setPrintStr(printStr);

		if (logger.isDebugEnabled()) {
			logger.info(String
					.format("end run doExecuteBiz,currentTime=%d,elapase_time=%d milseconds,httpSessonBean=%s",
							System.currentTimeMillis(),
							(System.currentTimeMillis() - currentTime),
							httpSessionBean));
		}

		return false;
	}

	
    private  CacheService cacheService;

    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }
    
    
	private String executePrint(OpenApiHttpRequestBean request) {
		logger.info("step3...");
		try {
			return this.getResponseBody(request);
		} catch (Exception e) {
			OpenApiException ex = null;
			if (e instanceof OpenApiException) {
				ex = (OpenApiException) e;
			} else {
				ex = new OpenApiException(OpenApiServiceErrorEnum.SYSTEM_BUSY,
						e.getCause());
			}
			logger.error("executePrint error, " + e.getMessage());
			// return XmlUtils.bean2xml((ex.getShortMsg("unknow")));
			return "error";
		} finally {
			// 从redis移除当前routebean
			String routeBeanKey = request.getRouteBeanKey();
			if (StringUtils.isNotBlank(routeBeanKey)) {
				cacheService.remove(routeBeanKey);
			}

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

	private String getResponseBody(OpenApiHttpRequestBean bean) {
		logger.info("step4....");
		String routeBeanKey = bean.getRouteBeanKey();
		OpenApiRouteBean routeBean = (OpenApiRouteBean) cacheService
				.get(routeBeanKey);
		Object body = (Object) routeBean.getServiceRsp();
		if (body instanceof String) {
			return body.toString();
		} else {
			throw new RuntimeException("返回内容格式不对...");
		}

	}
}
