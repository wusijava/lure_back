package com.wusi.reimbursement.service.impl;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.base.service.impl.BaseMybatisServiceImpl;
import com.wusi.reimbursement.entity.HomeMenu;
import com.wusi.reimbursement.entity.Menu;
import com.wusi.reimbursement.entity.RoleMenu;
import com.wusi.reimbursement.mapper.RoleMenuMapper;
import com.wusi.reimbursement.query.RoleMenuQuery;
import com.wusi.reimbursement.service.HomeMenuService;
import com.wusi.reimbursement.service.MenuService;
import com.wusi.reimbursement.service.RoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author admin
 * @date 2020-01-08 16:57:43
 **/
@Service
public class RoleMenuServiceImpl extends BaseMybatisServiceImpl<RoleMenu,Long> implements RoleMenuService {

    @Autowired
    private RoleMenuMapper roleMenuMapper;
    @Autowired
    private HomeMenuService homeMenuService;
    @Autowired
    private MenuService menuService;

    @Override
    protected BaseMapper<RoleMenu, Long> getBaseMapper() {
        return roleMenuMapper;
    }



    /**
     * map转list
     *
     * @param homeMenuMap
     */
    private List<HomeMenu> getHomeList(SortedMap<String, HomeMenu> homeMenuMap) {
        List<HomeMenu> menus = new ArrayList<>();
        for (String menu : homeMenuMap.keySet()) {
            menus.add(homeMenuMap.get(menu));
        }
        return menus;
    }

    @Override
    public List<HomeMenu> queryByType(Integer type) {
        RoleMenuQuery query = new RoleMenuQuery();
        query.setType(type);
        List<RoleMenu> menus = this.queryList(query);
        SortedMap<String, HomeMenu> homeMenuMap = new TreeMap<>();
        for (RoleMenu roleMenu : menus) {
            Menu menu = menuService.queryOpenByCode(roleMenu.getMenuCode());
            if (menu == null) {
                continue;
            }
            HomeMenu homeMenu = homeMenuMap.get(menu.getHomeCode());
            if (homeMenu == null) {
                homeMenu = homeMenuService.queryOpenByCode(roleMenu.getHomeMenuCode());
                if (homeMenu == null) {
                    continue;
                }
            }
            List<Menu> menuList = homeMenu.getSubmenus();
            if (menuList == null) {
                menuList = new ArrayList<>();
            }
            menuList.add(menu);
            homeMenu.setSubmenus(menuList);
            homeMenuMap.put(homeMenu.getCode(), homeMenu);
        }
        return getHomeList(homeMenuMap);
    }
}
