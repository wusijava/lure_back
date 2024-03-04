package com.wusi.reimbursement.controller;

import lombok.Data;

@Data
public class YearLureData {
    /**
     * 年
     */
    private String year;
    /**
     * 鱼获总数量
     */
    private Integer totalNum;

    /**
     * 出勤次数
     */
    private Integer num;

    /**
     * 年度打龟率
     */
    private String rate;



}
