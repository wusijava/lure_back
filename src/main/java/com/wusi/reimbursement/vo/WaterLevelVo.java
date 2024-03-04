package com.wusi.reimbursement.vo;

import com.wusi.reimbursement.common.Identifiable;
import lombok.Data;

import java.util.Date;

@Data
public class WaterLevelVo  {

  private Long id;
  /**
   * 水位 米
   */
  private String waterLevel;
  /**
   * 地址
   */
  private String address;
  private String href;
  private String createTime;



}
