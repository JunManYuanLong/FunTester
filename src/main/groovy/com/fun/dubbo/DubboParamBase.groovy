package com.fun.dubbo

import net.sf.json.JSONObject

class DubboParamBase {

    String type

    Object value

    DubboParamBase(String type, Object value) {
        this.type = type
        this.value = value
    }

    public static void main(String[] args) {

        new DubboParamBase("32r",new JSONObject())
    }
}
