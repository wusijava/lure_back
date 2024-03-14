package com.wusi.reimbursement;

import com.wusi.reimbursement.entity.*;
import com.wusi.reimbursement.generator.CodeGenerator;


public class MainEntity {

    public static void main(String[] args) {
        String basePack = Main.class.getPackage().getName();
        CodeGenerator codeGenerator = new CodeGenerator();
        codeGenerator.generateMybatisXml(basePack, ShuiWenWaterLevel.class);
        codeGenerator.generateDao(basePack, ShuiWenWaterLevel.class);
       codeGenerator.generateService(basePack, ShuiWenWaterLevel.class);
       //codeGenerator.generateCreateSqlForPackage("com.click.jd.merchant.modules");
    }
}
