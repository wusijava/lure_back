package com.wusi.reimbursement.vo;

import lombok.Data;

import java.util.Date;

@Data
public class AwardRecordVo {

  private Long id;
  /**
   * 奖励名称
   */
  private String awardName;
  /**
   * 类型 0惩罚 1奖励
   */
  private Integer type;
  private String typeDesc;
  /**
   * 状态  1已完成  -1 已放弃 不要了 0未完成
   */
  private Integer state;
  private String stateDesc;
  /**
   * 任务监督者
   */
  private String userTo;
  /**
   * 创建时间
   */
  private String  createTime;


}
