package com.wusi.reimbursement.mapper;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.entity.Weather;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author admin
 * @date 2021-09-08 11:36:15
 **/
@Mapper
public interface WeatherMapper extends BaseMapper<Weather,Long> {


    Weather queryByDate(String date);

    Weather queryLastOne();
}
