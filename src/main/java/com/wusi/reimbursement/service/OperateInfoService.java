package com.wusi.reimbursement.service;

import com.wusi.reimbursement.base.service.BaseService;
import com.wusi.reimbursement.entity.OperateInfo;

/**
 * @author admin
 * @date 2021-04-14 14:58:07
 **/
public interface OperateInfoService extends BaseService<OperateInfo,Long> {
    /**
     * 验证操作密码
     * @param pwd
     * @return
     */
    String verifyPwd(String pwd);
}
