package com.wusi.reimbursement.query;

import com.wusi.reimbursement.entity.IllegalLog;
import com.wusi.reimbursement.entity.SellLog;
import lombok.Data;

/**
 * @ Description   :  违规记录
 * @ Author        :  wusi
 * @ CreateDate    :  2020/1/16$ 17:14$
 */
@Data
public class IllegalLogQuery extends IllegalLog {
    private Integer page;

    private Integer limit;

    private Integer offset;
    private String startTime;

    private String endTime;
}
