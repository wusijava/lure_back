package com.wusi.reimbursement.query;

import com.wusi.reimbursement.entity.Address;
import com.wusi.reimbursement.entity.CollectivityLure;
import lombok.Data;

/**
 * @ Description   :  活动查询
 * @ Author        :  wusi
 * @ CreateDate    :  2020/1/9$ 11:10$
 */
@Data
public class CollectivityLureQuery extends CollectivityLure {
    private Integer page;

    private Integer limit;

    private Integer offset;
    private String startTime;

    private String endTime;
}
