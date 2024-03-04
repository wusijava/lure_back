package com.wusi.reimbursement;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.taobao.api.ApiException;
import com.wusi.reimbursement.utils.AesUtil;
import com.wusi.reimbursement.utils.WeekUtils;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@RunWith(SpringRunner.class)
@SpringBootTest
public class DingdingTest {
    @Test
    public void test() throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, ApiException {
        Long timestamp = System.currentTimeMillis();
        String secret = "SECe11d37757dce4204642016a3fccf622605dd504b66365628dcd207aef7acea8d";

        String stringToSign = timestamp + "\n" + secret;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
        byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
        String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");
        System.out.println(sign);
        String url = "https://oapi.dingtalk.com/robot/send?access_token=a6bb03fdcd4aaad711c4bf40e68e6eb71a23ac23f19f77e5c63082cb839b471f" + "&" + "timestamp=" + timestamp + "&" + "sign=" + sign;
        System.out.println(url);

        DingTalkClient client = new DefaultDingTalkClient(url);
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("text");
        OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
        text.setContent("睡什么觉啊,年轻人,起来high~");
        request.setText(text);

        OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
        at.setAtMobiles(Arrays.asList("132xxxxxxxx"));
        OapiRobotSendResponse response = client.execute(request);
        // isAtAll类型如果不为Boolean，请升级至最新SDK
        at.setIsAtAll(true);
        request.setAt(at);

//        request.setMsgtype("link");
//        OapiRobotSendRequest.Link link = new OapiRobotSendRequest.Link();
//        link.setMessageUrl("https://www.dingtalk.com/");
//        link.setPicUrl("");
//        link.setTitle("时代的火车向前开");
//        link.setText("这个即将发布的新版本，创始人xx称它为红树林。而在此之前，每当面临重大升级，产品经理们都会取一个应景的代号，这一次，为什么是红树林");
//        request.setLink(link);
//        request.setMsgtype("feedCard");
//        OapiRobotSendRequest.Feedcard feedcard = new OapiRobotSendRequest.Feedcard();
//        List<OapiRobotSendRequest.Links> linkList = new ArrayList<>();
//        OapiRobotSendRequest.Links links = new OapiRobotSendRequest.Links();
//        links.setMessageURL("https://www.dingtalk.com/");
//        links.setPicURL("https://www.allinpay.com
    }
    @Test
    public void getNum() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date parse = sdf.parse("2021-02-21 20:01:01");
        String ssqNum = WeekUtils.getSsqNum(parse);
        System.out.println(ssqNum);
    }
    @Test
    public void lamada(){
        ArrayList<User> list = new ArrayList<>();
        list.add(new User(1,"张无忌",25,1));
        list.add(new User(2,"周芷若",24,2));
        list.add(new User(3,"赵敏",23,2));
        Map<Integer, List<User>> genderGroup = list.stream().collect(Collectors.groupingBy(User::getSex, Collectors.toList()));
        System.out.println(genderGroup);
        int min = list.stream().mapToInt(User::getAge).min().orElse(-1);
        System.out.println(min);
    }
    @Test
    public void getMa(){
        String encrypt = AesUtil.Encrypt("OBXZ9O798A7", "xiangshikeji2017");
        System.out.println(encrypt);
    }
}
