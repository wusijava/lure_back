package com.wusi.reimbursement.vo;

import com.wusi.reimbursement.entity.Reimbursement;
import lombok.Data;

/**
 * @ Description   :  报销list
 * @ Author        :  wusi
 * @ CreateDate    :  2020/1/9$ 11:30$
 */
@Data
public class ReimbursementVo {
    private  Integer index;
    private Long id;
    private String productName;
    private String totalPrice;
    private String buyChannel;
    private String buyDate;
    private String reimbursementDate;
    private String remitDate;
    private String state;
    private String remark;

    public Integer getStatecode(String Desc){
        if (Reimbursement.State.success.getDesc().equals(Desc)){
            return Reimbursement.State.success.getCode();
        }  else if (Reimbursement.State.waiting.getDesc().equals(Desc)){
            return Reimbursement.State.waiting.getCode();
        }else {
            return Reimbursement.State.no.getCode();
        }
    }
    public static String[] headers = {
            "序号",
            "商品名称",
            "总价格",
            "购买渠道",
            "购买时间",
            "上交报销单时间",
            "报销到账时间",
            "报销状态",
            "备注",

    };
    /*
    * productName;
  private String totalPrice;
  private String buyChannel;
  private Date buyDate;
  private Date reimbursementDate;
  private Date remitDate;
  private Integer state;
  private String remark;*/
    public static String[] keys = {
            "index",
            "productName",
            "totalPrice",
            "buyChannel",
            "buyDate",
            "reimbursementDate",
            "remitDate",
            "state",
            "remark",
    };

}
