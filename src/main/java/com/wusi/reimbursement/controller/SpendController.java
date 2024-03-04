package com.wusi.reimbursement.controller;

import com.alibaba.fastjson.JSONObject;
import com.wusi.reimbursement.aop.SysLog;
import com.wusi.reimbursement.client.EstablishRedPacketModel;
import com.wusi.reimbursement.client.EstablishRedPacketRequest;
import com.wusi.reimbursement.client.EstablishRedPacketResponse;
import com.wusi.reimbursement.client.ZcRequestClient;
import com.wusi.reimbursement.common.Response;
import com.wusi.reimbursement.common.ratelimit.anonation.RateLimit;
import com.wusi.reimbursement.config.JmsMessaging;
import com.wusi.reimbursement.config.SendMessage;
import com.wusi.reimbursement.entity.ExcelDto;
import com.wusi.reimbursement.entity.RequestContext;
import com.wusi.reimbursement.entity.Spend;
import com.wusi.reimbursement.query.SpendQuery;
import com.wusi.reimbursement.service.IMailService;
import com.wusi.reimbursement.service.SpendService;
import com.wusi.reimbursement.utils.*;
import com.wusi.reimbursement.vo.SpendList;
import com.wusi.reimbursement.vo.SpendVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ Description   :  消费类controller
 * @ Author        :  wusi
 * @ CreateDate    :  2020/1/21$ 15:46$
 */
@RestController
@Slf4j
public class SpendController {

    static Boolean testCheckSign = true;
    static String testCharset = "UTF-8";
    static String testSignType = "RSA2";
    static String testAppId = "TEST202005041652231204911";
    static String testPrivateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCwkNm9N8dG245pBa9zdmSWmAv8uljUDOTr5PywMDFPIuhMjOBAgBCuUdWDTzR3m35hQ/dAGQCC/JP+eyexe+ZpSfn0JdotvOAmNAkwcMX/MuQjtJ3pjuzTZiwW2NYUJvWcoxBNOBUYBR1jVTB3XQikrw33ij5NUQL2f67UfnmdQ8JrJ2CEIxTt5nx3DY+pIa/1FueGDm802/Lu1f9Wj2e4wIBjHPWwPYgmv03zTFmxms3oIJRUq6IJZpHW17VIBJomyw8ohJOZBrMy0SX2k5of16dp346hpYNTprJyzLkgvHO8jodzmLFgGyTDtkCs8LeksDTmmjfnCYaybFHZ2t03AgMBAAECggEBAJtcOVs3c08z7ZEXgZQ1PrkLvLB6P6MGXX/7kyRso1agvoptAv1+Mi9QrnDGBsKfvYpURYDO/xZCrO4k635OKSXIA/oCPII2SX0UGGnZMT8mRnvsd26FZnl006Ke1XAR/9f922A20si552v6D5VX9T0DE2UW7U8W02aWXv129AedlcGQqCtIkcjHkvzosC08fTMT7LvTuUjHj7bHOD9UoPKBjWJidsRwwAov/b27WFOxqkIVxDWD7QhSSmqPJPQ4VP5vXi+N9YNS5cS1qH8AmUxvddWmvMSmdDSWxHQmMgvsduSZ0s3P67l2mtra3wI5mZkbX6saokI0/QaweX87c6ECgYEA/ZQEYHtwC9rTM5DcS610JFjIxtWH1QNTLKRwMp2rmNG4busZFXXxlsdeyPfyglt1uXdCMWZA9T9/oCJJTpv55UFBKsMbgDGd22SBo3KhfRW1J81FFrKZJo50Pw7Q8hpKorxTUWf858ztn/9rMcvSBJZoleoVjLQjPOtb9SsGwNkCgYEAskCLAa+JOIi9KYmUevpsTXhplN5fmiENN1d/nUxutug6WVDOsjbWCk8H+zG8tJSiHndQhWJIiKtBfGwtzYRbbTBQRoK+veVupRMdQoPDaPmCq+ldwQRnsaVsudta3hv7akT524dJLEqMZ28TlaK5+uYyakxIhK3hykNOVauHxI8CgYBDuyKEJtRhxjw9fMbqy9TG1JQkT+qtIes4dF+nlWe9NN8/eTpE+jDiZjRSF7BF02oZdNpQWZCmMSTEwAO2pIDWFFz0sBKLZjVU4X6jCr5Jq9+sVu7KRkAUBV7VbP6wfAdNemICz3TE8X7TCqU2MsvQ/9/FkzXYVFFJN1Bjpu9x2QKBgQCbNZoUYK1iPaZAidxw7KbrGgMNLkgeY9MnMhgbdlcdCr7r0HH6OcdL+J7heeBveNBlKM1DJ14zKrN9zJBhWHNnct8jVmsR3LnoIOmkZij7ue6vFCefjt9fjsRKXRcVOVZEpUTOg1ESsOLqmYx7CdNZBaI2bq/iX6mwPcTy0cYJPwKBgDR7lDxsyLnRC7MJvJGMDT8obOa0HJ7Ux5e7swId7+AZVc8XjRIFiD0uUVSbzE+5VQWTCUWC/kad4yuTmC3M+0ZOWjLGdj+a2p00TCcJYUBYdNsE6u5/CxW+ANMEmi3FitZEpyd8dK/SCQjYXE/KqmV9e0erUPfVCdT2VyUDTuOY";
    static String testPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmKb38IjsfoxMpUBQKd378cKNf1oB6djX8C1Zui1+RncUCKTuwkbztKLQmb+I72ntpJraF7n+lAOCLGGtqpVIDjJK7TR3bGKnWcFwKzkEiIb5jcvlytYsF9u3NnNmlQ+ejHLMAS6jXiV26qA1wqKwtSzlA+DBgCVpEsOXZJ7UlqUq5Y8xePAFkPLzgWIt+MIx/dO7dQThwFc2dg3M7qeoTOVMlXtVskXOMaNxvPPwtpehSggnkkaF9QMChzb4+6ASly8gBLel70w/ZJabm91EANV9T92PSjTKaDHHIxdrgPdcC4VTxF7tfvzPkGyQ3TcWE4I092IZagcM/gXmWQI27wIDAQAB";

