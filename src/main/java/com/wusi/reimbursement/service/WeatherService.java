package com.wusi.reimbursement.service;

import com.wusi.reimbursement.base.service.BaseService;
import com.wusi.reimbursement.entity.Weather;

/**
 * @author admin
 * @date 2021-09-08 11:36:15
 **/
public interface WeatherService extends BaseService<Weather,Long> {

    Weather queryByDate(String date);
}
