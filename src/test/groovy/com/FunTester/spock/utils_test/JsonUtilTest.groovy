package com.FunTester.spock.utils_test

import com.alibaba.fastjson.JSON
import com.funtester.utils.JsonUtil
import org.slf4j.Logger
import spock.lang.Shared
import spock.lang.Specification

import java.util.concurrent.atomic.AtomicInteger

public class JsonUtilTest extends Specification {

    @Shared
    Logger logger = getLogger(this.getClass().getName())

    @Shared
    AtomicInteger times = new AtomicInteger(1)

    @Shared
    JsonUtil json = JsonUtil.getInstance(JSON.parseObject("{" +
            "    \"store\": {" +
            "        \"book\": [" +
            "            {" +
            "                \"category\": \"reference\"," +
            "                \"author\": \"Nigel Rees\"," +
            "                \"title\": \"Sayings of the Century\"," +
            "                \"page\": \"D\"," +
            "                \"pages\": [\"S\",\"X\",\"G\"]," +
            "                \"price\": 8.95" +
            "            }," +
            "            {" +
            "                \"category\": \"fiction\"," +
            "                \"author\": \"Evelyn Waugh\"," +
            "                \"title\": \"Sword of Honour\"," +
            "                \"page\": \"A\"," +
            "                \"pages\": [\"A\",\"B\"]," +
            "                \"price\": 12.99" +
            "            }," +
            "            {" +
            "                \"category\": \"fiction\"," +
            "                \"author\": \"Herman Melville\"," +
            "                \"title\": \"Moby Dick\"," +
            "                \"isbn\": \"0-553-21311-3\"," +
            "                \"page\": \"B\"," +
            "                \"pages\": [\"E\",\"F\"]," +
            "                \"price\": 8.99" +
            "            }," +
            "            {" +
            "                \"category\": \"fiction\"," +
            "                \"author\": \"J. R. R. Tolkien\"," +
            "                \"title\": \"The Lord of the Rings\"," +
            "                \"isbn\": \"0-395-19395-8\"," +
            "                \"page\": \"C\"," +
            "                \"pages\": [\"C\",\"D\"]," +
            "                \"price\": 22.99" +
            "            }" +
            "        ]," +
            "        \"bicycle\": {" +
            "            \"color\": \"red\"," +
            "            \"price\": 19.95" +
            "        }" +
            "    }," +
            "    \"expensive\": 10," +
            "    \"ss\": [32,32,4,23]" +
            "}"))

    def setupSpec() {
        logger.info "测试类开始! ${logger.getName()}"
    }

    def setup() {
        logger.info "第 ${times.get()} 次测试结束!"
    }

    def cleanup() {
        logger.info "第 ${times.getAndIncrement()} 次测试结束!"
    }

    def cleanupSpec() {
        logger.info "测试类结束! ${logger.getName()}"
    }

    def "验证取值效果"() {

        expect:
        value as String == json.getString(path)

        where:
        value                           | path
        10                              | "\$.expensive"
        "Sword of Honour"               | "\$.store.book[1].title"
        "0-395-19395-8"                 | "\$.store.book[3].isbn"
        19.95                           | "\$.store.bicycle.price"
        "[19.95,8.95,12.99,8.99,22.99]" | "\$..price"
        "[]"                            | "\$..fdsss"
        ""                              | "\$.fdsss"

    }

    def "验证数组相关功能"() {
        expect:
        value as String == json.getString(path)

        where:
        value           | path
        """["S","X"]""" | "\$..book[0].pages[0,1]"
        """["G"]"""     | "\$..book[0].pages[-1]"
        """["C"]"""     | "\$..book[?(@.price == 22.99)].page"
        """["C"]"""     | "\$..book[?(@.price in [22.99])].page"
        """["D"]"""     | "\$..book[?(@.price nin [22.99,8.99,12.99])].page"
        """["C"]"""     | "\$..book[?(@.title =~ /.*Lord.*/)].page"
        """["D","C"]""" | "\$..book[?(@.title =~ /.*the.*/)].page"
        """["B","C"]""" | "\$..book[?(@.pages subsetof ['D','C','E','F'])].page"
    }

    def "验证处理数组的函数"() {
        expect:
        value == json.getDouble(path)

        where:
        value   | path
        91      | "\$.ss.sum()"
        4       | "\$.ss.min()"
        32      | "\$.ss.max()"
        22.75   | "\$.ss.avg()"
//        11.4318 | "\$.ss.stddev()"
        4       | "\$.ss.length()"

    }

}
