package com.wusi.reimbursement.entity;

import com.wusi.reimbursement.common.Identifiable;
import lombok.Data;

import java.util.Date;

/**
 * 加密密码
 */
@Data
public class PasswordRecord implements Identifiable<Long> {

  private Long id;
  /**
   * 账号或系统
   */
  private String acount;
  /**
   * 密文密码
   */
  private String password;
  /**
   * 用户
   */
  private String user;
  /**
   * 创建时间
   */
  private Date createTime;

}
