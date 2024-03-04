package com.wusi.reimbursement.client;

/**
 * @Author: huze
 * @Date: 2020/12/10 15:56
 */
public class EstablishRedPacketRequest extends ZcApiRequest<EstablishRedPacketResponse> {


    private static final long serialVersionUID = 8741042466281395185L;

    @Override
    public Class<EstablishRedPacketResponse> responseClass() {
        return EstablishRedPacketResponse.class;
    }

    public EstablishRedPacketRequest(){
        super();
        super.method = ApiMethod.ESTABLISH_REDPACKET.getMethod();
        super.version = ApiMethod.ESTABLISH_REDPACKET.getVersion();
    }
}
