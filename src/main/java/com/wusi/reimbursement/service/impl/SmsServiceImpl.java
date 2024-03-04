package com.wusi.reimbursement.service.impl;

import com.wusi.reimbursement.service.SmsService;

/**
 * @ Description   :  java类作用描述
 * @ Author        :  wusi
 * @ CreateDate    :  2020/11/5$ 15:21$
 */
public class SmsServiceImpl implements SmsService {
    public String send(String message) {
        System.out.println("send message:" + message);
        return message;
    }
}