package com.wusi.reimbursement.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUser implements Serializable {

    private String id;

    private String username;

    private String password;

    private String nickName;

    private Integer type;

    private String uid;

    private String salt;

    private String mobile;

    private String storeMarkCode;

    private String cityCode;

    private String provinceCode;
}
