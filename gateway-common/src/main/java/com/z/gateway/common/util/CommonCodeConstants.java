/**
 * 
 */
package com.z.gateway.common.util;

import java.io.Serializable;

/**
 * @author Administrator
 *
 */
public class CommonCodeConstants implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2305455673904228996L;
    public static final String API_SERVICE_KEY = "openapi.service.HandlerExecuteTemplate";
    public static final String API_TOKEN_KEY = "openapi.gettoken.HandlerExecuteTemplate";
    public static final String API_WTSERVICE_KEY = "openapi.wtService";
    public static final String API_SYSERVICE_KEY = "openapi.syService";
    public static final String API_GETDATA_KEY = "openapi.getSyData";

    public static final String HTTP = "http";
    public static final String HTTPS = "https";
    public static final String REQ_BEAN_KEY = "GATE_WAY_BEAN";

    public static final String ROUTE_BEAN_KEY = "BL_OPENAPI_ROUTE_BEAN";
    public static final String USER_TOKEN_KEY = "BL_OPENAPI_USER_TOKEN";
    public static final String USER_TOKEN_SYNC_SINGAL_KEY = "BL_OPENAPI_USER_TOKEN_SYNC_SINGAL";

    public static final String CTRL_STR = "_";
    public static final String ROUTE_APPID_KEY = "appId";
    public static final String ROUTE_TOKEN_KEY = "accessToken";

    public static final String SERVICE_TYPE_KEY = "servcieType";
    public static final String SERVICE_BODY_KEY = "body";
    public static final String SYSTEM_ERROR_KEY = "500";
    public static final String SERVICE_INVOKE_DATA = "servcieInvokeData";
    public static final String MDF_CHARSET_UTF_8 = "UTF-8";
    public static final String LOSE_MESSAGE_SENDSTATUS_UNSEND = "0";
    public static final String USER_LOGIN_KEY = "BL_OPENAPI_USER_LOGIN";

    public static final String TRACE_ID = "traceId";

    /**
     * 
     * 公共的请求参数 appId String 16 是 打包app的唯一标识 not null appToken String 32 是
     * app授权令牌,用于授权 not null apiId String 64 是 API编码即api的唯一标识 not null
     * apiVersion String 8 是 API版本号 not null timeStamp String 19 是
     * 时间戳，格式为yyyy-mm-dd HH:mm:ss，时区为GMT+8 not null signMethod String 8 是
     * 生成服务请求签名字符串所使用的算法类型，目前仅支持MD5， sign String 32 是 服务请求的签名字符串 not null
     * deviceToken String 16 否 硬件标识token,app首次安装时发放的硬件唯一性标识 userToken String 16
     * 否 用户token
     * 
     * 
     * application/xxxx-wwww.form
     * 
     * 
     * 请求格式: 1.对于post方法: { "publAttrs":{}, "busiAttrs":{} }
     * //content_type:application/json {}
     * 
     * 2.对于get方法: 公共参数及业务参数直接串接在url地址上 对于后端使用restful格式的get，在进行服务注册时使用方法名
     * user/1000 对应的注册格式 user/{userId} 然后前端请请使用的参数名为userId, 前端请求格式 GET
     * /gateway.do 而对于使用非restful格式的get方法直接将参数串在url地址上就可以了
     */

    /*
     * public static String pub_attrs = "publAttrs"; public static String
     * busi_attrs = "busiAttrs"; public static String content_type =
     * "application/json";
     * 
     * public static String app_id = "appId"; public static String app_token =
     * "appToken"; public static String api_id = "apiId"; public static String
     * version = "version"; public static String time_stamp = "timeStamp";
     * public static String sign_method = "signMethod"; public static String
     * sign = "sign"; public static String device_token = "deviceToken"; public
     * static String user_token = "userToken"; public static String format =
     * "format";
     */

    public static String getRouteBeanRedisKey(String key) {
        StringBuilder sb = new StringBuilder();
        sb.append(ROUTE_BEAN_KEY).append(CTRL_STR).append(key);
        return sb.toString();
    }

    public static enum REQUEST_METHOD {
        POST, GET
    }
}
