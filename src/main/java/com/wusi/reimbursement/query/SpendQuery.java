package com.wusi.reimbursement.query;

import com.wusi.reimbursement.entity.Reimbursement;
import com.wusi.reimbursement.entity.Spend;
import lombok.Data;

/**
 * @ Description   :  花销查询类
 * @ Author        :  wusi
 * @ CreateDate    :  2020/1/9$ 11:10$
 */
@Data
public class SpendQuery extends Spend {
    private Integer page;

    private Integer limit;

    private Integer offset;
    private String startTime;

    private String endTime;
}
