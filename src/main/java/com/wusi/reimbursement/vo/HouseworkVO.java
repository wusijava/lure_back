package com.wusi.reimbursement.vo;

import com.wusi.reimbursement.common.Identifiable;
import lombok.Data;

import java.util.Date;

/**
 * @ Description   :  housework
 * @ Author        :  wusi
 * @ CreateDate    :  2020/12/28$ 14:10$
 */
@Data
public class HouseworkVO  {
    private Long id;
    /**
     * 任务安排人
     */
    private String userNameBy;
    /**
     * 任务接受人
     */
    private String userNameTo;
    /**
     * 家务内容
     */
    private String content;

    /**
     * 备注
     */
    private String remark;
    /**
     * 是否接受任务 1已接受 0等待接受  -1已拒绝
     */
    private Integer receiveState;
    private String receiveStateDesc;
    /**
     * 状态 1已完成  0未完成
     */
    private Integer state;
    private String  stateDesc;
    /**
     * 评分
     */
    private String score;
    /**
     * 任务创建时间
     */
    private String createTime;
    /**
     * 任务要求完成时间
     *
     */
    private String requiredFinishTime;
    /**
     * 实际完成时间
     *
     */
    private String realityFinishTime;

}
