package com.wusi.reimbursement.query;

import com.wusi.reimbursement.entity.Math;
import lombok.Data;

/**
 * @ Description   :  MathQuery
 * @ Author        :  wusi
 * @ CreateDate    :  2020/1/9$ 11:10$
 */
@Data
public class MathQuery extends Math {
    private Integer page;

    private Integer limit;

    private Integer offset;
    private String startTime;

    private String endTime;
}
