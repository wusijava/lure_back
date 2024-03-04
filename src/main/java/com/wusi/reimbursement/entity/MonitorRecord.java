package com.wusi.reimbursement.entity;

import com.wusi.reimbursement.common.Identifiable;
import lombok.Data;

import java.util.Date;

/**
 * 每天的监控记录
 */
@Data
public class MonitorRecord implements Identifiable<Long> {

  private Long id;
  /**
   * 扫描总数
   */
  private String total;
  /***
   * 爱美下架个数
   */
  private String amyOfflineNum;
  /**
   * 爱美上架个数
   */
  private String amyOnNum;

  /**
   * 创建时间
   */
  private Date createTime;
  /**
   * 是处理
   */
  private String isDo;


}
