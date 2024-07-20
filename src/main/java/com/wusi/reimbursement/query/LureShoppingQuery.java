package com.wusi.reimbursement.query;

import com.wusi.reimbursement.entity.LureShopping;
import lombok.Data;

/**
 * @ Description   :  lure花销查询类
 * @ Author        :  wusi
 * @ CreateDate    :  2020/1/9$ 11:10$
 */
@Data
public class LureShoppingQuery extends LureShopping {
    private Integer page;

    private Integer limit;

    private Integer offset;
    private String startTime;

    private String endTime;

    private String wxRemarkCode;
    private String wxImgCode;
}
