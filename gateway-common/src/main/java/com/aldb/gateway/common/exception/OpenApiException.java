package com.aldb.gateway.common.exception;


public class OpenApiException extends RuntimeException {
    /**
     * 
     */
    private static final long serialVersionUID = 8710396445793589764L;

    private String errorCode = null;

    private String errorMsg = null;

    public String getErrorCode() {
        return errorCode;
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

    public OpenApiException() {

    }


    public OpenApiException(OpenApiServiceErrorEnum error) {
        super(error.getErrMsg());
        this.errorCode = error.getErrCode();
        this.errorMsg = error.getErrMsg();
    }

    public OpenApiException(OpenApiServiceErrorEnum error, Throwable cause) {
        super(error.getErrMsg(), cause);
        this.errorCode = error.getErrCode();
        this.errorMsg = error.getErrMsg();
    }

    public OpenApiException(String message) {
        super(message);
    }

    public OpenApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public OpenApiException(String code, String message) {
        super(message);
        this.errorCode = code;
        this.errorMsg = message;
    }

    public OpenApiException(Throwable cause) {
        super(cause);
    }


}
