package com.fun.config;

import com.fun.base.exception.ParamException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通用验证类型,包含,正则,JsonPath三项
 */
public enum VerifyType {

    CONTAIN, REGEX, JSONPATH, HANDLE;

    private static Logger logger = LoggerFactory.getLogger(VerifyType.class);

    /**
     * 获取验证类型,不区分大小写
     *
     * @param name
     * @return
     */
    public static VerifyType getRequestType(String name) {
        logger.debug("验证校验方式方式：{}", name);
        if (StringUtils.isEmpty(name)) ParamException.fail("参数不能为空!");
        name = name.toLowerCase();
        switch (name) {
            case "contain":
                return CONTAIN;
            case "regex":
                return REGEX;
            case "jsonpath":
                return JSONPATH;
            case "handle":
                return HANDLE;
            default:
                ParamException.fail(name + "参数错误!");
                return null;
        }
    }

}
