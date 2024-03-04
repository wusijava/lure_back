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
 * @ Description   :  开奖号码获取工具---百度
 * @ Author        :  wusi
 * @ CreateDate    :  2021/1/8$ 12:45$
 */
@Slf4j
public class SsqNumUtils {
    public static  SsqBonus getSsqNum() throws ParseException, IOException {
        log.error("获取开奖号码开始执行,{}", DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        SsqBonus ssq = new SsqBonus();
        String html = Jsoup.connect("https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=%E5%8F%8C%E8%89%B2%E7%90%83&fenlei=256&rsv_pq=80b37df9000408f5&rsv_t=010coSUSP%2FnvityAj%2FwITb2JWy1prLMoue7ZVxtbRk0cHcElsUmVreYjJzQ&rqlang=cn&rsv_dl=tb&rsv_enter=1&rsv_sug3=10&rsv_sug1=14&rsv_sug7=101&rsv_sug2=0&rsv_btype=i&inputT=4181&rsv_sug4=5061").timeout(200000).execute().body();
        //System.out.println(html);
        List<String> list = GetJsonValue(html, "<span class=\"c-icon c-icon-ball-red op_caipiao_ball_red c-gap-right-small\">");
        ssq.setTerm(list.get(0));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ssq.setCreateTime(sdf.parse(list.get(1)));
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(ssq.getCreateTime());
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        ssq.setWeek(weekDays[w]);
        ssq.setRed1(list.get(2));
        ssq.setRed2(list.get(3));
        ssq.setRed3(list.get(4));
        ssq.setRed4(list.get(5));
        ssq.setRed5(list.get(6));
        ssq.setRed6(list.get(7));
        ssq.setBlue(list.get(8));
        return ssq;
    }
    public static List<String> GetJsonValue(String jsonStr, String key) {
        List<String> ball = new ArrayList<>();
        //获取期数
        int index = jsonStr.indexOf("期开奖结果");
        String term = jsonStr.substring(index - 5, index);
        String start = term.substring(0, 2);
        String end = term.substring(2, 5);
        term="20"+start+"-"+end;
        ball.add(term);
        //开奖日期
        int bonusIndex = jsonStr.indexOf("开奖日期：");
        String bonusTime = jsonStr.substring(bonusIndex + "开奖日期：".length(), bonusIndex + "开奖日期：".length() + "2021-01-03 21:15:00".length());
        ball.add(bonusTime);
        for (int i = 0; i < 6; i++) {
            String value = getStr(jsonStr, key);
            ball.add(value);
            jsonStr = jsonStr.replace(key + value, value);
        }
        String blue = getStr(jsonStr, "<span class=\"c-icon c-icon-ball-blue op_caipiao_ball_blue c-gap-right-small\">");
        ball.add(blue);
        return ball;
    }

    public static String getStr(String jons, String div) {
        int index = jons.indexOf(div);
        int length = div.length();
        return jons.substring(index + length, index + length + 2);
    }
}
