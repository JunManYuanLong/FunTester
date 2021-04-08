package com.funtester.config


import com.funtester.base.exception.ParamException
import org.apache.commons.lang3.StringUtils
import org.apache.http.client.methods.HttpDelete
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPatch
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpPut
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

/**
 * 请求枚举类，fun备用，暂时无用,通过其他方式区分了post请求的参数格式
 */
enum RequestType {

    GET(HttpGet.METHOD_NAME), POST(HttpPost.METHOD_NAME), PUT(HttpPut.METHOD_NAME), DELETE(HttpDelete.METHOD_NAME),PATCH(HttpPatch.METHOD_NAME)

    static Logger logger = LogManager.getLogger(RequestType.class)

    String name

    private RequestType(String name) {
        this.name = name
    }

    /**
     * 获取请求类型
     * @param name
     * @return
     */
    static RequestType getInstance(String name) {
        logger.debug("验证请求方式：{}", name)
        if (StringUtils.isEmpty(name)) ParamException.fail("参数不能为空!")
        name = name.toUpperCase()
        switch (name) {
            case GET.name:
                return GET
            case POST.name:
                return POST
            case PUT.name:
                return PUT
            case DELETE.name:
                return DELETE
            case PATCH.name:
                return PATCH
            default:
                ParamException.fail("参数不支持!" + name)
        }

    }

}
