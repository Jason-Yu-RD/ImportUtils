package com.jason.imports.exceptions;

/**
 * Created by yuchangcun on 2016/9/1.
 */
public class AppRuntimeException extends RuntimeException{

    private String errCode;

    private String errMsg;


    public AppRuntimeException(String errCode, String errMsg) {
        super(errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
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
