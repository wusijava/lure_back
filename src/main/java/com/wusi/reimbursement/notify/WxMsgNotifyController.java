package com.wusi.reimbursement.notify;

import com.alibaba.fastjson.JSONObject;
import com.wusi.reimbursement.entity.LureFishGet;
import com.wusi.reimbursement.service.LureFishGetService;
import com.wusi.reimbursement.utils.DataUtil;
import com.wusi.reimbursement.utils.WXBizMsgCrypt;
import com.wusi.reimbursement.wx.dto.ImgResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import static java.lang.System.out;

@RestController
@RequestMapping(value = "notify")
@Slf4j
public class WxMsgNotifyController {
    @Autowired
    private LureFishGetService lureFishGetService;

    @RequestMapping(value = "wxMsg")
    @ResponseBody
    private void wxMsgNotify(HttpServletRequest request,
                             HttpServletResponse response){
        try {
            // 微信服务器POST消息时用的是UTF-8编码，在接收时也要用同样的编码，否则中文会乱码；
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            boolean isGet = request.getMethod().toLowerCase().equals("get");
            log.error("消息与时间接口收到请求，请求方式：" + (isGet ? "GET" : "POST"));
            if (isGet) {
                // 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
                String signature = request.getParameter("signature");
                // 时间戳
                String timestamp = request.getParameter("timestamp");
                // 随机数
                String nonce = request.getParameter("nonce");
                // 随机字符串
                String echostr = request.getParameter("echostr");
                PrintWriter out = null;
                out = response.getWriter();
                if (CheckUtils.checkSignature(signature, timestamp, nonce)) {
                    out.print(echostr);
                    out.flush();
                }
            }else {
                //先回复空字符串，然后执行自己的业务
                this.output(response, "success");
                String token = "lureking";//小程序上面配置的
                String appId = "wxc18b3bfa3d91ff3a";//这个也是要自己的小程序对应的
                String encodingAesKey = "kLYkSDFCcBuHE0hcqu51RBqmyKi3JXE8IxID8I7EQQr";//小程序上面配置的
                // 时间戳
                String timestamp = request.getParameter("timestamp");
                String msgSignature = request.getParameter("msg_signature");
                // 随机数
                String nonce = request.getParameter("nonce");
                //获取密文内容
                //com.alibaba.fastjson.JSONObject jsonObject= StringUtil.readStreamToJson(request.getReader());
                String str = "";
                BufferedReader bd=request.getReader();
                String inputLine;
                while ((inputLine = bd.readLine()) != null){
                    str+=inputLine;
                }
                log.error("读取数据流后字符串"+str);
                com.alibaba.fastjson.JSONObject jsonObject=JSONObject.parseObject(str);
                String encrypt = jsonObject.getString("Encrypt");
                //解密
                WXBizMsgCrypt pc = new WXBizMsgCrypt(token, encodingAesKey, appId);
                String jmResult = pc.decrypt(encrypt);
                log.error("微信回调内容"+jmResult);
                ImgResult imgResult = JSONObject.parseObject(jmResult, ImgResult.class);
                LureFishGet fishGet = lureFishGetService.selectByTraceId(imgResult.getTrace_id());
                if(DataUtil.isNotEmpty(fishGet)&&fishGet.getState().equals(0)){
                    if(!imgResult.getResult().getSuggest().equals("pass")){
                        fishGet.setState(-1);
                        lureFishGetService.updateById(fishGet);
                    }
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.close();
        }

    }

    private void output(HttpServletResponse response, String returnValue) {
        try {
            ServletOutputStream output = response.getOutputStream();
            output.write(returnValue.getBytes());
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
    class CheckUtils {
        private static String token = "lureking";
        /**
         * 验证签名
         * @param signature
         * @param timestamp
         * @param nonce
         * @return
         */
        public static boolean checkSignature(String signature, String timestamp,String nonce) {
            // 将token、timestamp、nonce三个参数进行字典排序
            String[] arr = new String[]{token, timestamp, nonce};
            Arrays.sort(arr);
            // 将三个参数字符串拼接成一个字符串
            StringBuilder content = new StringBuilder();
            for (int i = 0; i < arr.length; i++) {
                content.append(arr[i]);
            }
            try {
                //获取加密工具
                MessageDigest md = MessageDigest.getInstance("SHA-1");
                // 对拼接好的字符串进行sha1加密
                byte[] digest = md.digest(content.toString().getBytes());
                String tmpStr = byteToStr(digest);
                //获得加密后的字符串与signature对比
                return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return false;
        }

        private static String byteToStr(byte[] byteArray) {
            String strDigest = "";
            for (int i = 0; i < byteArray.length; i++) {
                strDigest += byteToHexStr(byteArray[i]);
            }
            return strDigest;
        }
        private static String byteToHexStr(byte mByte) {
            char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
                    'B', 'C', 'D', 'E', 'F'};
            char[] tempArr = new char[2];
            tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
            tempArr[1] = Digit[mByte & 0X0F];
            String s = new String(tempArr);
            return s;
        }
}
