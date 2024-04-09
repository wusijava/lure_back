package com.wusi.reimbursement.mapper;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.entity.LureShopping;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author admin
 * @date 2022-06-02 13:59:49
 **/
@Mapper
public interface LureShoppingMapper extends BaseMapper<LureShopping,Long> {


    void updateNickName(@Param("nickName") String nickName, @Param("newNickName") String newNickName);
}
