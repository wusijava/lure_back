package com.wusi.reimbursement.utils;


import com.wusi.reimbursement.entity.SsqStop;
import com.wusi.reimbursement.mapper.SsqStopMapper;
import com.wusi.reimbursement.service.SsqStopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @ Description   :  计算ssq期号
 * @ Author        :  wusi
 * @ CreateDate    :  2021/1/6$ 12:32$
 */
@Component
public class WeekUtils {
    private static SsqStopMapper SsqStopMapper;
    @Resource
    public  void setSsqStopMapper(SsqStopMapper ssqStopMapper){
        this.SsqStopMapper=ssqStopMapper;
    }

    public static String getSsqNum(Date date) throws ParseException {
        if(DataUtil.isEmpty(date)){
            date=new Date();
        }
        DateFormat sdf = new SimpleDateFormat(DateUtil.PATTERN_YYYY_MM_DD_HH_MM_SS);
        DateFormat sdf2 = new SimpleDateFormat(DateUtil.PATTERN_YYYY_MM_DD);
        Calendar calendar = Calendar.getInstance();
        String yearStr = String.valueOf(calendar.get(Calendar.YEAR));
        long days = DateUtil.betweenDays(sdf.parse(yearStr + "-01-01 00:00:00"), date);
        Calendar start = Calendar.getInstance();
        start.setTime(sdf.parse(yearStr + "-01-01 00:00:00"));
        int num = 0;
        //春节,国庆休市
        List<SsqStop> ssqStops = SsqStopMapper.queryAll();
        for (int i = 0; i < days; i++) {
            int w = start.get(Calendar.DAY_OF_WEEK) - 1;
            if (w < 0) {
                w = 0;
            }
            if (w == 0 || w == 2 || w == 4) {
                boolean isFestival=false;
                for(SsqStop stop:ssqStops){
                    //如果在休市内
                    if((start.getTime().getTime()>=stop.getStart().getTime())&&(start.getTime().getTime()<stop.getStop().getTime())){
                        isFestival=true;
                    }
                }
                if(!isFestival){
                    num++;
                }
            }
            start.add(Calendar.DAY_OF_YEAR, 1);
        }

        String format = sdf2.format(date);
        Calendar query = Calendar.getInstance();
        query.setTime(sdf2.parse(format));
        Date parse = sdf.parse(format + " 20:00:00");
        int w = query.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        if (w == 0 || w == 2 || w == 4) {
            //开奖当天20点后 只能获取下一期了
            boolean isJie=false;
            if (date.getTime() > parse.getTime()) {
                for(SsqStop stop:ssqStops){
                    //如果在休市内
                    if((date.getTime()>=stop.getStart().getTime())&&(date.getTime()<stop.getStop().getTime())){
                        isJie=true;
                    }
                }
                if(!isJie){
                    num++;
                }
            }
        }

        String numStr = String.valueOf(num + 1);
        if (numStr.length() < 3) {
            //if(String.valueOf(num).length()<3){
            for (int j = 1; j <= 3 - numStr.length(); j++) {
                numStr = "0" + numStr;
            }
        }
        String result = yearStr + "-" + numStr;
        return result;
    }
}
