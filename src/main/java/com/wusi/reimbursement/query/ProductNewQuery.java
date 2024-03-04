package com.wusi.reimbursement.query;

import com.wusi.reimbursement.entity.ProductNew;
import com.wusi.reimbursement.entity.Spend;
import lombok.Data;

/**
 * @ Description   :  淘宝产品查询类
 * @ Author        :  wusi
 * @ CreateDate    :  2020/1/9$ 11:10$
 */
@Data
public class ProductNewQuery extends ProductNew {
    private Integer page;

    private Integer limit;

    private Integer offset;
    private String startTime;

    private String endTime;
}
