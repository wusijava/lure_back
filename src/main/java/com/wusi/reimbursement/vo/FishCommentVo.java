package com.wusi.reimbursement.vo;

import lombok.Data;

import java.util.List;

/**
 * 鱼友圈评论
 */
@Data
public class FishCommentVo {

    private Long id;

    private Long fishId;

    private String comment;

    private String name;

    private List<FishCommentVo> reply;

    private String time;

}
