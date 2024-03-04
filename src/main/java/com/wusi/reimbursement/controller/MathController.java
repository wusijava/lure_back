package com.wusi.reimbursement.controller;

import com.wusi.reimbursement.common.Response;
import com.wusi.reimbursement.common.ratelimit.anonation.RateLimit;
import com.wusi.reimbursement.entity.AwardList;
import com.wusi.reimbursement.entity.AwardRecord;
import com.wusi.reimbursement.entity.Festival;
import com.wusi.reimbursement.entity.MathPlan;
import com.wusi.reimbursement.mapper.MathMapper;
import com.wusi.reimbursement.query.AwardRecordQuery;
import com.wusi.reimbursement.query.MathPlanQuery;
import com.wusi.reimbursement.query.MathQuery;
import com.wusi.reimbursement.service.*;
import com.wusi.reimbursement.utils.DataUtil;
import com.wusi.reimbursement.utils.DateUtil;
import com.wusi.reimbursement.utils.MoneyUtil;
import com.wusi.reimbursement.utils.RedisUtil;
import com.wusi.reimbursement.vo.*;
import com.wusi.reimbursement.vo.Math;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.CriteriaBuilder;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ Description   :  小柠檬专属controller
 * @ Author        :  wusi
 * @ CreateDate    :  2021/1/15$ 10:06$
 */
@RestController
@Slf4j
@RequestMapping(value = "api/math")
public class MathController {
    @Autowired
    private MathService MathService;
    @Autowired
    private MathMapper MathMapper;
    @Autowired
    private MathPlanService MathPlanService;
    @Autowired
    private AwardRecordService awardRecordService;
    @Autowired
    private AwardListService awardListService;
    @Autowired
    private FestivalService festivalService;

    @ResponseBody
    @RequestMapping(value = "getTi")
    @RateLimit(permitsPerSecond = 1, ipLimit = true, description = "限制出题频率")
    public Response<Math> getTi(Integer size) {
        //先判断缓存是否有未做的题
        //System.out.println(RedisUtil.get("redisTi"));
        if(RedisUtil.get("redisTi")!=null){
            Math ma=(Math)RedisUtil.get("redisTi");
            MathPlan mp= getPlan();
            if(DataUtil.isNotEmpty(mp)){
                ma.setNum(mp.getWeiDo());
            }else{
                ma.setNum(null);
            }
            return Response.ok(ma);
        }
        Math res = new Math();
        while (true) {
            Random r = new Random();
            int numberOne = r.nextInt(size);
            int numberTwo = r.nextInt(size);
            int number = r.nextInt(3);
            if (numberOne < numberTwo) {
                int temp = 0;
                temp = numberOne;
                numberOne = numberTwo;
                numberTwo = temp;
            }
            res.setNumOne(numberOne);
            res.setNumTwo(numberTwo);
            int result = 0;
            if (number % 2 == 1) {
                res.setSymbolOne("-");
                result = numberOne - numberTwo;
            } else {
                res.setSymbolOne("+");
                result = numberOne + numberTwo;
            }
            if (result > (size)||result<=0) {
                continue;
            }
            int symbolTwo = r.nextInt(3);
            int numberThree = r.nextInt(result);
            res.setNumThree(numberThree);
            if (symbolTwo % 2 == 1) {
                res.setSymbolTwo("-");
                result = result - numberThree;
            } else {
                res.setSymbolTwo("+");
                result = result + numberThree;
            }
            res.setResult(result);
            if (result < 100) {
                break;
            }
        }
        MathPlan plan = getPlan();
        if(DataUtil.isNotEmpty(plan)){
            res.setNum(plan.getWeiDo());
        }
        //避免退出刷新换题
        RedisUtil.set("redisTi", res);
        return Response.ok(res);
    }

