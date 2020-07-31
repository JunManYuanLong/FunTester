package com.FunTester.spock.pratice

import com.fun.utils.DecodeEncode
import org.slf4j.Logger
import spock.lang.Shared
import spock.lang.Specification

import static com.fun.frame.SourceCode.getLogger

class ZIP extends Specification {

    @Shared
    int a = 10;

    @Shared
    Logger logger = getLogger(this.getClass().getName())

    def setupSpec() {
        logger.info "测试类开始! ${logger.getName()}"
    }

    def setup() {
        logger.info "测试方法开始！"
    }

    def "测试加密解密"() {

        expect:
        name.length() > DecodeEncode.zipBase64(name).length()

        where:
        name << ["00000000000000000000000000000000000000000000000000000",
                "51666666666666666666666666666666666666666666666666666",
                "(&%^&%*&%(^(^(*&^*(&^(*&^(*^(*&%^%^\$^%##@#!#@!~~#@",
                "发大房东放大反动发动机吧就产国产过高冬季佛冬季风戳分床三佛",
                 "gkjdgjdgjlfdjgldkgjfdsafoiwehoirehtoiewho"]
    }


}



