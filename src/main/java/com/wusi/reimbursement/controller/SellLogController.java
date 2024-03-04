package com.wusi.reimbursement.controller;

import com.alibaba.fastjson.JSONObject;
import com.wusi.reimbursement.aop.SysLog;
import com.wusi.reimbursement.common.Response;
import com.wusi.reimbursement.entity.ExcelDto;
import com.wusi.reimbursement.entity.Reimbursement;
import com.wusi.reimbursement.entity.SellLog;
import com.wusi.reimbursement.query.SellLogListQuery;
import com.wusi.reimbursement.query.SellLogQuery;
import com.wusi.reimbursement.service.SellLogService;
import com.wusi.reimbursement.utils.DataUtil;
import com.wusi.reimbursement.utils.DateUtil;
import com.wusi.reimbursement.utils.MoneyUtil;
import com.wusi.reimbursement.utils.RedisUtil;
import com.wusi.reimbursement.vo.SellLogList;
import com.wusi.reimbursement.vo.SellLogListVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ Description   :  淘宝记账controller
 * @ Author        :  wusi
 * @ CreateDate    :  2020/1/16$ 17:07$
 */
@RestController
@Slf4j
public class SellLogController {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private SellLogService sellLogService;
    @Value("${excelDownloadUrl}")
    private String excelDownloadUrl;

    @RequestMapping("logList")
    @SysLog("销售列表")
    public Response<Page<SellLogList>> logList(SellLogQuery query) {
        if (DataUtil.isEmpty(query.getPage())) {
            query.setPage(0);
        }
        if (DataUtil.isEmpty(query.getLimit())) {
            query.setLimit(10);
        }
        Pageable pageable = PageRequest.of(query.getPage(), query.getLimit());
        Page<SellLog> page = sellLogService.queryPage(query, pageable);
        List<SellLogList> volist = new ArrayList<>();
        for (SellLog sellLog : page.getContent()) {
            volist.add(getVo(sellLog));
        }
        Page<SellLogList> vopage = new PageImpl<>(volist, pageable, page.getTotalElements());
        return Response.ok(vopage);
    }

    private SellLogList getVo(SellLog sellLog) {
        SellLogList sellLogList = new SellLogList();
        sellLogList.setId(sellLog.getId());
        sellLogList.setProduct(sellLog.getProduct());
        sellLogList.setBuyerName(sellLog.getBuyerName());
        sellLogList.setMyOrderNo(sellLog.getMyOrderNo());
        sellLogList.setSellMoney(sellLog.getSellMoney());
        sellLogList.setAmyOrderNo(sellLog.getAmyOrderNo());
        sellLogList.setBuyMoney(sellLog.getBuyMoney());
        sellLogList.setProfit(sellLog.getProfit());
        sellLogList.setRefund(sellLog.getRefund());
        sellLogList.setRemark(sellLog.getRemark());
        sellLogList.setOrderDate(DateUtil.formatDate(sellLog.getOrderDate(), DateUtil.PATTERN_YYYY_MM_DD));
        if (DataUtil.isEmpty(sellLog.getUrl())) {
            sellLogList.setUrl("http://www.photo.wearelie.com/temp/1/6i7pb8/{2}.jpg");
        } else {
            sellLogList.setUrl(sellLog.getUrl());
        }

        return sellLogList;
    }

    @RequestMapping("logDetail")
    public Response<SellLogList> todetails(SellLogQuery query) {
        SellLog sellLog = sellLogService.queryOne(query);
        return Response.ok(getVo(sellLog));
    }

    @RequestMapping("updateLog")
    @SysLog("更新销售")
    public Response<String> updateLog(SellLogListQuery query) throws ParseException {
        SellLog sellLog = getSellLog(query);
        SellLog oldSellLog = sellLogService.queryById(query.getId());
        //每次更新前 如销售金额 或  购买金额 退款要有变化
        if (!oldSellLog.getSellMoney().equals(query.getSellMoney()) || !oldSellLog.getBuyMoney().equals(query.getBuyMoney()) || !oldSellLog.getRefund().equals(query.getRefund())) {
            sellLog.setProfit(MoneyUtil.subtract(MoneyUtil.subtract(query.getSellMoney(), query.getBuyMoney()), query.getRefund()));
        }
        sellLogService.updateById(sellLog);
        return Response.ok("ok");
    }

    public SellLog getSellLog(SellLogList sellLogList) throws ParseException {
        if (sellLogList.getId() == null) {
            //新增时调用
            if (sellLogList.getProfit() == null) {
                sellLogList.setProfit(MoneyUtil.subtract(sellLogList.getSellMoney(), sellLogList.getBuyMoney()));
            }
            if (sellLogList.getProfit() != null && sellLogList.getRefund() != null) {
                sellLogList.setProfit(MoneyUtil.subtract(sellLogList.getProfit(), sellLogList.getRefund()));
            } else {
                sellLogList.setRefund("0.00");
            }
        }
        SellLog sellLog = new SellLog();
        sellLog.setId(sellLogList.getId());
        sellLog.setProduct(sellLogList.getProduct());
        sellLog.setBuyerName(sellLogList.getBuyerName());
        sellLog.setMyOrderNo(sellLogList.getMyOrderNo());
        sellLog.setSellMoney(sellLogList.getSellMoney());
        sellLog.setAmyOrderNo(sellLogList.getAmyOrderNo());
        sellLog.setBuyMoney(sellLogList.getBuyMoney());
        sellLog.setProfit(sellLogList.getProfit());
        sellLog.setRefund(sellLogList.getRefund());
        sellLog.setRemark(sellLogList.getRemark());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date orderDate = simpleDateFormat.parse(sellLogList.getOrderDate());
        sellLog.setOrderDate(orderDate);
        sellLog.setUrl(sellLogList.getUrl());
        return sellLog;
    }

