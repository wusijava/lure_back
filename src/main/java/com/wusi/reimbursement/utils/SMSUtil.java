package com.wusi.reimbursement.utils;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import org.json.JSONException;

import java.io.IOException;

public class SMSUtil {
    public static String sendSMS(String phoneNumber,String code,int templateId) {
        String reStr = ""; //定义返回值
        // 短信应用SDK AppID  1400开头
        int appid = 1400461503 ;
        // 短信应用SDK AppKey
        String appkey = "0c72576fc914f0e74d82b201be737117";
        // 短信模板ID，需要在短信应用中申请
        //templateId = 807342 ;
        // 签名，使用的是签名内容，而不是签名ID
        String smsSign = "wearelie";
        //随机生成四位验证码的工具类
        //String code = "吴思店铺的凯达8101";
        try {
            //参数，一定要对应短信模板中的参数顺序和个数，
            String[] params = {code};
            //创建ssender对象
            SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
            //发送
            SmsSingleSenderResult result = ssender.sendWithParam("86", phoneNumber,templateId, params, smsSign, "", "");
            System.out.println(result);
            if(result.result!=0){
                reStr = "error";
            }
            // 签名参数未提供或者为空时，会使用默认签名发送短信
           /* HttpSession session = request.getSession();
            //JSONObject存入数据
            JSONObject json = new JSONObject();
            json.put("Code", code);//存入验证码
            json.put("createTime", System.currentTimeMillis());//存入发送短信验证码的时间
            // 将验证码和短信发送时间码存入SESSION
            request.getSession().setAttribute("MsCode", json);
            reStr = "success";*/
        } catch (HTTPException e) {
            // HTTP响应码错误
            e.printStackTrace();
        } catch (JSONException e) {
            // json解析错误
            e.printStackTrace();
        } catch (IOException e) {
            // 网络IO错误
            e.printStackTrace();
        }catch (Exception e) {
            // 网络IO错误
            e.printStackTrace();
        }
        return "123";
    }

    public static void main(String[] args) {

        //SMSUtil.sendSMS(null, "15527875423");
    }

}