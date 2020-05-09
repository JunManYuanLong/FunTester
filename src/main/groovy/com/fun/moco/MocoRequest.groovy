package com.fun.moco

import com.fun.frame.SourceCode
import com.github.dreamhead.moco.RequestMatcher
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import static com.fun.moco.support.JsonExtractor.queryJson
import static com.github.dreamhead.moco.Moco.*

/**
 * requestmatcher获取
 */
class MocoRequest extends SourceCode {

    static Logger logger = LoggerFactory.getLogger(MocoRequest.class)

/**
 * url正则匹配
 * @param regex
 * @return
 */
    static RequestMatcher urlMatcher(String regex) {
        logger.debug("匹配请求的正则表达式：{}", regex)
        match uri(regex)
    }

/**
 * url匹配
 * @param url
 * @return
 */
    static RequestMatcher urlOnly(String url) {
        logger.debug("匹配的uri：{}", url)
        by uri(url)
    }

/**
 * url是否以文本开头
 * @param text
 * @return
 */
    static RequestMatcher urlStartsWith(String text) {
        logger.debug("匹配的url起始文本：{}", text)
        startsWith uri(text)
    }

/**
 * url是否以文本结束
 * @param text
 * @return
 */
    static RequestMatcher urlEndWith(String text) {
        logger.debug("匹配的url结束文本：{}", text)
        endsWith uri(text)
    }

/**
 * url是否包含文本
 * @param text
 * @return
 */
    static RequestMatcher urlContain(String piece) {
        logger.debug("匹配的url包含文本：{}", piece)
        contain uri(piece)
    }

/**
 * get请求参数是否相等
 * @param key
 * @param value
 * @return
 */
    static RequestMatcher eqArgs(String key, String value) {
        logger.debug("匹配的get请求参数key：{}，value：{}", key, value)
        eq query(key), value
    }

/**
 * post请求json数据参数是否相等
 * @param key
 * @param value
 * @return
 */
    static RequestMatcher eqParams(String key, String value) {
        logger.debug("匹配的post请求json参数key：{}，value：{}", key, value)
        eq queryJson(key), value
    }

/**
 * post请求form参数是否相等
 * @param key
 * @param value
 * @return
 */
    static RequestMatcher eqForm(String key, String value) {
        logger.debug("匹配的post请求form参数key：{}，value：{}", key, value)
        eq form(key), value
    }

/**
 * get请求参数是否存在
 * @param key
 * @return
 */
    static RequestMatcher existArgs(String key) {
        logger.debug("匹配的get请求参数key：{}是否存在", key)
        exist(query(key))
    }

/**
 * post请求json数据参数是否存在
 * @param key
 * @return
 */
    static RequestMatcher existParams(String key) {
        logger.debug("匹配的post请求json参数key：{}是否存在", key)
        exist(queryJson(key))
    }

/**
 * post请求form表单参数是否存在
 * @param key
 * @return
 */
    static RequestMatcher existForm(String key) {
        logger.debug("匹配的post请求form参数key：{}是否存在", key)
        exist(form(key))
    }

/**
 * header是否存在
 * @param key
 * @return
 */
    static RequestMatcher existHeader(String key) {
        logger.debug("匹配请求header中key：{}是否存在", key)
        exist(header(key))
    }

/**
 * cookie是否存在
 * @param key
 * @return
 */
    static RequestMatcher existCookie(String key) {
        logger.debug("匹配请求cookie中key：{}是否存在", key)
        exist(cookie(key))
    }

/**
 * 多个筛选条件
 * @param matcher
 * @param matchers
 * @return
 */
    static RequestMatcher both(RequestMatcher matcher, RequestMatcher... matchers) {
        return and(matcher, matchers);
    }

/**
 * 满足任意条件即可
 * @param matcher
 * @param matchers
 * @return
 */
    static RequestMatcher or(RequestMatcher matcher, RequestMatcher... matchers) {
        return com.github.dreamhead.moco.Moco.or(matcher, matchers);
    }

/**
 * 排除满足条件的请求
 * @param matcher
 * @param matchers
 * @return
 */
    static RequestMatcher not(RequestMatcher matcher, RequestMatcher... matchers) {
        return com.github.dreamhead.moco.Moco.not(matcher, matchers);
    }
}
