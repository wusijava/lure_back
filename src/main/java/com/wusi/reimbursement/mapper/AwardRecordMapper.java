package com.wusi.reimbursement.mapper;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.entity.AwardRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author admin
 * @date 2021-04-16 15:35:29
 **/
@Mapper
public interface AwardRecordMapper extends BaseMapper<AwardRecord,Long> {

Integer queryNum(String date);
}
