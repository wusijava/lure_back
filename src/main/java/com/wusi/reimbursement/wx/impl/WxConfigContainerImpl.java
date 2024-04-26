package com.wusi.reimbursement.wx.impl;

import com.wusi.reimbursement.wx.WxCache;
import com.wusi.reimbursement.wx.WxConfigContainer;
import com.wusi.reimbursement.wx.WxUtils;
import com.wusi.reimbursement.wx.dto.AccessToken;
import org.springframework.stereotype.Component;

/**
 * @Description 描述
 * @Author duchong
 * @Date 2023/1/3 14:00
 * @Version 1.0.0
 **/
@Component
public class WxConfigContainerImpl implements WxConfigContainer {

    final WxCache wxCache;

    public WxConfigContainerImpl(WxCache wxCache) {
        this.wxCache = wxCache;
    }

    @Override
    public String getCacheAccessToken(String appId, String appSecret) {
        String accessToken = null;
        if (accessToken == null) {
            accessToken = getAccessToken(appId, appSecret);
            //wxCache.saveAccessToken(appId,accessToken);
        }
        return accessToken;
    }

    @Override
    public void removeCacheAccessToken(String appId) {
        wxCache.removeAccessToken(appId);
    }
    private String getAccessToken(String appId,String appSecret){
        AccessToken accessToken = WxUtils.getAccessToken(appId, appSecret);
        return accessToken == null ? null : accessToken.getAccess_token();
    }

}