    static String url = "http://127.0.0.1:8884/gateway.do";


    static ZcRequestClient client = new ZcRequestClient(testAppId, testCharset, testSignType, testPrivateKey, testPublicKey, url, testCheckSign);
    @Autowired
    SpendService spendService;

    @Value("${excelDownloadUrl}")
    private String excelDownloadUrl;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private IMailService mailService;

    @RequestMapping("spendList")
    @ResponseBody
    @SysLog("开支列表")
    public Response<Page<SpendList>> productList(SpendQuery query) {
        if (DataUtil.isEmpty(query.getPage())) {
            query.setPage(0);
        }
        if (DataUtil.isEmpty(query.getLimit())) {
            query.setLimit(18);
        }
        Pageable pageable = PageRequest.of(query.getPage(), query.getLimit());
        Page<Spend> page = spendService.queryPage(query, pageable);
        //重新封装一个query 不带分页的query
        SpendQuery newQuery = new SpendQuery();
        newQuery.setItem(query.getItem());
        newQuery.setStartTime(query.getStartTime());
        newQuery.setEndTime(query.getEndTime());
        newQuery.setConsumer(query.getConsumer());
        List<Spend> List = spendService.queryList(newQuery);
        List<SpendList> volist = new ArrayList<>();
        for (Spend spend : page.getContent()) {
            volist.add(getVo(spend));
        }
        BigDecimal total = new BigDecimal("0.00");
        BigDecimal personTotal = new BigDecimal("0.00");
        for (Spend spend : List) {
            BigDecimal bigDecimal = new BigDecimal(spend.getPrice());
            total = bigDecimal.add(total);
        }
        for (SpendList spendlist : volist) {
            BigDecimal bigDecimal = new BigDecimal(spendlist.getPrice());
            personTotal = bigDecimal.add(personTotal);
        }
        for (SpendList spendList : volist) {
            spendList.setTotal(total.toString());
            spendList.setPersonTotal(personTotal.toString());
        }
        Page<SpendList> vopage = new PageImpl<>(volist, pageable, page.getTotalElements());
        return Response.ok(vopage);
    }

