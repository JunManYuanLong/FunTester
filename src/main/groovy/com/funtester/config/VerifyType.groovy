package com.funtester.config;

import com.funtester.base.exception.ParamException;
import org.apache.commons.lang3.StringUtils
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger;

/**
 * 通用验证类型,包含,正则,JsonPath,handle四项
 */
enum VerifyType {

    CONTAIN("contain"), REGEX("regex"), JSONPATH("jsonpath"), HANDLE("handle");

    String vname;

    VerifyType(String vname) {
        this.vname = vname;
    }

    private static Logger logger = LogManager.getLogger(VerifyType.class);

    /**
     * 获取验证类型,不区分大小写
     *
     * @param name
     * @return
     */
    static VerifyType getRequestType(String name) {
        logger.debug("验证校验方式方式：{}", name);
        if (StringUtils.isEmpty(name)) ParamException.fail("参数不能为空!");
        name = name.toLowerCase();
        switch (name) {
            case CONTAIN.getVname():
                return CONTAIN;
            case REGEX.getVname():
                return REGEX;
            case JSONPATH.getVname():
                return JSONPATH;
            case HANDLE.getVname():
                return HANDLE;
            default:
                ParamException.fail(name + "参数错误!");
        }
    }

}
