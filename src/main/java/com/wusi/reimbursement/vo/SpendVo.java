package com.wusi.reimbursement.vo;


import com.wusi.reimbursement.common.Identifiable;
import lombok.Data;

@Data
public class SpendVo implements Identifiable<Long> {
  private Integer index;
  private Long id;
  private String item;
  private String price;
  private String consumer;
  private String date;
  private String remark;

  public static String[] headers = {
          "序号",
          "消费明细",
          "支出",
          "消费人",
          "消费时间",
          "备注",

  };

  public static String[] keys = {
          "index",
          "item",
          "price",
          "consumer",
          "date",
          "remark",
  };



}
