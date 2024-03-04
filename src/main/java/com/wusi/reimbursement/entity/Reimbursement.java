package com.wusi.reimbursement.entity;


import com.wusi.reimbursement.common.Identifiable;
import lombok.Data;

import java.util.Date;

@Data
public class Reimbursement implements Identifiable<Long> {

  private Long id;
  private String productName;
  private String totalPrice;
  private String buyChannel;
  private Date buyDate;
  private Date reimbursementDate;
  private Date remitDate;
  private Integer state;
  private String remark;
  public enum State {
    no(-1, "未报销"),
    waiting(0, "报销中"),
    success(1, "已报销");

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
  public String getStateDesc(){
    if (State.success.getCode().equals(state)){
      return State.success.getDesc();
    }  else if (State.waiting.getCode().equals(state)){
      return State.waiting.getDesc();
    }else {
      return State.no.getDesc();
    }
  }
  public Integer getStatecode(String Desc){
    if (State.success.getDesc().equals(Desc)){
      return State.success.getCode();
    }  else if (State.waiting.getDesc().equals(Desc)){
      return State.waiting.getCode();
    }else {
      return State.no.getCode();
    }
  }

}
