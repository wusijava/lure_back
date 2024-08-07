package com.wusi.reimbursement.query;

import com.wusi.reimbursement.entity.LureSell;
import lombok.Data;

/**
 * @ Description   :  路亚销售
 * @ Author        :  wusi
 * @ CreateDate    :  2020/1/9$ 11:10$
 */
@Data
public class LureSellQuery extends LureSell {
    private Integer page;

    private Integer limit;

    private Integer offset;
    private String startTime;

    private String endTime;

}
