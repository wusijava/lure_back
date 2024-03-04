package com.wusi.reimbursement.entity;

import com.wusi.reimbursement.common.Identifiable;
import lombok.Data;

import java.util.Date;

@Data
public class AwardList implements Identifiable<Long> {

  private Long id;
  /**
   * 奖励或惩罚名称
   */
  private String awardName;
  /**
   * 类型  0惩罚 1奖励
   */
  private Integer type;
  /**
   * 惩罚或奖励级别
   */
  private Integer level;
  /**
   * 创建用户
   */
  private String createUser;
  /**
   * 创建时间
   */
  private Date createTime;


}
