package com.wusi.reimbursement.vo;

import lombok.Data;

import java.util.Date;

/**
 * wusi
 */
@Data
public class MathPlanVo  {

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
   * 正确数量
   */
  private String right;
  /**
   * 错误数量
   */
  private String error;
  /**
   * 正确率
   */
  private String rate;

}
