package com.fun.config;

/**
 * 请求枚举类，fun备用，在区分post请求参数类型中用到
 */
public enum RequestType {
    GET("get"), POST("post"), FUN("fun");

    String name;

    private RequestType(String name) {
        this.name = name;
    }

    public static RequestType getRequestType(String name) {
        for (RequestType requestType : RequestType.values()) {
            if (requestType.name.equalsIgnoreCase(name)) {
                return requestType;
            }
        }
        return FUN;
    }

    /**
     * 获取名字
     *
     * @return
     */
    public String getName() {
        return name;
    }
}
