package com.wusi.reimbursement;

import com.alibaba.fastjson.JSONObject;
import com.wusi.reimbursement.client.EstablishRedPacketModel;
import com.wusi.reimbursement.client.EstablishRedPacketRequest;
import com.wusi.reimbursement.client.EstablishRedPacketResponse;
import com.wusi.reimbursement.client.ZcRequestClient;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @program: riskgo
 * @description:
 * @author: Mr.Liu
 * @create: 2020-09-07 10:58
 **/
@RunWith(SpringRunner.class)
@SpringBootTest()
public class Test {

    static Boolean testCheckSign = true;
    static String testCharset = "UTF-8";
    static String testSignType = "RSA2";
    static String testAppId = "TEST202005041652231204911";
    static String testPrivateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCwkNm9N8dG245pBa9zdmSWmAv8uljUDOTr5PywMDFPIuhMjOBAgBCuUdWDTzR3m35hQ/dAGQCC/JP+eyexe+ZpSfn0JdotvOAmNAkwcMX/MuQjtJ3pjuzTZiwW2NYUJvWcoxBNOBUYBR1jVTB3XQikrw33ij5NUQL2f67UfnmdQ8JrJ2CEIxTt5nx3DY+pIa/1FueGDm802/Lu1f9Wj2e4wIBjHPWwPYgmv03zTFmxms3oIJRUq6IJZpHW17VIBJomyw8ohJOZBrMy0SX2k5of16dp346hpYNTprJyzLkgvHO8jodzmLFgGyTDtkCs8LeksDTmmjfnCYaybFHZ2t03AgMBAAECggEBAJtcOVs3c08z7ZEXgZQ1PrkLvLB6P6MGXX/7kyRso1agvoptAv1+Mi9QrnDGBsKfvYpURYDO/xZCrO4k635OKSXIA/oCPII2SX0UGGnZMT8mRnvsd26FZnl006Ke1XAR/9f922A20si552v6D5VX9T0DE2UW7U8W02aWXv129AedlcGQqCtIkcjHkvzosC08fTMT7LvTuUjHj7bHOD9UoPKBjWJidsRwwAov/b27WFOxqkIVxDWD7QhSSmqPJPQ4VP5vXi+N9YNS5cS1qH8AmUxvddWmvMSmdDSWxHQmMgvsduSZ0s3P67l2mtra3wI5mZkbX6saokI0/QaweX87c6ECgYEA/ZQEYHtwC9rTM5DcS610JFjIxtWH1QNTLKRwMp2rmNG4busZFXXxlsdeyPfyglt1uXdCMWZA9T9/oCJJTpv55UFBKsMbgDGd22SBo3KhfRW1J81FFrKZJo50Pw7Q8hpKorxTUWf858ztn/9rMcvSBJZoleoVjLQjPOtb9SsGwNkCgYEAskCLAa+JOIi9KYmUevpsTXhplN5fmiENN1d/nUxutug6WVDOsjbWCk8H+zG8tJSiHndQhWJIiKtBfGwtzYRbbTBQRoK+veVupRMdQoPDaPmCq+ldwQRnsaVsudta3hv7akT524dJLEqMZ28TlaK5+uYyakxIhK3hykNOVauHxI8CgYBDuyKEJtRhxjw9fMbqy9TG1JQkT+qtIes4dF+nlWe9NN8/eTpE+jDiZjRSF7BF02oZdNpQWZCmMSTEwAO2pIDWFFz0sBKLZjVU4X6jCr5Jq9+sVu7KRkAUBV7VbP6wfAdNemICz3TE8X7TCqU2MsvQ/9/FkzXYVFFJN1Bjpu9x2QKBgQCbNZoUYK1iPaZAidxw7KbrGgMNLkgeY9MnMhgbdlcdCr7r0HH6OcdL+J7heeBveNBlKM1DJ14zKrN9zJBhWHNnct8jVmsR3LnoIOmkZij7ue6vFCefjt9fjsRKXRcVOVZEpUTOg1ESsOLqmYx7CdNZBaI2bq/iX6mwPcTy0cYJPwKBgDR7lDxsyLnRC7MJvJGMDT8obOa0HJ7Ux5e7swId7+AZVc8XjRIFiD0uUVSbzE+5VQWTCUWC/kad4yuTmC3M+0ZOWjLGdj+a2p00TCcJYUBYdNsE6u5/CxW+ANMEmi3FitZEpyd8dK/SCQjYXE/KqmV9e0erUPfVCdT2VyUDTuOY";
    static String testPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmKb38IjsfoxMpUBQKd378cKNf1oB6djX8C1Zui1+RncUCKTuwkbztKLQmb+I72ntpJraF7n+lAOCLGGtqpVIDjJK7TR3bGKnWcFwKzkEiIb5jcvlytYsF9u3NnNmlQ+ejHLMAS6jXiV26qA1wqKwtSzlA+DBgCVpEsOXZJ7UlqUq5Y8xePAFkPLzgWIt+MIx/dO7dQThwFc2dg3M7qeoTOVMlXtVskXOMaNxvPPwtpehSggnkkaF9QMChzb4+6ASly8gBLel70w/ZJabm91EANV9T92PSjTKaDHHIxdrgPdcC4VTxF7tfvzPkGyQ3TcWE4I092IZagcM/gXmWQI27wIDAQAB";

    static String url = "http://127.0.0.1:8884/gateway.do";

    static ZcRequestClient client = new ZcRequestClient(testAppId, testCharset, testSignType, testPrivateKey, testPublicKey, url, testCheckSign);

    @org.junit.Test
    public static void main(String[] args) throws Exception {
        register();
    }
    static void register() throws Exception {
        EstablishRedPacketRequest request = new EstablishRedPacketRequest();
//        RiskPushQueryRequest request = new RiskPushQueryRequest();
//        ComplaintInquiryRequest request = new ComplaintInquiryRequest();
//        RiskStatementRequest request = new RiskStatementRequest();
        request.setModel(registerModel());
//        request.setModel(registerModel());
        EstablishRedPacketResponse response = client.execute(request);
//        RiskStatementResponse response = client.execute(request);
//        ComplaintInquiryResponse response = client.execute(request);
        System.err.println(JSONObject.toJSONString(response) + "========");
    }

    static EstablishRedPacketModel registerModel() {
        EstablishRedPacketModel model=new EstablishRedPacketModel();
        model.setAmount("100");
        //model.setTradeNo("123456");
        model.setOutTradeNo("123");
        model.setTitle("测试红包");
        //model.setState(1);
        model.setType(2);
        model.setMerchantNo("66666");
        model.setCashierPhoneNo("18602702325");
        model.setSellerNo("15527875423");
        model.setCustomPhone("1860254578");
       // model.setOrderTime(new Date());
        model.setDelayDays(1);
        model.setIsNextMonthSettle(0);
       // model.setCreateTime(new Date());
        return model;
    }

   /* @Test
    public void testRequestNo() {
        Map<String, String> map = new HashMap();
        map.put("smid", "123123");
        map.put("pid", "123");
        map.put("riskNo", "158519805534877168154");
        map.put("risktype", "risk_mobile_no");
        map.put("risklevel", "123456");
        String s = JSON.toJSONString(map);
        SendMessage.sendMessage(JmsMessaging.ORDER_CESHI, s);
    }*/


}
