package com.wusi.reimbursement.vo;

import lombok.Data;

import java.util.List;

/**
 * @author wusi
 * @description 主菜单
 * @date 2019-11-13 14:31:25
 */
@Data
public class HomeMenuList {

    private String title;

    private String name;

    private String icon;

    private List<MenuList> submenus;
}
