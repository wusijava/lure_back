package com.wusi.reimbursement.mapper;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.entity.WaterLevel;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author admin
 * @date 2021-05-18 09:18:16
 **/
@Mapper
public interface WaterLevelMapper extends BaseMapper<WaterLevel,Long> {


    WaterLevel queryByTime(String createTime);

}
