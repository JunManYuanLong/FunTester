package com.fun.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 请求枚举类，fun备用，在区分post请求参数类型中用到
 * fun是为了区分post请求，json格式传参用
 */
public enum RequestType {
    GET("get"), POST("post"), FUN("fun");

    static Logger logger = LoggerFactory.getLogger(RequestType.class);

    String name;

    private RequestType(String name) {
        this.name = name;
    }

    public static RequestType getRequestType(String name) {
        logger.debug("验证请求方式：{}", name);
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
