package com.wusi.reimbursement.entity;

import com.wusi.reimbursement.common.Identifiable;
import lombok.Data;

import java.util.Date;


@Data
public class User implements Identifiable<Long> {

  private Long id;
  private String username;
  private String password;
  private String mobile;
  private String nickName;
  private String uid;
  private Date createTime;
  private String salt;
  private Integer state;
  private Integer type;
  private String storeMarkCode;
  private String pwd;
  private String cityCode;
  private String provinceCode;
  private String remark;

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


  public enum Type {
    /**
     * 管理员
     */
    ADMIN(0),
    /**
     * 门店角色
     */
    USER(1),
    /**
     * 商户角色
     */
    MANAGE(2),
    /**
     * 市级账号
     * */
    CITY(3),
    /**
     * 省级账号
     * */
    PROVINCE(4);

    private Integer code;

    Type(Integer code) {
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
