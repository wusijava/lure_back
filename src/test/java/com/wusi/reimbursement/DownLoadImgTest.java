package com.wusi.reimbursement;

import com.wusi.reimbursement.entity.ProductNew;
import com.wusi.reimbursement.entity.Spend;
import com.wusi.reimbursement.query.ProductNewQuery;
import com.wusi.reimbursement.service.ProductNewService;
import com.wusi.reimbursement.service.SpendService;
import com.wusi.reimbursement.utils.DataUtil;
import com.wusi.reimbursement.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * @ Description   :  图片下载
 * @ Author        :  wusi
 * @ CreateDate    :  2021/1/26$ 11:19$
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class DownLoadImgTest {
    @Autowired
    private SpendService SpendService;
    @Test
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
                    String name=DateUtil.formatDate(str.getDate(), "yyyy-MM-dd")+str.getConsumer()+str.getItem();
                    OutputStream outputStream = new FileOutputStream("D:\\pic\\" +name +".jpg");
                    int temp = 0;
                    while ((temp = inputStream.read()) != -1) {
                        outputStream.write(temp);
                    }
                    System.out.println(name + ".jpg下载完毕!!!");
                }
            } catch (IOException e) {
                log.error("下载异常,{}", str.getUrl());
                continue;
            }

        }
    }
}
