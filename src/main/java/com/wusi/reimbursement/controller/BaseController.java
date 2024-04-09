package com.wusi.reimbursement.controller;

import com.wusi.reimbursement.aop.SysLog;
import com.wusi.reimbursement.common.Response;
import com.wusi.reimbursement.entity.Reimbursement;
import com.wusi.reimbursement.entity.RequestContext;
import com.wusi.reimbursement.entity.User;
import com.wusi.reimbursement.query.ReimbursementQuery;
import com.wusi.reimbursement.service.ReimbursementService;
import com.wusi.reimbursement.service.RoleService;
import com.wusi.reimbursement.service.UserService;
import com.wusi.reimbursement.utils.*;
import com.wusi.reimbursement.vo.HomeMenuList;
import com.wusi.reimbursement.vo.ReimbursementList;
import com.wusi.reimbursement.vo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ Description   :  登录控制器
 * @ Author        :  wusi
 * @ CreateDate    :  2020/1/7$ 11:25$
 */
@RestController
public class BaseController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;
    @Autowired
    private ReimbursementService reimbursementService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    @SysLog("登录")
    public Response<UserInfo> login(String username, String password, HttpSession session) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        RequestContext.RequestUser user = RequestContext.getCurrentUser();
        DingDingTalkUtils.sendDingDingMsg(user.getNickName() + "登录成功-" + sdf.format(new Date()));
        UserInfo info = new UserInfo();
        info.setMobile(user.getMobile());
        info.setUsername(user.getUsername());
        info.setUid(user.getUid());
        info.setImage(user.getImg());
        info.setNickName(user.getNickName());
        session.setAttribute("U", user);

        RedisUtil.set(user.getUid() + "-login", user.getUsername());
        Object user1 = session.getAttribute("user");
        return Response.ok(info);
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public Response<List<HomeMenuList>> list() {
        List<HomeMenuList> list = roleService.findPermissionByType(0);
        return Response.ok(list);
    }

    @RequestMapping(value = "/productList", method = RequestMethod.POST)
    @ResponseBody
    @SysLog("报销列表")
    public Response<Page<ReimbursementList>> productList(ReimbursementQuery query) {
        //System.out.println(query);
        if (DataUtil.isEmpty(query.getPage())) {
            query.setPage(0);
        }
        if (DataUtil.isEmpty(query.getLimit())) {
            query.setLimit(10);
        }
        Pageable pageable = PageRequest.of(query.getPage(), query.getLimit());
        Page<Reimbursement> page = reimbursementService.queryPage(query, pageable);
        //System.out.println(page);
        List<ReimbursementList> volist = new ArrayList<>();
        for (Reimbursement reimbursement : page.getContent()) {
            volist.add(getReimbursementLististVo(reimbursement));
        }
        Page<ReimbursementList> vopage = new PageImpl<>(volist, pageable, page.getTotalElements());
        return Response.ok(vopage);
    }

    private ReimbursementList getReimbursementLististVo(Reimbursement reimbursement) {
        ReimbursementList reimbursementList = new ReimbursementList();
        reimbursementList.setId(reimbursement.getId());
        reimbursementList.setProductName(reimbursement.getProductName());
        reimbursementList.setTotalPrice(reimbursement.getTotalPrice());
        reimbursementList.setBuyChannel(reimbursement.getBuyChannel());
        reimbursementList.setBuyDate(DateUtil.formatDate(reimbursement.getBuyDate(), DateUtil.PATTERN_YYYY_MM_DD));
        reimbursementList.setReimbursementDate(reimbursement.getReimbursementDate() == null ? "未上交单据" : DateUtil.formatDate(reimbursement.getReimbursementDate(), DateUtil.PATTERN_YYYY_MM_DD));
        reimbursementList.setRemitDate(reimbursement.getRemitDate() == null ? "未到账" : DateUtil.formatDate(reimbursement.getRemitDate(), DateUtil.PATTERN_YYYY_MM_DD));
        reimbursementList.setState(reimbursement.getStateDesc());
        reimbursementList.setRemark(reimbursement.getRemark());
        return reimbursementList;
    }

    @RequestMapping(value = "/toDetails", method = RequestMethod.POST)
    @ResponseBody
    @SysLog("报销详情")
    public Response<ReimbursementList> todetails(ReimbursementQuery query) {
        Reimbursement reimbursement = reimbursementService.queryOne(query);
        return Response.ok(getReimbursementLististVo(reimbursement));
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    @SysLog("更新报销列表")
    public Response<String> update(ReimbursementList query) throws ParseException {
        System.out.println(query.toString());
        Reimbursement reimbursement = getReimbursement(query);
        reimbursementService.updateByid(reimbursement);
        return Response.ok("ok");
    }

    private Reimbursement getReimbursement(ReimbursementList reimbursementList) throws ParseException {
        Reimbursement reimbursement = new Reimbursement();
        reimbursement.setId(reimbursementList.getId());
        reimbursement.setProductName(reimbursementList.getProductName());
        reimbursement.setTotalPrice(reimbursementList.getTotalPrice());
        reimbursement.setBuyChannel(reimbursementList.getBuyChannel());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date buydate = simpleDateFormat.parse(reimbursementList.getBuyDate());
        reimbursement.setBuyDate(buydate);
        if ("未上交单据".equals(reimbursementList.getReimbursementDate())) {
            reimbursement.setReimbursementDate(null);
        } else {
            Date reimbursementDate = simpleDateFormat.parse(reimbursementList.getReimbursementDate());
            reimbursement.setReimbursementDate(reimbursementDate);
        }
        if ("未到账".equals(reimbursementList.getRemitDate())) {
            reimbursement.setRemitDate(null);
        } else {
            Date remitDate = simpleDateFormat.parse(reimbursementList.getRemitDate());
            reimbursement.setRemitDate(remitDate);
        }
        reimbursement.setState(reimbursement.getStatecode(reimbursementList.getState()));
        //
        reimbursement.setRemark(reimbursementList.getRemark() == null ? "  " : reimbursementList.getRemark());

        //reimbursement.setRemark(reimbursementList.getRemark());
        return reimbursement;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    @SysLog("保存报销明细")
    public Response<String> save(ReimbursementList reimbursementList) throws ParseException {
        Reimbursement reimbursement = saveReimbursement(reimbursementList);
        reimbursementService.insert(reimbursement);
        return Response.ok("");
    }

    private Reimbursement saveReimbursement(ReimbursementList reimbursementList) throws ParseException {
        Reimbursement reimbursement = new Reimbursement();
        reimbursement.setId(reimbursementList.getId());
        reimbursement.setProductName(reimbursementList.getProductName());
        reimbursement.setTotalPrice(reimbursementList.getTotalPrice());
        reimbursement.setBuyChannel(reimbursementList.getBuyChannel());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date buydate = simpleDateFormat.parse(reimbursementList.getBuyDate());
        reimbursement.setBuyDate(buydate);
        reimbursement.setState(-1);
        if (reimbursementList.getRemark() == null || reimbursementList.getRemark().equals("")) {
            reimbursement.setRemark("");
        }
        reimbursement.setRemark(reimbursementList.getRemark());
        return reimbursement;
    }

    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    @SysLog("删除报销")
    public Response<String> del(ReimbursementQuery query) {
        try {
            reimbursementService.delById(query.getId());
            return Response.ok("删除的非常成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.ok("删除失败啊伙计！");
    }

    @RequestMapping(value = "/api/web/user/changePassword", method = RequestMethod.POST)
    @ResponseBody
    @SysLog("修改密码")
    public Response changePassword(String oldPassword, String newPassword, String nickName, String url) {
        if(DataUtil.isEmpty(oldPassword)&&DataUtil.isEmpty(newPassword)&&DataUtil.isEmpty(nickName)&&DataUtil.isEmpty(url)){
            return Response.fail("至少需要修改一项!");
        }
        if(DataUtil.isNotEmpty(oldPassword)&&DataUtil.isNotEmpty(newPassword)){
            if (StringUtils.isChinese(newPassword)) {
                return Response.fail("密码不能含有中文字符!");
            }
        }
        RequestContext.RequestUser user = RequestContext.getCurrentUser();
        String result = userService.changePassword(Long.valueOf(user.getId()), user.getSalt(), user.getPassword(), oldPassword, newPassword, nickName, url);
        if (result != null) {
            return Response.fail(result);
        }
        return Response.ok("修改成功");
    }

    //统计报销金额
    @RequestMapping(value = "/countReimbursement", method = RequestMethod.POST)
    @ResponseBody
    @SysLog("统计报销金额")
    public Response spendMonth() {

        //未报销
        String sql1 = "select sum(total_price)  as s from reimbursement where state=-1;";
        List<Map<String, Object>> map1 = jdbcTemplate.queryForList(sql1);
        //报销中
        String sql2 = "select sum(total_price)  as s from reimbursement where state=0;";
        List<Map<String, Object>> map2 = jdbcTemplate.queryForList(sql2);
        //已报销
        String sql3 = "select sum(total_price)  as s from reimbursement where state= 1;";
        List<Map<String, Object>> map3 = jdbcTemplate.queryForList(sql3);
        Object one = map1.get(0).getOrDefault("s", 0);
        Object two = map2.get(0).getOrDefault("s", 0);
        Object three = map3.get(0).getOrDefault("s", 0);
        Reimbursement reimbursement = new Reimbursement();
        if (one == null) {
            reimbursement.setRemark("0");
        } else {
            reimbursement.setRemark(one.toString());
        }
        if (two == null) {
            reimbursement.setBuyChannel("0");
        } else {
            reimbursement.setBuyChannel(two.toString());
        }
        if (three == null) {
            reimbursement.setProductName("0");
        } else {
            reimbursement.setProductName(three.toString());
        }

        return Response.ok(reimbursement);
    }


    @RequestMapping(value = "/api/web/user/addFisher", method = RequestMethod.POST)
    @ResponseBody
    @SysLog("新增龟佬")
    public Response addFisher(String phone, String name) {
        if (DataUtil.isEmpty(phone)) {
            return Response.fail("缺少手机号");
        }
        if (!StringUtils.isPhoneAll(phone)) {
            return Response.fail("手机号格式有误");
        }
        if (DataUtil.isEmpty(name)) {
            return Response.fail("缺少名字");
        }
        RequestContext.RequestUser user = RequestContext.getCurrentUser();
        User byUsername = userService.findByUsername(phone);
        if (byUsername != null) {
            return Response.fail("此手机号已存在！");
        }
        User byName = userService.findByName(name);
        if (byName != null) {
            return Response.fail("此昵称已存在！");
        }
        try {
            userService.createUser(name, phone, user.getNickName());
        } catch (Exception e) {
            return Response.fail("新增异常！");
        }
        return Response.ok("新增成功");
    }
}
