package com.wusi.reimbursement.controller;

import com.alibaba.fastjson.JSONObject;
import com.wusi.reimbursement.aop.SysLog;
import com.wusi.reimbursement.common.Response;
import com.wusi.reimbursement.common.ratelimit.anonation.RateLimit;
import com.wusi.reimbursement.config.JmsMessaging;
import com.wusi.reimbursement.config.SendMessage;
import com.wusi.reimbursement.entity.*;
import com.wusi.reimbursement.query.LureFishGetQuery;
import com.wusi.reimbursement.query.LureShoppingQuery;
import com.wusi.reimbursement.service.*;
import com.wusi.reimbursement.utils.*;
import com.wusi.reimbursement.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ Description   :  Lure Controller
 * @ Author        :  wusi
 * @ CreateDate    :  2022/6/2$ 11:05$
 */
@RestController
@Slf4j
public class LureController {
    @Autowired
    private LureShoppingService lureShoppingService;
    final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private JdbcTemplate jdbcTemplate;
    static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
    static SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
    static SimpleDateFormat sdf2 = new SimpleDateFormat(DateUtil.PATTERN_YYYY_MM_DD);
    @Autowired
    private LureFishKindService lureFishKindService;
    @Autowired
    private LureService lureService;
    @Autowired
    private LureFishGetService lureFishGetService;
    @Autowired
    private WaterLevelService waterLevelService;
    @Autowired
    private WeatherService weatherService;

    @RequestMapping(value = "api/saveLureSpend", method = RequestMethod.POST)
    @ResponseBody
    @SysLog("保存路亚消费项")
    @RateLimit(permitsPerSecond = 0.2, ipLimit = true, description = "限制增加频率")
    public Response<String> save(LureShopping spendList) throws Exception {
        RequestContext.RequestUser loginUser = RequestContext.getCurrentUser();
        if (!StringUtils.isNumeric(spendList.getPrice())) {
            return Response.fail("金额填数字,你个憨批~");
        }
        LureShopping shopping = getSpend(spendList, loginUser);
        lureShoppingService.insert(shopping);
        SendMessage.sendMessage(JmsMessaging.IMG_BACK_MESSAGE, JSONObject.toJSONString(shopping));
        return Response.ok("路亚毁一生!");
    }

    private LureShopping getSpend(LureShopping spendList, RequestContext.RequestUser user) throws ParseException {
        LureShopping spend = new LureShopping();
        spend.setId(spendList.getId());
        spend.setItem(spendList.getItem());
        spend.setPrice(spendList.getPrice());
        spend.setDate(new Date());
        spend.setRemark(spendList.getRemark());
        spend.setUrl(spendList.getUrl());
        spend.setUid(user.getUid());
        spend.setUserName(user.getNickName());
        spend.setRecommend(spendList.getRecommend());
        return spend;
    }

    //退单
    @RequestMapping(value = "api/refundLure")
    @SysLog("路亚消费退单")
    public Response<String> refundMoney(String amount, Long id) {
        boolean b = MoneyUtil.judgeMoney("0", amount);
        if (b) {
            return Response.fail("请输入正数!");
        }
        if (DataUtil.isEmpty(id)) {
            return Response.fail("参数不完整!");
        }
        LureShopping spend = lureShoppingService.queryById(id);
        if (DataUtil.isEmpty(amount)) {
            spend.setPrice("0");
            spend.setRemark(spend.getRemark() + "-全额退款");
        } else {
            if (!MoneyUtil.judgeMoney(spend.getPrice(), amount)) {
                return Response.fail("退款金额超过支付金额,你想白嫖??!!");
            }
            if (MoneyUtil.judgeMoney(amount, spend.getPrice())) {
                spend.setRemark(spend.getRemark() + "-全额退款");
            } else {
                spend.setRemark(spend.getRemark() + "-部分退款");
            }
            spend.setPrice(MoneyUtil.subtract(spend.getPrice(), amount));
        }
        lureShoppingService.updateById(spend);
        return Response.ok("路亚毁一生!");
    }


