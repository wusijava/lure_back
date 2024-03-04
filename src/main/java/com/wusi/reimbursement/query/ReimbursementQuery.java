package com.wusi.reimbursement.query;

import com.wusi.reimbursement.entity.Reimbursement;
import lombok.Data;

/**
 * @ Description   :  报销查询类
 * @ Author        :  wusi
 * @ CreateDate    :  2020/1/9$ 11:10$
 */
@Data
public class ReimbursementQuery extends Reimbursement {
    private Integer page;

    private Integer limit;

    private Integer offset;
    private String startTime;

    private String endTime;
}
