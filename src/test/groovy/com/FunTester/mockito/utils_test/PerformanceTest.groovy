package com.FunTester.mockito.utils_test

import com.fun.base.constaint.ThreadBase
import com.fun.base.constaint.ThreadLimitTimesCount
import com.fun.base.interfaces.MarkThread
import com.fun.config.HttpClientConstant
import com.fun.frame.SourceCode
import com.fun.frame.excute.Concurrent
import com.fun.frame.httpclient.FanLibrary
import com.fun.frame.thead.HeaderMark
import com.fun.frame.thead.RequestThreadTime
import com.fun.frame.thead.RequestThreadTimes
import org.apache.http.client.methods.HttpGet
import org.slf4j.Logger
import spock.lang.Shared
import spock.lang.Specification

import static com.fun.config.Constant.EMPTY
import static com.fun.config.Constant.TEST_ERROR_CODE
import static com.fun.frame.SourceCode.getLogger

class PerformanceTest extends Specification {

    @Shared
    Logger logger = getLogger(this.getClass().getName())

    def setupSpec() {
        logger.info "测试类开始! ${logger.getName()}"
    }

    def setup() {
        logger.info "测试方法开始！"
    }

    def cleanup() {
        logger.info "测试方法结束！"
    }

    def cleanupSpec() {
        logger.info "测试类结束! ${logger.getName()}"
    }

    def "测试并发情况下记录响应标记符的"() {
        given:
        HttpGet httpGet = FanLibrary.getHttpGet("https://cn.bing.com/");
        MarkThread mark = new HeaderMark("requestid")
        FanLibrary.getHttpResponse(httpGet);
        HttpClientConstant.MAX_ACCEPT_TIME = -1
        RequestThreadTimes threadTimes = new RequestThreadTimes(httpGet, 2, mark);
        new Concurrent(threadTimes * 2).start();

    }

    def "测试并发情况下记录响应标记符的,按照时间压测"() {
        given:
        HttpGet httpGet = FanLibrary.getHttpGet("https://cn.bing.com/");
        MarkThread mark = new HeaderMark("requestid")
        FanLibrary.getHttpResponse(httpGet);
        HttpClientConstant.MAX_ACCEPT_TIME = -1
        RequestThreadTime threadTimes = new RequestThreadTime(httpGet, 1, mark);
        new Concurrent(threadTimes * 2).start();

    }


    def "测试虚拟类内部类实现"() {
        given:
        def threads = []
        2.times {
            threads << new ThreadLimitTimesCount<Object>(null, 2, new MarkThread() {

                def i = SourceCode.getRandomInt(9) * 100


                @Override
                String mark(ThreadBase threadBase) {
                    return EMPTY + i++
                }

                @Override
                MarkThread clone() {
                    return null
                }
            }) {

                @Override
                protected void doing() throws Exception {
                    sleep(200)
                    logger.info("test method over once .")
                }

            }
        }
        HttpClientConstant.MAX_ACCEPT_TIME = TEST_ERROR_CODE
        new Concurrent(threads).start()

        expect:

        2 == 2

    }
}