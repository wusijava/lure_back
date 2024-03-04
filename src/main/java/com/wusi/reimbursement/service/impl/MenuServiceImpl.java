package com.wusi.reimbursement.service.impl;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.base.service.impl.BaseMybatisServiceImpl;
import com.wusi.reimbursement.entity.Menu;
import com.wusi.reimbursement.mapper.MenuMapper;
import com.wusi.reimbursement.query.MenuQuery;
import com.wusi.reimbursement.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author admin
 * @date 2020-01-08 15:32:17
 **/
@Service
public class MenuServiceImpl extends BaseMybatisServiceImpl<Menu,Long> implements MenuService {
    @Autowired
    private MenuMapper menuMapper;
    @Override
    protected BaseMapper<Menu, Long> getBaseMapper() {
        return menuMapper;
    }

    @Override
    public Menu queryOpenByCode(String code) {
        MenuQuery query = new MenuQuery();
        query.setCode(code);
        query.setState(Menu.State.open.getCode());
        List<Menu> menuList = this.queryList(query);
        return menuList == null || menuList.size() == 0 ? null : menuList.get(0);
    }
}