    private SpendList getVo(Spend spend) {
        SpendList spendList = new SpendList();
        spendList.setId(spend.getId());
        spendList.setItem(spend.getItem());
        spendList.setPrice(spend.getPrice());
        spendList.setConsumer(spend.getConsumer());
        spendList.setDate(DateUtil.formatDate(spend.getDate(), DateUtil.PATTERN_YYYY_MM_DD_HH_MM_SS));
        if (DataUtil.isEmpty(spend.getRemark())) {
            spendList.setRemark("暂无");
        } else {
            spendList.setRemark(spend.getRemark());
        }
        if (DataUtil.isEmpty(spend.getUrl())) {
            spendList.setUrl("http://www.photo.wearelie.com/temp/1/4yg16z/{2}.jpg");
        } else {
            spendList.setUrl(spend.getUrl());
        }

        return spendList;
    }

    @RequestMapping(value = "/spendDetail", method = RequestMethod.POST)
    @ResponseBody
    @SysLog("开支明细")
    public Response<SpendList> todetails(SpendQuery query) {
        Spend Spend = spendService.queryOne(query);
        return Response.ok(getVo(Spend));
    }

    @RequestMapping(value = "/spendUpdate", method = RequestMethod.POST)
    @ResponseBody
    @SysLog("更新消费明细")
    public Response<String> update(SpendList query) throws ParseException {
        System.out.println(query);
        Spend spend = getSpend(query);
        spendService.updateById(spend);
        return Response.ok("ok");
    }

    private Spend getSpend(SpendList spendList) throws ParseException {
        Spend spend = new Spend();
        spend.setId(spendList.getId());
        spend.setItem(spendList.getItem());
        spend.setPrice(spendList.getPrice());
        spend.setConsumer(spendList.getConsumer());
        /*SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date buydate=simpleDateFormat.parse(spendList.getDate());*/
        spend.setDate(new Date());
        spend.setRemark(spendList.getRemark());
        spend.setUrl(spendList.getUrl());
        return spend;
    }

