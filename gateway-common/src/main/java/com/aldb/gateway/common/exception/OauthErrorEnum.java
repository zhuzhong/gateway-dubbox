package com.aldb.gateway.common.exception;


public enum OauthErrorEnum {
	ERROR("zz10000", "service unavailable"),
	GRANTTYPE("zz10001", "grant_type is required"),
	APP_ID("zz10002", "appid is required"),
	SECRET("zz10003", "secret is required"),
	TIMSTAMP("zz10004", "timestamp is required"),
	SIGN("zz10005", "sign is required"),
	INVALID_SIGN("zz10006", "invalid sign"),
	INVALID_REQUEST("zz10007", "invalid request"),
	INVALID_CLIENT("zz10008", "invalid appId or apptoken"),
	INVALID_GRANT("zz10009", "invalid grant"),
	UNAUTHORIZED_CLIENT("zz10010", "unauthorized appId"),
	UNSUPPORTED_GRANT_TYPE("zz10011", "unsupported grant_type"),
	INVALID_TOKEN("zz10012", "invalid token"),
	ACCESS_DENIED("zz10013", "access denied"),
	API_ID("zz10014", "apiId is required"),
	ACCESSTOKEN("zz10015", "appToken is required"),
	INVALID_SERVICENAME("zz10016", "invalid service_name"),
	CONTENTTYPE("zz10017", "httprequest header content-type is required"),
	INVALID_CONTENTTYPE("zz10018", "invalid content-type,just application/xml or application/json"),
	INVALID_SECRET("zz10019", "invalid secret"),
	UN_VISIBLE_SERVICENAME("zz10021", "service is not visible"),
	LOCK_ITEM_APPID("zz10022", "current appid is locked"),
	LOCK_ITEM_API("zz10023", "current service is locked"),
	APP_UNDEFIND_WHITE("zz10024","undefind in whiteList"),
	SERVICE_UNDEFIND_WHITE("zz10025","service_name undefind in whiteList"),
	NOT_CALLBACKURL("zz10026","undefind in user's callBackUrl"),
	INTERFACE_FREQUENCY("zz10027", "api freq out of limit"),
	APP_TOKEN("zz10028", "apptoken is required"),
	;
	// 鎴愬憳鍙橀噺
    private String errCode;
	private String errMsg;
    
    // 鏋勯�犳柟娉�
    private OauthErrorEnum(String errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }
/*    // 鏅�氭柟娉�
    public static String getErrMsg(String errCode) {
        for (OauthErrorEnum c : OauthErrorEnum.values()) {
            if (c.getErrCode().equals(errCode)) {
                return c.getErrMsg();
            }
        }
        return null;
    }
    
    public static OauthErrorEnum getErr(String errCode) {
        for (OauthErrorEnum c : OauthErrorEnum.values()) {
            if (c.getErrCode().equals(errCode)) {
                return c;
            }
        }
        return null;
    }*/
    
    public String getErrCode() {
		return errCode;
	}
	
	public String getErrMsg() {
		return errMsg;
	}
	/*public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public void setErrCode(String errCode) {
        this.errCode = errCode;
    }*/
	
	/*private static Map<String,String> oauthErrorMap;
	static{
		oauthErrorMap = new HashMap<String,String>();
		oauthErrorMap.put("error", "zz10000");
		oauthErrorMap.put("invalid_request", "zz10007");
		oauthErrorMap.put("invalid_client", "zz10008");
		oauthErrorMap.put("invalid_grant", "zz10009");
		oauthErrorMap.put("unauthorized_client", "zz10010");
		oauthErrorMap.put("401", "zz10010");
		oauthErrorMap.put("unsupported_grant_type", "zz10011");
		oauthErrorMap.put("invalid_token", "zz10012");
		oauthErrorMap.put("access_denied", "zz10013");
	}
	public static Map<String, String> getOauthErrorMap() {
		return oauthErrorMap;
	}
	public static void setOauthErrorMap(Map<String, String> oauthErrorMap) {
		OauthErrorEnum.oauthErrorMap = oauthErrorMap;
	}*/
}
