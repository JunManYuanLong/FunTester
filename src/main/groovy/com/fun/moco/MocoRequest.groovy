package com.fun.moco

import com.fun.frame.SourceCode
import com.github.dreamhead.moco.RequestMatcher

import static com.fun.moco.JsonRequestExtractor.queryJson
import static com.github.dreamhead.moco.Moco.*

/**
 * requestmatcher获取
 */
class MocoRequest extends SourceCode {
/**
 * url正则匹配
 * @param regex
 * @return
 */
    static RequestMatcher urlMatcher(String regex) {
        match uri(regex)
    }

/**
 * url匹配
 * @param url
 * @return
 */
    static RequestMatcher urlOnly(String url) {
        by uri(url)
    }

/**
 * url是否以文本开头
 * @param text
 * @return
 */
    static RequestMatcher urlStartsWith(String text) {
        startsWith uri(text)
    }

/**
 * url是否以文本结束
 * @param text
 * @return
 */
    static RequestMatcher urlEndWith(String text) {
        endsWith uri(text)
    }

/**
 * url是否包含文本
 * @param text
 * @return
 */
    static RequestMatcher urlContain(String piece) {
        contain uri(piece)
    }

/**
 * get请求参数是否相等
 * @param key
 * @param value
 * @return
 */
    static RequestMatcher eqArgs(String key, String value) {
        eq query(key), value
    }

/**
 * post请求json数据参数是否相等
 * @param key
 * @param value
 * @return
 */
    static RequestMatcher eqParams(String key, String value) {
        eq queryJson(key), value
    }

/**
 * post请求form参数是否相等
 * @param key
 * @param value
 * @return
 */
    static RequestMatcher eqForm(String key, String value) {
        eq form(key), value
    }

/**
 * url中是否存在字符串
 * @param url
 * @return
 */
    static RequestMatcher existUrl(String piece) {
        exist contain(uri(piece))
    }

/**
 * get请求参数是否存在
 * @param key
 * @return
 */
    static RequestMatcher existArgs(String key) {
        exist(query(key))
    }

/**
 * post请求json数据参数是否存在
 * @param key
 * @return
 */
    static RequestMatcher existParams(String key) {
        exist(queryJson(key))
    }

/**
 * post请求form表单参数是否存在
 * @param key
 * @return
 */
    static RequestMatcher existForm(String key) {
        exist(form(key))
    }

/**
 * header是否存在
 * @param key
 * @return
 */
    static RequestMatcher existHeader(String key) {
        exist(header(key))
    }

/**
 * cookie是否存在
 * @param key
 * @return
 */
    static RequestMatcher existCookie(String key) {
        exist(cookie(key))
    }
}
