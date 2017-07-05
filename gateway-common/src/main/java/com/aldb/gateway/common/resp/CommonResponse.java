package com.aldb.gateway.common.resp;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;

public class CommonResponse<T> implements Serializable {

    private static final long serialVersionUID = 6238235564764746187L;

    private T respObj;

    private boolean isResp;

    private String errorCode;

    private String errorMsg;

    private String CORRECT_PREFIX = "200.";

    public CommonResponse() {

    }

    public CommonResponse(boolean isResp) {
        this.isResp = isResp;
    }

    public CommonResponse(boolean isResp, T respObj) {
        this.isResp = isResp;
        this.respObj = respObj;
    }

    public CommonResponse(Boolean isResp, String errorCode, String errorMsg) {
        this.isResp = isResp;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public boolean isSuccess() {
        if (StringUtils.startsWithAny(errorCode, new String[] { CORRECT_PREFIX })) {
            return true;
        }
        return errorCode == null;
    }

    public T getRespObj() {
        return respObj;
    }

    public void setRespObj(T respObj) {
        this.respObj = respObj;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public boolean isResp() {
        return isResp;
    }

    public void setResp(boolean isResp) {
        this.isResp = isResp;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
