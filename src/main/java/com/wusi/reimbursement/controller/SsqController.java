package com.wusi.reimbursement.controller;

import com.alibaba.fastjson.JSONObject;
import com.wusi.reimbursement.common.Response;
import com.wusi.reimbursement.entity.*;
import com.wusi.reimbursement.mapper.SsqMapper;
import com.wusi.reimbursement.query.SsqBonusQuery;
import com.wusi.reimbursement.query.SsqHistoryQuery;
import com.wusi.reimbursement.query.SsqParam;
import com.wusi.reimbursement.query.SsqQuery;
import com.wusi.reimbursement.service.*;
import com.wusi.reimbursement.utils.*;
import com.wusi.reimbursement.vo.KeyValue;
import com.wusi.reimbursement.vo.SsqBonusVo;
import com.wusi.reimbursement.vo.SsqVo;
import com.wusi.reimbursement.vo.suiJi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ Description   :  菩萨保佑 让我走向人生巅峰
 * @ Author        :  wusi
 * @ CreateDate    :  2021/1/5$ 10:13$
 */
@RestController
@Slf4j
@RequestMapping(value = "/api/ssq")
public class SsqController {
    @Autowired
    private SsqBonusService SsqBonusService;
    @Autowired
    private SsqService SsqService;
    @Autowired
    private SsqMapper SsqMapper;
    @Autowired
    private SsqQuickService ssqQuickService;
    @Autowired
    private SsqHistoryService SsqHistoryService;
    @Autowired
    private SsqImgService SsqImgService;
    @Value("${upload.qiniu.host}")
    private String imgPrefix;
    @Autowired
    private UploadService uploadService;

