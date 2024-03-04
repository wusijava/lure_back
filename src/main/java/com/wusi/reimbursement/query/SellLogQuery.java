package com.wusi.reimbursement.query;

import com.wusi.reimbursement.entity.SellLog;
import lombok.Data;

/**
 * @ Description   :  销售记录query
 * @ Author        :  wusi
 * @ CreateDate    :  2020/1/16$ 17:14$
 */
@Data
public class SellLogQuery  extends SellLog {
    private Integer page;

    private Integer limit;

    private Integer offset;
    private String startTime;

    private String endTime;
}
