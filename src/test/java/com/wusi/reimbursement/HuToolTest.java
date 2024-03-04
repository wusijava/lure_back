package com.wusi.reimbursement;

import cn.hutool.core.date.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.Console;
import java.util.Calendar;
import java.util.Date;

/**
 * @ Description   :  测试使用HuTool工具类 官网:https://hutool.cn/
 * @ Author        :  wusi
 * @ CreateDate    :  2021/2/6$ 15:04$
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class HuToolTest {




    @Test
    public void test1() throws InterruptedException {
        //当前时间
        Date date = DateUtil.date();
        Date date2 = DateUtil.date(Calendar.getInstance());
        Date date3 = DateUtil.date(System.currentTimeMillis());
        //System.out.println(date3);
        //当前时间字符串，格式：yyyy-MM-dd HH:mm:ss
        String now = DateUtil.now();
        //当前日期字符串，格式：yyyy-MM-dd
        String today = DateUtil.today();
        //获得年的部分
        int year = DateUtil.year(date);

        //获得月份，从0开始计数
        int month = DateUtil.month(date);

        //获得月份枚举
        Month month1 = DateUtil.monthEnum(date);

        String dateStr = "2017-03-01 22:33:23";
        Date date1 = DateUtil.parse(dateStr);
        //一天的开始，结果：2017-03-01 00:00:00
        Date beginOfDay = DateUtil.beginOfDay(date1);
        String str=DateUtil.formatDate(beginOfDay);
        //常用格式的格式化，结果：2017-03-01 formatDate
        String strDateTime=DateUtil.formatDateTime(beginOfDay);
        //结果：2017-03-01 00:00:00  formatDateTime

        String strTime=DateUtil.formatTime(date1);
        //日期偏移 结果:2017-03-03 22:33:23
        Date dateOffset=DateUtil.offset(date1, DateField.DAY_OF_MONTH,2);
        //偏移天数 2017-03-04 22:33:23
        Date dateOffsetDay=DateUtil.offsetDay(date1, 3);
        //时间偏移  结果:2017-03-01 19:33:23
        Date dateOffsetHour=DateUtil.offsetHour(date1, -3);
        //针对当前时间，提供了简化的偏移方法（例如昨天、上周、上个月等）

        //结果:2021-02-05 15:39:33  当前时间的前一天
        DateTime yesterday = DateUtil.yesterday();
        //DateUtil.tomorrow() 明天
        //DateUtil.lastWeek() 上周
        //DateUtil.nextWeek() 下周
        //DateUtil.lastMonth() 上个月
        //DateUtil.nextMonth() 下个月
        String dateStr2 = "2017-04-01 23:33:23";
        Date dateTwo = DateUtil.parse(dateStr2);
        //相差一个月，31天
        long betweenDay = DateUtil.between(date1, dateTwo, DateUnit.MINUTE);
        //结果:31
        String s = DateUtil.formatBetween(betweenDay, BetweenFormatter.Level.MINUTE);
        TimeInterval timer = DateUtil.timer();
        //Thread.sleep(1000*60);
        long interval = timer.interval();//花费毫秒数
        //返回花费时间，并重置开始时间
        long l = timer.intervalRestart();
        long min = timer.intervalMinute();//花费分钟数
        int i = DateUtil.ageOfNow("1990-01-30");
        //是否闰年
        boolean leapYear = DateUtil.isLeapYear(2017);
        //System.out.println(leapYear);
        System.out.println(3|9);

    }
}
