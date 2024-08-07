package com.wusi.reimbursement.wx.impl;

import com.alibaba.fastjson.JSON;
import com.wusi.reimbursement.wx.WxApi;
import com.wusi.reimbursement.wx.WxConfigContainer;
import com.wusi.reimbursement.wx.WxConfiguration;
import com.wusi.reimbursement.wx.WxUtils;
import com.wusi.reimbursement.wx.dto.OpenIdApi;
import com.wusi.reimbursement.wx.dto.PhoneNumberApi;
import com.wusi.reimbursement.wx.dto.MsgApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class WxApiImpl implements WxApi {

    private static final Logger log = LoggerFactory.getLogger(WxApiImpl.class);

    private final WxConfigContainer container;


    public WxApiImpl(WxConfigContainer container) {
        this.container = container;
    }

    @Override
    public String getPhoneNumber(String code, String appId) {
        String accessToken = container.getCacheAccessToken(WxConfiguration.getAppId(), WxConfiguration.getAppSecret());
        if (accessToken == null) {
            log.error("微信登陆获取ACCESS_TOKEN失败");
            throw new RuntimeException("获取TOKEN失败");
        }
        PhoneNumberApi numberApi = WxUtils.getPhoneNumber(accessToken,code);
        if (numberApi != null && !numberApi.isSuccess() && numberApi.getErrmsg().contains("access_token is invalid or not latest")){
            container.removeCacheAccessToken(WxConfiguration.getAppId());
            throw new RuntimeException("系统繁忙，请稍后重试");
        }
        if (numberApi == null || numberApi.getPhone_info() == null || numberApi.getPhone_info().getPhoneNumber() == null) {
            log.error("获取手机号码失败:{}", numberApi != null ? JSON.toJSONString(numberApi) : "");
            throw new RuntimeException("获取手机号码失败");
        }
        return numberApi.getPhone_info().getPhoneNumber();
    }

    @Override
    public MsgApi checkMsg(String content, Integer scene, String wxCode) {
        String accessToken = container.getCacheAccessToken(WxConfiguration.getAppId(), WxConfiguration.getAppSecret());
        OpenIdApi openId = WxUtils.getOpenId(wxCode, WxConfiguration.getAppId(), WxConfiguration.getAppSecret());
        return  WxUtils.checkMsg(accessToken, content, scene, openId.getOpenid());
    }

    @Override
    public MsgApi checkImg(String media_url, Integer scene, String wxCode) {
        String accessToken = container.getCacheAccessToken(WxConfiguration.getAppId(), WxConfiguration.getAppSecret());
        OpenIdApi openId = WxUtils.getOpenId(wxCode, WxConfiguration.getAppId(), WxConfiguration.getAppSecret());
        return  WxUtils.checkImg(accessToken, media_url, scene, openId.getOpenid());
    }


}
