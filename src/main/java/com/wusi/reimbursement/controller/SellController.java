package com.wusi.reimbursement.controller;

import com.wusi.reimbursement.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @ Description   :  按月生成销售报表模板
 * @ Author        :  wusi
 * @ CreateDate    :  2021/4/28$ 11:24$
 */
@Slf4j
@RestController(value = "open_controller")
@RequestMapping(value = "/api/open/")
public class SellController {
    @GetMapping(value = "getExcel")
    public void getExcel(String date, HttpServletRequest request, HttpServletResponse response) throws IOException {

        Date d = null;
        DateFormat sdf = new SimpleDateFormat(DateUtil.PATTERN_YYYY_MM_DD);
        try {
            d = sdf.parse(date+"-01");
        } catch (Exception e) {
            d = new Date();
            date = sdf.format(d);
        }
        File f = new File("/home/file/qipilang.xlsx");
        //File f = new File("D:\\excel\\qipilang.xlsx");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            Workbook wb = new XSSFWorkbook(fis);
            Sheet sheet = wb.getSheetAt(0);
            String str=date.substring(5, 7).startsWith("0")?date.substring(5, 7).replace("0", ""):date.substring(5, 7);
            wb.setSheetName(0,str+"月目标" );
            wb.setSheetName(1,str+"月冲刺目标");
            System.out.println(sheet.getSheetName());
            Calendar one = Calendar.getInstance();
            one.setTime(d);
            one.set(Calendar.DAY_OF_MONTH, one.getActualMinimum(Calendar.DAY_OF_MONTH));//获取第一天
            Date oneDate=one.getTime();
            System.out.println(oneDate);
            //最后一天
            Calendar last = Calendar.getInstance();
            last.setTime(d);
            last.set(Calendar.DAY_OF_MONTH, last.getActualMaximum(Calendar.DAY_OF_MONTH));//获取最后一天
            Date lastDate=last.getTime();
            System.out.println(lastDate);
            long days = DateUtil.betweenDays(oneDate, lastDate);
            //模板为31天 按实际天数删除多余行
            for(int m=(int)days;m<=32;m++){
                Row row = sheet.getRow(m+2);
                sheet.removeRow(row);
            }
            Row row;
            String[] weekDays = {"日", "一", "二", "三", "四", "五", "六"};
            List<Integer> list=new ArrayList<>();
            list.add(-1);
            for (int i =0;i<days+1;i++){
                int w = one.get(Calendar.DAY_OF_WEEK) - 1;
                if (w < 0) {
                    w = 0;
                }
                if(w==0){
                  list.add(i);
                }
                oneDate=one.getTime();
                row=sheet.getRow(i+1);
                if(row==null){
                    row=sheet.createRow(i+1);
                }
                Cell cellOne=row.createCell(0);
                Cell cellTwo=row.createCell(1);
                cellOne.setCellValue(sdf.format(oneDate));
                cellTwo.setCellValue(weekDays[w]);
                CellStyle style=wb.createCellStyle();
                style.setBorderBottom(new Short("1"));
                style.setBorderTop(new Short("1"));
                style.setBorderRight(new Short("1"));
                style.setBorderLeft(new Short("1"));
                cellOne.setCellStyle(style);
                cellTwo.setCellStyle(style);
                one.add(Calendar.DAY_OF_MONTH, 1);
            }
            System.out.println(list);
            Integer start;
            Integer end;
            for (int j=0;j<list.size()-1;j++){
                start=list.get(j);
                end=list.get(j+1);

                heBing(sheet ,start,end,4);
                heBing(sheet ,start,end,5);
                heBing(sheet ,start,end,6);
            }
            if(list.get(list.size()-1)!=days){
                CellRangeAddress bigOrderRegionOne = new CellRangeAddress( list.get(list.size()-1)+2, (int)days+1 , 4, 4);
                sheet.addMergedRegion(bigOrderRegionOne);
                CellRangeAddress bigOrderRegionTwo = new CellRangeAddress( list.get(list.size()-1)+2, (int)days+1 , 5, 5);
                sheet.addMergedRegion(bigOrderRegionTwo);
                CellRangeAddress bigOrderRegionThree = new CellRangeAddress( list.get(list.size()-1)+2, (int)days+1 , 6, 6);
                sheet.addMergedRegion(bigOrderRegionThree);
            }
            String file_name = "销售报表" + ".xlsx";
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(file_name.getBytes("GBK"), "ISO8859-1"));
            response.setCharacterEncoding("utf-8");
            OutputStream outputStream = response.getOutputStream();
            wb.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void heBing(Sheet sheet,int start,int end ,int index) {
        CellRangeAddress bigOrderRegion = new CellRangeAddress( start+2, end+1 , index, index);
        sheet.addMergedRegion(bigOrderRegion);
    }
    @GetMapping(value = "getBan")
    public void getBan(String date, HttpServletRequest request, HttpServletResponse response) throws IOException {

        Date d = null;
        DateFormat sdf = new SimpleDateFormat(DateUtil.PATTERN_YYYY_MM_DD);
        try {
            d = sdf.parse(date+"-01");
        } catch (Exception e) {
            d = new Date();
            date = sdf.format(d);
        }
        File f = new File("/home/file/ban.xlsx");
        //File f = new File("D:\\excel\\ban.xlsx");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            Workbook wb = new XSSFWorkbook(fis);
            Sheet sheet = wb.getSheetAt(0);
            String str=date.substring(5, 7).startsWith("0")?date.substring(5, 7).replace("0", ""):date.substring(5, 7);
            wb.setSheetName(0,str+"月班表" );
            Calendar one = Calendar.getInstance();
            one.setTime(d);
            one.set(Calendar.DAY_OF_MONTH, one.getActualMinimum(Calendar.DAY_OF_MONTH));//获取第一天
            Date oneDate=one.getTime();
            //最后一天
            Calendar last = Calendar.getInstance();
            last.setTime(d);
            last.set(Calendar.DAY_OF_MONTH, last.getActualMaximum(Calendar.DAY_OF_MONTH));//获取最后一天
            Date lastDate=last.getTime();
            long days = DateUtil.betweenDays(oneDate, lastDate);
            Row row;
            String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
            for (int i =0;i<days+1;i++){
                int w = one.get(Calendar.DAY_OF_WEEK) - 1;
                if (w < 0) {
                    w = 0;
                }
                CellStyle style=wb.createCellStyle();
                /*style.setFillBackgroundColor((IndexedColors.SEA_GREEN.getIndex()));
                style.setFillPattern(XSSFCellStyle.FINE_DOTS );*/
                style.setBorderBottom(new Short("1"));
                style.setBorderTop(new Short("1"));
                style.setBorderRight(new Short("1"));
                style.setBorderLeft(new Short("1"));
                row=sheet.getRow(0);
                row.createCell(0).setCellValue(str+"月份");
                row.createCell(i+1).setCellValue(i+1);
                row.getCell(i+1).setCellStyle(style);
                row=sheet.getRow(1);
                row.createCell(i+1).setCellValue(weekDays[w]);
                row.getCell(i+1).setCellStyle(style);
                one.add(Calendar.DAY_OF_MONTH, 1);
            }
            String file_name = "排班计划-" +date +".xlsx";
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(file_name.getBytes("GBK"), "ISO8859-1"));
            response.setCharacterEncoding("utf-8");
            OutputStream outputStream = response.getOutputStream();
            wb.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
