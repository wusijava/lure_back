package com.wusi.reimbursement.exception;



import com.wusi.reimbursement.client.EnumInterface;

import java.text.MessageFormat;

/**
 * 系统异常
 *
 * @author wusi
 * @date 2019-12-5 12:41:56
 **/
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = -7255435874971074687L;

    private String msg;
    private String code;

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String code, String msgFormat, Object... args) {
        super(MessageFormat.format(msgFormat, args));
        this.code = code;
        this.msg = MessageFormat.format(msgFormat, args);
    }

    public BaseException(EnumInterface enumInterface, Object... args) {
        super(MessageFormat.format(enumInterface.getMsg(), args));
        this.code = enumInterface.getCode();
        this.msg = MessageFormat.format(enumInterface.getMsg(), args);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
