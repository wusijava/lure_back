package com.wusi.reimbursement.controller;

import com.wusi.reimbursement.common.Response;
import com.wusi.reimbursement.entity.*;
import com.wusi.reimbursement.query.ClassPlanQuery;
import com.wusi.reimbursement.service.ClassPlanService;
import com.wusi.reimbursement.utils.DataUtil;
import com.wusi.reimbursement.utils.DateUtil;
import com.wusi.reimbursement.vo.ClassPlanVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ Description   :  排班计划
 * @ Author        :  wusi
 * @ CreateDate    :  2021/4/30$ 11:38$
 */
@RestController
@RequestMapping(value = "api/open/")
public class ClassPlanController {
    @Autowired
    private ClassPlanService classPlanService;
    @RequestMapping(value = "list")
    public Response<List<ClassPlanVo>> getList(ClassPlanQuery query) throws ParseException {
        DateFormat sdf = new SimpleDateFormat(DateUtil.PATTERN_YYYY_MM_DD);
        if (DataUtil.isEmpty(query.getPage())) {
            query.setPage(0);
        }
        query.setLimit(3);
        query.setPage(query.getLimit()*(query.getPage()));
        List<String> dateList=new ArrayList<>();
        if(DataUtil.isEmpty(query.getStartTime())||DataUtil.isEmpty(query.getEndTime())){
            //默认查询前后三天
            Date nowDate=new Date();
            Calendar startCalendar = Calendar.getInstance();
            String nowDateStr=sdf.format(nowDate);
            startCalendar.setTime(nowDate);
            startCalendar.add(Calendar.DAY_OF_MONTH, -1);
            Date pfeDate=startCalendar.getTime();
            String pfeDateStr=sdf.format(pfeDate);
            startCalendar.add(Calendar.DAY_OF_MONTH, 2);
            Date nextDate=startCalendar.getTime();
            String nextDateStr=sdf.format(nextDate);
            dateList.add(nowDateStr);
            dateList.add(pfeDateStr);
            dateList.add(nextDateStr);
            query.setDateList(dateList);
        }else{
            Date start=sdf.parse(query.getStartTime());
            Date end=sdf.parse(query.getEndTime());
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(start);
            long days = DateUtil.betweenDays(start, end);
            for (int i = 1; i <= days + 1; i++) {
                dateList.add(sdf.format(start));
                startCalendar.add(Calendar.DAY_OF_MONTH, 1);
                start=startCalendar.getTime();
            }
            query.setDateList(dateList);
        }
        long count=0;
        count=classPlanService.querycount(query);
        long page=0;
        if(count%query.getLimit()==0){
            page=count/query.getLimit();
        }else{
            page=count/query.getLimit()+1;
        }

        List<ClassPlan> planOne = classPlanService.queryListByParam(query);
        List<String> list = planOne.stream().map(ClassPlan::getDate).collect(Collectors.toList());
        ArrayList<ClassPlanVo> ssqParams = new ArrayList<>();
        for (String s : list) {
            ClassPlanQuery planQuery = new ClassPlanQuery();
            planQuery.setDate(s);
            List<ClassPlan> ssqs = classPlanService.queryList(planQuery);
            ssqParams.add(new ClassPlanVo(s,ssqs,page));
        }
        return Response.ok(ssqParams);
    }

}
