package com.wusi.reimbursement.listener;

import com.alibaba.fastjson.JSONObject;
import com.wusi.reimbursement.config.JmsMessaging;
import com.wusi.reimbursement.entity.Spend;
import com.wusi.reimbursement.utils.DataUtil;
import com.wusi.reimbursement.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * @ Description   :  图片备份
 * @ Author        :  wusi
 * @ CreateDate    :  2021/1/26$ 14:12$
 */
@Component
@Slf4j
public class ImgBackListener {
    @JmsListener(destination = JmsMessaging.IMG_BACK_MESSAGE)
    public void getMessage(String message) {
        Spend spend= JSONObject.parseObject(message, Spend.class);
        try {
            if (DataUtil.isNotEmpty(spend.getUrl())) {
                URL target = new URL(spend.getUrl());
                URLConnection urlConnection = target.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                OutputStream outputStream = new FileOutputStream("/home/reim/img/" + DateUtil.formatDate(spend.getDate(), "yyyy-MM-dd")+"-"+spend.getId() + ".jpg");
                int temp = 0;
                while ((temp = inputStream.read()) != -1) {
                    outputStream.write(temp);
                }
            }
        } catch (IOException e) {
            log.error("下载异常,{}", spend.getUrl());
        }
    }
}
