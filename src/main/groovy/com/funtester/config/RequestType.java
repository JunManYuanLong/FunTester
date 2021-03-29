package com.funtester.config;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 请求枚举类，fun备用，暂时无用,通过其他方式区分了post请求的参数格式
 */
public enum RequestType {

    GET("get"), POST("post"), FUN("fun");

    static Logger logger = LogManager.getLogger(RequestType.class);

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
