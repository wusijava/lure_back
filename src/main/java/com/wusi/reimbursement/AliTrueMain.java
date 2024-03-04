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
public class AliTrueMain {
    public static void main(String[] args) throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(
                "https://openapi.alipay.com/gateway.do",
                "2019112669430941",
                "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCKF0/RW6dR/NFzSVhh680+Vv2uy3YfkOejOBC8wKcCVs9o0B4RZ1/CnZceBostp0zXsx/kXbEdPAJ9FyPekah/ZeDNc/17IPtII2YybjIZjpvbFA7KARx3Y0+Rx2rlwWPoeXVr+c4oz33Kq4FgcI2DwIr0IJEcZmheVzvisbBxJ1R8DMgpfGGFIc0MIM6x9d+vu3IZcb6Z0nfZpqzybfeKh0EegaxQPp5pgxSSyR+Q7gBWm8aF4OJapOZiyjz9tqDIjZ4UQfozB6hMtcIgWvJUo3fAPdyZ9HkrszQaWeknGqtMuCVYdBh/RNqZ6GohBrtYT/1qQxezJnjBU/t6J3evAgMBAAECggEAUeF4xp8eG5NZbo6zBX0YejqCub6TECyP7Uw1QHbLkKuA2Jlu+srdETz4eilPJYbqHYxtALA9cSRAJYQvFK4xxzjVj32TdAsedKvo0UZeFqFk8QZ3rnfyNSiB6eLhE9MUNqiuY5QAFlrSL/Z6BIE4qELnIdKduMVQZYA8VjMeeDkGRLdYx4cFa0djo6z7l9+Ll5MKK6nMo9XHPkTSCqb3fojKsjiWOB9ZapCnczRQE2e6qdNKKnNv42lmkXRsBbfuLz+mjhrlc1YbifHdMpN5qscHYte0RP44AoDDU4TJStBlkczqHzTWvMWQCsJXSX/eehvMExWajqmfddHe38seAQKBgQDLnE3xhnGKEpx4nvc1gPZFz4YqmyR33y1OQZihLXzLl/3y51M4MQDm6XVgP9j+9RKzzpXCjNoP3WqLbGtZL0//JigL3CCeiAkKqM16WNmcB2KbLEMswtvTNfo3PYTYefcT+G0a3wdSENdiF3jiJ3ZqahaFe652sxbqMZjg8nEQbwKBgQCtn0e/llDt1J7Kv6MU0x2Q0R417QCsAvZ2HfGjNkVm1P63rcQE3Ql6neVEx/bgwx70Z0GfHbJaEppxw3eY7c/FxFniw0EI7/wDsoXdczrztTQX7z0fpJdj1yZ9jx9X4R5RgtAI8fYT0EsTyzpQo4iEMlOsYLjZyO7rnEqiCtkswQKBgQCZ0vRvzAprLbRYzIAnQ87K3wWVmq/qgAlSXexZmGv3xFRu9qTAhwUPbsDmflYKCEyg2yn5WUBFTBn6S2aucjgVo1YN37glDNEH5I+YJxf/8PRmFwKJBo3c+6KVqTx772jjBYQoEOcO8PpHeUQyV2325z8siUw9EP/23pGJIKCasQKBgHsxt9FDsWzwHzQxwl+2ZRYqq8sLvhxPhd3N5XUvvxL2Mz7Vt27UADPW8aVaVdyp1r8IvVFP2wt6g8gRMD19EJ59cmIqtWzIDAP7mmzFpKDkhHAvOynGuf0H3rHc6hqLC94+eaq/NSJd+8exvlEVmHpk02ZOwxsVLiJUJcjQ9cSBAoGBALCRU4PThIXnbFGcvAO9Me4C6yGPry7tSjXSl5YkvtXxA2BMjSH6T/2Z2hiYr6dRNCEF7xVNWgyqvHpS1+3gZgHhJaitTEc5/y78eLhv25Sg3QdzSyFAs/dSAaeeOSkS6WNP22VeO482zR3GBqhoMYaUZ+SUKySv7Dw4kaoqmuo3",
                "json",
                "GBK",
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArg0Vz2yJm5s/z9vUBbxfjwh8ipWpOePACH1WKwZYG6XVdJDI2YFP1cLhnAZjtrkfhdi8ezN3Xzh2MYC1vcbkHsIrRFGxwnCRoEfgpGRRL4u5gagMrQOtqYCO8twFbtdWem6PQls5j1OOJ76rwGiFUQLAXyivGFAiD7ub/1ixDu2R/TUQMFHuSyriRcYuovp3HPo2KwvEH/SbrhkJFE2aDShVeiofDIrXovNRRWRDroZYbdPrAsxej+WUqXOR+eYYHqAR1ZqAWF3W/DuoSwBO1CniF5cw7F8xXg9eDQm2JC5HiTEkO0l52zgYD+Rf4+rq1TJITT76rnzG2dWpzunPgQIDAQAB",
                "RSA2");
        AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
        request.setBizContent("{" +
                "\"out_biz_no\":\"123456\"," +
                "\"payee_type\":\"ALIPAY_LOGONID\"," +
                "\"payee_account\":\"15527875423\"," +
                "\"amount\":\"1\"," +
                "\"payer_show_name\":\"吴思\"," +
                "\"payee_real_name\":\"张明霞\"," +
                "\"remark\":\"支付测试\"" +
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
