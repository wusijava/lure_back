package com.wusi.reimbursement.controller;

import ch.qos.logback.core.pattern.color.RedCompositeConverter;
import com.alibaba.fastjson.JSONObject;
import com.wusi.reimbursement.common.Response;
import com.wusi.reimbursement.common.ratelimit.anonation.RateLimit;
import com.wusi.reimbursement.entity.*;
import com.wusi.reimbursement.query.AddressQuery;
import com.wusi.reimbursement.query.HomeworkQuery;
import com.wusi.reimbursement.query.HouseworkQuery;
import com.wusi.reimbursement.query.RemindRecordQuery;
import com.wusi.reimbursement.service.AddressService;
import com.wusi.reimbursement.service.HomeworkService;
import com.wusi.reimbursement.service.HouseworkService;
import com.wusi.reimbursement.service.RemindRecordService;
import com.wusi.reimbursement.utils.*;
import com.wusi.reimbursement.vo.AddressVO;
import com.wusi.reimbursement.vo.HomeworkList;
import com.wusi.reimbursement.vo.HouseworkVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ Description   :  作业&家务
 * @ Author        :  wusi
 * @ CreateDate    :  2020/12/24$ 15:56$
 */
@RestController
@Slf4j
@RequestMapping(value = "api/")
public class HomeworkController {
    @Autowired
    private HomeworkService homeworkService;
    @Autowired
    private HouseworkService houseworkService;
    @Autowired
    private RemindRecordService remindRecordService;
    @Autowired
    private AddressService addressService;

    @ResponseBody
    @RequestMapping(value = "addHomework")
    public Response<String> addHomework(HomeworkList homeworkList) {
        RequestContext.RequestUser user = RequestContext.getCurrentUser();
        Homework homework = getHomework(homeworkList);
        if (DataUtil.isNotEmpty(user) && "admin".equals(user.getUsername())) {
            homework.setName("吴思");
        }
        if (DataUtil.isNotEmpty(user) && "zmx".equals(user.getUsername())) {
            homework.setName("张明霞");
        }
        homework.setCreateTime(new Date());
        try {
            homeworkService.insert(homework);
        } catch (Exception e) {
            log.error("添加作业异常,{}", JSONObject.toJSONString(homeworkList));
        }
        return Response.ok("添加成功!");

    }

    private Homework getHomework(HomeworkList homeworkList) {
        Homework work = new Homework();
        work.setContent(homeworkList.getContent());
        work.setSubject(homeworkList.getSubject());
        work.setTimeConsuming(homeworkList.getTimeConsuming());
        work.setUrl(homeworkList.getUrl());
        work.setEvaluate(homeworkList.getEvaluate());
        if (DataUtil.isNotEmpty(homeworkList.getRemark())) {
            work.setRemark(homeworkList.getRemark());
        }
        return work;
    }

    @ResponseBody
    @RequestMapping(value = "homeworkList")
    public Response<Page<HomeworkList>> homeworkList(HomeworkQuery query) {
        if (DataUtil.isEmpty(query.getPage())) {
            query.setPage(0);
        }
        if (DataUtil.isEmpty(query.getLimit())) {
            query.setLimit(5);
        }
        Pageable pageable = PageRequest.of(query.getPage(), query.getLimit());
        Page<Homework> page = homeworkService.queryPage(query, pageable);
        List<HomeworkList> voList = new ArrayList<>();
        for (Homework homework : page.getContent()) {
            voList.add(getWork(homework));
        }
        Page<HomeworkList> voPage = new PageImpl<>(voList, pageable, page.getTotalElements());
        return Response.ok(voPage);
    }

    private HomeworkList getWork(Homework homework) {
        HomeworkList list = new HomeworkList();
        list.setName(homework.getName());
        list.setSubject(homework.getSubject());
        list.setCreateTime(DateUtil.formatDate(homework.getCreateTime(), "yyyy-MM-dd"));
        list.setContent(homework.getContent());
        list.setUrl(homework.getUrl());
        return list;
    }

