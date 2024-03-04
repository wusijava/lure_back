package com.wusi.reimbursement.entity;

import com.wusi.reimbursement.common.Identifiable;
import lombok.Data;

import java.util.Date;

@Data
public class OperateInfo implements Identifiable<Long> {

  private Long id;
  /**
   * 用户名
   */
  private String name;
  /**
   * 加密后的密码
   */
  private String password;


  /**
   * 状态  1正常 0冻结
   */
  private Integer state;
  /**
   * 创建时间
   */
  private Date createTime;

  public enum State {
    /**
     * 开启
     */
    OPEN(1),
    /**
     * 关闭
     */
    CLOSE(0);


    private Integer code;

    State(Integer code) {
      this.code = code;
    }

    public Integer getCode() {
      return code;
    }

    public void setCode(Integer code) {
      this.code = code;
    }
  }
}
