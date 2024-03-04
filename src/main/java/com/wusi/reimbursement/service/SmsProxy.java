package com.wusi.reimbursement.service;

import java.sql.SQLOutput;

/**
 * @ Description   :  java类作用描述
 * @ Author        :  wusi
 * @ CreateDate    :  2020/11/5$ 15:22$
 */
public class SmsProxy implements SmsService {
    private final SmsService smsService;
    public SmsProxy(SmsService smsService){
        this.smsService=smsService;
    }
    @Override
    public String send(String message) {
        //调用方法之前,可以添加自己的操作
        System.out.println("before");
        smsService.send("java");
        System.out.println("after");
        return null;
    }
}
