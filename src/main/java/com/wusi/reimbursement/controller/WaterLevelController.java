package com.wusi.reimbursement.controller;

import com.wusi.reimbursement.common.Response;
import com.wusi.reimbursement.entity.WaterLevel;
import com.wusi.reimbursement.query.WaterLevelQuery;
import com.wusi.reimbursement.service.WaterLevelService;
import com.wusi.reimbursement.utils.DataUtil;
import com.wusi.reimbursement.utils.DateUtil;
import com.wusi.reimbursement.utils.DingDingTalkUtils;
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
 * @ Description   :  水位数据监控
 * @ Author        :  wusi
 * @ CreateDate    :  2021/5/18$ 9:19$
 */
@RestController
@RequestMapping(value = "waterLevel")
@Slf4j
public class WaterLevelController {
    static SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.PATTERN_YYYY_MM_DD);
    static SimpleDateFormat sdf2 = new SimpleDateFormat(DateUtil.PATTERN_YYYYMM);
    @Autowired
    private WaterLevelService waterLevelService;
    @RequestMapping(value = "getHistoryData")
    public void getWaterLevel() throws IOException, ParseException {
        String html=null;
        List<WaterLevel> list= new ArrayList<>();
        for (int i=0;i<399;i++){
            if(i==0){
                html = Jsoup.connect("https://cj.msa.gov.cn/xxgk/xxgkml/aqxx/swgg/index.shtml").execute().body();
            }else{
                html = Jsoup.connect("https://cj.msa.gov.cn/xxgk/xxgkml/aqxx/swgg/index_"+i+".shtml").execute().body();
            }
            for(int j =0;j<20;j++){
                Integer index=html.indexOf("点水位公告<");
                if(index==-1){
                    break;
                }
                String str=html.substring(index-2, index);
                if("11".equals(str)||str.indexOf("8")>-1){
                    String href;
                    String date;
                    if("11".equals(str)){
                        href=html.substring(index-85, index-56);
                        date=html.substring(index-150, index-140);
                        if(!href.endsWith("shtml")){
                            href=html.substring(index-83, index-54);
                            date=html.substring(index-148, index-138);
                        }
                        if(!href.endsWith("shtml")){
                            href=html.substring(index-87, index-58);
                            date=html.substring(index-152, index-142);
                        }
                    }else{
                        href=html.substring(index-83, index-54);
                        date=html.substring(index-148, index-138);
                    }

                    String htmlInner = Jsoup.connect("https://cj.msa.gov.cn/xxgk/xxgkml/aqxx/swgg/"+href).execute().body();
                    Integer indexHan=htmlInner.indexOf("汉<");
                    String waterLeavel;
                    if(str.indexOf("8")>-1){
                         waterLeavel=htmlInner.substring(indexHan+792, indexHan+798).replace("<", "").replace(";", "").replace(" ", "").replace("\">", "").replace("/", "");
                    }else{
                         waterLeavel=htmlInner.substring(indexHan+213, indexHan+219).replace("<", "").replace(";", "").replace(" ", "").replace("\">", "").replace("/", "");
                         if(waterLeavel.indexOf(".")==-1){
                             waterLeavel=htmlInner.substring(indexHan+792, indexHan+798).replace("<", "").replace(";", "").replace(" ", "").replace("\">", "").replace("/", "");
                            if(waterLeavel.indexOf(".")==-1){
                                waterLeavel=htmlInner.substring(indexHan+739, indexHan+745).replace("<", "").replace(";", "").replace(" ", "").replace("\">", "").replace("/", "");
                            }
                         }
                    }
                    WaterLevel waterLevel=new WaterLevel();
                    waterLevel.setAddress("汉口");
                    waterLevel.setCreateTime(sdf.parse(date));
                    waterLevel.setWaterLevel(waterLeavel);
                    list.add(waterLevel);
                    waterLevelService.insert(waterLevel);
                    html=html.replaceFirst("点水位公告<", "点水位公告>");
                }else{
                    html=html.replaceFirst("点水位公告<", "点水位公告>");
                }
            }
            //waterLevelService.insertBatch(list);
        }

    }
    @RequestMapping(value = "getTodayData")
    @Scheduled(cron = "0 0 12,23 * * ?")
    public void getTodayData() throws Exception {
        log.error("开始获取水位,{}", sdf.format(new Date()));
        String html = Jsoup.connect("https://cj.msa.gov.cn/xxgk/xxgkml/aqxx/swgg/index.shtml").execute().body();
        Integer index = html.indexOf("点水位公告<");
        String str = html.substring(index - 2, index);
        if (str.indexOf("8") == -1) {
            html = html.replaceFirst("点水位公告<", "点水位公告>");
            index = html.indexOf("点水位公告<");
        }
        String href = html.substring(index - 87, index - 56);
        if(!href.endsWith(".shtml")){
            href=html.substring(index - 81, index - 52);
        }
        String date = html.substring(index - 148, index - 138);
        if(!date.startsWith("20")){
            date = html.substring(index - 146, index - 136);
        }
        href="https://cj.msa.gov.cn/xxgk/xxgkml/aqxx/swgg/" + href;

        //String htmlInner = Jsoup.connect(href).execute().body();
        //Integer indexHan = htmlInner.indexOf("汉<");
       /* String waterLeavel;
        if(htmlInner.substring(indexHan+213,indexHan+798).indexOf("n>黄<")>-1){
            waterLeavel = htmlInner.substring(indexHan + 213, indexHan + 219).replace("<", "").replace(";", "").replace(" ", "").replace("\">", "").replace("/", "");
        }else{
            waterLeavel = htmlInner.substring(indexHan + 792, indexHan + 798).replace("<", "").replace(";", "").replace(" ", "").replace("\">", "").replace("/", "");
        }*/
        WaterLevel num = waterLevelService.queryByTime(sdf.format(new Date()));
        if(DataUtil.isEmpty(num)){
            WaterLevel query=new WaterLevel();
            query.setCreateTime(new Date());
            query.setAddress("长江-汉口段");
            query.setHref(handHref(href,sdf2.format(new Date())));
            waterLevelService.insert(query);
            DingDingTalkUtils.sendDingDingMsg("已获取今日水位链接，请手动补充水位值");
        }
    }
    @RequestMapping(value = "getWaterLevelList")
    public Response<Page> getWaterLevelList(WaterLevelQuery query) {
        if (DataUtil.isEmpty(query.getPage())) {
            query.setPage(0);
        }
        if (DataUtil.isEmpty(query.getLimit())) {
            query.setLimit(5);
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
        Page<WaterLevel> page = waterLevelService.queryPage(query, pageable);
        List<WaterLevelVo> voList = new ArrayList<>();
        for (WaterLevel waterLevel : page.getContent()) {
            voList.add(getListVo(waterLevel));
        }
        Page<WaterLevelVo> voPage = new PageImpl<>(voList, pageable, page.getTotalElements());
        return Response.ok(voPage);
    }

    private WaterLevelVo getListVo(WaterLevel waterLevel) {
        WaterLevelVo vo=new WaterLevelVo();
        vo.setAddress(waterLevel.getAddress());
        vo.setHref(handHref(waterLevel.getHref(),sdf2.format(waterLevel.getCreateTime())));
        vo.setCreateTime(sdf.format(waterLevel.getCreateTime()));
        if(DataUtil.isNotEmpty(waterLevel.getWaterLevel())){
            vo.setWaterLevel(waterLevel.getWaterLevel()+"米");
        }else{
            vo.setWaterLevel("获取失败，请查看官网，并修改");
        }

        vo.setId(waterLevel.getId());
        return vo;
    }

    private String handHref(String href,String day) {
        if(!href.endsWith(".shtml")){
            Integer idnex= href.indexOf("/t");
            String h=href.substring(idnex, idnex+24);
            String date=href.substring(idnex+2, idnex+8);
            return "https://cj.msa.gov.cn/xxgk/xxgkml/aqxx/swgg/"+date+h;
        }
        if(href.endsWith(".shtml")){
            return "https://cj.msa.gov.cn/xxgk/xxgkml/aqxx/swgg/"+day+"/"+href.substring(href.length()-23, href.length());
        }

        return href;
    }

    @RequestMapping(value = "dayReport")
    //@Scheduled(cron = "0 0 18,19 * * ?")
    public void dayReport() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        if (w !=6 &&w!= 0) {
            DingDingTalkUtils.sendDingDingMsg(weekDays[w]+":钉钉日报提醒!");
        }
    }

    //@Scheduled(cron = "0 0 8,12,18,22 * * ?")
    public void taoBaoSign() throws Exception {
            DingDingTalkUtils.sendDingDingMsg("淘宝签到活动提醒!!!");
    }

    @RequestMapping(value = "changeWaterLevel")
    public void changeWaterLevel(Long id,String value) {
        WaterLevel waterLevel = waterLevelService.queryById(id);
        waterLevel.setWaterLevel(value);
        waterLevelService.updateById(waterLevel);
    }
}
