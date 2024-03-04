package com.wusi.reimbursement.vo;

import lombok.Data;

/**
 * @author wusi
 * @description 主菜单
 * @date 2019-11-13 14:31:08
 */
@Data
public class MenuList {

    private String title;

    private String icon;

    private String path;

    private String name;

    private String component;

    private Integer type;
}