    @ResponseBody
    @RequestMapping(value = "api/lureShoppingList")
    @SysLog("查看路亚消费项")
    public Response<Page<SpendList>> homeworkList(LureShoppingQuery query) {
        if (DataUtil.isEmpty(query.getPage())) {
            query.setPage(0);
        }
        query.setLimit(1);
        if(query.getRecommend()!=null&&query.getRecommend()==0){
            query.setUid(RequestContext.getCurrentUser().getUid());
            query.setRecommend(null);
        }
        Pageable pageable = PageRequest.of(query.getPage(), query.getLimit());
        Page<LureShopping> page = lureShoppingService.queryPage(query, pageable);
        List<SpendList> voList = new ArrayList<>();
        for (LureShopping lure : page.getContent()) {
            voList.add(getLureList(lure));
        }
        Page<SpendList> voPage = new PageImpl<>(voList, pageable, page.getTotalElements());
        return Response.ok(voPage);
    }

    private SpendList getLureList(LureShopping lure) {
        SpendList vo = new SpendList();
        vo.setRemark(lure.getRemark());
        vo.setUrl(lure.getUrl());
        vo.setItem(lure.getItem());
        vo.setDate(sdf.format(lure.getDate()));
        vo.setPrice(lure.getPrice());
        vo.setId(lure.getId());
        vo.setConsumer(lure.getUserName());
        return vo;
    }

