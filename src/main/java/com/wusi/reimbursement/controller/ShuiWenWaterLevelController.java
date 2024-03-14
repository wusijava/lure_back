package com.wusi.reimbursement.controller;

import com.alibaba.fastjson.JSON;
import com.wusi.reimbursement.common.Response;
import com.wusi.reimbursement.entity.ShuiWenWaterLevel;
import com.wusi.reimbursement.entity.WaterLevel;
import com.wusi.reimbursement.query.ShuiWenWaterLevelQuery;
import com.wusi.reimbursement.query.WaterLevelQuery;
import com.wusi.reimbursement.service.ShuiWenWaterLevelService;
import com.wusi.reimbursement.service.WaterLevelService;
import com.wusi.reimbursement.utils.DataUtil;
import com.wusi.reimbursement.utils.DateUtil;
import com.wusi.reimbursement.utils.DingDingTalkUtils;
import com.wusi.reimbursement.vo.ShuiWenWaterLevelVo;
import com.wusi.reimbursement.vo.WaterLevelVo;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @ Description   :  长江水文水位数据监控
 * @ Author        :  wusi
 * @ CreateDate    : 2024/03/14
 */
@RestController
@RequestMapping(value = "waterLevelNew")
@Slf4j
public class ShuiWenWaterLevelController {
    static SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.PATTERN_YYYY_MM_DD);
    static SimpleDateFormat sdf2 = new SimpleDateFormat(DateUtil.PATTERN_YYYYMM);
    @Autowired
    private ShuiWenWaterLevelService shuiWenWaterLevelService;
    @RequestMapping(value = "getHistoryData")
    public void getWaterLevel() throws IOException, ParseException {

    }
    @RequestMapping(value = "getTodayData")
    @Scheduled(cron = "0 0 12,23 * * ?")
    public void getTodayData() throws Exception {
        log.error("长江水文开始获取水位,{}", sdf.format(new Date()));
        String html = Jsoup.connect("http://www.cjh.com.cn/sqindex.html").execute().body();
        Integer start = html.indexOf("[");
        Integer end = html.indexOf("]")+1;
        String json=html.substring(start, end);

        ShuiWenWaterLevel shuiWenWaterLevel = shuiWenWaterLevelService.queryByDate(sdf.format(new Date()));
        if(DataUtil.isEmpty(shuiWenWaterLevel)){
            ShuiWenWaterLevel data=new ShuiWenWaterLevel();
            data.setCreateTime(new Date());
            data.setData(json);
            shuiWenWaterLevelService.insert(data);
        }

    }
    @RequestMapping(value = "getWaterLevelList")
    public Response<Page> getWaterLevelList(ShuiWenWaterLevelQuery query) {
        if (DataUtil.isEmpty(query.getPage())) {
            query.setPage(0);
        }
        if (DataUtil.isEmpty(query.getLimit())) {
            query.setLimit(2);
        }
        if(query.getStartTime()!=null&&query.getEndTime()!=null){
            String[] start=query.getStartTime().split("-");
            String startMonth="";
            String startDay="";
            startMonth=start[1].length()<2? "0" + start[1]:start[1];
            if(start[2].length()<2){
                startDay="0"+start[2];
            }else{
                startDay=start[2];
            }
            query.setStartTime(start[0]+"-"+startMonth+"-"+startDay);
            String[] end=query.getEndTime().split("-");
            String endMonth="";
            String endDay="";
            if(end[1].length()<2) {
                endMonth = "0" + end[1];
            }else{
                endMonth=end[1];
            }
            if(end[2].length()<2){
                endDay="0"+end[2];
            }else{
                endDay=end[2];
            }
            query.setEndTime(end[0]+"-"+endMonth+"-"+endDay);
        }
        Pageable pageable = PageRequest.of(query.getPage(), query.getLimit());
        Page<ShuiWenWaterLevel> page = shuiWenWaterLevelService.queryPage(query, pageable);
        List<ShuiWenWaterLevelVo> voList = new ArrayList<>();
        for (ShuiWenWaterLevel waterLevel : page.getContent()) {
            voList.add(getListVo(waterLevel));
        }
        Page<ShuiWenWaterLevelVo> voPage = new PageImpl<>(voList, pageable, page.getTotalElements());
        return Response.ok(voPage);
    }

    private ShuiWenWaterLevelVo getListVo(ShuiWenWaterLevel waterLevel) {
        ShuiWenWaterLevelVo vo =new ShuiWenWaterLevelVo();
        vo.setCreateTime(sdf.format(waterLevel.getCreateTime()));
        vo.setId(waterLevel.getId());
        vo.setList(JSON.parseArray(waterLevel.getData(), com.wusi.reimbursement.vo.ShuiWenWaterLevel.class));
        return vo;
    }



}
