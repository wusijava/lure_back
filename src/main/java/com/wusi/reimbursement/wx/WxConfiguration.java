package com.wusi.reimbursement.wx;


import lombok.Data;


@Data
public class WxConfiguration {

    private WxConfiguration() {

    }

    public static String getAppId() {
        return "wxc18b3bfa3d91ff3a";
    }

    public static String getAppSecret() {
        return "6285d86916514748d2ba47f8f6cd8eee";
    }
}
