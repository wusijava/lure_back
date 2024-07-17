package com.wusi.reimbursement.query;



import lombok.Data;

@Data
public class CollectivityLureVo {
    private Long id;

    /**
     * 发起人
     */
    private String creatorName;

    /**
     * 参与者姓名
     */
    private String participantName;

    /**
     * 路亚时间
     */
    private String lureDate;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 标点地址
     */
    private String address;



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
    //private Integer state;

    private String stateDesc;

    private Boolean showClose=false;

    private Boolean showJoin=false;

    private Boolean showQuit=false;

    private String fishDesc;
}
