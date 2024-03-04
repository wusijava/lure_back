package com.wusi.reimbursement.entity;

import com.wusi.reimbursement.common.Identifiable;
import lombok.Data;

import java.util.Date;

@Data
public class AwardRecord implements Identifiable<Long> {

  private Long id;
  /**
   * 奖励名称
   */
  private String awardName;
  /**
   * 类型 0惩罚 1奖励
   */
  private Integer type;
  /**
   * 状态  1已完成  -1 已放弃 不要了 0未完成
   */
  private Integer state;
  /**
   * 任务监督者
   */
  private String userTo;
  /**
   * 创建时间
   */
  private Date createTime;
  public enum State {
    bu(-1, "放弃"),
    waiting(0, "未完成"),
    success(1, "已完成");

    private Integer code;
    private String desc;

    State(Integer code, String desc) {
      this.code = code;
      this.desc = desc;
    }
    public Integer getCode() {
      return code;
    }

    public void setCode(Integer code) {
      this.code = code;
    }

    public String getDesc() {
      return desc;
    }

    public void setDesc(String desc) {
      this.desc = desc;
    }
  }
  public String getStateDesc() {
    if (State.success.getCode().equals(state)) {
      return "已完成";
    } else if (AwardRecord.State.bu.getCode().equals(state)) {
      return "放弃奖励";
    } else{
      return "未完成";
    }
  }
  public enum Type {
    fa(0, "惩罚"),
    jiang(1, "奖励");

    private Integer code;
    private String desc;

    Type(Integer code, String desc) {
      this.code = code;
      this.desc = desc;
    }
    public Integer getCode() {
      return code;
    }

    public void setCode(Integer code) {
      this.code = code;
    }

    public String getDesc() {
      return desc;
    }

    public void setDesc(String desc) {
      this.desc = desc;
    }
  }
  public String getTypeDesc() {
    if (Type.fa.getCode().equals(type)) {
      return "惩罚";
    } else {
      return "奖励";
    }
  }
}
