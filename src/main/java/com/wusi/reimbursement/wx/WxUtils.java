package com.wusi.reimbursement.wx;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wusi.reimbursement.wx.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
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
/*        JSONObject jsonObject = new JSONObject();
        jsonObject.put("content",content);
        jsonObject.put("version",2);
        jsonObject.put("scene",scene);
        jsonObject.put("openid",openId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        log.error("微信消息鉴别入参，{}", JSONObject.toJSONString(jsonObject));
        byte[] bytes=jsonObject.toJSONString().getBytes(StandardCharsets.UTF_8);
         HttpEntity<String> formEntity = new HttpEntity<String>(new String(bytes) ,headers);*/
        String url = "https://api.weixin.qq.com/wxa/msg_sec_check?access_token=" + accessToken;
        //创建客户端
        HttpClient httpclient = HttpClients.createDefault();
        //创建一个post请求
        HttpPost request = new HttpPost(url);
        //设置响应头
        request.setHeader("Content-Type", "application/json;charset=UTF-8");
        //通过fastJson设置json数据
        JSONObject postData = new JSONObject();
        //设置要检测的内容
        postData.put("content", content);
        postData.put("version", 2);
        postData.put("scene", scene);
        postData.put("openid", openId);
        log.error("文字鉴别如入参,{}", JSONObject.toJSONString(postData));
        String jsonString = postData.toString();
        request.setEntity(new StringEntity(jsonString,"utf-8"));
        try {
            HttpResponse response = httpclient.execute(request);
            // 从响应模型中获取响应实体
            org.apache.http.HttpEntity entity = response.getEntity();
            //得到响应结果
            String result = EntityUtils.toString(entity,"utf-8");
            return JSONObject.parseObject(result, MsgApi.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
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
