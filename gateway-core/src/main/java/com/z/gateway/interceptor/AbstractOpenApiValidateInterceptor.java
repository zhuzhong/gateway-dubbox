/**
 * 
 */
package com.z.gateway.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.z.gateway.common.OpenApiHttpRequestBean;
import com.z.gateway.common.util.CommonCodeConstants;

/**请求拦截器
 * @author Administrator
 *
 */
public abstract class AbstractOpenApiValidateInterceptor implements HandlerInterceptor {

   
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {
        // TODO Auto-generated method stub
        
    }

    
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {
        // TODO Auto-generated method stub
        
    }

  
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
    	String requestMethod=request.getMethod();
    	if(!requestMethod.equals(CommonCodeConstants.REQUEST_METHOD.GET.name())&&!requestMethod.equals(CommonCodeConstants.REQUEST_METHOD.POST.name())){
    		throw new RuntimeException("请求方法不对，请求方法必须是 GET 或POST");
    	}
        // 初始化请求bean
        OpenApiHttpRequestBean reqBean = iniOpenApiHttpRequestBean(request);
        request.setAttribute(CommonCodeConstants.REQ_BEAN_KEY, reqBean);
        return true;
    }
    
    protected abstract OpenApiHttpRequestBean iniOpenApiHttpRequestBean(
            HttpServletRequest request);

}
