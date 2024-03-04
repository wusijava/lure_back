package com.wusi.reimbursement.entity;

import com.wusi.reimbursement.common.Identifiable;
import lombok.Data;

import java.util.Date;

/**
 * @ Description   :  housework
 * @ Author        :  wusi
 * @ CreateDate    :  2020/12/28$ 14:10$
 */
@Data
public class Housework implements Identifiable<Long> {
    private Long id;
    /**
     * 任务安排人
     */
    private String userNameBy;
    /**
     * 任务接受人
     */
    private String userNameTo;
    /**
     * 家务内容
     */
    private String content;

    /**
     * 备注
     */
    private String remark;
    /**
     * 是否接受任务 1已接受 0等待接受  -1已拒绝
     */
    private Integer receiveState;
    /**
     * 状态 1已完成  0未完成 -1已拒绝
     */
    private Integer state;
    /**
     * 评分
     */
    private String score;
    /**
     * 任务创建时间
     */
    private Date createTime;
    /**
     * 任务要求完成时间
     *
     */
    private Date requiredFinishTime;
    /**
     * 实际完成时间
     *
     */
    private Date realityFinishTime;
    public enum State {
        yes(1, "已完成"),
        reject(-1, "拒绝"),
        not(0, "未完成");

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
    //是否接受任务 1已接受 0等待接受  -1已拒绝
    public enum ReceiveState {
        yes(1, "已接受"),
        wait(0, "等待接受"),
        reject(-1, "拒绝")
        ;

        private Integer code;
        private String desc;

        ReceiveState(Integer code, String desc) {
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
    public String getStateDesc(){
        if(State.yes.getCode().equals(state)){
            return "已完成";
        }else if(State.reject.getCode().equals(state)){
            return "已拒绝";
        }else{
            return "未完成";
        }
    }
    public String getReceiveStateDesc(){
        if(ReceiveState.yes.getCode().equals(receiveState)){
            return "已接受";
        }else if(ReceiveState.reject.getCode().equals(receiveState)){
            return "已拒绝";
        }else{
            return "等待接受";
        }
    }
}
