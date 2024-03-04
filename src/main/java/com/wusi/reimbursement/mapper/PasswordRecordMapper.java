package com.wusi.reimbursement.mapper;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.entity.PasswordRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author admin
 * @date 2021-04-14 14:42:14
 **/
@Mapper
public interface PasswordRecordMapper extends BaseMapper<PasswordRecord,Long> {

    void deleteByID(@Param("id") Long id);
}
