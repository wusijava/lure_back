package com.wusi.reimbursement.controller;

import com.alibaba.fastjson.JSONObject;
import com.wusi.reimbursement.aop.SysLog;
import com.wusi.reimbursement.common.Response;
import com.wusi.reimbursement.common.ratelimit.anonation.RateLimit;
import com.wusi.reimbursement.config.JmsMessaging;
import com.wusi.reimbursement.config.SendMessage;
import com.wusi.reimbursement.entity.*;
import com.wusi.reimbursement.query.*;
import com.wusi.reimbursement.service.*;
import com.wusi.reimbursement.utils.*;
import com.wusi.reimbursement.vo.*;
import com.wusi.reimbursement.wx.dto.MsgApi;
import com.wusi.reimbursement.wx.impl.WxApiImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
import java.util.*;
import java.util.stream.Collectors;

import static com.alibaba.fastjson.JSONObject.*;

/**
 * @ Description   :  Lure Controller
 * @ Author        :  wusi
 * @ CreateDate    :  2022/6/2$ 11:05$
 */
@RestController
@Slf4j
@Api(tags = "路了个鸭")
public class LureController {
    @Autowired
    private LureShoppingService lureShoppingService;
    final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    final SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd HH:mm");
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
    private WeatherService weatherService;
    @Autowired
    private UserService userService;
    @Autowired
    private ShuiWenWaterLevelService shuiWenWaterLevelService;
    @Autowired
    private FishCommentService fishCommentService;
    @Autowired
    private CollectivityLureService collectivityLureService;
    @Autowired
    private WxApiImpl wxApi;

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
        SendMessage.sendMessage(JmsMessaging.IMG_BACK_MESSAGE, toJSONString(shopping));
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
        if (DataUtil.isEmpty(query.getLimit())) {
            query.setLimit(10);
        }
        if (query.getRecommend() != null && query.getRecommend() == 0) {
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
    @SysLog("获取鱼种")
    public Response<List<JSONObject>> getFishList() {
        List<LureFishKind> lureFishKinds = lureFishKindService.queryList(new LureFishKind());
        List<JSONObject> list = new ArrayList<>();
        for (int i = 1; i <= lureFishKinds.size(); i++) {
            SelectionFish fish = new SelectionFish();
            fish.setId(i);
            fish.setName(lureFishKinds.get(i - 1).getName());
            list.add(parseObject(toJSONString(fish)));
        }
        return Response.ok(list);
    }

    @RequestMapping(value = "api/userList")
    @SysLog("获取用户列表")
    public Response<List<JSONObject>> userList() {
        List<User> lureFishKinds = userService.queryList(new UserQuery());
        List<JSONObject> list = new ArrayList<>();
        for (int i = 1; i <= lureFishKinds.size(); i++) {
            SelectionFish fish = new SelectionFish();
            fish.setId(i);
            fish.setName(lureFishKinds.get(i - 1).getNickName());
            list.add(parseObject(toJSONString(fish)));
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
            list.add(parseObject(toJSONString(fish)));
        }
        return Response.ok(list);
    }

    @RequestMapping(value = "api/saveFish")
    @SysLog("保存中鱼或打龟数据")
    @RateLimit(permitsPerSecond = 0.2, ipLimit = true, description = "限制频率")
    public Response<String> saveFish(SaveFish saveFish) throws Exception {
        String traceId="";
        if(DataUtil.isNotEmpty(saveFish.getWxRemarkCode())){
            MsgApi s = wxApi.checkMsg(saveFish.getRemark(), 1,saveFish.getWxRemarkCode());
            if(!s.getResult().getSuggest().equals("pass")){
                return Response.fail("文字包含敏感字符，请修改！");
            }
            if(DataUtil.isNotEmpty(saveFish.getUrl())&&DataUtil.isNotEmpty(saveFish.getWxImgCode())){
                MsgApi imgREsult = wxApi.checkImg(saveFish.getUrl(), 1,saveFish.getWxImgCode());
                if(!imgREsult.getErrmsg().equals("ok")){
                    return Response.fail("图片包含敏感内容，请修改！");
                }
                if(DataUtil.isNotEmpty(imgREsult.getTrace_id())){
                    traceId=  imgREsult.getTrace_id();
                }
            }
        }
        Date date = saveFish.getDate() == null ? new Date() : new SimpleDateFormat("yyyy-MM-dd").parse(saveFish.getDate());
        if (DateUtil.isSameDay(date, new Date())) {
            date = new Date();
        }
        RequestContext.RequestUser loginUser = RequestContext.getCurrentUser();
        Weather weather = null;
        if (DataUtil.isNotEmpty(saveFish.getLng()) && DataUtil.isNotEmpty(saveFish.getLat()) && !"null".equals(saveFish.getLat())) {
            if (date.getTime() < new SimpleDateFormat("yyyy-MM-dd").parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())).getTime()) {
                weather = weatherService.queryByDate(new SimpleDateFormat("yyyy-MM-dd").format(date));
            } else {
                weather = WeatherUtils.getWeather(saveFish.getLng() + "," + saveFish.getLat());
            }
        } else {
            weather = weatherService.queryByDate(new SimpleDateFormat("yyyy-MM-dd").format(date));
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
        data.setIsRepeat(LureFishGet.IsRepeat.no.getCode());
        if(DataUtil.isNotEmpty(traceId)){
            data.setTraceId(traceId);
        }
        if(DataUtil.isNotEmpty(saveFish.getUrl())){
            data.setState(0);
        }else{
            data.setState(1);
        }
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
        }
        data.setCreateTime(date);
        data.setUid(loginUser.getUid());
        data.setUserName(loginUser.getNickName());
        try {
            //同一人一天不能增加两个打龟记录
            if (data.getGetFish() == 0) {
                LureFishGetQuery query = new LureFishGetQuery();
                query.setUid(loginUser.getUid());
                query.setTime(format2.format(date));
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
            if (data.getGetFish() == 1) {
                LureFishGetQuery query = new LureFishGetQuery();
                query.setUid(loginUser.getUid());
                query.setTime(format2.format(date));
                query.setGetFish(0);
                LureFishGet fishGet = lureFishGetService.queryOne(query);
                if (DataUtil.isNotEmpty(fishGet)) {
                    lureFishGetService.deleteById(fishGet.getId());
                }
            }
            if ((DataUtil.isNotEmpty(saveFish.getNum()) && saveFish.getNum() == 1) || (DataUtil.isNotEmpty(saveFish.getGetFish()) && saveFish.getGetFish() == 0)) {
                lureFishGetService.insert(data);
            } else {
                List<LureFishGet> getList = new ArrayList<>();
                getList.add(data);
                LureFishGet newFish = new LureFishGet();
                BeanUtils.copyProperties(data, newFish);
                for (int i = 0; i < saveFish.getNum() - 1; i++) {
                    newFish.setIsRepeat(LureFishGet.IsRepeat.yes.getCode());
                    getList.add(newFish);
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
        if (DataUtil.isEmpty(query.getLimit())) {
            query.setLimit(10);
        }
        query.setState(1);
        query.setUid(RequestContext.getCurrentUser().getUid());
        Pageable pageable = PageRequest.of(query.getPage(), query.getLimit());
        Page<LureFishGet> page = lureFishGetService.queryPage(query, pageable);
        List<LureFishGetVo> voList = new ArrayList<>();
        for (LureFishGet lure : page.getContent()) {
            voList.add(getLureFishList(lure, RequestContext.getCurrentUser().getUid()));
        }
        Page<LureFishGetVo> voPage = new PageImpl<>(voList, pageable, page.getTotalElements());
        return Response.ok(voPage);
    }

    private LureFishGetVo getLureFishList(LureFishGet lure, String uid) {
        LureFishGetVo vo = new LureFishGetVo();
        vo.setFishKind(lure.getFishKind());
        vo.setWeight(lure.getWeight() + "斤");
        vo.setLure(lure.getLure());
        vo.setTmp(lure.getTmpMin() + "℃-" + lure.getTmpMax() + "℃");
        vo.setId(lure.getId());
        vo.setImageUrl(DataUtil.isEmpty(lure.getImageUrl()) ? "http://www.picture.lureking.cn/temp/1/v4zz82.jpg" : lure.getImageUrl());
        vo.setPres(lure.getPres() + "hPa");
        vo.setCond(lure.getCondTxtDay() + "-" + lure.getCondTxtNight());
        vo.setCreateTime(sdf.format(lure.getCreateTime()));
        vo.setRemark(DataUtil.isEmpty(lure.getRemark()) ? "无备注" : lure.getRemark());
        vo.setAddress(DataUtil.isEmpty(lure.getAddress()) ? "大师不愿意透露地址" : lure.getAddress());
        vo.setLength(lure.getLength() + "cm");
        String str = sdf2.format(lure.getCreateTime());
        ShuiWenWaterLevel level = shuiWenWaterLevelService.queryByDate(str);
        vo.setWaterLevel(level == null ? "无历史数据" : getLevel(level) + "m");
        if (DataUtil.isNotEmpty(lure.getUse())) {
            vo.setUse(lure.getUse() == 1 ? "放油" : "放流");
        }
        if (lure.getGetFish() == 1) {
            vo.setNum(lureFishGetService.getnum(str, uid));
            vo.setDesc("中鱼" + lureFishGetService.getnum(str, uid) + "条");
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
        List<Geti> monthCount = lureFishGetService.getGeti(loginUser.getUid(), type);
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
        if (DataUtil.isEmpty(query.getPage())) {
            query.setPage(0);
        }
        if (DataUtil.isEmpty(query.getLimit())) {
            query.setLimit(10);
        }
        //query.setNotUid((RequestContext.getCurrentUser().getUid()));
        query.setIsRepeat(LureFishGet.IsRepeat.no.getCode());
        Pageable pageable = PageRequest.of(query.getPage(), query.getLimit());
        Page<LureFishGet> page = lureFishGetService.queryPage(query, pageable);
        List<LureFishGet> list = page.getContent();
        List<String> uid = list.stream().map(w -> w.getUid()).distinct().collect(Collectors.toList());
        List<User> users = userService.queryByUidList(uid);
        List<FishShare> voList = new ArrayList<>();
        for (LureFishGet lure : page.getContent()) {
            voList.add(getFishShareList(lure, users));
        }
        Page<FishShare> voPage = new PageImpl<>(voList, pageable, page.getTotalElements());
        return Response.ok(voPage);
    }

    private FishShare getFishShareList(LureFishGet lure, List<User> users) throws ParseException {
        FishShare share = new FishShare();
        share.setId(lure.getId());
        share.setKind(lure.getFishKind());
        share.setLure(lure.getLure());
        share.setSize(lure.getLength() + "厘米-" + lure.getWeight() + "斤");
        share.setUrl(lure.getImageUrl());
        share.setName(lure.getUserName());
        share.setUse(lure.getUse() == 2 ? "放流" : "放油");
        if (DataUtil.isNotEmpty(lure.getProvince())) {
            if (DataUtil.isEmpty(lure.getCity())) {
                share.setAdd(lure.getProvince());
            } else {
                share.setAdd(lure.getProvince() + "·" + lure.getCity());
            }
        }
        long days = DateUtil.daysBetween(lure.getCreateTime(), new Date());
        if (days == 0) {
            share.setTime("今天");
        } else {
            share.setTime(days + "天前");
        }
        share.setImg(users.stream().filter(w -> w.getUid().equals(lure.getUid())).collect(Collectors.toList()).get(0).getImg());
        share.setComment(fishCommentService.queryComment(lure.getId()));
        share.setZan(fishCommentService.queryZan(lure.getId()));
        share.setIsZan(share.getZan().contains(RequestContext.getCurrentUser().getNickName()));
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


    @RequestMapping(value = "/api/firstFish")
    @ResponseBody
    @SysLog("解锁鱼种")
    public Response<List<FirstFish>> firstFish() {
        RequestContext.RequestUser loginUser = RequestContext.getCurrentUser();
        List<FirstFish> monthCount = lureFishGetService.firstFish(loginUser.getUid());
        return Response.ok(monthCount);
    }

    @RequestMapping(value = "/api/getIndexWeatherAndWaterLevel")
    @ResponseBody
    @SysLog("获取首页天气及水位")
    public Response<IndexWeatherAndWaterLevelVo> getIndexWeatherAndWaterLevel(MiniAppIndex index) throws Exception {
        if (DataUtil.isNotEmpty(RedisUtil.get("getIndexWeatherAndWaterLevel"))) {
            log.error("获取首页天气及水位缓存");
            Object o = RedisUtil.get("getIndexWeatherAndWaterLevel");
            return Response.ok(JSONObject.parseObject((String) RedisUtil.get("getIndexWeatherAndWaterLevel"), IndexWeatherAndWaterLevelVo.class));
        }
        Weather weather = null;
        if (DataUtil.isEmpty(index.getLat()) || DataUtil.isEmpty(index.getLng())) {
            weather = weatherService.queryLastOne();
        } else {
            weather = WeatherUtils.getWeather(index.getLng() + "," + index.getLat());
        }
        IndexWeatherAndWaterLevelVo vo = new IndexWeatherAndWaterLevelVo();
        BeanUtils.copyProperties(weather, vo);
        ShuiWenWaterLevel shuiWenWaterLevel = shuiWenWaterLevelService.queryLastOne();
        vo.setWaterLevel(getLevel(shuiWenWaterLevel));
        vo.setDownOrUp(shuiWenWaterLevel.getDownOrUp());
        vo.setValue(shuiWenWaterLevel.getValue());
        RedisUtil.set("getIndexWeatherAndWaterLevel", JSONObject.toJSONString(vo), 1800);
        return Response.ok(vo);
    }

    private String getLevel(ShuiWenWaterLevel shuiWenWaterLevel) {
        List<ShuiWenWaterLevelJson> shuiWenWaterLevelJsons = parseArray(shuiWenWaterLevel.getData(), ShuiWenWaterLevelJson.class);
        List<ShuiWenWaterLevelJson> hankou = shuiWenWaterLevelJsons.stream().filter(w -> w.getStnm().equals("汉口")).collect(Collectors.toList());
        return MoneyUtil.formatMoney(hankou.get(0).getZ());
    }


    @RequestMapping(value = "/api/getIndexWeatherAndWaterLevelNow")
    @ResponseBody
    @SysLog("获取实时天气及水位")
    public Response<IndexWeatherAndWaterLevelVoNow> getIndexWeatherAndWaterLevelNow(MiniAppIndex index) throws Exception {
        if (DataUtil.isNotEmpty(RedisUtil.get("getIndexWeatherAndWaterLevelNow"))) {
            log.error("获取实时天气及水位缓存");
            Object o = RedisUtil.get("getIndexWeatherAndWaterLevelNow");
            return Response.ok(JSONObject.parseObject((String) RedisUtil.get("getIndexWeatherAndWaterLevelNow"), IndexWeatherAndWaterLevelVoNow.class));
        }
        IndexWeatherAndWaterLevelVoNow weather = null;
        if (DataUtil.isEmpty(index.getLat()) || DataUtil.isEmpty(index.getLng())) {
            weather = NewWeatherUtils.getWeather("114.186769,30.453926");
        } else {
            weather = NewWeatherUtils.getWeather(index.getLng() + "," + index.getLat());
        }
        IndexWeatherAndWaterLevelVoNow vo = new IndexWeatherAndWaterLevelVoNow();
        BeanUtils.copyProperties(weather, vo);
        ShuiWenWaterLevel shuiWenWaterLevel = shuiWenWaterLevelService.queryLastOne();
        vo.setWaterLevel(getLevel(shuiWenWaterLevel));
        vo.setDownOrUp(shuiWenWaterLevel.getDownOrUp());
        vo.setValue(shuiWenWaterLevel.getValue());
        RedisUtil.set("getIndexWeatherAndWaterLevelNow", JSONObject.toJSONString(vo), 1800);
        return Response.ok(vo);
    }

    @RequestMapping(value = "/api/xcxData")
    @ResponseBody
    @SysLog("获取小程序我的页面数据")
    public Response<MyMiniProData> myXcxData() {
        RequestContext.RequestUser loginUser = RequestContext.getCurrentUser();
        return Response.ok(lureFishGetService.myXcxData(loginUser.getUid()));
    }

    @RequestMapping(value = "/api/editFish")
    @ResponseBody
    @SysLog("修改鱼的尺寸大小")
    public Response<String> editFish(Long id, String weight, String length) {
        if (DataUtil.isEmpty(id)) {
            return Response.fail("缺少id!");
        }
        if (DataUtil.isEmpty(weight)) {
            return Response.fail("缺少重量!");
        }
        if (DataUtil.isEmpty(length)) {
            return Response.fail("缺少长度!");
        }
        try {
            lureFishGetService.editFish(id, weight, length);
        } catch (Exception e) {
            return Response.fail("修改异常");
        }
        return Response.ok("修改成功");
    }

    @RequestMapping(value = "/api/comment")
    @ResponseBody
    @SysLog("鱼友圈评论")
    public Response<List<FishCommentVo>> comment(Long fishId, Long replyId, String comment,String wxCode) {
        if (DataUtil.isEmpty(fishId) && DataUtil.isEmpty(replyId)) {
            return Response.fail("缺少id!");
        }
        if (DataUtil.isEmpty(comment)) {
            return Response.fail("评论不能为空!");
        }
        if(DataUtil.isNotEmpty(wxCode)){
            MsgApi s = wxApi.checkMsg(comment, 1,wxCode);
            if(!s.getResult().getSuggest().equals("pass")){
                return Response.fail("包含敏感字符，请修改！");
            }
        }
        fishCommentService.add(fishId, replyId, comment);
        return Response.ok(fishCommentService.queryComment(fishId));
    }

    @RequestMapping(value = "/api/delComment")
    @ResponseBody
    @SysLog("删除鱼友圈评论")
    public Response<String> delComment(Long id) {
        if (DataUtil.isEmpty(id)) {
            return Response.fail("缺少id!");
        }
        try {
            fishCommentService.deleteById(id);
        } catch (Exception e) {
            return Response.fail("删除失败");

        }
        return Response.ok("删除成功");
    }

    @RequestMapping(value = "/api/zan")
    @ResponseBody
    @SysLog("鱼友圈点赞")
    public Response<String> zan(Long id) {
        if (DataUtil.isEmpty(id)) {
            return Response.fail("缺少id!");
        }
        fishCommentService.zan(id);
        return Response.ok("点赞成功");
    }

    @RequestMapping(value = "/api/cancelZan")
    @ResponseBody
    @SysLog("鱼友圈点赞")
    public Response<String> cancelZan(Long id) {
        if (DataUtil.isEmpty(id)) {
            return Response.fail("缺少id!");
        }
        fishCommentService.cancelZan(id);
        return Response.ok("取消成功");
    }

    @RequestMapping(value = "/api/baoKou")
    @ResponseBody
    @SysLog("暴扣记录")
    public Response<List<BaoKouVo>> baoKou(Integer num) {
        if (DataUtil.isEmpty(num)) {
            return Response.fail("缺少条数!");
        }
        return Response.ok(lureFishGetService.baoKou(num, RequestContext.getCurrentUser().getUid()));
    }

    @RequestMapping(value = "/api/lureGet")
    @ResponseBody
    @SysLog("统计解锁路亚饵")
    public Response<List<FishCount>> lureGet() {
        return Response.ok(lureFishGetService.lureGet(RequestContext.getCurrentUser().getUid(), null));
    }

    @RequestMapping(value = "/api/createCollectivityLure")
    @ResponseBody
    @SysLog("创建集体活动")
    public Response<String> createCollectivityLure(CreateCollectivity data) {
        if(DataUtil.isNotEmpty(data.getWxCode())){
            MsgApi s = wxApi.checkMsg(data.getRemark()+data.getActivityName()+data.getAddress()+data.getSlogan(), 1,data.getWxCode());
            if(!s.getResult().getSuggest().equals("pass")){
                return Response.fail("包含敏感字符，请修改！");
            }
        }
        try {
            collectivityLureService.createCollectivity(RequestContext.getCurrentUser(), data);
        } catch (Exception e) {
            return Response.fail("创建失败");
        }
        return Response.ok("创建成功");
    }


    @RequestMapping(value = "/api/joinCollectivityLure")
    @ResponseBody
    @SysLog("加入集体活动")
    public Response<String> joinCollectivityLure(Long id) {
        try {
            collectivityLureService.joinCollectivity(id);
        } catch (Exception e) {
            return Response.fail("加入失败");
        }
        return Response.ok("加入成功");
    }

    @RequestMapping(value = "/api/quitCollectivityLure")
    @ResponseBody
    @SysLog("加入集体活动")
    public Response<String> quitCollectivityLure(Long id) {
        try {
            collectivityLureService.quitCollectivity(id);
        } catch (Exception e) {
            return Response.fail("退出失败");
        }
        return Response.ok("退出成功");
    }


    @ResponseBody
    @RequestMapping(value = "api/collectivityLureList")
    @SysLog("路亚活动列表")
    public Response<Page<CollectivityLureVo>> lureGetList(CollectivityLureQuery query) {
        if (DataUtil.isEmpty(query.getPage())) {
            query.setPage(0);
        }
        if (DataUtil.isEmpty(query.getLimit())) {
            query.setLimit(10);
        }
        Pageable pageable = PageRequest.of(query.getPage(), query.getLimit());
        Page<CollectivityLure> page = collectivityLureService.queryPage(query, pageable);
        List<CollectivityLureVo> voList = new ArrayList<>();
        for (CollectivityLure lure : page.getContent()) {
            voList.add(getCollectivity(lure));
        }
        Page<CollectivityLureVo> voPage = new PageImpl<>(voList, pageable, page.getTotalElements());
        return Response.ok(voPage);
    }

    private CollectivityLureVo getCollectivity(CollectivityLure lure) {
        CollectivityLureVo vo=new CollectivityLureVo();
        vo.setCreatorName(lure.getCreatorName());
        vo.setActivityName(lure.getActivityName());
        vo.setSlogan(lure.getSlogan());
        vo.setAddress(lure.getAddress());
        vo.setParticipantName(lure.getParticipantName());
        if(lure.getState()==-1){
            vo.setStateDesc("已关闭");
        }else{
            Integer days=DateUtil.daysBetween2(lure.getLureDate(), new Date());
            vo.setStateDesc(days>0?"已过期":"进行中");
            if(days<=0 &&RequestContext.getCurrentUser().getUid().equals(lure.getCreatorUid())){
                vo.setShowClose(Boolean.TRUE);
            }
            if(days<=0&&lure.getParticipantUid().contains(RequestContext.getCurrentUser().getUid())&&!RequestContext.getCurrentUser().getUid().equals(lure.getCreatorUid())){
                vo.setShowQuit(Boolean.TRUE);
            }
        }
        if(vo.getStateDesc().equals("进行中")&&!lure.getParticipantUid().contains(RequestContext.getCurrentUser().getUid())){
            vo.setShowJoin(true);
        }
        List<String> uids = Arrays.asList(lure.getParticipantUid().split(";"));
        List<String> names = Arrays.asList(lure.getParticipantName().split(";"));
        List<MonthRate> lureFishGets = lureFishGetService.queryByDateAndUidList(format2.format(lure.getLureDate()), uids);
        String fishDesc="";
        for (String name : names) {
            List<MonthRate> collect = lureFishGets.stream().filter(w -> w.getName().equals(name)).collect(Collectors.toList());
            fishDesc=fishDesc+name+"("+getFishDesc(collect)+");";
        }
        vo.setFishDesc(fishDesc);
        vo.setLureDate(day.format(lure.getLureDate()));
        vo.setCreateTime(sdf.format(lure.getCreateTime()));
        vo.setRemark(lure.getRemark());
        vo.setId(lure.getId());
        return vo;
    }

    private String getFishDesc(List<MonthRate> mr) {
        if(DataUtil.isEmpty(mr)){
            return "暂无记录";
        }
        if(mr.get(0).getGetFish()==0){
            return "打龟";
        }
        String desc="";
        List<String> fishKind = mr.stream().map(w -> w.getFishKind()).distinct().collect(Collectors.toList());
        for (String fish : fishKind) {
            long count = mr.stream().filter(w -> w.getFishKind().equals(fish)).count();
            desc=desc+fish+"×"+count+"、";
        }
        return desc.substring(0,desc.length()-1);
    }

    @ResponseBody
    @RequestMapping(value = "api/closeCollectivity")
    @SysLog("关闭群活动")
    public Response<String> lureGetList(Long id) {
        CollectivityLure collectivityLure = collectivityLureService.queryById(id);
        if(DataUtil.isEmpty(collectivityLure)){
            return Response.fail("id有误");
        }
        collectivityLure.setState(-1);
        collectivityLureService.updateById(collectivityLure);
        return  Response.ok("关闭成功");
    }

}
