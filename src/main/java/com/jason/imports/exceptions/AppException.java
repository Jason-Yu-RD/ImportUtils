package com.jason.imports.exceptions;


/**
 * Created by yuchangcun on 2016/7/29.
 */
public class AppException extends Exception{

    private String errCode;

    private String errMsg;


    public AppException(String errCode, String errMsg) {
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
