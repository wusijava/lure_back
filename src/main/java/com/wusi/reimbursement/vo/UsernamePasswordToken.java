package com.wusi.reimbursement.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsernamePasswordToken {
    private String username;
    private String password;
}
