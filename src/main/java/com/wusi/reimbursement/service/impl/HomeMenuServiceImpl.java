package com.wusi.reimbursement.service.impl;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.base.service.impl.BaseMybatisServiceImpl;
import com.wusi.reimbursement.entity.HomeMenu;
import com.wusi.reimbursement.entity.Menu;
import com.wusi.reimbursement.mapper.HomeMenuMapper;
import com.wusi.reimbursement.query.HomeMenuQuery;
import com.wusi.reimbursement.service.HomeMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class HomeMenuServiceImpl extends BaseMybatisServiceImpl<HomeMenu,Long> implements HomeMenuService {

    @Autowired
    private HomeMenuMapper homeMenuMapper;


    @Override
    protected BaseMapper<HomeMenu, Long> getBaseMapper() {
        return homeMenuMapper;
    }

    @Override
    public HomeMenu queryOpenByCode(String code) {
        HomeMenuQuery query = new HomeMenuQuery();
        query.setCode(code);
        query.setState(Menu.State.open.getCode());
        List<HomeMenu> menuList = this.queryList(query);
        return menuList == null || menuList.size() == 0 ? null : menuList.get(0);
    }
}
