package com.wusi.reimbursement.utils;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 钉钉群发消息
 */


public class DingDingTalkUtils {
    public  static void sendDingDingMsg(String content) throws Exception {
        Long timestamp = System.currentTimeMillis();
        String secret = "SECe11d37757dce4204642016a3fccf622605dd504b66365628dcd207aef7acea8d";

        String stringToSign = timestamp + "\n" + secret;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
        byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
        String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");
        String url = "https://oapi.dingtalk.com/robot/send?access_token=a6bb03fdcd4aaad711c4bf40e68e6eb71a23ac23f19f77e5c63082cb839b471f" + "&" + "timestamp=" + timestamp + "&" + "sign=" + sign;

        DingTalkClient client = new DefaultDingTalkClient(url);
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        OapiRobotSendRequest.At at=new OapiRobotSendRequest.At();
        //at.setIsAtAll(true);
        List<String> mob=new ArrayList<>();
        //mob.add("15387179733");
        mob.add("18602702325");
        //mob.add("18627228022");
        //mob.add("15549056894");
        at.setAtMobiles(mob);
        request.setMsgtype("text");
        request.setAt(at);
        OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
        text.setContent(content);
        request.setText(text);
        OapiRobotSendResponse response = client.execute(request);
        at.setIsAtAll(true);
        request.setAt(at);
    }
}
