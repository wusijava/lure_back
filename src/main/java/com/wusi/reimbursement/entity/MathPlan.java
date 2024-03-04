package com.wusi.reimbursement.entity;

import com.wusi.reimbursement.common.Identifiable;
import lombok.Data;

import java.util.Date;

/**
 * wusi
 */
@Data
public class MathPlan implements Identifiable<Long> {

  private Long id;
  /**
   * 总任务
   */
  private String task;
  /**
   * 已做
   */
  private String yiDo;
  /**
   * 未做
   */
  private String weiDo;
  /**
   * 日期
   */
  private String time;
  /**
   * 创建时间
   */
  private Date createTime;
  /**
   * 正确数量
   */
  private String right;
  /**
   * 错误数量
   */
  private String error;

}
