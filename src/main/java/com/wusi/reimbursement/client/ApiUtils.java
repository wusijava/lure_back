package com.wusi.reimbursement.client;

import java.util.UUID;

/**
 * @author wusi
 * @description 接口工具类
 * @date 2019-12-5 12:43:28
 */
public class ApiUtils {

    public static String getTimestamp() {
        return String.valueOf(System.currentTimeMillis());
    }

    public static String getRequestId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
