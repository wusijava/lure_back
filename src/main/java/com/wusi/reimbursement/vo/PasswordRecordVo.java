package com.wusi.reimbursement.vo;

import lombok.Data;

import java.util.Date;

/**
 * 加密密码
 */
@Data
public class PasswordRecordVo {

  private Long id;
  /**
   * 账号或系统
   */
  private String acount;
  /**
   * 用户
   */
  private String user;
  /**
   * 创建时间
   */
  private String createTime;

}
