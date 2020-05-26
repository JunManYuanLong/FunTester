package com.fun.moco

import com.fun.base.bean.Result
import com.fun.moco.support.CycleHandler
import com.fun.moco.support.DelayHandler
import com.fun.moco.support.LimitHandler
import com.fun.moco.support.QPSHandler
import com.fun.moco.support.RandomHandler
import com.github.dreamhead.moco.ResponseHandler
import com.github.dreamhead.moco.procedure.LatencyProcedure
import com.google.common.collect.FluentIterable
import com.alibaba.fastjson.JSONObject

import java.util.concurrent.TimeUnit

import static com.github.dreamhead.moco.Moco.*
import static com.github.dreamhead.moco.internal.ApiUtils.textToResource
import static com.github.dreamhead.moco.util.Iterables.asIterable

/**
 * responsehandle获取
 * 这里的继承关系为了更方便调用mocorequest和mocoresponse的静态方法
 */
@SuppressWarnings("all")
class MocoResponse extends MocoRequest {

/**
 * 返回文本信息
 * @param content
 * @return
 */
    static ResponseHandler textRes(String content) {
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
    static ResponseHandler jsonRes(JSONObject json) {
        with json.toString()
    }

/**
 * 返回对象
 * @param result
 * @return
 */
    static ResponseHandler obRes(Result result) {
        with result.toString()
    }

    static ResponseHandler success(Object result) {
        with Result.success(result).toString()
    }

    static ResponseHandler fail(Object result) {
        with Result.fail(result).toString()
    }

/**
 * 随机返回文本
 * @param contents
 * @return
 */
    static ResponseHandler cycleRes(String content, String... contents) {
        cycle content, contents
    }
/**
 * 随机返回文本
 * @param contents
 * @return
 */
    static ResponseHandler cycleRes(JSONObject content, JSONObject... contents) {
        cycle content.toString(), (String[]) contents.toList().stream().map {x -> x.toString()}.toArray()
    }


    /**
     * 随机response
     * @param handlers
     * @return
     */
    static ResponseHandler cycleRes(ResponseHandler handler, ResponseHandler... handlers) {
        cycle handler, handlers
    }

/**
 * 随机返回文本，会停留在最后一个文本内容
 * @param content
 * @param contents
 * @return
 */
    static ResponseHandler sequenceRes(String content, String... contents) {
        seq content, contents
    }
/**
 * 随机返回文本，会停留在最后一个文本内容
 * @param content
 * @param contents
 * @return
 */
    static ResponseHandler sequenceRes(JSONObject content, JSONObject... contents) {
        seq content.toString(), (String[]) contents.toList().stream().map {x -> x.toString()}.toArray()
    }

/**
 * 随机返回，最后会停留在最后一个handle
 * @param handler
 * @param handlers
 * @return
 */
    static ResponseHandler sequenceRes(ResponseHandler handler, ResponseHandler... handlers) {
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
        json.keySet().stream().map {x -> setHeader(x.toString(), json.getString(x))}.toArray() as ResponseHandler[]
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

/**
 * 延迟
 * @param duration 时间，单位：毫秒
 * @return
 */
    static LatencyProcedure delay(long duration) {
        latency duration, TimeUnit.MILLISECONDS
    }

/**
 * 随机
 * @param content
 * @param contents
 * @return
 */
    static ResponseHandler random(String content, String... contents) {
        RandomHandler.newSeq(FluentIterable.from(asIterable(content, contents)).transform(textToResource()))
    }

/**
 * 随机返回
 * @param json
 * @param jsons
 * @return
 */
    static ResponseHandler random(JSONObject json, JSONObject... jsons) {
        RandomHandler.newSeq(FluentIterable.from(asIterable(json.toString(), jsons.toList().stream().map {x -> x.toString()}.toArray() as String[])).transform(textToResource()))
    }

/**
 * 随机
 * @param handler
 * @param handlers
 * @return
 */
    static ResponseHandler random(ResponseHandler handler, ResponseHandler... handlers) {
        RandomHandler.newSeq(asIterable(handler, handlers))
    }

/**
 * 延迟响应
 * @param handler
 * @param time 时间,单位ms,存在理论BUG,不能低于50ms
 * @return
 */
    static ResponseHandler delay(ResponseHandler handler, int time) {
        DelayHandler.newSeq(handler, time)
    }

/**
 * 延迟响应,默认1000ms
 * @param handler
 * @return
 */
    static ResponseHandler delay(ResponseHandler handler) {
        DelayHandler.newSeq(handler, 1000)
    }

/**
 * 创建固定QPS的ResponseHandler,默认QPS=1
 * @param handler
 * @return
 */
    static ResponseHandler qps(ResponseHandler handler) {
        QPSHandler.newSeq(handler, 1000)
    }

/**
 * 创建固定QPS的ResponseHandler
 * @param handler
 * @param gap
 * @return
 */
    static ResponseHandler qps(ResponseHandler handler,int gap) {
        QPSHandler.newSeq(handler, gap)
    }

/**
 * 循环返回
 * @param content
 * @param contents
 * @return
 */
    static ResponseHandler cycle(String content, String... contents) {
        CycleHandler.newSeq(FluentIterable.from(asIterable(content, contents)).transform(textToResource()))
    }

/**
 * 循环返回
 * @param json
 * @param jsons
 * @return
 */
    static ResponseHandler cycle(JSONObject json, JSONObject... jsons) {
        CycleHandler.newSeq(FluentIterable.from(asIterable(json.toString(), jsons.toList().stream().map {x -> x.toString()}.toArray() as String[])).transform(textToResource()))
    }

/**
 * 循环返回
 * @param handler
 * @param handlers
 * @return
 */
    static ResponseHandler cycle(ResponseHandler handler, ResponseHandler... handlers) {
        CycleHandler.newSeq(asIterable(handler, handlers))
    }

/**
 * 限制访问频率,默认访问间隔1000ms
 * @param unlimited 不受限返回
 * @param limited 受限时候返回
 * @return
 */
    static ResponseHandler limit(String unlimited, String limited) {
        limit textRes(limited), textRes(limited)
    }

    static ResponseHandler limit(JSONObject unlimited, JSONObject limited) {
        limit jsonResponse(limited), jsonResponse(limited)
    }

    static ResponseHandler limit(ResponseHandler unlimited, ResponseHandler limited) {
        limit unlimited, limited, 1000
    }


/**
 *
 * 限制访问频率
 * @param unlimited 不受限返回
 * @param limited 受限时候返回
 * @param interval 访问间隔
 * @return
 */
    static ResponseHandler limit(String unlimited, String limited, int interval) {
        limit textRes(unlimited), textRes(limited), interval
    }

    static ResponseHandler limit(JSONObject unlimited, JSONObject limited, int interval) {
        limit unlimited.toString(), limited.toString(), interval
    }

    static ResponseHandler limit(ResponseHandler unlimited, ResponseHandler limited, int interval) {
        LimitHandler.newSeq(unlimited, limited, interval)
    }
}
