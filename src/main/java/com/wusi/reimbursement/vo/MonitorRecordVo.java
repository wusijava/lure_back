package com.wusi.reimbursement.vo;

import lombok.Data;


/**
 * 每天的监控记录
 */
@Data
public class MonitorRecordVo  {

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
  private String createTime;
  /**
   * 是处理
   */
  private String isDo;


}
