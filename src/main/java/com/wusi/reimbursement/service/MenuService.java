package com.wusi.reimbursement.service;

import com.wusi.reimbursement.base.service.BaseService;
import com.wusi.reimbursement.entity.Menu;

/**
 * @author admin
 * @date 2020-01-08 15:32:17
 **/
public interface MenuService extends BaseService<Menu,Long> {
    /**
     * 查询开启的菜单
     *
     * @param code
     * @return
     */
    Menu queryOpenByCode(String code);
}
