package com.wusi.reimbursement.exception;

import lombok.Data;


@Data
public class BizException extends RuntimeException {

    private String code;

    private String message;

    public BizException(String code,String message,Throwable throwable){
        super(message,throwable);
        this.code = code;
        this.message = message;
    }

    public BizException(String code,String message){
        super(message);
        this.code = code;
        this.message = message;
    }

    public BizException(String message){
        super(message);
        this.message = message;
    }

    public BizException(Throwable throwable){
        super(throwable);
    }

}
