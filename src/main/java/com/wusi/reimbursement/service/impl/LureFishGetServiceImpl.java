package com.wusi.reimbursement.service.impl;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.base.service.impl.BaseMybatisServiceImpl;
import com.wusi.reimbursement.entity.LureFishGet;
import com.wusi.reimbursement.entity.LureShopping;
import com.wusi.reimbursement.mapper.LureFishGetMapper;
import com.wusi.reimbursement.query.LureFishGetQuery;
import com.wusi.reimbursement.query.LureShoppingQuery;
import com.wusi.reimbursement.service.LureFishGetService;
import com.wusi.reimbursement.service.LureShoppingService;
import com.wusi.reimbursement.utils.DataUtil;
import com.wusi.reimbursement.utils.DateUtil;
import com.wusi.reimbursement.utils.MoneyUtil;
import com.wusi.reimbursement.utils.StringUtils;
import com.wusi.reimbursement.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author admin
 * @date 2022-06-02 13:59:23
 **/
@Service
public class LureFishGetServiceImpl extends BaseMybatisServiceImpl<LureFishGet, Long> implements LureFishGetService {

    @Autowired
    private LureFishGetMapper lureFishGetMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private LureFishGetService lureFishGetService;
    @Autowired
    private LureShoppingService lureShoppingService;

    @Override
    protected BaseMapper<LureFishGet, Long> getBaseMapper() {
        return lureFishGetMapper;
    }

    @Override
    public List<FishCount> fishCount(String uid, String monthStr) {
        return lureFishGetMapper.fishCount(uid, monthStr);
    }

    @Override
    public List<MonthCount> getMonthCount(String uid) {
        return lureFishGetMapper.monthCount(uid);
    }

    @Override
    public Integer getnum(String str,String uid) {
        return lureFishGetMapper.getnum(str,uid);
    }

