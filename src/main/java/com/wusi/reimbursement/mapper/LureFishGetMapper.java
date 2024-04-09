package com.wusi.reimbursement.mapper;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.entity.LureFishGet;
import com.wusi.reimbursement.vo.FishCount;
import com.wusi.reimbursement.vo.MonthCount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author admin
 * @date 2022-06-02 13:59:23
 **/
@Mapper
public interface LureFishGetMapper extends BaseMapper<LureFishGet,Long> {


    List<FishCount> fishCount(@Param("uid") String uid, @Param("monthStr") String monthStr);

    List<MonthCount> monthCount(String uid);

    Integer getnum(@Param("str") String str,@Param("uid") String uid);

    List<LureFishGet> queryByType(String type, String time);

    List<LureFishGet> queryByMonth(@Param("type")String type, @Param("format") String format);

    List<LureFishGet>  getCalendarFish(String month, String uid);

    void updateNickName(@Param("nickName") String nickName, @Param("newNickName") String newNickName);
}
