package com.wusi.reimbursement.controller;

import com.wusi.reimbursement.entity.Spend;
import com.wusi.reimbursement.service.SpendService;
import com.wusi.reimbursement.utils.DataUtil;
import com.wusi.reimbursement.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * @ Description   :  七牛云图片备份腾讯云
 * @ Author        :  wusi
 * @ CreateDate    :  2021/1/26$ 13:23$
 */
@RestController
@Slf4j
public class ImgBackController {
    @Autowired
    private com.wusi.reimbursement.service.SpendService SpendService;
    @RequestMapping(value ="imgBack" )
    public void downLoad()  {
        List<Spend> spends = SpendService.queryList(new Spend());
        for (Spend str:spends){
            try {
                if(DataUtil.isNotEmpty(str.getUrl())){
                    URL target = new URL(str.getUrl());
                    URLConnection urlConnection = target.openConnection();
                    //获取输入流
                    InputStream inputStream = urlConnection.getInputStream();
                    //获取输出流 这里是下载保存图片到本地的路径
                    //String name= DateUtil.formatDate(str.getDate(), "yyyy-MM-dd")+str.getConsumer()+str.getItem();
                    OutputStream outputStream = new FileOutputStream("/home/reim/img/" +str.getId() +".jpg");
                    int temp = 0;
                    while ((temp = inputStream.read()) != -1) {
                        outputStream.write(temp);
                    }
                }
            } catch (IOException e) {
                log.error("下载异常,{}", str.getUrl());
                continue;
            }

        }
    }
}
