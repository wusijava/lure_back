package com.wusi.reimbursement.controller;

;
import com.wusi.reimbursement.service.IMailService;
import com.wusi.reimbursement.utils.BackupDbUtil;
import com.wusi.reimbursement.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.Date;

/**
 * 线上数据自动备份程序
 */

@Controller
@RequestMapping(value = "api/open/")
public class BackUpController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private IMailService mailService;
    @Scheduled(cron = "0 40 15 * * *")
    @RequestMapping(value = "backUpDataBase")
    @ResponseBody
    public void cron() {
        try {
            //生成备份文件
            String filePathTaobao = BackupDbUtil.backup("taobao");
            //发送邮件
            mailService.sendAttachmentsMail("513936307@qq.com","腾讯云数据库备份"+ DateUtil.formatDate(new Date(), "yyyy-MM-dd"),"今日备份已发到附件,请及时保存备份!",filePathTaobao);
            //删除7天之前的文件
            String path="/home/back/dababase/";
            File file=new File(path);
            File[] files=file.listFiles();
            for (int i=0;i<files.length;i++){
                if(files[i].isFile()){
                    long fileTime = files[i].lastModified();
                    long now=System.currentTimeMillis();
                    //获取天数
                    long time=(now-fileTime)/(1000*60*60*24);
                    logger.error(files[i].getName()+"文件时间,{}",time);
                    if(time>7){
                        logger.error("删除过期7天的文件,文件名:{}", files[i].getName());
                        files[i].delete();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
