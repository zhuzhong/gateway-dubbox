/**
 * 
 */
package com.z.gateway.service.support;

import java.util.ArrayList;
import java.util.List;

import com.z.gateway.common.entity.ApiServerInfo;
import com.z.gateway.common.util.CommonCodeConstants;
import com.z.gateway.service.ApiServerInfoReq;
import com.z.gateway.service.RegistryReaderService;

/**
 * @author sunff
 *
 */
public class DemoRegistryReaderServiceImpl implements RegistryReaderService {

    @Override
    public List<ApiServerInfo> queryApiInterfaceByApiId(ApiServerInfoReq req) {
        List<ApiServerInfo> s = new ArrayList<>();

        if ("sina".equals(req.getApiId())) {

            ApiServerInfo aif = new ApiServerInfo();
            aif.setApiId(req.getApiId());
            aif.setProtocol("http");
            aif.setHostAddress("www.sina.com");
            aif.setRequestMethod(CommonCodeConstants.REQUEST_METHOD.GET.name());

            s.add(aif);
        }

        return s;
    }

}
