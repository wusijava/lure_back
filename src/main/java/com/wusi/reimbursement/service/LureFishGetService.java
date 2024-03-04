package com.wusi.reimbursement.service;

import com.wusi.reimbursement.base.service.BaseService;
import com.wusi.reimbursement.entity.LureFishGet;
import com.wusi.reimbursement.vo.*;

import java.util.List;

/**
 * @author admin
 * @date 2022-06-02 13:59:23
 **/
public interface LureFishGetService extends BaseService<LureFishGet,Long> {

    List<FishCount> fishCount(String uid,String monthStr);

    List<MonthCount> getMonthCount(String uid);

    Integer getnum(String str,String uid);

    List<MonthData> getMonthData(String uid);

    List<MonthData> getYearData(String uid);

    List<Geti> getGeti(String uid,String type);

    List<PaiHang> paiHang(String  type);

    List<LureFishGet> queryByType(String type, String time);

    List<FishDayCount> FishDayCount(String uid);

    List<GuiWang> guiWang(String type);

    List<LureFishGet> queryByMonth(String type,String format);

    List<SpendPai> spendPai();
}
