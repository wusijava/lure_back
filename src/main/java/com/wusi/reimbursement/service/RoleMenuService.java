package com.wusi.reimbursement.service;

import com.wusi.reimbursement.base.service.BaseService;
import com.wusi.reimbursement.entity.HomeMenu;
import com.wusi.reimbursement.entity.RoleMenu;

import java.util.List;

/**
 * @author admin
 * @date 2020-01-08 16:57:43
 **/
public interface RoleMenuService extends BaseService<RoleMenu,Long> {
    /**
     * 查询开启的菜单
     * @param type
     * */
    List<HomeMenu> queryByType(Integer type);
}
