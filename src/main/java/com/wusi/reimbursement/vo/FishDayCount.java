package com.wusi.reimbursement.vo;

import lombok.Data;

@Data
public class FishDayCount {
    /**
     * 鱼种
     */
    private String fish;

    /**
     * 上次中鱼时间
     */
    private String lastDate;

    /**
     * 距离天数
     */
    private String day;
}
