package com.wusi.reimbursement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @ Description   :  java类作用描述
 * @ Author        :  wusi
 * @ CreateDate    :  2021/2/5$ 18:01$
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class User {
    private Integer id;
    private String name;
    private Integer age;
    private Integer sex;

    public User(Integer id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
}
