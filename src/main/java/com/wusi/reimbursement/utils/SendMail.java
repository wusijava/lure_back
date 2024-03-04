package com.wusi.reimbursement.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * @ Description   :  发送邮件方法
 * @ Author        :  wusi
 * @ CreateDate    :  2019/11/15$ 9:58$
 */
public class SendMail {
    //发件人地址
    public static String senderAddress = "513936307@qq.com";
    //收件人地址
    public static String recipientAddress = "513936307@qq.com";
    //发件人账户名
    public static String senderAccount = "wusi";
    //发件人账户密码
    public static String senderPassword = "wusi20150402";

    public static void sendQQMail(String text) throws Exception {
        //1、连接邮件服务器的参数配置
        Properties props = new Properties();
        //设置用户的认证方式
       // props.setProperty("mail.smtp.auth", "true");
        //设置传输协议
        props.setProperty("mail.transport.protocol", "smtp");
        //设置发件人的SMTP服务器地址
        props.setProperty("mail.smtp.host", "smtp.qq.com");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.ssl.enable", "true");


        // 创建验证器
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                // 密码验证
                return new PasswordAuthentication("513936307", "gomlsnqhyiekbjba");

            }};


        //2、创建定义整个应用程序所需的环境信息的 Session 对象
        Session session = Session.getInstance(props,auth);
        //设置调试信息在控制台打印出来
        session.setDebug(true);
        //3、创建邮件的实例对象
        Message msg =getMimeMessage(session,text);

        //4、根据session对象获取邮件传输对象Transport
        Transport transport = session.getTransport();
        //设置发件人的账户名和密码
        transport.connect(senderAccount, senderPassword);
        //发送邮件，并发送到所有收件人地址，message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
       // transport.sendMessage(msg, msg.getAllRecipients());

        //如果只想发送给指定的人，可以如下写法
       transport.sendMessage(msg, new Address[]{new InternetAddress(recipientAddress)});

        //5、关闭邮件连接
        transport.close();


    }
    public static MimeMessage getMimeMessage(Session session,String text) throws Exception{
        //创建一封邮件的实例对象
        MimeMessage msg = new MimeMessage(session);
        //设置发件人地址
        msg.setFrom(new InternetAddress(senderAddress));
        /**
         * 设置收件人地址（可以增加多个收件人、抄送、密送），即下面这一行代码书写多行
         * MimeMessage.RecipientType.TO:发送
         * MimeMessage.RecipientType.CC：抄送
         * MimeMessage.RecipientType.BCC：密送
         */
        msg.setRecipient(MimeMessage.RecipientType.TO,new InternetAddress(recipientAddress));
        //设置邮件主题
        msg.setSubject("淘宝产品监控邮件","UTF-8");
        //设置邮件正文
        msg.setContent(text, "text/html;charset=UTF-8");
        //设置邮件的发送时间,默认立即发送
        msg.setSentDate(new Date());

        return msg;
    }

}