    @RequestMapping(value = "getBonusNum")
    @ResponseBody
    @Scheduled(cron = "0 0 22,23 * * ?")
    public void getBonusNum() throws Exception {
        SsqBonus ssq = SsqNumGuanWangUtils.getSsqNum();
        List<String> kaiJiang=new ArrayList<>();
        SsqBonus query = new SsqBonus();
        query.setTerm(ssq.getTerm());
        Long num = SsqBonusService.queryCount(query);
        long numBaidu=2;
        if(num>0){
            SsqBonus baidu = null;
            try {
                baidu = SsqNumUtils.getSsqNum();
            } catch (Exception e) {
                //DingDingTalkUtils.sendDingDingMsg("百度获取开奖异常~~,请重试!");
            }
            query.setTerm(baidu.getTerm());
            numBaidu = SsqBonusService.queryCount(query);
            if(numBaidu==0){
                ssq.setTerm(baidu.getTerm());
                ssq.setRed1(baidu.getRed1());
                ssq.setRed2(baidu.getRed2());
                ssq.setRed3(baidu.getRed3());
                ssq.setRed4(baidu.getRed4());
                ssq.setRed5(baidu.getRed5());
                ssq.setRed6(baidu.getRed6());
                ssq.setBlue(baidu.getBlue());
                ssq.setWeek(baidu.getWeek());
                kaiJiang.add(ssq.getRed1());
                kaiJiang.add(ssq.getRed2());
                kaiJiang.add(ssq.getRed3());
                kaiJiang.add(ssq.getRed4());
                kaiJiang.add(ssq.getRed5());
                kaiJiang.add(ssq.getRed6());
            }
        }else{
            kaiJiang.add(ssq.getRed1());
            kaiJiang.add(ssq.getRed2());
            kaiJiang.add(ssq.getRed3());
            kaiJiang.add(ssq.getRed4());
            kaiJiang.add(ssq.getRed5());
            kaiJiang.add(ssq.getRed6());
        }
        //加入到历史库
        SsqHistory his=new SsqHistory();
        his.setTerm(ssq.getTerm());
        SsqHistory history = SsqHistoryService.queryOne(his);
        if(DataUtil.isEmpty(history)){
            SsqHistory ins=new SsqHistory();
            ins.setTerm(ssq.getTerm());
            ins.setRed1(ssq.getRed1());
            ins.setRed2(ssq.getRed2());
            ins.setRed3(ssq.getRed3());
            ins.setRed4(ssq.getRed4());
            ins.setRed5(ssq.getRed5());
            ins.setCreateTime(new Date());
            ins.setRed6(ssq.getRed6());
            ins.setBlue(ssq.getBlue());
            ins.setBonusTime(DateUtil.formatDate(new Date(), "yyyy-MM-dd"));
            SsqHistoryService.insert(ins);
        }
        if (num < 1||numBaidu<1) {
            SsqBonusService.insert(ssq);
            //发送钉钉推送
            DingDingTalkUtils.sendDingDingMsg("今日开奖信息:期数:"+ssq.getTerm()+",红球:"+ssq.getRed1()+"-"+ssq.getRed2()+"-"+ssq.getRed3()+"-"+ssq.getRed4()+"-"+ssq.getRed5()+"-"+ssq.getRed6()+"蓝球:"+ssq.getBlue());
            //去查找有误购买此期的购买记录
            Ssq buy=new Ssq();
            buy.setTerm(query.getTerm());
            List<Ssq> buyList = SsqService.queryList(buy);
            if(DataUtil.isNotEmpty(buyList)){
                for(Ssq buyOne:buyList){
                    if(!query.getTerm().equals(buyOne.getTerm())){
                        break;
                    }
                    int redNum=0;
                    int blueNum=0;
                    if(buyOne.getBlue().equals(ssq.getBlue())){
                        blueNum=1;
                    }
                    List<String> myNum=new ArrayList<>();
                    myNum.add(buyOne.getRed1());
                    myNum.add(buyOne.getRed2());
                    myNum.add(buyOne.getRed5());
                    myNum.add(buyOne.getRed6());
                    myNum.add(buyOne.getRed3());
                    myNum.add(buyOne.getRed4());
                    for(int i=0;i<6;i++){
                        for(int j=0;j<6;j++){
                            if(myNum.get(i).equals(kaiJiang.get(j))){
                                redNum++;
                            }
                        }
                    }
                    buyOne.setRedNum(String.valueOf(redNum));
                    buyOne.setBlueNum(String.valueOf(blueNum));
                    if (redNum>=4||blueNum==1){
                        buyOne.setIsBonus("1");
                        if(blueNum==1&&redNum<3){
                            buyOne.setBonus("5");
                        }else if(blueNum==1&&redNum==3){
                            buyOne.setBonus("10");
                        }else if(blueNum==0&&redNum==4){
                            buyOne.setBonus("10");
                        }else if((blueNum==1&&redNum==4)||redNum==5){
                            buyOne.setBonus("200");
                        }else if(blueNum==1&&redNum==5){
                            buyOne.setBonus("3000");
                        }else if(redNum==6&&blueNum==0){
                            buyOne.setBonus("500000");
                        }else{
                            buyOne.setBonus("5000000");
                        }
                        //钉钉群发中奖消息
                        DingDingTalkUtils.sendDingDingMsg("恭喜:"+buyOne.getUser()+",喜中"+MoneyUtil.multiply(buyOne.getBonus(), buyOne.getNum())+"元大奖!!!");
                        //发送中奖短信
                        //SMSUtil.sendSMS("18602702325", buyOne.getBonus(), 842665);
                        if(DataUtil.isNotEmpty(buyOne.getType())&&buyOne.getType().equals(2)){
                            buyOne.setCommission(MoneyUtil.multiply(buyOne.getNum(), MoneyUtil.multiply(buyOne.getBonus(), MoneyUtil.devide(buyOne.getRate(), "100"))));
                        }
                    }else{
                        //未中奖
                        buyOne.setIsBonus("-1");
                        buyOne.setBonus("0");
                        DingDingTalkUtils.sendDingDingMsg("未中奖,没关系,下期五百万,菩萨保佑!");
                    }
                    buyOne.setWeek(ssq.getWeek());
                    buyOne.setBonusTime(ssq.getCreateTime());
                    SsqService.updateById(buyOne);
                }

            }else{
                log.error("本期未购买,下期继续,{}", ssq.getTerm());
            }
        }
        log.error("获取开奖号码执行结束,{}", DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
    }


    @RequestMapping(value = "getSsqRecord")
    @ResponseBody
    public Response<Page<SsqBonusVo>> getSsqRecord(SsqBonusQuery query) {
        if (DataUtil.isEmpty(query.getPage())) {
            query.setPage(0);
        }
        query.setLimit(2);
        Pageable pageable = PageRequest.of(query.getPage(), query.getLimit());
        Page<SsqBonus> page = SsqBonusService.queryPage(query, pageable);
        List<SsqBonusVo> voList = new ArrayList<>();
        for (SsqBonus ssqBonus : page.getContent()) {
            voList.add(getSsqVo(ssqBonus));
        }
        Page<SsqBonusVo> voPage = new PageImpl<>(voList, pageable, page.getTotalElements());
        return Response.ok(voPage);
    }

    private SsqBonusVo getSsqVo(SsqBonus ssqBonus) {
        SsqBonusVo vo = new SsqBonusVo();
        vo.setId(ssqBonus.getId());
        vo.setTerm(ssqBonus.getTerm());
        SsqImg img=new SsqImg();
        img.setTerm(ssqBonus.getTerm());
        SsqImg ssqImg = SsqImgService.queryOne(img);
        if(DataUtil.isNotEmpty(ssqImg)){
            vo.setUrl(ssqImg.getUrl());
        }
        vo.setRed1(ssqBonus.getRed1());
        vo.setRed2(ssqBonus.getRed2());
        vo.setRed3(ssqBonus.getRed3());
        vo.setRed4(ssqBonus.getRed4());
        vo.setRed5(ssqBonus.getRed5());
        vo.setRed6(ssqBonus.getRed6());
        vo.setBlue(ssqBonus.getBlue());
        vo.setCreateTime(DateUtil.formatDate(ssqBonus.getCreateTime(), "yyyy-MM-dd")+" 21:15");
        vo.setWeek(ssqBonus.getWeek());
        //查找自己的购买记录
        Ssq ssq=new Ssq();
        ssq.setTerm(vo.getTerm());
        List<Ssq> ssqs = SsqService.queryList(ssq);
        if(DataUtil.isNotEmpty(ssqs)){
            List<SsqVo> list=new ArrayList<>();
            for(Ssq s:ssqs){
                SsqVo ssqVo=new SsqVo();
                ssqVo.setId(s.getId());
                ssqVo.setTerm(s.getTerm());
                ssqVo.setRed4(s.getRed4());
                ssqVo.setRed5(s.getRed5());
                ssqVo.setRed6(s.getRed6());
                ssqVo.setRed1(s.getRed1());
                ssqVo.setRed2(s.getRed2());
                ssqVo.setRed3(s.getRed3());
                ssqVo.setBlue(s.getBlue());
                ssqVo.setNum(s.getNum());
                ssqVo.setBlue(s.getBlue());
                ssqVo.setRedNum(s.getRedNum());
                ssqVo.setBlueNum(s.getBlueNum());
                ssqVo.setBonus(s.getBonus());
                list.add(ssqVo);
            }
            vo.setList(list);
        }else{
            vo.setList(null);
        }
        return vo;
    }
    @RequestMapping(value = "getSsqNum")
    @ResponseBody
    public Response<String> getSsqNum() throws ParseException {
        return Response.ok(WeekUtils.getSsqNum(null));
    }
    @RequestMapping(value = "addSsq")
    @ResponseBody
    public Response<String> addSsq(Ssq Ssq) {
        RequestContext.RequestUser loginUser = RequestContext.getCurrentUser();
        HashSet set=new HashSet();
        set.add(Ssq.getRed1());
        set.add(Ssq.getRed2());
        set.add(Ssq.getRed3());
        set.add(Ssq.getRed4());
        set.add(Ssq.getRed5());
        set.add(Ssq.getRed6());
        if(set.size()<6){
            return Response.fail("红球有重复!");
        }
        if(  (Integer.valueOf(Ssq.getRed1())>33||Integer.valueOf(Ssq.getRed1())<1)||Ssq.getRed1().length()!=2){
            return Response.fail("红球范围或格式错误错误!");
        }
        if(  (Integer.valueOf(Ssq.getRed2())>33||Integer.valueOf(Ssq.getRed2())<1)||Ssq.getRed2().length()!=2){
            return Response.fail("红球范围或格式错误错误!");
        }
        if(  (Integer.valueOf(Ssq.getRed3())>33||Integer.valueOf(Ssq.getRed3())<1)||Ssq.getRed3().length()!=2){
            return Response.fail("红球范围或格式错误错误!");
        }
        if(  (Integer.valueOf(Ssq.getRed4())>33||Integer.valueOf(Ssq.getRed4())<1)||Ssq.getRed4().length()!=2){
            return Response.fail("红球范围或格式错误错误!");
        }
        if(  (Integer.valueOf(Ssq.getRed5())>33||Integer.valueOf(Ssq.getRed5())<1)||Ssq.getRed5().length()!=2){
            return Response.fail("红球范围或格式错误错误!");
        }
        if( ( Integer.valueOf(Ssq.getRed6())>33||Integer.valueOf(Ssq.getRed6())<1)||Ssq.getRed6().length()!=2){
            return Response.fail("红球范围或格式错误错误!");
        }
        if(  (Integer.valueOf(Ssq.getBlue())>16||Integer.valueOf(Ssq.getBlue())<1)||Ssq.getBlue().length()!=2){
            return Response.fail("篮球范围或格式错误错误!");
        }
        Ssq.setCreateTime(new Date());
        Ssq.setIsBonus("0");
        Ssq.setBonus("0");
        Ssq.setUser(loginUser.getNickName());
        //代买
        if(DataUtil.isNotEmpty(Ssq.getType())&&Ssq.getType().equals(2)){
            //代买者
            if(DataUtil.isEmpty(Ssq.getBuyer())){
                return Response.fail("请指定购买者!!");
            }
            if(Ssq.getBuyer().equals(loginUser.getNickName())){
                return Response.fail("无法指定自己购买!!");
            }
            Ssq.setState(0);
            Ssq.setCommission("0");
        }else{
            Ssq.setType(1);
            Ssq.setState(1);
        }
        try {
            SsqService.insert(Ssq);
        } catch (Exception e) {
            log.error("新增异常,{}", JSONObject.toJSONString(Ssq));
        }
        return Response.ok("定能暴富!");
    }

    @RequestMapping(value = "buyRecord")
    @ResponseBody
    public Response<List<SsqParam>> buyRecord(SsqQuery query) {
        RequestContext.RequestUser loginUser = RequestContext.getCurrentUser();
        if (DataUtil.isEmpty(query.getPage())) {
            query.setPage(0);
        }
        query.setLimit(5);
        if(DataUtil.isNotEmpty(query.getType())&&query.getType().equals(3)){
            query.setBuyer(loginUser.getNickName());
        }
        query.setPage(query.getLimit()*(query.getPage()));
        Map<String, Object> value = SsqMapper.getValue();
        String bonus=(String.valueOf(value.get("bonus")));
        String spend=MoneyUtil.multiply((String.valueOf(value.get("count"))),"2") ;
        long count=0;
        if(DataUtil.isNotEmpty(query.getType())&&query.getType().equals(3)){
            count=SsqService.querycount(loginUser.getNickName());
        }else{
            count=SsqService.querycount(null);
        }
        long page=0;
        if(count%query.getLimit()==0){
            page=count/query.getLimit();
        }else{
            page=count/query.getLimit()+1;
        }
        List<Ssq> ssqs1 = SsqService.queryListByParam(query);
        List<String> list = ssqs1.stream().map(Ssq::getTerm).collect(Collectors.toList());
        ArrayList<SsqParam> ssqParams = new ArrayList<>();
        String commission=SsqMapper.getCommission(loginUser.getNickName());

        for (String s : list) {
            SsqQuery ssqQuery = new SsqQuery();
            ssqQuery.setTerm(s);

            if(DataUtil.isNotEmpty(query.getType())&&query.getType().equals(3)){
                ssqQuery.setBuyer(loginUser.getNickName());
            }
            List<Ssq> ssqs = SsqService.queryList(ssqQuery);
            List<SsqVo> list2=new ArrayList<>();
            for(Ssq Ssq:ssqs){
                list2.add(getSsqVo2(Ssq));
            }
            //图片
            SsqImg img=new SsqImg();
            img.setTerm(ssqQuery.getTerm());
            SsqImg ssqImg = SsqImgService.queryOne(img);
            String url="";
            if(DataUtil.isNotEmpty(ssqImg)){
                url=ssqImg.getUrl();
            }else{
                url="http://www.photo.wearelie.com/temp/1/c6qg8n.jpg";
            }
            ssqParams.add(new SsqParam(list2,ssqQuery.getTerm(),page,bonus,spend,commission,url));
        }
        return Response.ok(ssqParams);
    }



    private SsqVo getSsqVo2(Ssq ssq) {
        SsqVo vo= new SsqVo();
        vo.setRed5(ssq.getRed5());
        vo.setRed6(ssq.getRed6());
        vo.setBlue(ssq.getBlue());
        vo.setRed1(ssq.getRed1());
        vo.setRed2(ssq.getRed2());
        vo.setRed3(ssq.getRed3());
        vo.setRed4(ssq.getRed4());
        vo.setNum(ssq.getNum());
        vo.setTerm(ssq.getTerm());
        vo.setIsBonus(ssq.getIsBonusDesc());
        vo.setState(ssq.getState());
        vo.setId(ssq.getId());
        vo.setCreateTime(DateUtil.formatDate(ssq.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
        return vo;
    }

    @RequestMapping(value = "suiJi")
    @ResponseBody
    public Response<suiJi> buyRecord() {
        suiJi suiJi=new suiJi();
        List<Integer> num=new ArrayList<>();
        Random r = new Random();
        int i=0;
        while(true) {
            int number = r.nextInt(32)+1;
            if(!num.contains(number)) {
                num.add(number);
                i++;
                if(i==6){
                    break;
                }
            }
        }
        Collections.sort(num, (y,x)->{
            return x-y;
        });
        List<String> str=new ArrayList<>();
        for (int j=0;j<6;j++){
            if(String.valueOf(num.get(j)).length()<2){
                str.add(j, "0"+String.valueOf(num.get(j)));
            }else{
                str.add(j, String.valueOf(num.get(j)));
            }
        }
        suiJi.setRed6(str.get(0));
        suiJi.setRed5(str.get(1));
        suiJi.setRed4(str.get(2));
        suiJi.setRed3(str.get(3));
        suiJi.setRed2(str.get(4));
        suiJi.setRed1(str.get(5));
        int blue = r.nextInt(15)+1;
        if(String.valueOf(blue).length()<2){
            suiJi.setBlue("0"+String.valueOf(blue));
        }else{
            suiJi.setBlue(String.valueOf(blue));
        }
        return Response.ok(suiJi);
    }

    @RequestMapping(value = "changeSsqState")
    @ResponseBody
    public Response<String> changeSsqState(Long id,Integer state) {
        if(DataUtil.isEmpty(id)||DataUtil.isEmpty(state)){
            return Response.fail("缺少参数！");
        }
        Ssq ssq = SsqService.queryById(id);
        ssq.setState(state);
        try {
            SsqService.updateById(ssq);
        } catch (Exception e) {
            log.error("更新异常，{}", id);
            return Response.fail("操作失败！");
        }
        return Response.ok("操作成功！");
    }

    @RequestMapping(value = "getOnlineNum")
    @ResponseBody
    public Response<SsqBonus> getOnlineNum(Integer type) throws IOException, ParseException {
        SsqBonus ssqNum=null;
        if(type.equals(1)){
            ssqNum = SsqNumUtils.getSsqNum();
        }else{
            ssqNum = SsqNumGuanWangUtils.getSsqNum();
        }
       return Response.ok(ssqNum);

    }
    @RequestMapping(value = "SsqQuick")
    @ResponseBody
    public Response<List<SsqQuick>> SsqQuick() {
        RequestContext.RequestUser loginUser = RequestContext.getCurrentUser();
        SsqQuick quick=new SsqQuick();
        quick.setUser(loginUser.getNickName());
        List<SsqQuick> ssqQuicks = ssqQuickService.queryList(quick);
        return Response.ok(ssqQuicks);
    }

    @RequestMapping(value = "history")
    @ResponseBody
    public Response<Page<SsqHistory>> history(SsqHistoryQuery query) {
        if (DataUtil.isEmpty(query.getPage())) {
            query.setPage(0);
        }
        query.setLimit(10);
        Pageable pageable = PageRequest.of(query.getPage(), query.getLimit());
        Page<SsqHistory> page = SsqHistoryService.queryPage(query, pageable);
        List<SsqHistory> voList = new ArrayList<>();
        for (SsqHistory his : page.getContent()) {
            voList.add(his);
        }
        Page<SsqHistory> voPage = new PageImpl<>(voList, pageable, page.getTotalElements());
        return Response.ok(voPage);
    }
    @RequestMapping(value = "getHistory")
    @ResponseBody
    public Response<String> getHistory(SsqHistoryQuery query) {
        if(DataUtil.isEmpty(query.getRed1())&&DataUtil.isEmpty(query.getRed2())&&DataUtil.isEmpty(query.getRed3())&&DataUtil.isEmpty(query.getRed4())&&DataUtil.isEmpty(query.getRed5())&&DataUtil.isEmpty(query.getRed6())&&DataUtil.isEmpty(query.getBlue())){
            return Response.ok("最少输入一个号码!");
        }
        Long num= SsqHistoryService.queryCount(query);
        if(num==0){
            return Response.ok("历史未出过此号!");
        }else{
            return Response.ok("历史已出过"+num+"次!");
        }
    }

    @RequestMapping(value = "getHistoryTimes")
    @ResponseBody
    public Response<List<KeyValue>> getHistoryTimes(Integer count) {
        List<Map<String, Object>> value = SsqHistoryService.getValue(count);
        List<KeyValue> list=new ArrayList<>();
        for(Map map:value){
            list.add(getDto(map));
        }
        return Response.ok(list);
    }

    private KeyValue getDto(Map map) {
        KeyValue vo=new KeyValue();
        if(map.get("ball")!=null){
            vo.setKey((String)map.get("ball"));
        }
        if(map.get("count")!=null){
            vo.setValue(String.valueOf(map.get("count")));
        }
        return vo;
    }


    @RequestMapping(value = "blueThree")
    @ResponseBody
    public Response<List<String>> blueThree(Integer count) {
        List<SsqHistory> list = SsqHistoryService.queryList(new SsqHistory());
        List<String > blue=new ArrayList<>();
        for(int j=1;j<=16;j++){
            if(String.valueOf(j).length()<2){
                blue.add("0"+String.valueOf(j));
            }else{
                blue.add(String.valueOf(j));
            }
        }
        for (SsqHistory query:list){
           for(int i=0;i<blue.size();i++){
               if(query.getBlue().equals(blue.get(i))){
                   blue.remove(query.getBlue());
                   break;
               }
           }
           if(blue.size()==count){
               break;
           }
        }
        return Response.ok(blue);
    }

    @RequestMapping(value = "saveImg")
    @ResponseBody
    public Response<String> saveImg(String url,String term) {
        RequestContext.RequestUser loginUser = RequestContext.getCurrentUser();
        if(DataUtil.isEmpty(url)||DataUtil.isEmpty(term)){
            return Response.fail("参数不完整!");
        }
        SsqImg save=new SsqImg();
        save.setTerm(term);
        save.setCreateTime(new Date());
        try {
            //钉钉群推送消息
            String urlNew=imgPrefix+uploadService.moveInformFile(url);
            DingDingTalkUtils.sendDingDingMsg(loginUser.getNickName()+"购买了:"+term+"期彩票,彩票照片地址:"+urlNew);
            save.setUrl(urlNew);
            SsqImgService.insert(save);
        } catch (Exception e) {
            log.error("保存异常,{}", term);
            return Response.fail("上传异常!");
        }
        return Response.ok("上传成功!");
    }

    @RequestMapping(value = "changeBlue")
    @ResponseBody
    public Response<String> changeBlue() {
        List<SsqQuick> my=this.SsqQuick().getData();
        Response<List<String>> listResponse = this.blueThree(my.size());
        List<String> list=listResponse.getData();
        for(int i=0; i<my.size();i++){
            my.get(i).setBlue(list.get(i));
            ssqQuickService.updateById(my.get(i));
        }
        return Response.ok("同步成功");
    }

    //加入购买提醒
    @RequestMapping(value = "mindBuySsq")
    @ResponseBody
    @Scheduled(cron = "0 0 18 * * ?")
    public void mindBuySsq() throws Exception {
        Calendar cal = Calendar.getInstance();
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //cal.setTime(sdf.parse("2021-03-14"));
        cal.setTime(new Date());
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        //星期天轮到tomcat购买 星期六或星期天提醒
        if(w==0||w==6){
            //首先获取此时的购买期数
            String ssqNum = WeekUtils.getSsqNum(null);
            //查找彩票图片地址是否有此期
            SsqImg query=new SsqImg();
            query.setTerm(ssqNum);
            Long aLong = SsqImgService.queryCount(query);
            //未查到 及未购买
            if(aLong<1){
                DingDingTalkUtils.sendDingDingMsg("小伙子,你五百万的项目完工了么?你睡得着觉,吃的下饭?");
                //发送短信
                SMSUtil.sendSMS("18627228022", "五百万的项目", 826585);
            }
        }

    }
}
