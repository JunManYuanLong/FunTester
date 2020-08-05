package com.fun.utils

import com.fun.config.Constant
import com.fun.frame.SourceCode
import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.JsonPathException
import org.codehaus.groovy.runtime.typehandling.GroovyCastException
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**下面是例子,官方文档地址:https://github.com/json-path/JsonPath/blob/master/README.md
 * $.store.book[*].author	The authors of all books
 * $..author	All authors
 * $.store.*	All things, both books and bicycles
 * $.store..price	The price of everything
 * $..book[2]	The third book
 * $..book[-2]	The second to last book
 * $..book[0,1]	The first two books
 * $..book[:2]	All books from index 0 (inclusive) until index 2 (exclusive)
 * $..book[1:2]	All books from index 1 (inclusive) until index 2 (exclusive)
 * $..book[-2:]	Last two books
 * $..book[2:]	Book number two from tail
 * $..book[?(@.isbn)]	All books with an ISBN number
 * $.store.book[?(@.price < 10)]	All books in store cheaper than 10
 * $..book[?(@.price <= $['expensive'])]	All books in store that are not "expensive"
 * $..book[?(@.author =~ /.*REES/i)]	All books matching regex (ignore case)
 * $..*	Give me every thing
 * $..book.length()	The number of books
 *
 *
 * min()	Provides the min value of an array of numbers	Double
 * max()	Provides the max value of an array of numbers	Double
 * avg()	Provides the average value of an array of numbers	Double
 * stddev()	Provides the standard deviation value of an array of numbers	Double
 * length()	Provides the length of an array	Integer
 * sum()	Provides the sum value of an array of numbers	Double
 */
class JsonUtil extends SourceCode {

    private static Logger logger = LoggerFactory.getLogger(JsonUtil.class)


    static String getString(Object json, String path) {
        def object = get(json, path)
        object == null ? Constant.EMPTY : object.toString()
    }


    static int getInt(Object json, String path) {
        changeStringToInt(getString(json, path))
    }

    static int getBoolean(Object json, String path) {
        changeStringToBoolean(getString(json, path))
    }

    static int getLong(Object json, String path) {
        changeStringToLong(getString(json, path))
    }

    static List getList(Object json, String path) {
        get(json, path) as List
    }

    static <T> T getT(Object json, String path,Class<T> tClass) {
        try {
            get(json, path) as T
        } catch (GroovyCastException e) {
            logger.warn("类型转换失败!", e)
            null
        }
    }

    static Object get(Object json, String path) {
        try {
            JsonPath.read(json, path)
        } catch (JsonPathException e) {
            logger.warn("jsonpath:{}解析失败", path, e)
            new Object()
        }
    }

}
