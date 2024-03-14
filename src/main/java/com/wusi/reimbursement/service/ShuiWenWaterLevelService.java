package com.wusi.reimbursement.service;

import com.wusi.reimbursement.base.service.BaseService;
import com.wusi.reimbursement.entity.ShuiWenWaterLevel;

/**
 * @author Java
 * @date 2024-03-14 15:59:24
 **/
public interface ShuiWenWaterLevelService extends BaseService<ShuiWenWaterLevel,Long> {

    ShuiWenWaterLevel queryByDate(String format);
}
