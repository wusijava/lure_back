package com.wusi.reimbursement.wx;


public interface WxConfigContainer {
    String getCacheAccessToken(String appId, String appSecret);

    void removeCacheAccessToken(String appId);
}
