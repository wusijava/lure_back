package com.wusi.reimbursement.query;

import com.wusi.reimbursement.entity.Address;
import com.wusi.reimbursement.entity.SsqHistory;
import lombok.Data;

/**
 * @ Description   :  ssq历史
 * @ Author        :  wusi
 * @ CreateDate    :  2020/1/9$ 11:10$
 */
@Data
public class SsqHistoryQuery extends SsqHistory {
    private Integer page;

    private Integer limit;

    private Integer offset;
    private String startTime;

    private String endTime;
}
