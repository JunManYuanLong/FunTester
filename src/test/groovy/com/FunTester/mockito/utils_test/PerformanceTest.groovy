package com.FunTester.mockito.utils_test

import com.funtester.base.constaint.ThreadBase
import com.funtester.base.constaint.ThreadLimitTimesCount
import com.funtester.base.interfaces.MarkThread
import com.funtester.config.HttpClientConstant
import com.funtester.frame.SourceCode
import com.funtester.frame.thread.HeaderMark
import com.funtester.frame.thread.RequestThreadTime
import com.funtester.frame.thread.RequestThreadTimes
import com.funtester.httpclient.FunLibrary
import org.apache.http.client.methods.HttpGet
import org.slf4j.Logger
import spock.lang.Shared
import spock.lang.Specification

import static com.funtester.config.Constant.EMPTY
import static com.funtester.config.Constant.TEST_ERROR_CODE

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
        HttpGet httpGet = FunLibrary.getHttpGet("https://cn.bing.com/");
        MarkThread mark = new HeaderMark("requestid")
        FunLibrary.getHttpResponse(httpGet);
        HttpClientConstant.MAX_ACCEPT_TIME = -1
        RequestThreadTimes threadTimes = new RequestThreadTimes(httpGet, 2, mark);
//        new Concurrent(threadTimes * 2,"测试并发情况下记录响应标记符的").start();

    }

    def "测试并发情况下记录响应标记符的,按照时间压测"() {
        given:
        HttpGet httpGet = FunLibrary.getHttpGet("https://cn.bing.com/");
        MarkThread mark = new HeaderMark("requestid")
        FunLibrary.getHttpResponse(httpGet);
        HttpClientConstant.MAX_ACCEPT_TIME = -1
        RequestThreadTime threadTimes = new RequestThreadTime(httpGet, 1, mark);
//        new Concurrent(threadTimes * 2).start();

    }


    def "测试虚拟类内部类实现"() {
        given:
        def threads = []
        3.times {
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
//        new Concurrent(threads).start()

        expect:

        2 == 2

    }

    def "测试虚拟类内部类实现,连续多次"() {
        given:
        def threads = []
        3.times {
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
//        new Concurrent(threads).start()
//        sleep(1000)
//        new Concurrent(threads).start()

        expect:

        2 == 2

    }
}