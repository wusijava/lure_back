package com.wusi.reimbursement.service;

import com.wusi.reimbursement.base.service.BaseService;
import com.wusi.reimbursement.entity.WaterLevel;

/**
 * @author admin
 * @date 2021-05-18 09:18:16
 **/
public interface WaterLevelService extends BaseService<WaterLevel,Long> {

    WaterLevel queryByTime(String createTime);

}