    @RequestMapping(value = "/spendDel", method = RequestMethod.POST)
    @ResponseBody
    @SysLog("删除消费项")
    public Response<String> del(SpendList query) {
        try {
            spendService.delById(query.getId());
            return Response.ok("删除SUCCESS!!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.fail("删除失败!!!");
    }

    //saveSpend
    @RequestMapping(value = "/saveSpend", method = RequestMethod.POST)
    @ResponseBody
    @SysLog("保存消费项")
    @RateLimit(permitsPerSecond = 0.2, ipLimit = true, description = "限制导出频率")
    public Response<String> save(SpendList spendList) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(!StringUtils.isNumeric(spendList.getPrice())){
            return Response.fail("金额填数字,你个憨批~");
        }
        Spend spend = getSpend(spendList);
        spendService.insert(spend);
        //图片备份  同步较慢  改用mq
        SendMessage.sendMessage(JmsMessaging.IMG_BACK_MESSAGE, JSONObject.toJSONString(spend));

        //红包创建平台创建红包
        /*EstablishRedPacketRequest request = new EstablishRedPacketRequest();
        request.setModel(registerModel(spendList));
        EstablishRedPacketResponse response = client.execute(request);*/
        DingDingTalkUtils.sendDingDingMsg(spend.getConsumer()+"消费了"+spend.getItem()+",金额:"+spend.getPrice()+",备注:"+spend.getRemark()+",消费时间:"+sdf.format(new Date()));
        return Response.ok("添加成功!");
    }

    @RequestMapping(value = "out", method = RequestMethod.POST)
    @ResponseBody
    public Response<String> batchExport(SpendQuery query) {
        List<Spend> spendList = spendService.queryList(query);
        List<SpendVo> voList = new ArrayList<>();
        int index = 1;
        for (Spend spend : spendList) {
            SpendVo spendVo = getSpendVo(spend);
            spendVo.setIndex(index);
            voList.add(spendVo);
            index++;
        }
        ExcelDto dto = new ExcelDto();
        dto.setHeaders(SpendVo.headers);
        dto.setKeys(SpendVo.keys);
        dto.setObjectList(parser(voList));
        String key = UUID.randomUUID().toString().replaceAll("-", "");
        RedisUtil.set(key, dto, 1000 * 60 * 30L);
        String url = excelDownloadUrl + key;
        return Response.ok(url);
    }

    private List<JSONObject> parser(List<SpendVo> spendVo) {
        return JSONObject.parseArray(JSONObject.toJSONString(spendVo), JSONObject.class);
    }

    private SpendVo getSpendVo(Spend spend) {
        SpendVo spendVo = new SpendVo();
        // reimbursementList.setId(reimbursement.getId());
        spendVo.setId(spend.getId());
        spendVo.setItem(spend.getItem());
        spendVo.setPrice(spend.getPrice());
        spendVo.setConsumer(spend.getConsumer());
        spendVo.setDate(DateUtil.formatDate(spend.getDate(), DateUtil.PATTERN_YYYY_MM_DD));
        spendVo.setRemark(spend.getRemark());
        return spendVo;
    }

    //统计本月消费金额
    @RequestMapping(value = "/spendMonth", method = RequestMethod.POST)
    @ResponseBody
    @SysLog("统计本月消费金额")
    public Response spendMonth() {
        Date d = null;
        DateFormat sdf = new SimpleDateFormat(DateUtil.PATTERN_YYYY_MM_DD);
        d = new Date();
        String date = sdf.format(d);
        //当月消费
        String sql = "select sum(price)  as s from spend where date_format(date,'%Y-%m')=date_format(now(),'%Y-%m');";
        List<Map<String, Object>> map = jdbcTemplate.queryForList(sql);
        //本年消费
        String sqlYear = "select sum(price)  as y from spend where date_format(date,'%Y')=date_format(now(),'%Y');";
        List<Map<String, Object>> mapYear = jdbcTemplate.queryForList(sqlYear);
        Object sum = map.get(0).getOrDefault("s", 0);
        Object year = mapYear.get(0).getOrDefault("y", 0);
        Spend spend = new Spend();
        spend.setPrice(sum.toString());
        spend.setItem(year.toString());
        return Response.ok(spend);
    }

    //新增相同的消费
    @RequestMapping(value = "submitAddSame")
    public void submitAddSame(SpendVo spend) {
        System.out.println(spend);
        Spend same = new Spend();
        same.setDate(new Date());
        same.setConsumer(spend.getConsumer());
        same.setItem(spend.getItem());
        same.setPrice(spend.getPrice());
        spendService.insert(same);
    }

    static EstablishRedPacketModel registerModel(SpendList list) {
        EstablishRedPacketModel model = new EstablishRedPacketModel();
        model.setAmount(list.getPrice());
        //model.setTradeNo("123456");
        model.setOutTradeNo(StringUtils.getTradeNo());
        model.setTitle(list.getConsumer() + "创建的消费红包!");
        //model.setState(1);
        model.setType(2);

        model.setMerchantNo("66666");
        model.setCashierPhoneNo("18602702325");
        model.setSellerNo("15527875423");
        model.setCustomPhone("18602702325");
        // model.setOrderTime(new Date());
        model.setDelayDays(0);
        model.setIsNextMonthSettle(0);
        // model.setCreateTime(new Date());
        return model;
    }
    static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    //生成每月账单并转成图片形式发送至邮箱
    @RequestMapping(value = "/createPdfImg", method = RequestMethod.POST)
    @ResponseBody
    @Scheduled(cron = "0 30 11 1,22 * ?")
    public void createPdfImg() throws ParseException {
        log.error("开始生成月度账单~");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, -1);
        Date lastMonth = cal.getTime();
        String monthStr=format.format(lastMonth);
        //最早的记录是2019-1月
        Date parse = format.parse("2019-01");
        if(cal.getTime().getTime()<parse.getTime()){
            log.error("最早记录时间为2019-01月");
            return;
        }
        //当月消费
        String sql = "select sum(price)  as s from spend where date_format(date,'%Y-%m')='"+monthStr+"';";
        List<Map<String, Object>> map = jdbcTemplate.queryForList(sql);
        BigDecimal total = (BigDecimal)map.get(0).getOrDefault("s", 0);
        //当月吴思消费
        String wusi = "select sum(price)  as s from spend where date_format(date,'%Y-%m')='"+monthStr+"' and consumer='吴思';";
        List<Map<String, Object>> mapWusi = jdbcTemplate.queryForList(wusi);
        BigDecimal totalWusi = (BigDecimal)mapWusi.get(0).getOrDefault("s", 0);
        //当月张明霞消费
        String zmx = "select sum(price)  as s from spend where date_format(date,'%Y-%m')='"+monthStr+"' and consumer='张明霞';";
        List<Map<String, Object>> mapZmx = jdbcTemplate.queryForList(zmx);
        BigDecimal totalZxm = (BigDecimal)mapZmx.get(0).getOrDefault("s", 0);
        //当月小柠檬消费
        String wyc = "select sum(price)  as s from spend where date_format(date,'%Y-%m')='"+monthStr+"' and consumer='吴艺橙';";
        List<Map<String, Object>> mapWyc = jdbcTemplate.queryForList(wyc);
        BigDecimal totalWyc = (BigDecimal)mapWyc.get(0).getOrDefault("s", 0);
        //当月小力消费
        String xl = "select sum(price)  as s from spend where date_format(date,'%Y-%m')='"+monthStr+"' and consumer='小力';";
        List<Map<String, Object>> mapXl = jdbcTemplate.queryForList(xl);
        BigDecimal totalXl = (BigDecimal)mapXl.get(0).getOrDefault("s", 0);
        //当月电费消费
        String df = "select sum(price)  as s from spend where date_format(date,'%Y-%m')='"+monthStr+"' and item='电费';";
        List<Map<String, Object>> mapDf = jdbcTemplate.queryForList(df);
        BigDecimal totalDf = (BigDecimal)mapDf.get(0).getOrDefault("s", 0);
        //当月水费消费
        String sf = "select sum(price)  as s from spend where date_format(date,'%Y-%m')='"+monthStr+"' and item='水费';";
        List<Map<String, Object>> mapSf = jdbcTemplate.queryForList(sf);
        BigDecimal totalSf = (BigDecimal)mapSf.get(0).getOrDefault("s", 0);
        //当月最大一笔开销
        String max = "SELECT sp.consumer as c,sp.item as i,sp.price  as s FROM spend sp where price=(SELECT max(price) FROM spend where date_format(date,'%Y-%m')='"+monthStr+"' ) and date_format(date,'%Y-%m')='"+monthStr+"'";
        List<Map<String, Object>> mapMax = jdbcTemplate.queryForList(max);
        String  name = (String) mapMax.get(0).getOrDefault("c", 0);
        String  item = (String) mapMax.get(0).getOrDefault("i", 0);
        BigDecimal totalMax = (BigDecimal)mapMax.get(0).getOrDefault("s", 0);
        Map<String, String> pdfMap = new HashMap<>();
        if(DataUtil.isNotEmpty(total)){
            pdfMap.put("total", total.toString());
        }else{
            pdfMap.put("total", "0");
        }
        if(DataUtil.isNotEmpty(totalWusi)){
            pdfMap.put("wusi", totalWusi.toString());
        }else{
            pdfMap.put("wusi", "0");
        }
        if(DataUtil.isNotEmpty(totalZxm)){
            pdfMap.put("zmx", totalZxm.toString());
        }else{
            pdfMap.put("zmx", "0");
        }
        if(DataUtil.isNotEmpty(totalWyc)){
            pdfMap.put("wyc", totalWyc.toString());
        }else{
            pdfMap.put("wyc", "0");
        }
        if(DataUtil.isNotEmpty(totalXl)){
            pdfMap.put("xl", totalXl.toString());
        }else{
            pdfMap.put("xl", "0");
        }
        if(DataUtil.isNotEmpty(totalDf)){
            pdfMap.put("df", totalDf.toString());
        }else{
            pdfMap.put("df", "0");
        }
        if(DataUtil.isNotEmpty(totalSf)){
            pdfMap.put("sf", totalSf.toString());
        }else{
            pdfMap.put("sf", "0");
        }
        pdfMap.put("max", name+"消费的"+item+totalMax+"元");
        pdfMap.put("month", monthStr);
        pdfMap.put("remark", monthStr+"月账单");
        pdfMap.put("date",sdf.format(new Date()) );
        PdfUtil.createPdf("/home/file/home.pdf", "/home/file/"+monthStr+".pdf",pdfMap);
        //转成图片
        PdfToImgUtils.pdf2Image("/home/file/"+monthStr+".pdf","/home/file/",300,1,monthStr);
        mailService.sendAttachmentsMail("513936307@qq.com", monthStr+"家庭账单", "附件是"+monthStr+"的账单,请查收!", "/home/file/"+monthStr+".png");
    }