    @RequestMapping(value = "api/delLure", method = RequestMethod.POST)
    @ResponseBody
    @SysLog("删除消费项")
    public Response<String> del(SpendList query) {
        try {
            lureShoppingService.delById(query.getId());
            return Response.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.fail("删除失败!!!");
    }


    @RequestMapping(value = "api/deleteFish", method = RequestMethod.POST)
    @ResponseBody
    @SysLog("删除鱼获")
    public Response<String> deleteFish(SpendList query) {
        try {
            lureFishGetService.deleteById(query.getId());
            return Response.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.fail("删除失败!!!");
    }
    @RequestMapping(value = "api/lureMonthSpend")
    public Response<String> monthSpend() {
        RequestContext.RequestUser loginUser = RequestContext.getCurrentUser();
        String monthStr = format.format(new Date());
        String sql = "select sum(price)  as s from lure_shopping where  uid ='" + loginUser.getUid() + "';";
        List<Map<String, Object>> map = jdbcTemplate.queryForList(sql);
        BigDecimal total = (BigDecimal) map.get(0).getOrDefault("s", 0);
        if (DataUtil.isEmpty(total)) {
            total = new BigDecimal("0");
        }
        return Response.ok(String.valueOf(total));
    }

    @RequestMapping(value = "api/getFishList")
    @SysLog("查看路亚鱼获")
    public Response<List<JSONObject>> getFishList() {
        List<LureFishKind> lureFishKinds = lureFishKindService.queryList(new LureFishKind());
        List<JSONObject> list = new ArrayList<>();
        for (int i = 1; i <= lureFishKinds.size(); i++) {
            SelectionFish fish = new SelectionFish();
            fish.setId(i);
            fish.setName(lureFishKinds.get(i - 1).getName());
            list.add(JSONObject.parseObject(JSONObject.toJSONString(fish)));
        }
        return Response.ok(list);
    }

    @RequestMapping(value = "api/getLureList")
    public Response<List<JSONObject>> getLureList() {
        List<Lure> lureList = lureService.queryList(new Lure());
        List<JSONObject> list = new ArrayList<>();
        for (int i = 1; i <= lureList.size(); i++) {
            SelectionFish fish = new SelectionFish();
            fish.setId(i);
            fish.setName(lureList.get(i - 1).getLure());
            list.add(JSONObject.parseObject(JSONObject.toJSONString(fish)));
        }
        return Response.ok(list);
    }

    @RequestMapping(value = "api/saveFish")
    @SysLog("保存中鱼或打龟数据")
    public Response<String> saveFish(SaveFish saveFish) throws Exception {
        RequestContext.RequestUser loginUser = RequestContext.getCurrentUser();
        Weather weather = null;
        if (DataUtil.isNotEmpty(saveFish.getLng()) && DataUtil.isNotEmpty(saveFish.getLat())&&!"null".equals(saveFish.getLat())) {
            weather = WeatherUtils.getWeather(saveFish.getLng() + "," + saveFish.getLat());
        }else{
            weather=weatherService.queryByDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        }
        LureFishGet data = new LureFishGet();
        data.setFishKind(saveFish.getFishKind());
        data.setWeight(saveFish.getWeight());
        data.setLength(saveFish.getLength());
        data.setLure(saveFish.getLure());
        data.setProvince(saveFish.getProvince());
        data.setCity(saveFish.getCity());
        data.setDistrict(saveFish.getDistrict());
        data.setLng(saveFish.getLng());
        data.setLat(saveFish.getLat());
        data.setAddress(saveFish.getAddress());
        data.setImageUrl(saveFish.getUrl());
        data.setRemark(saveFish.getRemark());
        data.setUse(saveFish.getEatFish());
        data.setGetFish((DataUtil.isNotEmpty(saveFish.getGetFish()) && saveFish.getGetFish() == 0) ? 0 : 1);
        if (DataUtil.isNotEmpty(weather)) {
            data.setCondTxtDay(weather.getCondTxtDay());
            data.setCondTxtNight(weather.getCondTxtNight());
            data.setHum(weather.getHum());
            data.setPres(weather.getPres().toString());
            data.setSunRise(weather.getSunRise());
            data.setSunSet(weather.getSunSet());
            data.setTmpMax(weather.getTmpMax());
            data.setTmpMin(weather.getTmpMin());
            data.setWindDir(weather.getWindDir());
            data.setWindDc(weather.getWindSc());
            data.setWindSpd(weather.getWindSpd());
            data.setCreateTime(new Date());
        }
        data.setUid(loginUser.getUid());
        data.setUserName(loginUser.getNickName());
        try {
            //同一人一天不能增加两个打龟记录
            if(data.getGetFish() == 0){
                LureFishGetQuery query = new LureFishGetQuery();
                query.setUid(loginUser.getUid());
                query.setTime(format2.format(new Date()));
                query.setGetFish(0);
                Long aLong = lureFishGetService.queryCount(query);
                if (aLong > 0) {
                    return Response.fail("一天打一次龟还不够？");
                }
                query.setGetFish(1);
                Long bLong = lureFishGetService.queryCount(query);
                if (bLong > 0) {
                    return Response.fail("有鱼获了，怎么还打龟？");
                }
            }
            //处理先增加打龟记录再增加鱼获
            if(data.getGetFish() == 1){
                LureFishGetQuery query = new LureFishGetQuery();
                query.setUid(loginUser.getUid());
                query.setTime(format2.format(new Date()));
                query.setGetFish(0);
                LureFishGet fishGet = lureFishGetService.queryOne(query);
                if (DataUtil.isNotEmpty(fishGet)) {
                    lureFishGetService.deleteById(fishGet.getId());
                }
            }
            if((DataUtil.isNotEmpty(saveFish.getNum())&&saveFish.getNum()==1)||(DataUtil.isNotEmpty(saveFish.getGetFish())&&saveFish.getGetFish()==0)){
                lureFishGetService.insert(data);
            }else{
                List<LureFishGet> getList=new ArrayList<>();
                for (int i=0;i<saveFish.getNum();i++){
                    getList.add(data);
                }
                lureFishGetService.insertBatch(getList);
            }
            String result = (DataUtil.isNotEmpty(saveFish.getGetFish()) && saveFish.getGetFish() == 0) ? "打龟了!" : "中鱼了!";
            DingDingTalkUtils.sendDingDingMsg(loginUser.getNickName() + result);
        } catch (Exception e) {
            return Response.fail("添加异常");
        }
        return Response.ok("添加成功");
    }

    @ResponseBody
    @RequestMapping(value = "api/lureGetList")
    @SysLog("鱼获列表")
    public Response<Page<LureFishGetVo>> lureGetList(LureFishGetQuery query) {
        if (DataUtil.isEmpty(query.getPage())) {
            query.setPage(0);
        }
        query.setLimit(1);
        query.setUid(RequestContext.getCurrentUser().getUid());
        query.setPage(query.getLimit() * (query.getPage()));
        Pageable pageable = PageRequest.of(query.getPage(), query.getLimit());
        Page<LureFishGet> page = lureFishGetService.queryPage(query, pageable);
        List<LureFishGetVo> voList = new ArrayList<>();
        for (LureFishGet lure : page.getContent()) {
            voList.add(getLureFishList(lure,RequestContext.getCurrentUser().getUid()));
        }
        Page<LureFishGetVo> voPage = new PageImpl<>(voList, pageable, page.getTotalElements());
        return Response.ok(voPage);
    }

    private LureFishGetVo getLureFishList(LureFishGet lure,String uid) {
        LureFishGetVo vo = new LureFishGetVo();
        vo.setFishKind(lure.getFishKind());
        vo.setWeight(lure.getWeight() + "斤");
        vo.setLure(lure.getLure());
        vo.setTmp(lure.getTmpMin() + "℃-" + lure.getTmpMax() + "℃");
        vo.setId(lure.getId());
        vo.setImageUrl(lure.getImageUrl());
        vo.setPres(lure.getPres() + "hPa");
        vo.setCond(lure.getCondTxtDay() + "-" + lure.getCondTxtNight());
        vo.setCreateTime(sdf.format(lure.getCreateTime()));
        vo.setRemark(lure.getRemark());
        //vo.setAddress(lure.getAddress());
        vo.setAddress(DataUtil.isEmpty(lure.getAddress())?"大师不愿意透露地址":lure.getAddress());
        vo.setLength(lure.getLength() + "cm");
        String str = sdf2.format(lure.getCreateTime());
        WaterLevel level = waterLevelService.queryByTime(str);
        vo.setWaterLevel(level == null ? "暂无数据" : DataUtil.isEmpty(level.getWaterLevel())?"暂无数据":  level.getWaterLevel()+ "米");
        if (DataUtil.isNotEmpty(lure.getUse())) {
            vo.setUse(lure.getUse() == 1 ? "放油" : "放流");
        }
        if (lure.getGetFish() == 1) {
            vo.setNum(lureFishGetService.getnum(str,uid));
            vo.setDesc("中鱼" + lureFishGetService.getnum(str,uid) + "条");
        } else {
            vo.setDesc("打龟");
        }
        vo.setWeek(DateUtil.getWeekOfDate(lure.getCreateTime()));
        return vo;
    }


    @RequestMapping(value = "/api/fishCount")
    @ResponseBody
    @SysLog("统计鱼获")
    public Response<List<FishCount>> getFishCount() {
        return Response.ok(lureFishGetService.fishCount(RequestContext.getCurrentUser().getUid(), null));
    }


    @RequestMapping(value = "/api/monthCount")
    @ResponseBody
    @SysLog("按月份统计鱼获")
    public Response<List<MonthCount>> getMonthCount() {
        List<MonthCount> monthCount = lureFishGetService.getMonthCount(RequestContext.getCurrentUser().getUid());
        for (MonthCount count : monthCount) {
            List<FishCount> fishCounts = lureFishGetService.fishCount(RequestContext.getCurrentUser().getUid(), count.getMonth());
            count.setFishCount(fishCounts);
        }
        return Response.ok(monthCount);
    }

    @RequestMapping(value = "/api/monthData")
    @ResponseBody
    @SysLog("月度数据")
    public Response<List<MonthData>> getMonthData() {
        List<MonthData> monthCount = lureFishGetService.getMonthData(RequestContext.getCurrentUser().getUid());
        return Response.ok(monthCount);
    }

    @RequestMapping(value = "/api/yearData")
    @ResponseBody
    @SysLog("年度数据")
    public Response<List<MonthData>> yearData() {
        List<MonthData> monthCount = lureFishGetService.getYearData(RequestContext.getCurrentUser().getUid());
        return Response.ok(monthCount);
    }

    @RequestMapping(value = "/api/geti")
    @ResponseBody
    @SysLog("个体纪录查看")
    public Response<List<Geti>> geti(String type) {
        RequestContext.RequestUser loginUser = RequestContext.getCurrentUser();
        List<Geti> monthCount = lureFishGetService.getGeti(loginUser.getUid(),type);
        return Response.ok(monthCount);
    }

    @RequestMapping(value = "/api/sort")
    @ResponseBody
    public Response<List<PaiHang>> sort(String type) {
        List<PaiHang> monthCount = lureFishGetService.paiHang(type);
        return Response.ok(monthCount);
    }

    @RequestMapping(value = "/api/fishShare")
    @ResponseBody
    @SysLog("查看鱼获共享")
    public Response<Page<FishShare>> fishShare(LureFishGetQuery query) throws Exception {
        if (DataUtil.isEmpty(query.getPage()) || query.getPage() == 0) {
            DingDingTalkUtils.sendDingDingMsg(RequestContext.getCurrentUser().getNickName() + "查看共享鱼获！");
        }
        if (DataUtil.isEmpty(query.getPage())) {
            query.setPage(0);
        }
        query.setLimit(1);
        query.setNotUid((RequestContext.getCurrentUser().getUid()));
        query.setPage(query.getLimit() * (query.getPage()));
        Pageable pageable = PageRequest.of(query.getPage(), query.getLimit());
        Page<LureFishGet> page = lureFishGetService.queryPage(query, pageable);
        List<FishShare> voList = new ArrayList<>();
        for (LureFishGet lure : page.getContent()) {
            voList.add(getFishShareList(lure));
        }
        Page<FishShare> voPage = new PageImpl<>(voList, pageable, page.getTotalElements());
        return Response.ok(voPage);
    }

    private FishShare getFishShareList(LureFishGet lure) {
        FishShare share = new FishShare();
        share.setKind(lure.getFishKind());
        share.setLure(lure.getLure());
        share.setSize(lure.getLength() + "厘米-" + lure.getWeight() + "斤");
        share.setUrl(lure.getImageUrl());
        share.setName(lure.getUserName());
        return share;
    }


    @RequestMapping(value = "/api/fishDayCount")
    @ResponseBody
    @SysLog("中鱼天数计算")
    public Response<List<FishDayCount>> fishDayCount() {
        RequestContext.RequestUser loginUser = RequestContext.getCurrentUser();
        List<FishDayCount> monthCount = lureFishGetService.FishDayCount(loginUser.getUid());
        return Response.ok(monthCount);
    }


    @RequestMapping(value = "/api/guiWang")
    @ResponseBody
    @SysLog("龟王数据")
    public Response<List<GuiWang>> guiWang(String type) {
        List<GuiWang> monthCount = lureFishGetService.guiWang(type);
        return Response.ok(monthCount);
    }

    @RequestMapping(value = "/api/spendPai")
    @ResponseBody
    @SysLog("花费排行")
    public Response<List<SpendPai>> spendPai() {
        List<SpendPai> monthCount = lureFishGetService.spendPai();
        return Response.ok(monthCount);
    }
}