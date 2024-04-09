package com.wusi.reimbursement.controller;

import com.wusi.reimbursement.common.Response;
import com.wusi.reimbursement.entity.RequestContext;
import com.wusi.reimbursement.service.LureFishGetService;
import com.wusi.reimbursement.utils.DataUtil;
import com.wusi.reimbursement.vo.CalendarFish;
import io.swagger.annotations.Api;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@Slf4j
@Api(tags = "路了个鸭V2")
@RequestMapping(value = "api/v2")
public class NewLureController {
    @Autowired
    private LureFishGetService lureFishGetService;

    static SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
    static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");

    @RequestMapping(value = "calendarFish")
    public Response<List<CalendarFish>> getCalendarFish(String month) {
        if (DataUtil.isEmpty(month)) {
            month = format.format(new Date());
        }
        return Response.ok(lureFishGetService.getCalendarFish(month, RequestContext.getCurrentUser().getUid()));
    }
}
