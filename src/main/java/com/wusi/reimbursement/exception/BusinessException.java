package com.wusi.reimbursement.exception;


import com.wusi.reimbursement.client.EnumInterface;

/**
 * @author wusi
 * @description 业务异常
 * @date 2019-11-29 11:36:11
 */
public class BusinessException extends BaseException {

    private static final long serialVersionUID = -141708246396363251L;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String code, String message) {
        super(code, message);
    }

    public BusinessException(EnumInterface enums, Object... args) {
        super(enums.getCode(), enums.getMsg(), args);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