    @ResponseBody
    @RequestMapping(value = "checkTi")
    @RateLimit(permitsPerSecond = 0.3, ipLimit = true, description = "限制出题频率")
    public Response<String> checkTi(Math math,Long rowId,Integer type) {
        com.wusi.reimbursement.entity.Math count=null;
        if(DataUtil.isNotEmpty(rowId)){
            count = MathService.queryById(rowId);
        }
        if (DataUtil.isEmpty(math.getResult())) {
            return Response.ok("请输入你的答案!~");
        }
        int two=check(math,rowId,count,0);
        if (two == math.getResult()) {
            //获取缓存题
            Math m=null;
            if(type.equals(1)){
                m= (Math)RedisUtil.get("cuoTi");
            }else{
                m= (Math)RedisUtil.get("redisTi");
            }
            if(m.getNumOne().equals(math.getNumOne())&&m.getNumTwo().equals(math.getNumTwo())&&m.getNumThree().equals(math.getNumThree())&&m.getSymbolOne().equals(math.getSymbolOne())&&m.getSymbolTwo().equals(math.getSymbolTwo())){
                //表示错题
                if(type.equals(1)){
                    RedisUtil.del("cuoTi");
                }else{
                    RedisUtil.del("redisTi");
                }

            }
            return Response.ok("答对了,小柠檬不错哦~");
        } else {
            return Response.ok("答错了,小柠檬加油哦~");
        }

    }
    @RateLimit(permitsPerSecond = 0.3, ipLimit = true, description = "限制出题频率")
    public int check(Math math, Long rowId, com.wusi.reimbursement.entity.Math count,Integer type){
        int one = 0;
        int two = 0;
        if (math.getSymbolOne().equals("-")) {
            one = math.getNumOne() - math.getNumTwo();
        } else if(math.getSymbolOne().equals("+")){
            one = math.getNumOne() + math.getNumTwo();
        }else if("×".equals(math.getSymbolOne())){
            two =  math.getNumOne() * math.getNumTwo();
        }else{
            two = math.getNumOne() / math.getNumTwo();
        }
        if (DataUtil.isNotEmpty(math.getSymbolTwo())&&"-".equals(math.getSymbolTwo())) {
            two = one - math.getNumThree();
        } else if(DataUtil.isNotEmpty(math.getSymbolTwo())&&"+".equals(math.getSymbolTwo())){
            two = one + math.getNumThree();
        }

        //记录
        com.wusi.reimbursement.entity.Math log = new com.wusi.reimbursement.entity.Math();
        if(DataUtil.isNotEmpty(math.getSymbolTwo())){
            log.setContent(math.getNumOne() + math.getSymbolOne() + math.getNumTwo() + math.getSymbolTwo() + math.getNumThree() + "=" + math.getResult());
        }else{
            log.setContent(math.getNumOne() + math.getSymbolOne() + math.getNumTwo() + "=" + math.getResult());
        }

        log.setCreateTime(new Date());

        //加入任务
        MathPlan query = new MathPlan();
        query.setTime(DateUtil.formatDate(new Date(), "yyyy-MM-dd"));
        MathPlan plan = MathPlanService.queryOne(query);
        if (DataUtil.isEmpty(plan)) {
            MathPlan newPlan = new MathPlan();
            newPlan.setTime(DateUtil.formatDate(new Date(), "yyyy-MM-dd"));
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
            //星期六或星期天暂定150题
            if (w == 0 || w == 6) {
                newPlan.setTask("150");
            } else {
                //平时做50题
                newPlan.setTask("50");
            }
            newPlan.setWeiDo(newPlan.getTask());
            newPlan.setYiDo("0");
            newPlan.setRight("0");
            newPlan.setError("0");
            newPlan.setCreateTime(new Date());
            MathPlanService.insert(newPlan);
        }
        //再查一次
        MathPlan sec = new MathPlan();
        sec.setTime(DateUtil.formatDate(new Date(), "yyyy-MM-dd"));
        MathPlan secPlan = MathPlanService.queryOne(sec);
        if (two == math.getResult()) {
            if(math.getIndex()!=null){
                RedisUtil.del("index");
            }
            log.setResult("对");
            secPlan.setYiDo(MoneyUtil.add(secPlan.getYiDo(), "1"));
            secPlan.setWeiDo(MoneyUtil.subtract(secPlan.getTask(), secPlan.getYiDo()));
            secPlan.setRight(MoneyUtil.add(secPlan.getRight(), "1"));
            if(secPlan.getWeiDo().equals("0")){
                createAward(secPlan);
            }
            if (Integer.valueOf(secPlan.getWeiDo()) < 0) {
                secPlan.setWeiDo("0");
            }
            if(DataUtil.isNotEmpty(rowId)){
                count.setCount(count.getCount()+1);
                MathService.updateById(count);
            }
        } else {
            if(DataUtil.isNotEmpty(math.getIndex())){
                Integer result=0;
                if(math.getIndex()==0){
                    if(math.getSymbolOne().equals("-")){
                        result=math.getResult()+math.getNumTwo();
                    }else{
                        result=math.getResult()-math.getNumTwo();
                    }
                    if(math.getSymbolTwo().equals("-")){
                        result=result+math.getNumThree();
                    }else{
                        result=result-math.getNumThree();
                    }
                }
                //第二个为括号
                if(math.getIndex()==1){
                   if(math.getSymbolOne().equals("-")){
                      if(math.getSymbolTwo().equals("-")){
                          result=math.getNumOne()-math.getNumThree()-math.getResult();
                      } else{
                          result=math.getNumOne()+math.getNumThree()-math.getResult();
                      }
                   }else{
                       if(math.getSymbolTwo().equals("-")){
                           result=math.getResult()-math.getNumOne()+math.getNumThree();
                       } else{
                           result=math.getResult()-math.getNumOne()-math.getNumThree();
                       }
                   }
                }
                //第三个为括号
                if(math.getIndex()==2){
                    if(math.getSymbolTwo().equals("-")){
                        if(math.getSymbolOne().equals("-")){
                            result=math.getNumOne()-math.getNumTwo()-math.getResult();
                        } else{
                            result=math.getNumOne()+math.getNumTwo()-math.getResult();
                        }
                    }else{
                        if(math.getSymbolOne().equals("-")){
                            result=math.getResult()-math.getNumOne()+math.getNumTwo();
                        } else{
                            result=math.getResult()-math.getNumOne()-math.getNumTwo();
                        }
                    }
                }
                log.setRightResult(result);
            }else{
                log.setRightResult(two);
            }
            log.setResult("错");
            log.setCount(0);
            secPlan.setError(MoneyUtil.add(secPlan.getError(), "1"));
            secPlan.setTask(MoneyUtil.add(secPlan.getTask(), "2"));
            secPlan.setWeiDo(MoneyUtil.add(secPlan.getWeiDo(), "2"));
        }
        log.setTime(DateUtil.formatDate(new Date(), "yyyy-MM-dd"));
        log.setType(type);
        MathService.insert(log);
        MathPlanService.updateById(secPlan);
        return two;
    }

