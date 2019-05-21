package com.fun;

import com.fun.frame.Output;
import net.sf.json.JSONObject;

public class TD {

    private static final String sign_salt = "PMS@1QAZXSW2";

    public static void main(String[] args) throws Exception {

        String ss = "34323423";
        JSONObject json = JSONObject.fromObject("{\"requestid\":\"071834775404\",\"is_new\":\"1\",\"uid\":\"82951377345\",\"page\":\"1\",\"pagesize\":\"200\",\"token\":\"a16b20d639a0ead6ef4284e1a789455c\"}");
        Output.output(json);
        json.remove("token");
        Output.output(json);
    }

}