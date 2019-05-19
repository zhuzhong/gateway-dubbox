/**
 * 
 */
package com.z.gateway.service.support;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

	
	private List<RegistryReaderService> registryReaderServiceImpls;
    @Override
    public ApiServerInfo queryApiInterfaceByApiId(ApiServerInfoReq req) {
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
    		 List<String> sets=serviceInfos.stream().map(a->a.toString()).collect(Collectors.toList());
    		 
             String hostAddress = loadBalancerService.chooseOne(new LbKey(req.getApiId(),req.getApiId()), sets);
             ApiServerInfo apiInterface = new ApiServerInfo();
             apiInterface.setApiId(req.getApiId());
             apiInterface.setProtocol(CommonCodeConstants.HTTP);
             apiInterface.setHostAddress(hostAddress);
             return apiInterface;
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
