package com.wusi.reimbursement;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;

/**
 * @ Description   :  java类作用描述
 * @ Author        :  wusi
 * @ CreateDate    :  2020/11/28$ 15:46$
 */
public class AliMain {
    public static void main(String[] args) throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(
                "https://openapi.alipaydev.com/gateway.do",
                "2016102200741226",
                "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCFa4M9NpAggOvG7i+mZsPOGdovuq62PlGUN2UyDmIJJAkJKVP1sAjBfAKG+xrTbciAXkJX8oCMlylvLpVyNq4NjEIOtHfFqH9zn4f7n+U0x/9gykLSK9o6/beNH1MpCWiES9FWNjxBXFOKJRAKK6ZB5v52rBoBC1f4JOP/Oflhbusw4cUZ3RQRgClYQv7WqlseNVOgU+JhzrAVsy83/HCN1g51wpPTt2w2Zn9VRwchbr71MqqGOpyQuVLOJ8rdOJ628tdNLpushfqqZ3ZbiFkdC7fvNaGYw4okquaGhVS+CW5mcETIp517hskoWkdeRcqjcCDxPJUT+nRgneYNpzcxAgMBAAECggEAOBBUhp1t9OqXqPEfvsnCD9IXOCU3E0eemiyo7l7S3UpyLZJbQItnMn68xSGxRRUFwsNZF4HEeM9wTtMBY4wMmY7Nl0ZIahuC6wAncRngg8xiyLZVW+Gng1BoF/oIxN66N5Vwcsy5hzGXvR4T3mI3K8w4M3/gMX4lyhUeZXpkC4egznQ/sjIBXvdSj9NmzGMgJoFVDmqy/4gAKIIoapp67ZW/7eJvGrJ+8F+sCcBDXUKqzGzj6RXAvFc/QmAXV0eu9cdNzIN3LeIDOKdGnQ/mU1s0iVqjwFPY3yb87O4efocvwo4axPn/BzZ/JAEAgrcsxHL4YgsNkVNujslfjFd9rQKBgQD7BDukoIQ+GYv18Qg/9htT3FRGxuVSwhiUjzQp8el1r0o82lwMn1sBpImGAhLXMnnV6++TJb0COANeZJvm99WPrabMhJOVOLDsptaO+wStV5SOLvcLH0+Z94nY3GufyK+x3+m1THcwrNImivThBjd+/prvVhwVf7xf6Etq0jVq9wKBgQCIEZtLtISriHPNAeeIOn9QMktvx1qGRSHNsf+CY7qi0WB5+lh0SUCr3+8PV9SWSSnzw0atKX/KA9udvaJLxjxzWnwrxGa/dNWcjRqJWuUURghBLkTq1ciYG1tNAe2AOdrkHoF0Oo/uXzJBvy91y+Bg5ddtJWioJJR+7Lnnj2B9FwKBgQCfGrvDSYsI4aB5L2IPSKthqFIy2ncY0bZK08o4nAFies0PbUjTJ17D18rjigTvKitXkMi1+EGpWl+oQRUgfQ58l5EWKSJyfxCFr+pnjmwUg2LCDLrB7gLCkMhrEKuXB9l1YXv9Uo5p5+D8VS+KQiqC8Fa8RVRfvCtmjxLejocVWQKBgHozpc8F3qjXMXkKns/gCXsS0GEhHxG7gLJli92kh3e2pvild09ogmZaBS84aDpYlw7Pb+gQfy4E7PXr25hF/wByYVZFWynTWgzxRMXbFiVxB/TSAx8MxMymtQaiFpS0YPvC0ex6s80XPeGCPLOEWI1tnl3t3OI1iA+9tvBZ+gQdAoGAT5U4LAA82J2IKUj6Gc84H9g985RmG1zFVWdw4YhkHaYYgExjuHoEJYly8Gnse78OFwJd3pF5wLX5lFpmbpDeLX/RzA+XqpxpKEN7eBbjD0R3aoKye/RHa2rSl/l/UMp95r9UxYIZo4seIRgB5tvyy6CDBNkeSelNI/wXqRyhcaQ=",
                "json",
                "GBK",
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoFXIW3grYLu5pXVKh/CP6wiIKXtkkyNN+EQl56QoUgBXQQjNzOoUlX6HMqtVzQ3gKxsXpEuWWhZzmYWVNZ5lSmxem4khZNY0XPP72kIC8TMzLuIOiLJJYG++HhYPT33rj6FI2+VE0BZIv0hkUx4JIHPElrEfmeXsfDb8QKhKia2HEVvBB1mgiP5W4cPopfbzBuQRlELLb2ErN1MeN2Qq5MLjQCMqW0jTMD4u7NML5vMCXULupR/7Q+O6bQM4Ur1w2VfXYF/oRj/TTpd0hAjQvPuh19BhX0nGta0PcBedhld+dsTRc+IkEazsYWImgKJ1BQCT45y0ns3NFMPx2IKYfQIDAQAB",
                "RSA2");
        AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
        request.setBizContent("{" +
                "\"out_biz_no\":\"123456\"," +
                "\"payee_type\":\"ALIPAY_LOGONID\"," +
                "\"payee_account\":\"ttltdk2439@sandbox.com\"," +
                "\"amount\":\"10000\"," +
                "\"payer_show_name\":\"吴思\"," +
                "\"payee_real_name\":\"沙箱环境\"," +
                "\"remark\":\"转账备注\"" +
                "  }");
        AlipayFundTransToaccountTransferResponse response = alipayClient.execute(request);
        System.out.println(response);
        if(response.isSuccess()){
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
    }
}
