package com.wusi.reimbursement.entity;

import com.wusi.reimbursement.common.Identifiable;
import lombok.Data;


@Data
public class RoleMenu implements Identifiable<Long> {

    private Long id;

    private Integer type;

    private String homeMenuCode;

    private String menuCode;


}
