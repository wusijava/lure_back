package com.wusi.reimbursement.query;

import com.wusi.reimbursement.entity.User;
import lombok.Data;


@Data
public class UserQuery extends User {

    private Integer page;

    private Integer limit;


}
