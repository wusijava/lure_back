package com.wusi.reimbursement.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FishShare {
    private Long id;
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

    /**
     * 鱼友圈评论
     */
    private List<FishCommentVo> comment= new ArrayList<>();

    private List<String> zan;

    private Boolean isZan;


}
