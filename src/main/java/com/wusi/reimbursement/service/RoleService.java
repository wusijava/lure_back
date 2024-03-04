package com.wusi.reimbursement.service;

import com.wusi.reimbursement.base.service.BaseService;
import com.wusi.reimbursement.entity.Role;
import com.wusi.reimbursement.vo.HomeMenuList;

import java.util.List;


public interface RoleService extends BaseService<Role,Long> {
    /**
     * 查询该角色的权限
     *
     * @param type
     */
    List<HomeMenuList> findPermissionByType(Integer type);
}
