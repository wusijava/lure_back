package com.wusi.reimbursement.vo;


import lombok.Data;

@Data
public class IllegalLogVo {
    private Long id;

    private String uid;

    private String userName;

    private String content;

    private String createTime;

    private String reason;

    /**
     * 1文字  2图片 3视频
     */
    private String typeDesc;

    /**
     * 1鱼获图 2消费图 3头像 4评论
     */
    private String sourceDesc;

}
