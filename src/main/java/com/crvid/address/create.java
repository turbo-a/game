package com.crvid.address;

import io.bumo.model.request.operation.BaseOperation;
import io.bumo.model.response.result.data.Signature;

/**
 * @ClassName create
 * @Description TODO
 * @Auther elsfk
 * @Date2021/7/3 15:33
 * @Version 1.0
 **/
public class create {
    public static void main(String[] args) {
        createToken createToken = new createToken();
        String s = createToken.seralizeTransaction(3l,createToken.buildOperations());
        Signature[] signatures = createToken.signTransaction(s);
        String s1 = createToken.submitTransaction(s, signatures);
        System.out.println(s1);
    }
}
