package com.wusi.reimbursement.wx;


import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;

public class WxConstants {

    private WxConstants(){}
    private static final String WX_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={0}&secret={1}";
    private static final String WX_PHONE_NUMBER_URL = "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token={0}";
    private static final String WX_OPEN_PID_URL = "https://api.weixin.qq.com/wxa/getpluginopenpid?access_token={0}";
    private static final String WX_OPEN_ID_URL = "https://api.weixin.qq.com/sns/jscode2session?appid={0}&secret={1}&js_code={2}&grant_type=authorization_code";
    private static final String WX_MSG_SECCHECK = "https://api.weixin.qq.com/wxa/msg_sec_check?access_token={0}";
    private static final String WX_IMG_SECCHECK = "https://api.weixin.qq.com/wxa/media_check_async?access_token={0}";
    public static String getWxAccessTokenUrl(String appId,String appSecret){
        return MessageFormat.format(WX_ACCESS_TOKEN_URL,appId,appSecret);
    }

    public static String getWxPhoneNumberUrl(String accessToken){
        return MessageFormat.format(WX_PHONE_NUMBER_URL,accessToken);
    }

    public static String getWxOpenPidUrl(String accessToken){
        return MessageFormat.format(WX_OPEN_PID_URL,accessToken);
    }

    public static String getWxOpenIdUrl(String code,String appId,String appSecret){
        return MessageFormat.format(WX_OPEN_ID_URL,appId,appSecret,code);
    }
    public static String getWxMsgCheck(String accessToken){
        return MessageFormat.format(WX_MSG_SECCHECK,accessToken);
    }
    public static String getWxImgCheck(String accessToken){
        return MessageFormat.format(WX_IMG_SECCHECK,accessToken);
    }

    private static final String TEST_APP_SECRET = "40788e79dd987f0967229c69c74957cb";
    private static final String TEST_APP_ID = "wxc2f4b2b4a15404de";


    public static void main(String[] args) {
        testGetAccessToken();
    }

    static final String TEST_ACCESS_TOKEN = "64_ngDVM_i8L8D33uIjjugDNSjOzTRrpNghmEGamZXD3Fwv8oezAZBQEWNuqtefEvFrMrTR9VO8A73wHOw16BtLAbhK0tJ98zs9dosfqaOs64Z7wGED5HDk9vt5ExwWDDdAEAPWW";

    static void testGetPhoneNumber(){
        JSONObject obj = new JSONObject();
        obj.put("code", "7fb81c649c3c5a83487c7f0259d8b0c67779fc246e54421ca5e0793e7e60d032");
        System.err.println(sendJsonPost(getWxPhoneNumberUrl(TEST_ACCESS_TOKEN),obj.toJSONString()));
    }

    static void testGetAccessToken(){
        String result = template.getForObject(getWxAccessTokenUrl(TEST_APP_ID,TEST_APP_SECRET),String.class);
        System.err.println(result);
    }

    private static RestTemplate template = new RestTemplate();

    public static String sendJsonPost(String url, String jsonStr) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> formEntity = new HttpEntity<String>(jsonStr, headers);
        return template.postForEntity(url, formEntity,String.class).getBody();
    }
}