    @RequestMapping(value = "logExport", method = RequestMethod.POST)
    @ResponseBody
    @SysLog("导出销售明细")
    public Response<String> batchExport(SellLogQuery query) {
        List<SellLog> sellLogs = sellLogService.queryList(query);
        List<SellLogListVo> voList = new ArrayList<>();
        int index = 1;
        for (SellLog sellLog : sellLogs) {
            SellLogListVo sellLogListVo = getListVo(sellLog);
            sellLogListVo.setIndex(index);
            voList.add(sellLogListVo);
            index++;
        }
        ExcelDto dto = new ExcelDto();
        dto.setHeaders(SellLogListVo.headers);
        dto.setKeys(SellLogListVo.keys);
        dto.setObjectList(parser(voList));
        String key = UUID.randomUUID().toString().replaceAll("-", "");
        RedisUtil.set(key, dto, 1000 * 60 * 30L);
        String url = excelDownloadUrl + key;
        return Response.ok(url);
    }

    private List<JSONObject> parser(List<SellLogListVo> SellLogListVo) {
        return JSONObject.parseArray(JSONObject.toJSONString(SellLogListVo), JSONObject.class);
    }

    private SellLogListVo getListVo(SellLog sellLog) {
        SellLogListVo vo = new SellLogListVo();
        vo.setId(sellLog.getId());
        vo.setProduct(sellLog.getProduct());
        vo.setBuyerName(sellLog.getBuyerName());
        vo.setMyOrderNo(sellLog.getMyOrderNo());
        vo.setSellMoney(sellLog.getSellMoney());
        vo.setAmyOrderNo(sellLog.getAmyOrderNo());
        vo.setBuyMoney(sellLog.getBuyMoney());
        vo.setProfit(sellLog.getProfit());
        vo.setRefund(sellLog.getRefund());
        vo.setRemark(sellLog.getRemark());
        vo.setOrderDate(DateUtil.formatDate(sellLog.getOrderDate(), DateUtil.PATTERN_YYYY_MM_DD));
        return vo;
    }

    @RequestMapping(value = "order/save", method = RequestMethod.POST)
    @ResponseBody
    @SysLog("保存销售明细")
    public Response<String> save(SellLogList query) throws ParseException {
        SellLog sellLog = getSellLog(query);
        try {
            sellLogService.insert(sellLog);
            return Response.ok("添加成功");
        } catch (Exception e) {
            log.error("添加失败{}", e.getMessage());
        }
        return Response.fail("添加失败");
    }

    @RequestMapping(value = "order/del", method = RequestMethod.POST)
    @ResponseBody
    @SysLog("删除销售")
    public Response<String> del(SellLog query) throws ParseException {
        try {
            sellLogService.deleteById(query.getId());
            return Response.ok("删除成功!!!");
        } catch (Exception e) {
            log.error("添加失败{}", e.getMessage());
        }
        return Response.fail("删除失败!!!");
    }

    //统计报销金额
    @RequestMapping(value = "/countProfit", method = RequestMethod.POST)
    @ResponseBody
    @SysLog("统计利润")
    public Response spendMonth() {

        //2020
        String sql1 = "SELECT sum(profit) as s FROM sell_log where   product='2020';";
        List<Map<String, Object>> map1 = jdbcTemplate.queryForList(sql1);
        //本月利润
        String sql2 = "select sum(profit)  as s from sell_log where date_format(order_date,'%Y-%m')=date_format(now(),'%Y-%m') and product !='2020';";
        List<Map<String, Object>> map2 = jdbcTemplate.queryForList(sql2);
        //本年利润
        String sql3 = "select sum(profit)  as s from sell_log where date_format(order_date,'%Y')=date_format(now(),'%Y') and product !='2020';";
        List<Map<String, Object>> map3 = jdbcTemplate.queryForList(sql3);
        Object one = map1.get(0).getOrDefault("s", 0);
        Object two = map2.get(0).getOrDefault("s", 0);
        Object three = map3.get(0).getOrDefault("s", 0);
        Reimbursement reimbursement = new Reimbursement();
        if (one == null) {
            reimbursement.setRemark("0");
        } else {
            reimbursement.setRemark(one.toString());
        }
        if (two == null) {
            reimbursement.setBuyChannel("0");
        } else {
            reimbursement.setBuyChannel(two.toString());
        }
        if (three == null) {
            reimbursement.setProductName("0");
        } else {
            reimbursement.setProductName(three.toString());
        }

        return Response.ok(reimbursement);
    }
}
