package com.wusi.reimbursement.vo;

import lombok.Data;

@Data
public class FishCount {
    /**
     * 鱼种
     */
    private String fishName;

    /**
     * 数量
     */
    private Integer num;

    private String img;

    private String desc;
}