    @Override
    public List<MonthData> getMonthData(String uid) {
        LureFishGet query = new LureFishGetQuery();
        query.setUid(uid);
        List<LureFishGet> lureFishGets = lureFishGetService.queryList(query);
        List<MonthRate> rate = new ArrayList<>();
        for (LureFishGet lureFishGet : lureFishGets) {
            MonthRate data = new MonthRate();
            data.setGetFish(lureFishGet.getGetFish());
            data.setMonth(new SimpleDateFormat("yyyy-MM").format(lureFishGet.getCreateTime()));
            data.setDay(new SimpleDateFormat("yyyy-MM-dd").format(lureFishGet.getCreateTime()));
            data.setUse(lureFishGet.getUse());
            data.setFishKind(lureFishGet.getFishKind());
            rate.add(data);
        }

        List<String> monthStr = getMonthStr(rate);
        List<MonthData> dataList = new ArrayList<>();
        for (String month : monthStr) {
            Integer day = 0;
            MonthData innerData = new MonthData();
            innerData.setMonth(month);
            List<MonthRate> mr = rate.stream().filter(w -> w.getMonth().equals(month)).collect(Collectors.toList());
            List<MonthRate> fish = rate.stream().filter(w -> w.getMonth().equals(month) && w.getGetFish() == 1).collect(Collectors.toList());
            if (mr.size() >= 2) {
                for (int i = 0; i <= fish.size() - 2; i++) {
                    int tempDay = DateUtil.daysBetween2(DateUtil.strToDate2(fish.get(fish.size() - 1 - i).getDay()), DateUtil.strToDate2(fish.get(fish.size() - i - 2).getDay()));
                    if (tempDay > day) {
                        day = tempDay;
                        innerData.setBeginDay(fish.get(fish.size() - 1 - i).getDay());
                        innerData.setEndDay(fish.get(fish.size() - 2 - i).getDay());
                    }
                }
            }
            innerData.setNum(mr.stream().map(w -> w.getDay()).distinct().collect(Collectors.toList()).size());
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(DateUtil.strToDate3(month));
            innerData.setMonthNum(startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            innerData.setTime(innerData.getNum() + "/" + innerData.getMonthNum());
            innerData.setFailNum(Integer.parseInt(String.valueOf(mr.stream().filter(w -> w.getGetFish() == 0).count())));
            innerData.setSuccessNum(innerData.getNum() - innerData.getFailNum());
            innerData.setRate(BigDecimal.valueOf(innerData.getFailNum()).divide(BigDecimal.valueOf(innerData.getNum()), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).setScale(2).toString() + "%");
            if (day > 0) {
                innerData.setDay(day + "天(" + innerData.getBeginDay() + "-" + innerData.getEndDay() + ")");
            } else {
                innerData.setDay("0");
            }
            innerData.setFishNum(Integer.parseInt(String.valueOf(mr.stream().filter(w -> w.getGetFish() == 1).count())));
            //去年同月鱼获
            startCalendar.add(Calendar.MONTH, -12);
            List<MonthRate> last = rate.stream().filter(w -> w.getMonth().equals(DateUtil.formatDate(startCalendar.getTime(), "yyyy-MM"))).collect(Collectors.toList());
            innerData.setFishNumLastYear(last == null ? 0 : last.size());
            innerData.setFangNum(Integer.parseInt(String.valueOf(mr.stream().filter(w -> w.getUse() != null && w.getUse() == 2).count())));
            String sql = "select sum(price)  as s from lure_shopping where uid='" + uid + "' and date_format(date,'%Y-%m')='" + month + "';";
            List<Map<String, Object>> map = jdbcTemplate.queryForList(sql);
            BigDecimal total = (BigDecimal) map.get(0).getOrDefault("s", 0);
            if (DataUtil.isEmpty(total)) {
                total = new BigDecimal("0");
            }
            innerData.setSpend(MoneyUtil.add(String.valueOf(total), MoneyUtil.multiply(String.valueOf(innerData.getNum()), "10")) + ":(装备:" + String.valueOf(total) + "+油费:" + MoneyUtil.multiply(String.valueOf(innerData.getNum()), "10") + ")");
            innerData.setMaxFail(continueGetFish(0, 0, mr, 0,""));
            innerData.setFishDesc(getFishDesc(mr.stream().filter(w->w.getGetFish()==1).collect(Collectors.toList())));
            dataList.add(innerData);
        }
        return dataList;
    }

    private String continueGetFish(Integer c, Integer temp, List<MonthRate> mr, Integer i,String date) {
        if (i<mr.size()) {
            if (mr.get(i).getGetFish() == 0) {
                c++;
                if(c>temp){
                    date="";
                    for(int j=i;j>i-c;j--){
                        date=date+mr.get(j).getDay()+"/";
                    }
                    if(date.endsWith("/")){
                        date=date.substring(0,date.length()-1) ;
                    }
                }
                temp=c > temp ? c : temp;
                return continueGetFish(c, temp, mr, i + 1,date);
            }else{
                c = 0;
                return continueGetFish(c, c > temp ? c : temp, mr, i + 1,date);
            }
        }
        return date==""||temp==1?"无连龟数据":"最多"+temp+"连龟:("+date+")";
    }

    private List<String> getMonthStr(List<MonthRate> rate) {
        List<String> str = new ArrayList<>();
        for (MonthRate monthRate : rate) {
            if (!str.contains(monthRate.getMonth())) {
                str.add(monthRate.getMonth());
            }
        }
        return str;
    }

    @Override
    public List<MonthData> getYearData(String uid) {
        LureFishGet query = new LureFishGetQuery();
        query.setUid(uid);
        List<LureFishGet> lureFishGets = lureFishGetService.queryList(query);
        List<MonthRate> rate = new ArrayList<>();
        for (LureFishGet lureFishGet : lureFishGets) {
            MonthRate data = new MonthRate();
            data.setGetFish(lureFishGet.getGetFish());
            data.setMonth(new SimpleDateFormat("yyyy").format(lureFishGet.getCreateTime()));
            data.setDay(new SimpleDateFormat("yyyy-MM-dd").format(lureFishGet.getCreateTime()));
            data.setUse(lureFishGet.getUse());
            data.setFishKind(lureFishGet.getFishKind());
            rate.add(data);
        }

        List<String> monthStr = rate.stream().map(w -> w.getMonth()).distinct().collect(Collectors.toList());
        List<MonthData> dataList = new ArrayList<>();
        for (String month : monthStr) {
            Integer day = 0;
            MonthData innerData = new MonthData();
            innerData.setMonth(month);
            List<MonthRate> mr = rate.stream().filter(w -> w.getMonth().equals(month)).collect(Collectors.toList());
            List<MonthRate> fish = rate.stream().filter(w -> w.getMonth().equals(month) && w.getGetFish() == 1).collect(Collectors.toList());
            if (mr.size() >= 2) {
                for (int i = 0; i <= fish.size() - 2; i++) {
                    int tempDay = DateUtil.daysBetween2(DateUtil.strToDate2(fish.get(fish.size() - 1 - i).getDay()), DateUtil.strToDate2(fish.get(fish.size() - i - 2).getDay()));
                    if (tempDay > day) {
                        day = tempDay;
                        innerData.setBeginDay(fish.get(fish.size() - 1 - i).getDay());
                        innerData.setEndDay(fish.get(fish.size() - 2 - i).getDay());
                    }
                }
            }
            innerData.setNum(mr.stream().map(w -> w.getDay()).distinct().collect(Collectors.toList()).size());
            innerData.setFailNum(Integer.parseInt(String.valueOf(mr.stream().filter(w -> w.getGetFish() == 0).count())));
            innerData.setSuccessNum(innerData.getNum() - innerData.getFailNum());
            innerData.setRate(BigDecimal.valueOf(innerData.getFailNum()).divide(BigDecimal.valueOf(innerData.getNum()), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).setScale(2).toString() + "%");
            if (day > 0) {
                innerData.setDay(day + "天(" + innerData.getBeginDay() + "-" + innerData.getEndDay() + ")");
            } else {
                innerData.setDay("0");
            }
            innerData.setFishNum(Integer.parseInt(String.valueOf(mr.stream().filter(w -> w.getGetFish() == 1).count())));
            innerData.setFangNum(Integer.parseInt(String.valueOf(mr.stream().filter(w -> w.getUse() != null && w.getUse() == 2).count())));
            String sql = "select sum(price)  as s from lure_shopping where  uid='" + uid + "' and  date_format(date,'%Y')='" + month + "';";
            List<Map<String, Object>> map = jdbcTemplate.queryForList(sql);
            BigDecimal total = (BigDecimal) map.get(0).getOrDefault("s", 0);
            if (DataUtil.isEmpty(total)) {
                total = new BigDecimal("0");
            }
            innerData.setSpend(String.valueOf(total) + "元");
            innerData.setMaxFail(continueGetFish(0, 0, mr, 0,""));
            innerData.setFishDesc(getFishDesc(mr.stream().filter(w->w.getGetFish()==1).collect(Collectors.toList())));
            dataList.add(innerData);
        }
        return dataList;
    }

    private String getFishDesc(List<MonthRate> mr) {
        if(DataUtil.isEmpty(mr)){
            return "暂无鱼获！";
        }
        String desc="";
        List<String> fishKind = mr.stream().map(w -> w.getFishKind()).distinct().collect(Collectors.toList());
        for (String fish : fishKind) {
            long count = mr.stream().filter(w -> w.getFishKind().equals(fish)).count();
            desc=desc+fish+"×"+count+"、";
        }
        return desc.substring(0,desc.length()-1);
    }

    @Override
    public List<Geti> getGeti(String uid,String type) {
        LureFishGet query = new LureFishGetQuery();
        query.setGetFish(1);
        if("personal".equals(type)){
            query.setUid(uid);
        }
        List<LureFishGet> lureFishGets = lureFishGetService.queryList(query);
        List<String> collect = lureFishGets.stream().map(w -> w.getFishKind()).distinct().collect(Collectors.toList());
        List<LureFishGet> data = new ArrayList<>();
        for (String name : collect) {
            LureFishGet fishGet = new LureFishGet();
            List<LureFishGet> collect1 = lureFishGets.stream().filter(w -> w.getFishKind().equals(name)).collect(Collectors.toList());
            String tempWeight = "0";
            for (LureFishGet lureFishGet : collect1) {
                if (MoneyUtil.judgeMoney(lureFishGet.getWeight(), tempWeight)) {
                    tempWeight = lureFishGet.getWeight();
                    fishGet = lureFishGet;
                }
            }
            data.add(fishGet);
        }
        List<Geti> returnData = new ArrayList<>();
        for (LureFishGet datum : data) {
            Geti geti = new Geti();
            geti.setName(datum.getFishKind());
            geti.setAddress(DataUtil.isEmpty(datum.getAddress())?"大师不愿意透露地址":datum.getAddress());
            geti.setLength(datum.getLength());
            geti.setWeight(datum.getWeight());
            geti.setTime(new SimpleDateFormat("yyyy-MM-dd").format(datum.getCreateTime()));
            geti.setUserName(datum.getUserName());
            geti.setUrl(datum.getImageUrl());
            returnData.add(geti);
        }
        return returnData;
    }

    @Override
    public List<PaiHang> paiHang(String type) {
        List<LureFishGet> lureFishGets = new ArrayList<>();
        if ("total".equals(type)) {
            lureFishGets = lureFishGetService.queryByType(type, null);
        } else if ("year".equals(type)) {
            lureFishGets = lureFishGetService.queryByType(type, new SimpleDateFormat("yyyy").format(new Date()));
        } else {
            lureFishGets = lureFishGetService.queryByType(type, new SimpleDateFormat("yyyy-MM").format(new Date()));
        }
        List<String> collect = lureFishGets.stream().map(w -> w.getUserName()).distinct().collect(Collectors.toList());
        List<PaiHang> data = new ArrayList<>();
        for (String name : collect) {
            PaiHang paiHang = new PaiHang();
            List<LureFishGet> collect1 = lureFishGets.stream().filter(w -> w.getUserName().equals(name)).collect(Collectors.toList());
            paiHang.setName(name);
            paiHang.setNum(collect1.size());
            data.add(paiHang);
        }
        data.sort(Comparator.comparing(PaiHang::getNum).reversed());
        for (int i = 0; i <= data.size() - 1; i++) {
            data.get(i).setIndex("第" + (i + 1) + "名");
        }
        return data;
    }

    @Override
    public List<LureFishGet> queryByType(String type, String time) {
        return lureFishGetMapper.queryByType(type, time);
    }

    @Override
    public List<FishDayCount> FishDayCount(String uid) {
        LureFishGet query = new LureFishGetQuery();
        query.setGetFish(1);
        query.setUid(uid);
        List<LureFishGet> lureFishGets = lureFishGetService.queryList(query);
        List<String> collect = lureFishGets.stream().map(w -> w.getFishKind()).distinct().collect(Collectors.toList());
        List<FishDayCount> data = new ArrayList<>();
        for (String name : collect) {
            FishDayCount fishGet = new FishDayCount();
            List<LureFishGet> collect1 = lureFishGets.stream().filter(w -> w.getFishKind().equals(name)).collect(Collectors.toList());
            fishGet.setFish(collect1.get(0).getFishKind());
            fishGet.setLastDate(new SimpleDateFormat("yyyy-MM-dd").format(collect1.get(0).getCreateTime()));
            fishGet.setDay(DateUtil.daysBetween2(collect1.get(0).getCreateTime(),new Date())+"天");
            data.add(fishGet);
        }
        return data;
    }

    @Override
    public List<GuiWang> guiWang(String type) {
        List<GuiWang> list=new ArrayList<>();
        List<LureFishGet> lureFishGets = lureFishGetService.queryByMonth(type,type.equals("month")?new SimpleDateFormat("yyyy-MM").format(new Date()):new SimpleDateFormat("yyyy").format(new Date()));
        if(lureFishGets.size()==0){
            return list;
        }
        List<String> collect = lureFishGets.stream().map(w -> w.getUserName()).distinct().collect(Collectors.toList());
        for (String name : collect) {
            GuiWang wang=new GuiWang();
            List<LureFishGet> collect1 = lureFishGets.stream().filter(w -> w.getUserName().equals(name)).collect(Collectors.toList());
            List<GuiMonthRate> rateWang = new ArrayList<>();
            for (LureFishGet lureFishGet : collect1) {
                GuiMonthRate data = new GuiMonthRate();
                data.setGetFish(lureFishGet.getGetFish());
                data.setDay(new SimpleDateFormat("yyyy-MM-dd").format(lureFishGet.getCreateTime()));
                data.setName(lureFishGet.getUserName());
                rateWang.add(data);
            }
            wang.setRate(BigDecimal.valueOf(rateWang.stream().filter(w -> w.getGetFish() == 0).count()).divide(BigDecimal.valueOf(rateWang.stream().map(w -> w.getDay()).distinct().collect(Collectors.toList()).size()), 4, BigDecimal.ROUND_HALF_UP));
            wang.setName(name);
            list.add(wang);
        }
        list.sort(Comparator.comparing(GuiWang::getRate).reversed());
        for (int i = 0; i <= list.size() - 1; i++) {
            list.get(i).setRateStr(list.get(i).getRate().multiply(new BigDecimal("100")).setScale(2).toString()+"%");
            list.get(i).setIndex("第" + (i + 1) + "名");
        }
        return list;

    }

    @Override
    public List<LureFishGet> queryByMonth(String type,String format) {
        return lureFishGetMapper.queryByMonth(type,format);
    }

    @Override
    public List<SpendPai> spendPai() {
        List<LureShopping> lureShoppings = lureShoppingService.queryList(new LureShoppingQuery());
        LureFishGetQuery query=new LureFishGetQuery();
        query.setGetFish(1);
        List<LureFishGet> lureFishGets = lureFishGetService.queryList(query);
        List<String> collect = lureShoppings.stream().map(w -> w.getUid()).distinct().collect(Collectors.toList());
        List<SpendPai> list=new ArrayList<>();
        for (String uid : collect) {
            SpendPai pai=new SpendPai();
            List<LureShopping> personalSpend = lureShoppings.stream().filter(w -> w.getUid().equals(uid)).collect(Collectors.toList());
            pai.setName(personalSpend.get(0).getUserName());
            BigDecimal spend=new BigDecimal("0");
            for (LureShopping lureShopping : personalSpend) {
                spend= spend.add(new BigDecimal(lureShopping.getPrice()));
            }
            long count = lureFishGets.stream().filter(w -> w.getUid().equals(uid)).count();
            pai.setPrice(count==0?"可怜,一条鱼都没有":MoneyUtil.devide(spend.toString(), String.valueOf(count)));
            pai.setNum(count);
            pai.setSpend(spend);
            list.add(pai) ;
        }
        list.sort(Comparator.comparing(SpendPai::getSpend).reversed());
        for (int i = 0; i <= list.size() - 1; i++) {
            list.get(i).setIndex("第" + (i + 1) + "名");
        }
        return list;
    }

    @Override
    public List<FirstFish> firstFish(String uid) {
        List<FirstFish> list=new ArrayList<>();
        LureFishGet query = new LureFishGetQuery();
        query.setUid(uid);
        query.setGetFish(1);
        List<LureFishGet> lureFishGets = lureFishGetService.queryList(query);
        List<String> fishKind = lureFishGets.stream().map(w -> w.getFishKind()).distinct().collect(Collectors.toList());
        for (String name : fishKind) {
            FirstFish firstFish=new FirstFish();
            List<LureFishGet> fishList = lureFishGets.stream().filter(w -> w.getFishKind().equals(name)).collect(Collectors.toList());
            LureFishGet fishGet = fishList.get(fishList.size() - 1);
            firstFish.setAddress(fishGet.getAddress());
            firstFish.setFish(name);
            firstFish.setTime(new SimpleDateFormat("yyyy-MM-dd").format(fishGet.getCreateTime()));
            firstFish.setAddress(fishGet.getAddress());
            firstFish.setWeather(fishGet.getTmpMin()+"℃-"+fishGet.getTmpMax()+"℃");
            list.add(firstFish);
        }
        return list;
    }
}
