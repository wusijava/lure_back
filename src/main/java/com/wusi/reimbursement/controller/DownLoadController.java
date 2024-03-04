package com.wusi.reimbursement.controller;

import com.wusi.reimbursement.aop.SysLog;
import com.wusi.reimbursement.common.Response;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @ Description   :  附件下载controller
 * @ Author        :  wusi
 * @ CreateDate    :  2020/1/15$ 17:28$
 */
@RestController
public class DownLoadController  {
    @RequestMapping("/ok")
    public  Response<String> ok(){
        System.out.println("ok");
        return Response.ok("2020-01-21");
    }
    @RequestMapping("fileDownload")
    @SysLog("下载文件")
    public Response<String> fileDownLoad(HttpServletResponse response) throws IOException {
        System.out.println("进入文件下载controller");
        File file = new File("/home/file/bx.xls");
        String fileName = file.getName();
        //InputStream ins = new FileInputStream(file);
        InputStream fis=new BufferedInputStream(new FileInputStream(file));
        /* 设置文件ContentType类型，这样设置，会自动判断下载文件类型 */
       // response.setContentType("application/msword;charset=utf-8");
        response.setContentType("application/x-xls;charset=utf-8");
        /* 设置文件头：最后一个参数是设置下载文件名 */
        response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("utf-8"), "ISO8859-1"));
        response.setCharacterEncoding("utf-8");
        try{
            byte[] b = new byte[fis.available()];
            fis.read(b);
            OutputStream out = response.getOutputStream();
            out.write(b);
            out.flush();
            fis.close();
            out.close();
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
        return null;
    }
}
