package com.wusi.reimbursement.utils;

import com.wusi.reimbursement.entity.SsqBonus;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * @ Description   :  开奖号码获取工具_官网
 * @ Author        :  wusi
 * @ CreateDate    :  2021/1/8$ 12:45$
 */
@Slf4j
public class SsqNumGuanWangUtils {
    public static  SsqBonus getSsqNum() throws ParseException, IOException {
        log.error("获取开奖号码开始执行,{}", DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        SsqBonus ssq = new SsqBonus();
        String html = Jsoup.connect("https://shuangseqiu.cjcp.com.cn/").timeout(200000).execute().body();
        //System.out.println(html);
        List<String> list = GetJsonValue(html, "<div class=\"red_q\">");
        ssq.setTerm(list.get(0));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ssq.setCreateTime(sdf.parse(list.get(1)));
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(ssq.getCreateTime());
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        ssq.setRed4(list.get(5));
        ssq.setRed5(list.get(6));
        ssq.setRed6(list.get(7));
        ssq.setBlue(list.get(8));
        ssq.setWeek(weekDays[w]);
        ssq.setRed1(list.get(2));
        ssq.setRed2(list.get(3));
        ssq.setRed3(list.get(4));
        return ssq;
    }

        public static List<String> GetJsonValue(String jsonStr, String key) {
            List<String> ball=new ArrayList<>();
            //获取期数
            int index = jsonStr.indexOf("中国福利彩票双色球第");
            String term=jsonStr.substring(index+"中国福利彩票双色球第".length(), index+"中国福利彩票双色球第".length()+4)+"-"+jsonStr.substring(index+"中国福利彩票双色球第".length()+4, index+"中国福利彩票双色球第".length()+7);
            //开奖日期
            int bonusIndex = jsonStr.indexOf("开奖日期:");
            String bonusTime=jsonStr.substring(bonusIndex+"开奖日期:".length(), bonusIndex+"开奖日期:".length()+"2021-03-07".length());
            ball.add(term);
            ball.add(bonusTime);
            for(int i=0;i<6;i++){
                String value=getStr(jsonStr,key);
                ball.add(value);
                jsonStr=jsonStr.replace(key+value, value);
            }
            String blue=getStr(jsonStr,"<div class=\"blue_q\">");
            ball.add(blue);
            return ball;
        }
        public static String getStr(String jons,String div){
            int index = jons.indexOf(div);
            int length = div.length();
            return jons.substring(index + length, index + length+2);
        }
}