    private void createAward(MathPlan secPlan) {
        String add = MoneyUtil.add(secPlan.getError(), secPlan.getRight());
        String rightRate = MoneyUtil.devide(secPlan.getRight(), add);
        System.out.println(rightRate);
        //一等奖
        if(MoneyUtil.judgeMoney(rightRate, "1")){
            createAwardLog(1,5);
        }else if(MoneyUtil.judgeMoney(rightRate, "0.95")){
            createAwardLog(1,4);
        }else if(MoneyUtil.judgeMoney(rightRate, "0.9")){
            createAwardLog(1,3);
        }else if(MoneyUtil.judgeMoney(rightRate, "0.85")){
            createAwardLog(1,2);
        }else if(MoneyUtil.judgeMoney(rightRate, "0.80")){
            createAwardLog(1,1);
        }else if(MoneyUtil.judgeMoney(rightRate, "0.75")){
            createAwardLog(0,1);
        }else if(MoneyUtil.judgeMoney(rightRate, "0.70")){
            createAwardLog(0,2);
        } else if(MoneyUtil.judgeMoney(rightRate, "0.65")){
            createAwardLog(0,3);
        }else if(MoneyUtil.judgeMoney(rightRate, "0.60")){
            createAwardLog(0,4);
        }else{
            createAwardLog(0,5);
        }
    }
    void createAwardLog(Integer type,Integer level){
        Integer num = awardRecordService.queryNum(sdf.format(new Date()));
        if(num==0){
            AwardRecord record=new AwardRecord();
            AwardList query=new AwardList();
            query.setType(type);
            query.setLevel(level);
            List<AwardList> awardLists = awardListService.queryList(query);
            Random r = new Random();
            int index = r.nextInt(awardLists.size());
            record.setAwardName(awardLists.get(index).getAwardName());
            record.setType(awardLists.get(index).getType());
            record.setState(0);
            record.setCreateTime(new Date());
            awardRecordService.insert(record);
        }
    }


