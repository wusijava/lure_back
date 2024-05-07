package com.wusi.reimbursement.entity;

import com.wusi.reimbursement.common.Identifiable;
import lombok.Data;

import java.util.Date;

@Data
public class CollectivityLure implements Identifiable<Long> {
    private Long id;

    /**
     * 发起人
     */
    private String creatorName;

    /**
     * 发起人uid
     */
    private String creatorUid;

    /**
     * 参与者uid
     */
    private String participantUid;

    /**
     * 参与者姓名
     */
    private String participantName;

    /**
     * 路亚时间
     */
    private Date lureDate;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 标点地址
     */
    private String address;


    /**
     * 标点地址经度
     */
    private String lng;

    /**
     * 标点纬度
     */
    private String lat;

    /**
     * 备注
     */
    private String remark;


    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 活动口号
     */
    private String slogan;

    /**
     * 活动状态  1开启 -1关闭
     */
    private Integer state;

    public enum State {
        close(-1, "关闭"),
        open(1, "开启");

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

}
