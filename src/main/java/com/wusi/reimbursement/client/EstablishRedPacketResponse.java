package com.wusi.reimbursement.client;

/**
 * @Author: huze
 * @Date: 2020/12/10 15:57
 */
public class EstablishRedPacketResponse extends ZcApiResult<EstablishRedPacketModel> {
    @Override
    public Class<EstablishRedPacketModel> modelClass() {
        return EstablishRedPacketModel.class;
    }

}