    @RequestMapping(value = "homeworkLog")
    @ResponseBody
    public Response<List<MathParam>> homeworkLog(MathQuery query) {
        if (DataUtil.isEmpty(query.getPage())) {
            query.setPage(0);
        }
        query.setLimit(1);
        long count = MathService.querycount();
        long page = 0;
        if (count % query.getLimit() == 0) {
            page = count / query.getLimit();
        } else {
            page = count / query.getLimit() + 1;
        }
        List<String> result = MathMapper.getResult();
        query.setPage(query.getLimit() * (query.getPage()));
        List<com.wusi.reimbursement.entity.Math> maths = MathService.queryListByParam(query);
        List<String> list = maths.stream().map(com.wusi.reimbursement.entity.Math::getTime).collect(Collectors.toList());
        ArrayList<MathParam> MathParam = new ArrayList<>();
        for (String time : list) {
            String task = null;
            String yiDo = null;
            String weiDo = null;
            String rightToday;
            String errorToday;
            MathPlan plan = new MathPlan();
            plan.setTime(time);
            MathPlan queryPlan = MathPlanService.queryOne(plan);
            task = queryPlan.getTask();
            yiDo = queryPlan.getYiDo();
            weiDo = queryPlan.getWeiDo();
            rightToday = queryPlan.getRight();
            errorToday = queryPlan.getError();
            com.wusi.reimbursement.entity.Math math = new com.wusi.reimbursement.entity.Math();
            math.setTime(time);
            List<com.wusi.reimbursement.entity.Math> ssqs = MathService.queryList(math);
            List<com.wusi.reimbursement.entity.Math> list2 = new ArrayList<>();
            for (com.wusi.reimbursement.entity.Math com : ssqs) {
                list2.add(com);
            }
            MathParam.add(new MathParam(list2, time, page, result.get(0), result.get(1), task, yiDo, weiDo, rightToday, errorToday));
        }
        return Response.ok(MathParam);
    }

    //错题
    @ResponseBody
    @RequestMapping(value = "cuoTi")
    @RateLimit(permitsPerSecond = 1, ipLimit = true, description = "限制出题频率")
    public Response<Math> cuoTi() {
        if(RedisUtil.get("cuoTi")!=null){
            Math ma=(Math)RedisUtil.get("cuoTi");
            MathPlan mp= getPlan();
            if(DataUtil.isNotEmpty(mp)){
                ma.setNum(mp.getWeiDo());
            }else{
                ma.setNum(null);
            }
            ma.setSource("来自未做错题缓存!");
            return Response.ok(ma);
        }
        List<com.wusi.reimbursement.entity.Math> maths ;
        String source="";
        if (DataUtil.isEmpty(RedisUtil.get("ti"))) {
            log.error("无缓存,数据库取值");
            com.wusi.reimbursement.entity.Math cuoTi = new com.wusi.reimbursement.entity.Math();
            cuoTi.setResult("错");
            cuoTi.setCount(1);
            cuoTi.setType(0);
            maths = MathService.queryList(cuoTi);
            if(maths.size()==0){
                return Response.ok(null);
            }
            source="无缓存,数据库取值,共计剩余:"+maths.size()+"道错题!";
            //存缓存  存5分钟
            long time=maths.size();
            RedisUtil.set("ti", maths,  time);
        } else {
            long ti = RedisUtil.getExpire("ti");
            source="有缓存,redis取值,缓存剩余"+ti+"秒!";
            log.error("有缓存,redis取值");
            Object object = RedisUtil.get("ti");
            maths = (List<com.wusi.reimbursement.entity.Math>) object;
        }
        Integer size = maths.size();
        Random r = new Random();
        int index = r.nextInt(size);
        com.wusi.reimbursement.entity.Math ti = maths.get(index);
        String content = ti.getContent();
        String back = content.replace("-", "+");
        int i = back.indexOf("+");
        int j = back.indexOf("+", i + 1);
        int k = back.indexOf("=");
        Math vo = new Math();
        vo.setNumOne(Integer.valueOf(back.substring(0, i)));
        vo.setSymbolOne(content.substring(i, i + 1));
        vo.setNumTwo(Integer.valueOf(back.substring(i + 1, j)));
        vo.setSymbolTwo(content.substring(j, j + 1));
        vo.setNumThree(Integer.valueOf(back.substring(j + 1, k)));
        vo.setSource(source);
        vo.setRowId(ti.getId());
        MathPlan plan=getPlan();
        if(DataUtil.isNotEmpty(plan)){
            vo.setNum(plan.getWeiDo());
        }
        RedisUtil.set("cuoTi", vo);
        return Response.ok(vo);
    }

    private MathPlan getPlan() {
        MathPlan query = new MathPlan();
        query.setTime(DateUtil.formatDate(new Date(), "yyyy-MM-dd"));
        MathPlan plan = MathPlanService.queryOne(query);
        return plan;
    }

