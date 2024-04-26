package com.wusi.reimbursement.entity;

import com.wusi.reimbursement.common.Identifiable;
import lombok.Data;

import java.util.Date;

@Data
public class FishComment  implements Identifiable<Long> {
    private Long id;

    private Long fishId;

    private Long replyId;

    private String comment;

    private String commentUid;

    private String commentName;

    private Date createTime;

    /**
     * type=1 评论   type=2 赞
     */
    private Integer type;

}
