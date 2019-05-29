package com.fun.moco

import com.fun.frame.SourceCode
import com.github.dreamhead.moco.ResponseHandler
import com.github.dreamhead.moco.procedure.LatencyProcedure
import io.netty.handler.codec.Headers
import net.sf.json.JSONObject

import java.util.concurrent.TimeUnit

import static com.github.dreamhead.moco.Moco.*

/**
 * responsehandle获取
 */
class MocoResponseUtil extends SourceCode {

/**
 * 返回文本信息
 * @param content
 * @return
 */
    static ResponseHandler contentResponse(String content) {
        with content
    }

/**
 * 设置json格式的返回值
 * <p>
 *  json格式转成string返回
 * </p>
 * @param json
 * @return
 */
    static ResponseHandler jsonResponse(JSONObject json) {
        with json.toString()
    }

    static ResponseHandler randownResponse(ResponseHandler... handlers) {
        Headers[getRandomInt(handlers.size() - 1)]
    }

    static ResponseHandler randownResponse(String... contents) {
        with contents[getRandomInt(contents.size() - 1)]
    }

    static ResponseHandler sequenceResponse(String content, String... contents) {
        seq content, contents
    }

    static ResponseHandler sequenceResponse(ResponseHandler handler, ResponseHandler... handlers) {
        seq handler, handlers
    }

/**
 * 设置cookie，只支持一个cookie设置，因为header不允许相同的key重复
 * @param key
 * @param value
 * @return
 */
    static ResponseHandler setCookie(String key, String value) {
        cookie key, value
    }

/**
 * 设置header
 * @param key
 * @param value
 * @return
 */
    static ResponseHandler setHeader(String key, String value) {
        header key, value
    }

/**
 * 批量设置header
 * @param json
 * @return
 */
    static ResponseHandler[] setHeader(JSONObject json) {
        List<ResponseHandler> list = new ArrayList<>()
        json.each { x -> list << setHeader(x.key, x.value) }
        list.toArray()
    }

/**
 * 设置HTTP响应码，默认200
 * @param code
 * @return
 */
    static ResponseHandler setStatus(int code) {
        status code
    }

/**
 * 代理地址
 * @param url
 * @return
 */
    static ResponseHandler setProxy(String url) {
        proxy url
    }

    static LatencyProcedure limit(long duration) {
        latency duration, TimeUnit.MILLISECONDS
    }
}
