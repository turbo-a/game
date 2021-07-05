package com.crvid.address;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.bumo.SDK;
import io.bumo.crypto.Keypair;
import io.bumo.model.request.AccountCheckValidRequest;
import io.bumo.model.request.AccountGetInfoRequest;
import io.bumo.model.request.AccountGetNonceRequest;
import io.bumo.model.request.SDKConfigure;
import io.bumo.model.request.operation.AccountSetMetadataOperation;
import io.bumo.model.request.operation.AssetIssueOperation;
import io.bumo.model.request.operation.BaseOperation;
import io.bumo.model.response.AccountCheckValidResponse;
import io.bumo.model.response.AccountGetInfoResponse;
import io.bumo.model.response.AccountGetNonceResponse;
import io.bumo.model.response.result.AccountGetInfoResult;
import io.bumo.model.response.result.AccountGetNonceResult;

/**
 * @ClassName newAddress
 * @Description TODO
 * @Auther elsfk
 * @Date2021/7/2 21:43
 * @Version 1.0
 **/
public class newAddress {
    public static void main(String[] args) {
        String url = "http://seed1.bumo.io";
        SDK sdk = SDK.getInstance(url);
        SDKConfigure sdkConfigure = new SDKConfigure();
        sdkConfigure.setHttpConnectTimeOut(1000);
        sdkConfigure.setHttpReadTimeOut(5000);
        sdkConfigure.setUrl("http://seed1.bumo.io:16002");
        sdk = SDK.getInstance(sdkConfigure);
        Keypair keypair = Keypair.generator();
        System.out.println(keypair.getPrivateKey());
        System.out.println(keypair.getPublicKey());
        System.out.println(keypair.getAddress());
        String address = "buQZNc4crX43XQdtc76Ah1cHruneMgJSW1Su";
        AccountCheckValidRequest request = new AccountCheckValidRequest();
        request.setAddress(address);


    }
}
