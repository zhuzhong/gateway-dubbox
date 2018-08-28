package com.z.gateway.service.support;

import java.util.List;

import org.springframework.util.CollectionUtils;

import com.z.gateway.common.OpenApiRouteBean;
import com.z.gateway.service.AuthenticationService;

public class DefaultAuthenticationServiceImpl implements AuthenticationService {

    
    List<AuthenticationSingelService> innerAuthenticationServices;
    
	public void setInnerAuthenticationServices(List<AuthenticationSingelService> innerAuthenticationServices) {
        this.innerAuthenticationServices = innerAuthenticationServices;
    }
	
    /**
	 * 鉴定该接口是否对于该app进行授权
	 */
	@Override
	public void doAuthOpenApiHttpRequestBean(OpenApiRouteBean requestBean) {
	    if(CollectionUtils.isEmpty(innerAuthenticationServices)) {
	        return;
	    }
	    
	for(AuthenticationSingelService authenticationService:innerAuthenticationServices) {
	    if(authenticationService.doAuthOpenApiHttpRequestBean(requestBean)) {return;}
	}
	    
	    
	}

}