    @ResponseBody
    @RequestMapping(value = "homeworkTotal")
    @RateLimit(permitsPerSecond = 1, ipLimit = true, description = "限制出题频率")
    public Response<Page<MathPlanVo>> homeworkTotal(MathPlanQuery query) {
        if (DataUtil.isEmpty(query.getPage())) {
            query.setPage(0);
        }
        if (DataUtil.isEmpty(query.getLimit())) {
            query.setLimit(2);
        }
        Pageable pageable = PageRequest.of(query.getPage(), query.getLimit());
        Page<MathPlan> page = MathPlanService.queryPage(query, pageable);
        List<MathPlanVo> voList = new ArrayList<>();
        for (MathPlan plan : page.getContent()) {
            voList.add(getPlanVo(plan));
        }
        Page<MathPlanVo> voPage = new PageImpl<>(voList, pageable, page.getTotalElements());
        return Response.ok(voPage);
    }

    private MathPlanVo getPlanVo(MathPlan plan) {
        MathPlanVo vo = new MathPlanVo();
        vo.setTask(plan.getTask());
        vo.setYiDo(plan.getYiDo());
        vo.setWeiDo(plan.getWeiDo());
        vo.setTime(plan.getTime());
        vo.setRight(plan.getRight());
        vo.setError(plan.getError());
        BigDecimal one=new BigDecimal(plan.getRight());
        BigDecimal two=new BigDecimal(MoneyUtil.add(plan.getRight(), plan.getError()));
        vo.setRate(one.divide(two, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).setScale(2).toString()+"%");
        return vo;
    }

    @ResponseBody
    @RequestMapping(value = "getTiKindTwo")
    @RateLimit(permitsPerSecond = 1, ipLimit = true, description = "限制出题频率")
    public Response<Math> homeworkTotal() {
        Response<Math> ti = this.getTi(10);
        Math data = ti.getData();
        int numberOne=0;
        if(RedisUtil.get("index")!=null){
            numberOne=(Integer) RedisUtil.get("index");
        }else{
            Random r = new Random();
            numberOne = r.nextInt(3);
            RedisUtil.set("index", numberOne);
        }
        if(numberOne==0){
            data.setNumOne(null);
        }
        if(numberOne==1){
            data.setNumTwo(null);
        }
        if(numberOne==2){
            data.setNumThree(null);
        }

        data.setIndex(numberOne);
        MathPlan plan = getPlan();
        if(DataUtil.isNotEmpty(plan)){
            data.setNum(plan.getWeiDo());
        }
        return Response.ok(data);
    }

