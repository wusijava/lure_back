package com.wusi.reimbursement.entity;

import com.wusi.reimbursement.common.Identifiable;
import lombok.Data;

import java.util.Date;

/***
 * 违规记录
 */
@Data
public class IllegalLog implements Identifiable<Long> {
    private Long id;

    private String uid;

    private String userName;

    private String content;

    private Date createTime;

    private String reason;

    /**
     * 1文字  2图片 3视频
     */
    private Integer type;

    /**
     * 1鱼获图 2消费图 3头像 4评论
     */
    private Integer source;

    /**
     * -1违规  0待定 1正常
     */
    private Integer state;

    private String traceId;

    public enum Type {
        text(1 ,"文字"),
        img(2 ,"图片"),
        video(3, "视频");

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

    public enum Source {
        FISH(1 ,"鱼获信息"),
         SHOPPING(2 ,"消费信息"),
        USERIMGAGE(3, "修改昵称"),
        COMMENT(4, "评论信息");

        private Integer code;
        private String desc;

        Source(Integer code, String desc) {
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
    public String getTypeDesc() {
        if (Type.text.code.equals(type)) {
            return Type.text.getDesc();
        } else if (Type.img.code.equals(type)) {
            return  Type.img.getDesc();
        } else {
            return  Type.video.getDesc();
        }
    }
    public String getSourceDesc() {
        if (Source.FISH.code.equals(source)) {
            return Source.FISH.getDesc();
        } else if (Source.SHOPPING.code.equals(source)) {
            return Source.SHOPPING.getDesc();
        } else if(Source.USERIMGAGE.code.equals(source)){
            return Source.USERIMGAGE.getDesc();
        }else{
            return Source.COMMENT.getDesc();
        }
    }

}
