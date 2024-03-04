package com.wusi.reimbursement.vo;

import com.wusi.reimbursement.entity.ClassPlan;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 排班计划
 */
@Data
@AllArgsConstructor
public class ClassPlanVo{


  /**
   * 排班日期
   */
  private String date;
  /**
   * list
   */
  private List<ClassPlan> planList;

  private long totalPage;

}
