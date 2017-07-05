package com.aldb.gateway.common.exception;

public enum OpenApiServiceErrorEnum {
    // TODO:common error errorcode from zz00000 to zz09999
    SYSTEM_SUCCESS("zz00000", "success"), SYSTEM_BUSY("zz00001", "server is busy"), SYSTEM_QUEUE_DEEPTH("zz00002",
            "the queue reached max deepth"), VALIDATE_PARAM_ERROR("zz00100", "输入参数有误！"), REMOTE_INVOKE_ERROR(
            "zz00101", "远程服务错误！"), PARA_NORULE_ERROR("zz00102", "请求参数格式不符合规则"), VALIDATE_ERROR("zz00103", "校验有误"), DATA_OPER_ERROR(
            "zz00104", "数据操作异常"), APPLICATION_ERROR("zz00200", "业务逻辑异常"), APPLICATION_OPER_ERROR("zz00201", "系统业务异常"), DATA_EMPTY_ERROR(
            "zz00300", "查询结果为空"),
    // TODO:gateway error errorcode from zz20000 to zz29999

    // TODO:front error errorcode from zz30000 to zz39999

    // TODO:console error errorcode from zz40000 to zz49999

    // TODO:batch error errorcode from zz50000 to zz59999

    ;
    // 成员变量
    private String errCode;
    private String errMsg;

    // 构造方法
    private OpenApiServiceErrorEnum(String errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    // 普通方法
    public static String getErrMsg(String errCode) {
        for (OpenApiServiceErrorEnum c : OpenApiServiceErrorEnum.values()) {
            if (c.getErrCode().equals(errCode)) {
                return c.getErrMsg();
            }
        }
        return null;
    }

    public static OpenApiServiceErrorEnum getErr(String errCode) {
        for (OpenApiServiceErrorEnum c : OpenApiServiceErrorEnum.values()) {
            if (c.getErrCode().equals(errCode)) {
                return c;
            }
        }
        return null;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
