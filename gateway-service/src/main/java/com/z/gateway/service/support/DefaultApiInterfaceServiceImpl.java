/**
 * 
 */
package com.z.gateway.service.support;

import com.z.gateway.common.entity.ApiInterface;
import com.z.gateway.common.util.CommonCodeConstants;
import com.z.gateway.service.ApiInterfaceService;

/**
 * @author zhuzhong
 *
 */
public class DefaultApiInterfaceServiceImpl implements ApiInterfaceService {

    @Override
    public ApiInterface queryApiInterfaceByApiId(String apiId, String version) {
        if (apiId != null && apiId.equals("1")) {
            ApiInterface apiInterface = new ApiInterface();
            apiInterface.setApiId(apiId);
            apiInterface.setProtocol(CommonCodeConstants.HTTP);
            apiInterface.setHostAddress("localhost:8380");
            return apiInterface;
        } else if(apiId!=null&& apiId.equals("magicmall")){
            ApiInterface apiInterface = new ApiInterface();
            apiInterface.setApiId(apiId);
            apiInterface.setProtocol(CommonCodeConstants.HTTP);
            apiInterface.setHostAddress("http://www.baidu.com/");
            return apiInterface;
        }
        return null;
    }

}
