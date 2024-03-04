package com.wusi.reimbursement.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: PDF工具
 * @author: jack ma
 * @create: 2021-03-04 11:09
 **/
public class PdfUtil {


    public static void createPdf(String templatePath, String newPDFPath, Map<String, String> map) {
        PdfReader reader;
        FileOutputStream out;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            //输出流
            out = new FileOutputStream(newPDFPath);
            //读取pdf模板
            reader = new PdfReader(templatePath);
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            java.util.Iterator<String> it = form.getFields().keySet().iterator();
            while (it.hasNext()) {
                String name = it.next().toString();
                /*if (name.toLowerCase().contains("images")) {
                    continue;
                }*/
                //填写内容部分
                form.setField(name, map.getOrDefault(name, null));
            }
            // 填充图片
          /*  int pageNo = form.getFieldPositions("images").get(0).page;
            Rectangle signRect = form.getFieldPositions("images").get(0).position;
            float x = signRect.getLeft();
            float y = signRect.getBottom();
            Image image = Image.getInstance(map.get("images"));
            *//*获取操作的页面*//*
            PdfContentByte under = stamper.getOverContent(pageNo);
            *//*根据域的大小缩放图片*//*
            image.scaleToFit(signRect.getWidth(), signRect.getHeight());
            *//*添加图片*//*
            image.setAbsolutePosition(x, y);
            under.addImage(image);*/
            //如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.setFormFlattening(true);
            stamper.close();
            Document doc = new Document();
            PdfCopy copy = new PdfCopy(doc, out);
            doc.open();
            PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), 1);
            copy.addPage(importPage);
            doc.close();

        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }


    }


    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("total", "14040.9");
        map.put("max", "6150");
        map.put("min", "0.99");
        //5427.26
        map.put("wusi", "5427.26");
        map.put("zmx", "2410.04");
        map.put("wyc", "6180.7");
        map.put("xl", "22.9");
        map.put("xl", "22.9");
        map.put("df", "100");
        map.put("sf", "0");
        map.put("month", "2021-02");
        map.put("remark", "以后每月统计上个月开销");
        map.put("date", "2021-03-17");
        String templatePath = "C:\\\\excel\\\\home.pdf";
        //生成的新文件路径
        String newPDFPath = "D:\\\\excel\\\\home.pdf";

        createPdf(templatePath, newPDFPath, map);
    }
}
