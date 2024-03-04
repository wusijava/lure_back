package com.wusi.reimbursement.query;

import com.wusi.reimbursement.entity.Address;
import com.wusi.reimbursement.entity.Spend;
import lombok.Data;

/**
 * @ Description   :  地址查询
 * @ Author        :  wusi
 * @ CreateDate    :  2020/1/9$ 11:10$
 */
@Data
public class AddressQuery extends Address {
    private Integer page;

    private Integer limit;

    private Integer offset;
    private String startTime;

    private String endTime;
}
