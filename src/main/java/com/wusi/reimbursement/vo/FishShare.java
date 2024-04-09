package com.wusi.reimbursement.vo;

import lombok.Data;

@Data
public class FishShare {
    private String name;

    private String kind;

    private String lure;

    private String size;

    private String url;

    private String use;

    /**
     * 鱼友圈显示时间
     */
    private String time;

    /**
     * 地址
     */
    private String add;

    private String img;
}
