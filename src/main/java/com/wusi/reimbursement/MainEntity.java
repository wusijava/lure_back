package com.wusi.reimbursement;

import com.wusi.reimbursement.entity.*;
import com.wusi.reimbursement.generator.CodeGenerator;


public class MainEntity {

    public static void main(String[] args) {
        String basePack = Main.class.getPackage().getName();
        CodeGenerator codeGenerator = new CodeGenerator();
        codeGenerator.generateMybatisXml(basePack, CollectivityLure.class);
        codeGenerator.generateDao(basePack, CollectivityLure.class);
       codeGenerator.generateService(basePack, CollectivityLure.class);
       //codeGenerator.generateCreateSqlForPackage("com.click.jd.merchant.modules");
    }
}
