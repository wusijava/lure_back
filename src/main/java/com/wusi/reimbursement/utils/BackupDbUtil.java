package com.wusi.reimbursement.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
@Slf4j
public class BackupDbUtil {
     public static String backup(String database) throws Exception {
      System.out.println("------开始备份数据库定时任务------");
      String user = "root"; //数据库的用户名
      String password = "Wusi20150402";//数据库的密码
      //String database = "red_packet";//要备份的数据库名
      Date date = new Date();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
      String filepath = "/home/back/dababase/"+sdf.format(date)+database+".sql";
      File file = new File(filepath);
      if(!file.exists()){
       file.createNewFile();  
      }
         ShellUtils executor = new ShellUtils("43.142.106.205", user, password);
         executor.exec("docker exec -i  wusi-mysql mysqldump -uroot -pwusi20150402  taobao >"+filepath);
      return filepath;
     }
    }