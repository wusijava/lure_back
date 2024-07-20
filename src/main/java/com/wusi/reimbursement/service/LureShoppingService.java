package com.wusi.reimbursement.service;

import com.wusi.reimbursement.base.service.BaseService;
import com.wusi.reimbursement.entity.LureShopping;

/**
 * @author admin
 * @date 2022-06-02 13:59:49
 **/
public interface LureShoppingService extends BaseService<LureShopping,Long> {

    void delById(Long id);

    void updateNickName(String nickName, String newNickName);

    LureShopping selectByTraceId(String traceId);
}
