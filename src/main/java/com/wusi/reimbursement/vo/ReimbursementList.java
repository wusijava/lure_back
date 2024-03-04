package com.wusi.reimbursement.vo;

import com.wusi.reimbursement.entity.Reimbursement;
import lombok.Data;


/**
 * @ Description   :  报销list
 * @ Author        :  wusi
 * @ CreateDate    :  2020/1/9$ 11:30$
 */
@Data
public class ReimbursementList  {
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

}
