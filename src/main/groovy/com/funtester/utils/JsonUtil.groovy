package com.funtester.utils
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
 * min()	最小值	Double
 * max()	最大值	Double
 * avg()	平均值	Double
 * stddev()	标准差	Double
 * length()	数组长度	Integer
 * sum()	数组之和	Double
 * ==	left is equal to right (note that 1 is not equal to '1')
 * !=	left is not equal to right
 * <	left is less than right
 * <=	left is less or equal to right
 * >	left is greater than right
 * >=	left is greater than or equal to right
 * =~	left matches regular expression [?(@.name =~ /foo.*?/i)]
 * in	left exists in right [?(@.size in ['S', 'M'])]
 * nin	left does not exists in right
 * subsetof	子集 [?(@.sizes subsetof ['S', 'M', 'L'])]
 * anyof	left has an intersection with right [?(@.sizes anyof ['M', 'L'])]
 * noneof	left has no intersection with right [?(@.sizes noneof ['M', 'L'])]
 * size	size of left (array or string) should match right
 * empty	left (array or string) should be empty
 *
 * groovy需要  \$  Java不需要直接  $
 *
 */
class JsonUtil {

//    private static Logger logger = LogManager.getLogger(JsonUtil.class)
//
//    /**
//     * 用户构建对象,获取verify对象
//     */
//    private JSONObject json
//
//    private JsonUtil(JSONObject json) {
//        this.json = json
//    }
//
//    static JsonUtil getInstance(JSONObject json) {
//        new JsonUtil(json)
//    }
//
//    JsonVerify getVerify(String path) {
//        JsonVerify.getInstance(this.json, path)
//    }
//
//    /**
//     * 获取string对象
//     * @param path
//     * @return
//     */
//    String getString(String path) {
//        def object = get(path)
//        object == null ? Constant.EMPTY : object.toString()
//    }
//
//
//    /**
//     * 获取int类型
//     * @param path
//     * @return
//     */
//    int getInt(String path) {
//        SourceCode.changeStringToInt(getString ( path))
//    }
//
//    /**
//     * 获取boolean类型
//     * @param path
//     * @return
//     */
//    int getBoolean(String path) {
//        SourceCode.changeStringToBoolean(getString(path))
//    }
//
//    /**
//     * 获取long类型
//     * @param path
//     * @return
//     */
//    int getLong(String path) {
//        SourceCode.changeStringToLong(getString(path))
//    }
//
//    /**
//     * 获取double类型
//     * @param path
//     * @return
//     */
//    double getDouble(String path) {
//        SourceCode.changeStringToDouble(getString(path))
//    }
//
//    /**
//     * 获取list对象
//     * @param path
//     * @return
//     */
//    List getList(String path) {
//        get(path) as List
//    }
//
//    /**
//     * 获取匹配对象,类型传参
//     * 这里不加public  IDE会报错
//     * @param path
//     * @param tClass
//     * @return
//     */
//    public <T> T getT(String path, Class<T> tClass) {
//        try {
//            get(path) as T
//        } catch (ClassCastException e) {
//            logger.warn("类型转换失败!", e)
//        }
//    }
//
//    /**
//     * 获取匹配对象,这里如果使用模糊路径匹配失败,返回"[]",如果绝对路径报错
//     * @param path
//     * @return
//     */
//    def get(String path) {
//        logger.debug("匹配对象:{},表达式:{}", json.toString(), path)
//        if (json == null || json.isEmpty()) ParamException.fail("json为空或者null,参数错误!")
//        try {
//            JsonPath.read(this.json, path)
//        } catch (JsonPathException e) {
//            logger.warn("json: {} 解析失败,path: {}", json.toString(), path, e)
//        }
//    }
//

}
