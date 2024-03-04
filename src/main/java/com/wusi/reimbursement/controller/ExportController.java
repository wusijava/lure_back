package com.wusi.reimbursement.controller;

import com.alibaba.fastjson.JSONObject;
import com.wusi.reimbursement.aop.SysLog;
import com.wusi.reimbursement.common.Response;
import com.wusi.reimbursement.entity.ClassPlan;
import com.wusi.reimbursement.entity.ExcelDto;
import com.wusi.reimbursement.entity.Reimbursement;
import com.wusi.reimbursement.query.ReimbursementQuery;
import com.wusi.reimbursement.service.ClassPlanService;
import com.wusi.reimbursement.service.ReimbursementService;
import com.wusi.reimbursement.utils.DataUtil;
import com.wusi.reimbursement.utils.DateUtil;
import com.wusi.reimbursement.utils.PoiUtil;
import com.wusi.reimbursement.utils.RedisUtil;
import com.wusi.reimbursement.vo.ReimbursementVo;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @ Description   :  导入导出controller
 * @ Author        :  wusi
 * @ CreateDate    :  2020/1/10$ 10:14$
 */
@RestController
public class ExportController {
    @Autowired
    private ReimbursementService reimbursementService;
    @Value("${excelDownloadUrl}")
    private String excelDownloadUrl;
    @Autowired
    private ClassPlanService classPlanService;

    @RequestMapping(value = "/Import", method = RequestMethod.POST)
    @ResponseBody
    public Response<String> Import(MultipartFile file) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        HSSFWorkbook workbook = PoiUtil.getWorkBook(file);
        if (workbook == null) {
            throw new RuntimeException("导入excel出错");
        }
        HSSFSheet sheet = workbook.getSheet("Sheet1");
        if (sheet == null) {
            throw new RuntimeException("导入excel出错");
        }

        ClassPlan plan = classPlanService.queryLast();
        String excelDate=null;
        HSSFRow row=null;
        row = sheet.getRow(0);
        format(row);
        excelDate = getData(row, 0);
        if(DataUtil.isNotEmpty(plan)){
            String date = plan.getDate().substring(0, 7);
            if (excelDate.equals(date)) {
                return Response.fail(date + "月数据已生成!");
            }
        }
        Date excel = sdf.parse(excelDate + "-01");
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(excel);
        int days = startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        List<ClassPlan> in=new ArrayList<>();
        for (int i = 2; i <= 8; i++) {
            Date start = sdf.parse(excelDate + "-01");
            startCalendar.setTime(start);
            row = sheet.getRow(i);
            String name = getData(row, 0);
            for (int j = 1; j <=days; j++) {
                ClassPlan planDay=new ClassPlan();
                String data = getData(row, j);
                Date nowDate=startCalendar.getTime();
                String day=sdf.format(nowDate);
                planDay.setName(name);
                planDay.setDate(day);
                planDay.setCteateTime(new Date());
                planDay.setClasses(data);
                in.add(planDay);
                startCalendar.add(Calendar.DAY_OF_MONTH, 1);
            }
            i++;
        }
        classPlanService.insertBatch(in);
        /*List<Reimbursement> reimbursementList=getReimbursementList(file);
        reimbursementService.insertBatch(reimbursementList);*/
        return Response.ok("");
    }

    private List<Reimbursement> getReimbursementList(MultipartFile file) throws ParseException {
        List<Reimbursement> list = new ArrayList<>();
        HSSFWorkbook workbook = PoiUtil.getWorkBook(file);
        if (workbook == null) {
            throw new RuntimeException("导入excel出错");
        }
        HSSFSheet sheet = workbook.getSheet("Sheet1");
        if (sheet == null) {
            throw new RuntimeException("导入excel出错");
        }
        Integer rowNum = sheet.getLastRowNum();
        Reimbursement qualification;
        for (int i = 1; i <= rowNum; i++) {
            qualification = new Reimbursement();
            HSSFRow row = sheet.getRow(i);
            format(row);
            qualification.setProductName(getData(row, 1));
            qualification.setTotalPrice(getData(row, 2));
            qualification.setBuyChannel(getData(row, 3));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date buydate = simpleDateFormat.parse(getData(row, 4));
            qualification.setBuyDate(buydate);
            qualification.setState(-1);
            qualification.setRemark(getData(row, 5));
            list.add(qualification);
        }
        return list;
    }

    private void format(HSSFRow row) {
        row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
        row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
        row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
        row.getCell(4).setCellType(Cell.CELL_TYPE_STRING);
        row.getCell(5).setCellType(Cell.CELL_TYPE_STRING);
        // row.getCell(6).setCellType(Cell.CELL_TYPE_STRING);
        //row.getCell(7).setCellType(Cell.CELL_TYPE_STRING);
        //row.getCell(8).setCellType(Cell.CELL_TYPE_STRING);
    }

    private String getData(HSSFRow row, Integer cellNum) {
        return format(row.getCell(cellNum).getStringCellValue());

    }

    private String format(String s) {
        if (DataUtil.isEmpty(s)) {
            return "";
        }
        return s.trim().replaceAll("\r", "").replaceAll("\n", "");
    }

    @RequestMapping(value = "Export", method = RequestMethod.POST)
    @ResponseBody
    @SysLog("导出文件")
    public Response<String> batchExport(ReimbursementQuery query) {
        List<Reimbursement> reimbursementList = reimbursementService.queryList(query);
        List<ReimbursementVo> voList = new ArrayList<>();
        int index = 1;
        for (Reimbursement reimbursement : reimbursementList) {
            ReimbursementVo reimbursementvo = getReimbursementLististVo(reimbursement);
            reimbursementvo.setIndex(index);
            voList.add(reimbursementvo);
            index++;
        }
        ExcelDto dto = new ExcelDto();
        dto.setHeaders(ReimbursementVo.headers);
        dto.setKeys(ReimbursementVo.keys);
        dto.setObjectList(parser(voList));
        String key = UUID.randomUUID().toString().replaceAll("-", "");
        RedisUtil.set(key, dto, 1000 * 60 * 30L);
        String url = excelDownloadUrl + key;
        return Response.ok(url);
    }

    private List<JSONObject> parser(List<ReimbursementVo> reimbursementVo) {
        return JSONObject.parseArray(JSONObject.toJSONString(reimbursementVo), JSONObject.class);
    }

    private ReimbursementVo getReimbursementLististVo(Reimbursement reimbursement) {
        ReimbursementVo reimbursementList = new ReimbursementVo();
        reimbursementList.setId(reimbursement.getId());
        reimbursementList.setProductName(reimbursement.getProductName());
        reimbursementList.setTotalPrice(reimbursement.getTotalPrice());
        reimbursementList.setBuyChannel(reimbursement.getBuyChannel());
        reimbursementList.setBuyDate(DateUtil.formatDate(reimbursement.getBuyDate(), DateUtil.PATTERN_YYYY_MM_DD));
        reimbursementList.setReimbursementDate(reimbursement.getReimbursementDate() == null ? "未上交单据" : DateUtil.formatDate(reimbursement.getReimbursementDate(), DateUtil.PATTERN_YYYY_MM_DD));
        reimbursementList.setRemitDate(reimbursement.getRemitDate() == null ? "未到账" : DateUtil.formatDate(reimbursement.getRemitDate(), DateUtil.PATTERN_YYYY_MM_DD));
        reimbursementList.setState(reimbursement.getStateDesc());
        reimbursementList.setRemark(reimbursement.getRemark());
        return reimbursementList;
    }
}
