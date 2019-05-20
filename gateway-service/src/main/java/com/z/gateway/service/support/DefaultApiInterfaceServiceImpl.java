/**
 * 
 */
package com.z.gateway.service.support;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.z.gateway.common.entity.ApiServerInfo;
import com.z.gateway.common.util.CommonCodeConstants;
import com.z.gateway.service.ApiServerInfoReq;
import com.z.gateway.service.ApiServerInfoService;
import com.z.gateway.service.RegistryReaderService;
import com.z.gateway.service.lb.LbKey;
import com.z.gateway.service.lb.LoadBalanceService;
import com.z.gateway.service.lb.support.RandomLoadBalanceImpl;

/**
 * @author zhuzhong
 *
 */
public class DefaultApiInterfaceServiceImpl implements ApiServerInfoService {

    private static final Logger logger=LoggerFactory.getLogger(DefaultApiInterfaceServiceImpl.class);
	
	private List<RegistryReaderService> registryReaderServiceImpls;
    @Override
    public ApiServerInfo queryApiInterfaceByApiId(ApiServerInfoReq req) {
        logger.info("req={}",req);
    	if(CollectionUtils.isEmpty(registryReaderServiceImpls)) {
    		return null;
    	}
    	List<ApiServerInfo> serviceInfos=new ArrayList<>();
    	for(RegistryReaderService s:registryReaderServiceImpls) {
    		List<ApiServerInfo> list=s.queryApiInterfaceByApiId(req);
    		if(!CollectionUtils.isEmpty(list)) {
    			serviceInfos.addAll(list);
    		}
    		
    	}
    	
    	 if (!CollectionUtils.isEmpty(serviceInfos)) {
    		// List<String> sets=serviceInfos.stream().map(a->a.toString()).collect(Collectors.toList());
    		 
    	     ApiServerInfo hostAddress = loadBalancerService.chooseOne(new LbKey(req.getApiId(),req.getApiId()), serviceInfos);
//             ApiServerInfo apiInterface = new ApiServerInfo();
//             apiInterface.setApiId(req.getApiId());
//             apiInterface.setProtocol(CommonCodeConstants.HTTP);
//             apiInterface.setHostAddress(hostAddress);
//             return apiInterface;
    	     return hostAddress;
         }
    	
    	
    	return null;
    }
    
    
    private LoadBalanceService loadBalancerService;
    
    
    public void init() {
      
        if (loadBalancerService == null) {
            loadBalancerService = new RandomLoadBalanceImpl();
        }
       
    }
    
    
    public void setLoadBalancerService(LoadBalanceService loadBalancerService) {
        this.loadBalancerService = loadBalancerService;
    }


	public void setRegistryReaderServiceImpls(List<RegistryReaderService> registryReaderServiceImpls) {
		this.registryReaderServiceImpls = registryReaderServiceImpls;
	}
    
	
    
    
    
    

}
