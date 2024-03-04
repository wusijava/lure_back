package com.wusi.reimbursement.vo;

import lombok.Data;

import java.util.List;


@Data
public class MonthData {
    /**
     * 月份
     */
    private String  month;
    /**
     * 出勤次数
     */
    private Integer num;
    /**
     * 当月天数
     */
    private Integer monthNum;

    private String time;

    /**
     * 有收获出勤数
     */
    private Integer successNum;

    /**
     * 打龟次数
     */
    private Integer failNum;

    /**
     * 打龟率
     */
    private String rate;

    /**
     * 鱼获数量
     */
    private Integer fishNum;
    private Integer fishNumLastYear;

    /**
     * 放流条数
     */
    private Integer fangNum;

    /**
     * 最大中鱼天数间隔
     */
    private String day;

    private String beginDay;
    private String endDay;

    /**
     * 花费
     */
    private String spend;

    /**
     * 最大几连龟
     */
    private String maxFail;

    private String fishDesc;
}
