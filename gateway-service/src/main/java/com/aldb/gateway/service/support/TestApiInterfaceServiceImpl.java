/**
 * 
 */
package com.aldb.gateway.service.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.aldb.gateway.common.entity.ApiInterface;
import com.aldb.gateway.common.util.CommonCodeConstants;
import com.aldb.gateway.service.ApiInterfaceService;

/**
 * @author Administrator
 *
 */
public class TestApiInterfaceServiceImpl implements ApiInterfaceService {

    private static Log log = LogFactory.getLog(TestApiInterfaceServiceImpl.class);

    @Override
    public ApiInterface findOne(String apiId, String version) {
        log.info("为了测试，关于这个接口，现在直接返回一个接口的值");
        if (apiId.equals("1")) {

            ApiInterface aif = new ApiInterface();
            aif.setApiId(apiId);
            aif.setVersion(version);
            aif.setProtocol("http");
            aif.setHostAddress("www.baidu.com");
            // aif.setPort(null);
            aif.setRequestMethod(CommonCodeConstants.REQUEST_METHOD.GET.name());
            // aif.setTargetUrl("/");
            return aif;
        } else if (apiId.equals("2")) {
            ApiInterface aif = new ApiInterface();
            aif.setApiId(apiId);
            aif.setVersion(version);
            aif.setProtocol("http");
            aif.setHostAddress("www.sina.com");
            // aif.setPort(null);
            aif.setRequestMethod(CommonCodeConstants.REQUEST_METHOD.GET.name());
            // aif.setTargetUrl("/");
            return aif;
        }else if (apiId.equals("3")) {
            ApiInterface aif = new ApiInterface();
            aif.setApiId(apiId);
            aif.setVersion(version);
            aif.setProtocol("http");
            aif.setHostAddress("www.jd.com");
            // aif.setPort(null);
            aif.setRequestMethod(CommonCodeConstants.REQUEST_METHOD.GET.name());
            // aif.setTargetUrl("/");
            return aif;
        }else  if (apiId.equals("4")) {

            ApiInterface aif = new ApiInterface();
            aif.setApiId(apiId);
            aif.setVersion(version);
            aif.setProtocol("https");
            aif.setHostAddress("www.baidu.com");
            // aif.setPort(null);
            aif.setRequestMethod(CommonCodeConstants.REQUEST_METHOD.GET.name());
            // aif.setTargetUrl("/");
            return aif;
        }else  if (apiId.equals("5")) {

            ApiInterface aif = new ApiInterface();
            aif.setApiId(apiId);
            aif.setVersion(version);
            aif.setProtocol("https");
            aif.setHostAddress("www.jd.com");
            // aif.setPort(null);
            aif.setRequestMethod(CommonCodeConstants.REQUEST_METHOD.GET.name());
            // aif.setTargetUrl("/");
            return aif;
        }
        return null;

    }

}
