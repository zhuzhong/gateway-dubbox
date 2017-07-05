package com.aldb.gateway.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.aldb.gateway.common.OpenApiHttpRequestBean;
import com.aldb.gateway.common.util.CommonCodeConstants;
import com.aldb.gateway.util.OpenApiResponseUtils;
import com.alibaba.fastjson.JSON;

public class OpenApiExceptionHandler implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {

		OpenApiHttpRequestBean reqBean = (OpenApiHttpRequestBean) request
				.getAttribute(CommonCodeConstants.REQ_BEAN_KEY);

		reqBean.setPrintStr(JSON.toJSONString(ex));
		OpenApiResponseUtils.writeRsp(response, reqBean);

		return null;
	}

}