    @ResponseBody
    @RequestMapping(value = "checkTiKindTwo")
    @RateLimit(permitsPerSecond = 1, ipLimit = true, description = "限制出题频率")
    public Response<String> checkTiKindTwo(Math math) {
        if(DataUtil.isEmpty(math.getNumOne())||DataUtil.isEmpty(math.getNumTwo())||DataUtil.isEmpty(math.getNumThree())){
            return Response.fail("请填写答案!");
        }
        int b=check(math, null, null,3);
        if(b==math.getResult()){
            RedisUtil.del("redisTi");
            return Response.ok("答对了,小柠檬不错哦~");
        } else {
            return Response.ok("答错了,小柠檬加油哦~");
        }
    }
    @ResponseBody
    @RequestMapping(value = "getchengChuTi")
    @RateLimit(permitsPerSecond = 1, ipLimit = true, description = "限制出题频率")
    public Response<Math> getchengChuTi(Integer size) {
        //先判断缓存是否有未做的题
        if(RedisUtil.get("getchengChuTi")!=null){
            Math ma=(Math)RedisUtil.get("getchengChuTi");
            MathPlan mp= getPlan();
            if(DataUtil.isNotEmpty(mp)){
                ma.setNum(mp.getWeiDo());
            }else{
                ma.setNum(null);
            }
            return Response.ok(ma);
        }
        Math res = new Math();
            Random r = new Random();
        int numberOne;
            while(true){
                numberOne = r.nextInt(size);
                if(numberOne>0){
                    break;
                }
            }
            int numberTwo = r.nextInt(size);
            int number = r.nextInt(3);
            int result=numberOne*numberTwo;
            if(number%2==0){
                res.setSymbolOne("×");
                res.setNumOne(numberOne);
                res.setNumTwo(numberTwo);
                res.setResult(result);
            }else{
                res.setSymbolOne("÷");
                res.setNumOne(result);
                res.setNumTwo(numberOne);
                res.setResult(result/numberOne);
            }
        MathPlan plan = getPlan();
        if(DataUtil.isNotEmpty(plan)){
            res.setNum(plan.getWeiDo());
        }
        //避免退出刷新换题
        RedisUtil.set("getchengChuTi", res);
        return Response.ok(res);
    }
    static SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.PATTERN_YYYY_MM_DD);
    @ResponseBody
    @RequestMapping(value = "checkChengChu")
    @RateLimit(permitsPerSecond = 1, ipLimit = true, description = "限制出题频率")
    public Response<String> checkChengChu(Math math) {
        if(DataUtil.isEmpty(math.getResult())){
            return Response.fail("请填写答案!");
        }
        int b=check(math, null, null,1);
        if(b==math.getResult()){
            RedisUtil.del("getchengChuTi");
            return Response.ok("答对了,小柠檬不错哦~");
        } else {
            return Response.ok("答错了,小柠檬加油哦~");
        }
    }

    @ResponseBody
    @RequestMapping(value = "getAwardList")
    @RateLimit(permitsPerSecond = 1, ipLimit = true, description = "限制出题频率")
    public Response<Page<AwardRecordVo>> getAwardList(AwardRecordQuery query) {
        if (DataUtil.isEmpty(query.getPage())) {
            query.setPage(0);
        }
        if (DataUtil.isEmpty(query.getLimit())) {
            query.setLimit(5);
        }
        Pageable pageable = PageRequest.of(query.getPage(), query.getLimit());
        Page<AwardRecord> page = awardRecordService.queryPage(query, pageable);
        List<AwardRecordVo> voList = new ArrayList<>();
        for (AwardRecord award : page.getContent()) {
            voList.add(getAwardVo(award));
        }
        Page<AwardRecordVo> voPage = new PageImpl<>(voList, pageable, page.getTotalElements());
        return Response.ok(voPage);
    }

    private AwardRecordVo getAwardVo(AwardRecord award) {
        AwardRecordVo vo=new AwardRecordVo();
        vo.setAwardName(award.getAwardName());
        vo.setCreateTime(sdf.format(award.getCreateTime()));
        vo.setStateDesc(award.getStateDesc());
        vo.setTypeDesc(award.getTypeDesc());
        vo.setState(award.getState());
        vo.setType(award.getType());
        vo.setId(award.getId());
        return vo;
    }
    @RequestMapping(value = "handleAwardState")
    public Response<String> handleAwardState(Long id,Integer value){
        if(DataUtil.isEmpty(value)||DataUtil.isEmpty(id)){
            return Response.fail("参数有误!");
        }
        AwardRecord awardRecord = awardRecordService.queryById(id);
        awardRecord.setState(value);
        awardRecordService.updateById(awardRecord);
        return Response.ok("更新成功!");
    }

    //数的比较
    @RequestMapping(value = "compareNum")
    public Response<CompareNum> handleAwardState(Integer size){
        Random r = new Random();
        CompareNum compareNum=new CompareNum();
        int numberOne = r.nextInt(size);
        compareNum.setNumOne(String.valueOf(numberOne));
        int numberTwo = r.nextInt(size);
        compareNum.setNumTwo(String.valueOf(numberTwo));
        return Response.ok(compareNum);
    }
    //数的比较
    @RequestMapping(value = "checkCompare")
    public Response<String> checkCompare(String numOne,String fu,String numTwo){
        Integer one= Integer.valueOf(numOne);
        Integer two= Integer.valueOf(numTwo);
        Boolean result=false;
        if("＜".equals(fu)){
            result=one<two;
        }else if("＞".equals(fu)){
            result=one>two;
        }else{
            result=one==two;
        }
        if(result){
           return Response.ok("答对了,小柠檬不错哦~");
        }else{
            return Response.ok("答错了,小柠檬继续加油~");
        }
    }
    @RequestMapping(value = "changeFestival")
    public Response<String> handleAwardState(String content,String date) throws ParseException {
         SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.PATTERN_YYYY_MM_DD_HH_MM_SS);
        Festival festival = festivalService.queryById(2L);
        festival.setName(content);
        festival.setTime(sdf.parse(date+" 00:00:01").getTime());
        festivalService.updateById(festival);
        return Response.ok("修改成功！");
    }

}
