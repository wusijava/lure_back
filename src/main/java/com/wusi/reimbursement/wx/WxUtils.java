package com.wusi.reimbursement.wx;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wusi.reimbursement.wx.dto.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;


public class WxUtils {

    private WxUtils() {
    }
    private static final RestTemplate TEMPLATE = new RestTemplate();

    public static AccessToken getAccessToken(String appId, String appSecret){
        return TEMPLATE.getForObject(WxConstants.getWxAccessTokenUrl(appId,appSecret),AccessToken.class);
    }

    public static OpenIdApi getOpenId(String code, String appId, String appSecret){
        String res = TEMPLATE.getForObject(WxConstants.getWxOpenIdUrl(code,appId,appSecret),String.class);
        return  res != null && !"".equals(res) ? JSON.parseObject(res).toJavaObject(OpenIdApi.class) : null;
    }

    public static PhoneNumberApi getPhoneNumber(String accessToken, String code){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",code);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> formEntity = new HttpEntity<String>(jsonObject.toJSONString(), headers);
        return TEMPLATE.postForEntity(WxConstants.getWxPhoneNumberUrl(accessToken), formEntity,PhoneNumberApi.class).getBody();
    }

    public static OpenPidApi getOpenPid(String accessToken, String code){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",code);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> formEntity = new HttpEntity<String>(jsonObject.toJSONString(), headers);
        return TEMPLATE.postForEntity(WxConstants.getWxOpenPidUrl(accessToken), formEntity,OpenPidApi.class).getBody();
    }

    public static MsgApi checkMsg(String accessToken, String content, Integer scene, String openId){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("content",content);
        jsonObject.put("version",2);
        jsonObject.put("scene",scene);
        jsonObject.put("openid",openId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> formEntity = new HttpEntity<String>(jsonObject.toJSONString(), headers);
        return TEMPLATE.postForEntity(WxConstants.getWxMsgCheck(accessToken), formEntity, MsgApi.class).getBody();
    }

    public static MsgApi checkImg(String accessToken, String media_url, Integer scene, String openId){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("media_url",media_url);
        jsonObject.put("version",2);
        jsonObject.put("scene",scene);
        jsonObject.put("openid",openId);
        jsonObject.put("media_type",2);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> formEntity = new HttpEntity<String>(jsonObject.toJSONString(), headers);
        return TEMPLATE.postForEntity(WxConstants.getWxImgCheck(accessToken), formEntity, MsgApi.class).getBody();
    }
}
