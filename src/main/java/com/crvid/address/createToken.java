package com.crvid.address;

import com.alibaba.fastjson.JSONObject;
import io.bumo.SDK;
import io.bumo.common.ToBaseUnit;
import io.bumo.model.request.SDKConfigure;
import io.bumo.model.request.TransactionBuildBlobRequest;
import io.bumo.model.request.TransactionSignRequest;
import io.bumo.model.request.TransactionSubmitRequest;
import io.bumo.model.request.operation.AccountSetMetadataOperation;
import io.bumo.model.request.operation.AssetIssueOperation;
import io.bumo.model.request.operation.BaseOperation;
import io.bumo.model.response.TransactionBuildBlobResponse;
import io.bumo.model.response.TransactionSignResponse;
import io.bumo.model.response.TransactionSubmitResponse;
import io.bumo.model.response.result.data.Signature;

/**
 * @ClassName token
 * @Description TODO
 * @Auther elsfk
 * @Date2021/7/3 15:28
 * @Version 1.0
 **/
public class createToken {
    public BaseOperation[] buildOperations() {
        // The account address to issue apt1.0 token
        String issuerAddress = "buQZNc4crX43XQdtc76Ah1cHruneMgJSW1Su";
        // The token name
        String name = "Turbo";
        // The token code
        String code = "Turbo";
        // The apt token version
        String version = "1.0";
        // The apt token icon
        String icon = "";
        // The token total supply number
        Long totalSupply = 1000000000L;
        // The token now supply number
        Long nowSupply = 1000000000L;
        // The token description
        String description = "Turbo TOKEN";
        // The token decimals
        Integer decimals = 0;

        // Build asset issuance operation
        AssetIssueOperation assetIssueOperation = new AssetIssueOperation();
        assetIssueOperation.setSourceAddress(issuerAddress);
        assetIssueOperation.setCode(code);
        assetIssueOperation.setAmount(nowSupply);

        // If this is an atp 1.0 token, you must set metadata like this
        JSONObject atp10Json = new JSONObject();
        atp10Json.put("name", name);
        atp10Json.put("code", code);
        atp10Json.put("description", description);
        atp10Json.put("decimals", decimals);
        atp10Json.put("totalSupply", totalSupply);
        atp10Json.put("icon", icon);
        atp10Json.put("version", version);

        String key = "asset_property_" + code;
        String value = atp10Json.toJSONString();
        // Build setMetadata
        AccountSetMetadataOperation accountSetMetadataOperation = new AccountSetMetadataOperation();
        accountSetMetadataOperation.setSourceAddress(issuerAddress);
        accountSetMetadataOperation.setKey(key);
        accountSetMetadataOperation.setValue(value);

        BaseOperation[] operations = {assetIssueOperation, accountSetMetadataOperation};
        return operations;
    }

    public String seralizeTransaction(Long nonce,  BaseOperation[] operations) {
        String url = "http://seed1.bumo.io";
        SDK sdk = SDK.getInstance(url);
        SDKConfigure sdkConfigure = new SDKConfigure();
        sdkConfigure.setHttpConnectTimeOut(1000);
        sdkConfigure.setHttpReadTimeOut(5000);
        sdkConfigure.setUrl("http://seed1.bumo.io:16002");
        sdk = SDK.getInstance(sdkConfigure);
        String transactionBlob = null;

        // The account address to issue atp1.0 token
        String senderAddresss = "buQZNc4crX43XQdtc76Ah1cHruneMgJSW1Su";
        // The gasPrice is fixed at 1000L, the unit is MO
        Long gasPrice = 1000L;
        // Set up the maximum cost 50.03BU
        Long feeLimit = ToBaseUnit.BU2MO("200.03");
        // Nonce should add 1
        nonce += 1;

        // Build transaction  Blob
        TransactionBuildBlobRequest transactionBuildBlobRequest = new TransactionBuildBlobRequest();
        transactionBuildBlobRequest.setSourceAddress(senderAddresss);
        transactionBuildBlobRequest.setNonce(nonce);
        transactionBuildBlobRequest.setFeeLimit(feeLimit);
        transactionBuildBlobRequest.setGasPrice(gasPrice);
        for (int i = 0; i < operations.length; i++) {
            transactionBuildBlobRequest.addOperation(operations[i]);
        }
        TransactionBuildBlobResponse transactionBuildBlobResponse = sdk.getTransactionService().buildBlob(transactionBuildBlobRequest);
        if (transactionBuildBlobResponse.getErrorCode() == 0) {
            transactionBlob = transactionBuildBlobResponse. getResult().getTransactionBlob();
        } else {
            System.out.println("error: " + transactionBuildBlobResponse.getErrorDesc());
        }
        return transactionBlob;
    }
    public Signature[] signTransaction(String transactionBlob) {
        String url = "http://seed1.bumo.io";
        SDK sdk = SDK.getInstance(url);
        SDKConfigure sdkConfigure = new SDKConfigure();
        sdkConfigure.setHttpConnectTimeOut(1000);
        sdkConfigure.setHttpReadTimeOut(5000);
        sdkConfigure.setUrl("http://seed1.bumo.io:16002");
        sdk = SDK.getInstance(sdkConfigure);
        Signature[] signatures = null;
        // The account private key to issue atp1.0 token
        String senderPrivateKey = "privbUWRm3QjppKxWFGEh9FSwujiKjwPiGKHs9ikdzqKosHPB9xKa1U3";

        // Sign transaction BLob
        TransactionSignRequest transactionSignRequest = new TransactionSignRequest();
        transactionSignRequest.setBlob(transactionBlob);
        transactionSignRequest.addPrivateKey(senderPrivateKey);
        TransactionSignResponse transactionSignResponse = sdk.getTransactionService().sign(transactionSignRequest);
        if (transactionSignResponse.getErrorCode() == 0) {
            signatures = transactionSignResponse.getResult().getSignatures();
        } else {
            System.out.println("error: " + transactionSignResponse.getErrorDesc());
        }
        return signatures;
    }
    public String submitTransaction(String transactionBlob, Signature[] signatures) {
        String url = "http://seed1.bumo.io";
        SDK sdk = SDK.getInstance(url);
        SDKConfigure sdkConfigure = new SDKConfigure();
        sdkConfigure.setHttpConnectTimeOut(1000);
        sdkConfigure.setHttpReadTimeOut(5000);
        sdkConfigure.setUrl("http://seed1.bumo.io:16002");
        sdk = SDK.getInstance(sdkConfigure);
        String  hash = null;
        // Submit transaction
        TransactionSubmitRequest transactionSubmitRequest = new TransactionSubmitRequest();
        transactionSubmitRequest.setTransactionBlob(transactionBlob);
        transactionSubmitRequest.setSignatures(signatures);
        TransactionSubmitResponse transactionSubmitResponse = sdk.getTransactionService().submit(transactionSubmitRequest);
        if (0 == transactionSubmitResponse.getErrorCode()) {
            hash = transactionSubmitResponse.getResult().getHash();
        } else {
            System.out.println("error: " + transactionSubmitResponse.getErrorDesc());
        }
        return  hash ;
    }
}
