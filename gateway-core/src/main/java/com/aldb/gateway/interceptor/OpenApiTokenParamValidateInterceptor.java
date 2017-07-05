/**
 * 
 */
package com.aldb.gateway.interceptor;

import javax.servlet.http.HttpServletRequest;

import com.aldb.gateway.common.OpenApiHttpRequestBean;
import com.aldb.gateway.common.util.CommonCodeConstants;

/**
 * @author Administrator
 *
 */
public class OpenApiTokenParamValidateInterceptor extends AbstractOpenApiValidateInterceptor {

    @Override
    protected OpenApiHttpRequestBean iniOpenApiHttpRequestBean(HttpServletRequest request) {
        OpenApiHttpRequestBean bean = new OpenApiHttpRequestBean();
        bean.setOperationType(CommonCodeConstants.API_TOKEN_KEY);
        return bean;
    }

}