    //addHouseWork
    @ResponseBody
    @RequestMapping(value = "addHouseWork")
    public Response<String> addHouseWork(String content, String remark, Boolean checked, String date, String user) throws ParseException {
        RequestContext.RequestUser loginUser = RequestContext.getCurrentUser();
        if (DataUtil.isEmpty(content)) {
            return Response.fail("内容必填!");
        }
        if (user.equals(loginUser.getNickName())) {
            return Response.fail("你个憨批,你确定要给自己安排家务?!");
        }
        if (content.length() > 12) {
            return Response.fail("内容长度需小于12个字!");
        }
        if (DataUtil.isEmpty(user)) {
            return Response.fail("必须指定任务完成人!");
        }

        Housework work = new Housework();
        work.setUserNameBy(loginUser.getNickName());
        work.setUserNameTo(user);
        work.setContent(content);
        work.setCreateTime(new Date());
        if (DataUtil.isNotEmpty(remark)) {
            work.setRemark(remark);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        work.setRequiredFinishTime(sdf.parse(date + " 23:59:59"));
        work.setReceiveState(Housework.ReceiveState.wait.getCode());
        work.setState(Housework.State.not.getCode());
        try {
            houseworkService.insert(work);
            if (checked) {
                String phone = null;
                if ("吴思".equals(user)) {
                    phone = "18602702325";
                }
                if ("张明霞".equals(user)) {
                    phone = "15527875423";
                }
                if (DataUtil.isNotEmpty(phone)) {
                    SMSUtil.sendSMS(phone, content, 826585);
                }
            }
        } catch (Exception e) {
            log.error("指派家庭任务异常,{}", content);
        }
        return Response.ok("指派家庭任务成功!");
    }

    //myTask
    @ResponseBody
    @RequestMapping(value = "myTask")
    public Response<Page<HouseworkVO>> myTask(HouseworkQuery query) {

        if (DataUtil.isEmpty(query.getPage())) {
            query.setPage(0);
        }
        if (DataUtil.isEmpty(query.getLimit())) {
            query.setLimit(5);
        }
        RequestContext.RequestUser loginUser = RequestContext.getCurrentUser();
        //我的任务
        if (query.getType().equals(1)) {
            query.setUserNameTo(loginUser.getNickName());
            //我安排的
        } else {
            query.setUserNameBy(loginUser.getNickName());
        }
        Pageable Pageable = PageRequest.of(query.getPage(), query.getLimit());
        Page<Housework> page = houseworkService.queryPage(query, Pageable);

        List<HouseworkVO> voList = new ArrayList<>();
        for (Housework Housework : page.getContent()) {
            voList.add(gethouseworkVo(Housework));
        }
        Page<HouseworkVO> voPage = new PageImpl<>(voList, Pageable, page.getTotalElements());
        return Response.ok(voPage);

    }

    private HouseworkVO gethouseworkVo(Housework housework) {
        HouseworkVO vo = new HouseworkVO();
        vo.setId(housework.getId());
        vo.setRemark(housework.getRemark());
        vo.setContent(housework.getContent());
        vo.setCreateTime(DateUtil.formatDate(housework.getCreateTime(), "yyyy-MM-dd HH:mm:ss "));
        vo.setRequiredFinishTime(DateUtil.formatDate(housework.getRequiredFinishTime(), "yyyy-MM-dd HH:mm:ss "));
        if (DataUtil.isNotEmpty(housework.getRealityFinishTime())) {
            vo.setRealityFinishTime(DateUtil.formatDate(housework.getRealityFinishTime(), "yyyy-MM-dd HH:mm:ss "));
        }
        vo.setStateDesc(housework.getStateDesc());
        vo.setState(housework.getState());
        vo.setReceiveStateDesc(housework.getReceiveStateDesc());
        vo.setReceiveState(housework.getReceiveState());
        vo.setUserNameBy(housework.getUserNameBy());
        return vo;
    }

    //receiveWork
    @ResponseBody
    @RequestMapping(value = "receiveWork")
    public Response<String> receiveWork(Integer state, Long id, Integer stateType) {
        Housework housework = houseworkService.queryById(id);
        if (DataUtil.isEmpty(housework)) {
            return Response.fail("查询不存在!");
        }
        try {
            //1是更新接受状态
            if (stateType.equals(1)) {
                if(state.equals(1)){
                    housework.setReceiveState(state);
                }else{
                    housework.setReceiveState(state);
                    housework.setState(state);
                }
            } else {
                housework.setState(state);
                housework.setRealityFinishTime(new Date());
            }
            houseworkService.updateById(housework);
        } catch (Exception e) {
            log.error("更新异常,{}", id);
        }
        return Response.ok("处理成功!");
    }

    //发短信
    @ResponseBody
    @RequestMapping(value = "sendMsg")
    @RateLimit(permitsPerSecond = 0.01, ipLimit = true, description = "限制短信频率")
    public void sendMsg(String user) {
        RemindRecord remindRecord = new RemindRecord();
        remindRecord.setTimeStr(DateUtil.formatDate(new Date(), "yyyy-MM-dd"));
        Long num = remindRecordService.queryCount(remindRecord);
        if (num < 1) {
            SMSUtil.sendSMS("18602702325", user, 828706);
            RemindRecord record = new RemindRecord();
            record.setTimeStr(DateUtil.formatDate(new Date(), "yyyy-MM-dd"));
            record.setCreateTime(new Date());
            remindRecordService.insert(record);
        }

    }

    @ResponseBody
    @RequestMapping(value = "saveAdd")
    @RateLimit(permitsPerSecond = 0.01, ipLimit = true, description = "限制存储频率")
    public void saveAdd(Address address) throws Exception {
        RequestContext.RequestUser loginUser = RequestContext.getCurrentUser();
        address.setName(loginUser.getNickName());
        address.setCreateTime(new Date());
        //log.error("过期时间："+ RedisUtil.getExpire(loginUser.getUid()));
        //log.error("过期键："+ RedisUtil.get(loginUser.getUid()));
        if(DataUtil.isEmpty(RedisUtil.get(loginUser.getUid()))){
            DingDingTalkUtils.sendDingDingMsg(loginUser.getNickName()+":"+address.getAddress());
            addressService.insert(address);
            RedisUtil.set(loginUser.getUid(),loginUser.getUid(),3600);
            return;
        }
        //log.error(loginUser.getUsername()+"-缓存时间内，不通知！");
    }

    @ResponseBody
    @RequestMapping(value = "address")
    public Response<Page<AddressVO>> showAddress(AddressQuery query) {
        if (DataUtil.isEmpty(query.getPage())) {
            query.setPage(0);
        }
        query.setLimit(5);
        Pageable pageable = PageRequest.of(query.getPage(), query.getLimit());
        Page<Address> addresses = addressService.queryPage(query, pageable);
        List<AddressVO> voList = new ArrayList<>();
        for (Address address : addresses) {
            voList.add(getAddress(address));
        }
        Page<AddressVO> voPage = new PageImpl<>(voList, pageable, addresses.getTotalElements());
        return Response.ok(voPage);

    }

    private AddressVO getAddress(Address address) {
        AddressVO vo = new AddressVO();
        vo.setId(address.getId());
        vo.setAddress(address.getAddress());
        vo.setCity(address.getCity());
        vo.setCreateTime(DateUtil.formatDate(address.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
        vo.setDistrict(address.getDistrict());
        vo.setProvince(address.getProvince());
        vo.setName(address.getName());
        vo.setJwd(address.getLng() + "," + address.getLat());
        return vo;
    }

    @ResponseBody
    @RequestMapping(value = "remind")
    public Response<Page<RemindRecord>> showAddress(RemindRecordQuery query) {
        if (DataUtil.isEmpty(query.getPage())) {
            query.setPage(0);
        }
        query.setLimit(5);
        Pageable pageable = PageRequest.of(query.getPage(), query.getLimit());
        Page<RemindRecord> remind = remindRecordService.queryPage(query, pageable);
        List<RemindRecord> voList = new ArrayList<>();
        for (RemindRecord RemindRecord : remind) {
            voList.add(getRec(RemindRecord));
        }
        Page<RemindRecord> voPage = new PageImpl<>(voList, pageable, remind.getTotalElements());
        return Response.ok(voPage);
    }

    private RemindRecord getRec(RemindRecord remindRecord) {
        RemindRecord rec = new RemindRecord();
        rec.setId(remindRecord.getId());
        rec.setTimeStr(DateUtil.formatDate(remindRecord.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
        return rec;
    }

    @ResponseBody
    @RequestMapping(value = "deleteRec")
    public void deleteRec(Long id) {
        remindRecordService.deleteById(id);
    }

    @ResponseBody
    @RequestMapping(value = "mind")
    public Response<String> mind() {
        RequestContext.RequestUser loginUser = RequestContext.getCurrentUser();
        Housework query=new Housework();
        query.setUserNameTo(loginUser.getNickName());
        query.setState(Housework.State.not.getCode());
        Long aLong = houseworkService.queryCount(query);
        if(aLong>0){
            return Response.ok(loginUser.getNickName()+",您有"+aLong+"项家务未完成!已完成任务,请及时更改状态!");
        }
        return Response.ok(null);
    }
}
