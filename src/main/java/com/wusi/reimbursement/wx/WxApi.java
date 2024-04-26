package com.wusi.reimbursement.wx;

/**
 * @Description 描述
 * @Author duchong
 * @Date 2023/1/3 14:00
 * @Version 1.0.0
 **/
public interface  WxApi {

    String getPhoneNumber(String code, String appId);
}
