package com.wusi.reimbursement.mapper;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.entity.LureFishGet;
import com.wusi.reimbursement.vo.FishCount;
import com.wusi.reimbursement.vo.MonthCount;
import com.wusi.reimbursement.vo.MonthRate;
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

    List<MonthRate> baoKou(@Param("num") Integer num, @Param("uid") String uid);

    List<FishCount> lureGet(String uid, String monthStr);

    List<MonthRate> queryByDateAndUidList(String day, List<String> uids);

    LureFishGet selectByTraceId(String traceId);

    void updateStateByTraceId(@Param("state") Integer state,@Param("traceId") String traceId);
}
