package com.wusi.reimbursement.entity;

import com.wusi.reimbursement.common.Identifiable;
import lombok.Data;

import java.util.Date;


@Data
public class Menu implements Identifiable<Long> {

    private Long id;

    private String title;

    private String icon;

    private String path;

    private String name;

    private String code;

    private String homeCode;

    private Integer type;

    private Date createTime;

    private String component;

    private Integer state;

    public enum State {
        open(1, "开启的"),
        close(0, "关闭的");

        private Integer code;
        private String desc;

        State(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    public enum Type {
        menu(1, "菜单"),
        btn(2, "按钮");

        private Integer code;
        private String desc;

        Type(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
