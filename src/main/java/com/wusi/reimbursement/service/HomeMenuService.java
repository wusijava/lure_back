package com.wusi.reimbursement.service;

import com.wusi.reimbursement.base.service.BaseService;
import com.wusi.reimbursement.entity.HomeMenu;

/**
 * @author admin
 * @date 2020-01-08 15:31:45
 **/
public interface HomeMenuService extends BaseService<HomeMenu,Long> {
    /**
     * 查询开启的菜单
     *
     * @param code
     * @return
     */
    HomeMenu queryOpenByCode(String code);
}
