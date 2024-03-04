package com.wusi.reimbursement.entity;

import com.wusi.reimbursement.common.Identifiable;
import lombok.Data;

import java.util.Date;

/**
 * 排班计划
 */
@Data
public class ClassPlan implements Identifiable<Long> {

  private Long id;
  /**
   * 排班日期
   */
  private String date;
  /**
   * 员工姓名
   */
  private String name;
  /**
   * 班次0休息1早班2插班3晚班
   */
  private String classes;
  /**
   * 创建时间
   */
  private Date cteateTime;

  //0休息，a早班，b晚班，c插班
  public enum Classes {
    xiuxi("O", "休息"),
    zao("A", "早班"),
    cha("C", "插班"),
    wan("B", "晚班"),
    meeting("E", "公司开会");

    private String code;
    private String desc;

    Classes(String code, String desc) {
      this.code = code;
      this.desc = desc;
    }
    public String getCode() {
      return code;
    }

    public void setCode(String code) {
      this.code = code;
    }

    public String getDesc() {
      return desc;
    }

    public void setDesc(String desc) {
      this.desc = desc;
    }
  }
  public String getClassPlanDesc() {
    if (Classes.xiuxi.getCode().equals(classes)) {
      return "休息";
    } else if(Classes.zao.getCode().equals(classes)) {
      return "早班";
    }else if (Classes.cha.getCode().equals(classes)){
      return "插班";
    }else if (Classes.meeting.getCode().equals(classes)){
      return "公司开会";
    }else{
      return "晚班";
    }
  }

}
