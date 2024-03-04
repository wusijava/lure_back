package com.wusi.reimbursement.service.impl;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.base.service.impl.BaseMybatisServiceImpl;
import com.wusi.reimbursement.entity.HomeMenu;
import com.wusi.reimbursement.entity.Menu;
import com.wusi.reimbursement.entity.Role;
import com.wusi.reimbursement.mapper.RoleMapper;
import com.wusi.reimbursement.service.RoleMenuService;
import com.wusi.reimbursement.service.RoleService;
import com.wusi.reimbursement.utils.DataUtil;
import com.wusi.reimbursement.vo.HomeMenuList;
import com.wusi.reimbursement.vo.MenuList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 * @date 2020-01-08 16:57:27
 **/
@Service
public class RoleServiceImpl extends BaseMybatisServiceImpl<Role,Long> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    protected BaseMapper<Role, Long> getBaseMapper() {
        return roleMapper;
    }

    @Override
    public List<HomeMenuList> findPermissionByType(Integer type) {
        List<HomeMenuList> menuList = new ArrayList<>();
        List<HomeMenu> homeMenuList = roleMenuService.queryByType(type);
        for (HomeMenu menu:homeMenuList){
            HomeMenuList list = new HomeMenuList();
            list.setTitle(menu.getTitle());
            list.setIcon(menu.getIcon());
            list.setName(menu.getName());
            if (DataUtil.isNotEmpty(menu.getSubmenus())){
                List<MenuList> submenus = new ArrayList<>();
                for (Menu m:menu.getSubmenus()){
                    MenuList ml = new MenuList();
                    ml.setTitle(m.getTitle());
                    ml.setIcon(m.getIcon());
                    ml.setName(m.getName());
                    ml.setPath(m.getPath());
                    ml.setComponent(m.getComponent());
                    ml.setType(m.getType());
                    submenus.add(ml);
                }
                list.setSubmenus(submenus);
            }
            menuList.add(list);
        }
        return menuList;
    }
}