    //显示当月消费
    @RequestMapping(value = "monthSpend")
    public Response<String> monthSpend() {
        String monthStr=format.format(new Date());
        String sql = "select sum(price)  as s from spend where date_format(date,'%Y-%m')='"+monthStr+"';";
        List<Map<String, Object>> map = jdbcTemplate.queryForList(sql);
        BigDecimal total = (BigDecimal)map.get(0).getOrDefault("s", 0);
        if(DataUtil.isEmpty(total)){
            total=new BigDecimal("0");
        }
        return Response.ok("本月已消费"+total+"元,请理智消费!尤其是张明霞!");
    }
    //退单
    @RequestMapping(value = "refundMoney")
    public Response<String> refundMoney(String amount,Long id) {
        boolean b = MoneyUtil.judgeMoney("0", amount);
        if(b ){
            return Response.fail("请输入正数!");
        }
        if(DataUtil.isEmpty(id)){
            return Response.fail("参数不完整!");
        }
        Spend spend = spendService.queryById(id);
        if(DataUtil.isEmpty(spend)){
            return Response.fail("无此消费记录!");
        }
        if(DataUtil.isEmpty(amount)){
            spend.setPrice("0");
            spend.setRemark(spend.getRemark()+"--全额退款");
        }else{
            if(!MoneyUtil.judgeMoney(spend.getPrice(), amount)){
                return Response.fail("退款金额超过支付金额,你想白嫖??!!");
            }
            if(MoneyUtil.judgeMoney(amount ,spend.getPrice())){
                spend.setRemark(spend.getRemark()+"--全额退款");
            }else{
                spend.setRemark(spend.getRemark()+"--部分退款");
            }
            spend.setPrice(MoneyUtil.subtract(spend.getPrice(), amount));
        }
        spendService.updateById(spend);
        return  Response.ok("操作成功!");
    }
}
