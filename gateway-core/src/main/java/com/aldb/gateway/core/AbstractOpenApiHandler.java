package com.aldb.gateway.core;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.aldb.gateway.common.OpenApiHttpRequestBean;
import com.aldb.gateway.common.exception.OauthErrorEnum;
import com.aldb.gateway.common.exception.OpenApiException;

public abstract class AbstractOpenApiHandler implements Command {

    protected static Log logger = LogFactory.getLog(AbstractOpenApiHandler.class);

    // public String accessServiceUri;

    // public String accessTokenUri;

    // public String openApiCacheName;

    public final String CONTENT_TYPE_KEY = "content-type";
    public final String CONTENT_TYPE_XML = "application/xml";
    public final String CONTENT_TYPE_JSON = "application/json";
    public final String HEADER_HOST_KEY = "host";
    public final String HEADER_SERVER_KEY = "server";

    /*
     * public OauthErrorEnum getBlErrorObj(String errorCode) { String
     * blErrorCode = OauthErrorEnum.getOauthErrorMap().get(errorCode);
     * OauthErrorEnum error = null; if (StringUtils.isBlank(blErrorCode)) {
     * error = OauthErrorEnum.ACCESS_DENIED; } else { error =
     * OauthErrorEnum.getErr(blErrorCode); } return error; }
     */

    public Map<String, String> getHeadersInfo(HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }

        return map;
    }

    public void validateRequestHeader(OpenApiHttpRequestBean routeBean) {
        String contentType = routeBean.getReqHeader().get(CONTENT_TYPE_KEY);
        if (StringUtils.isBlank(contentType)) {
            throw new OpenApiException(OauthErrorEnum.CONTENTTYPE.getErrCode(), OauthErrorEnum.CONTENTTYPE.getErrMsg());
        }
        if (!contentType.contains(CONTENT_TYPE_JSON) && !contentType.contains(CONTENT_TYPE_XML)) {
            throw new OpenApiException(OauthErrorEnum.INVALID_CONTENTTYPE.getErrCode(),
                    OauthErrorEnum.INVALID_CONTENTTYPE.getErrMsg());
        }
    }

    // step1
    @Override
    public boolean execute(Context context) {
        // logger.info("step1,调用execute　方法");
        return doExcuteBiz(context);
    }

    public abstract boolean doExcuteBiz(Context context);

   /* protected CacheService cacheService = new DefaultCacheServiceImpl(); // 饿汉模式，注入一个默认的

    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }*/
}
