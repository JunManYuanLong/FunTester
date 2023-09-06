package com.funtester.frame;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.funtester.base.bean.AbstractBean;
import com.funtester.config.Constant;
import com.funtester.utils.StringUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fusesource.jansi.Ansi;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.funtester.frame.SourceCode.random;
import static org.fusesource.jansi.Ansi.ansi;

@SuppressWarnings("all")
public class Output extends Constant {

    private static Logger logger = LogManager.getLogger(Output.class);

    private static final String UP = rgb("~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~");

    private static final String DOWN = rgb("~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~");

    public static String Pre = "♨ ♨ ";

    public static String F = "➤";

    public static String J = SPACE_1 + F + SPACE_1;

    public static String Q = F + SPACE_1 + F + SPACE_1;

    public static void output(AbstractBean bean) {
        output(bean.toJson());
    }

    /**
     * 输出，自带log方法,排除root用户使用输出
     *
     * @param text
     */
    public static void output(String text) {
        logger.info(text);
    }

    /**
     * 随机彩色输出
     *
     * @param text
     */
    public static void outRGB(String text) {
        logger.info(rgb(text));
    }

    public static void output(byte[] bytes) {
        logger.info(new String(bytes));
    }

    /**
     * 输出，针对各种不同情况做兼容
     * <p>
     * 在处理两个对象，默认情况第一个是说明文字，第二个是list内容
     * </p>
     *
     * @param object
     */
    public static void output(Object... object) {
        if (ArrayUtils.isEmpty(object)) {
            logger.warn("怎么空了呢！");
        } else if (object.length == 1) {
            if (object[0] instanceof List) {
                output((List) object[0]);
            } else {
                output(object[0].toString());
            }
        } else if (object.length == 2) {
            output(object[0]);
            if (object[1] instanceof List) {
                output((List) object[1]);
            } else {
                output(object[1]);
            }
        } else if (object.getClass().isArray()) {
            output(Arrays.asList(object));
        }
    }

    /**
     * 如遇到重复元素,会导致计数不准确(已经解决了)
     *
     * @param list
     */
    public static void output(List list) {
        AtomicInteger integer = new AtomicInteger(1);
        list.forEach(x -> output("第" + integer.getAndIncrement() + "个:" + x.toString()));
    }

    public static void output(Iterator its) {
        its.forEachRemaining(x -> output(x.toString()));
    }

    /**
     * 输出无序集合
     *
     * @param its
     */
    public static void output(Iterable its) {
        its.forEach(x -> output(x.toString()));
    }

    /**
     * 输出json数组
     *
     * @param jsonArray
     */
    public static void output(JSONArray jsonArray) {
        if (jsonArray == null || jsonArray.isEmpty()) {
            output("jsonarray对象为空!");
            return;
        }
        jsonArray.forEach(x -> {
            try {
                output(JSON.parseObject(x.toString()));
            } catch (JSONException e) {
                output(x);
            }
        });
    }

    /**
     * 输出数组
     *
     * @param arrays
     */
    public static <F extends Number> void output(F[] nums) {
        if (ArrayUtils.isEmpty(nums)) return;
        Arrays.asList(nums).forEach(x -> output(x));
    }

    /**
     * 泛型做输出数字对象
     *
     * @param x
     * @param <F>
     */
    public static <F extends Number> void output(F f) {
        output(f.toString());
    }

    public static void output(Object o) {
        if (o == null) logger.warn("怎么空了呢！");
        else output(o.toString());
    }

    public static void output(int[] nums) {
        output(ArrayUtils.toObject(nums));
    }

    public static void output(long[] nums) {
        output(ArrayUtils.toObject(nums));
    }

    public static void output(double[] nums) {
        output(ArrayUtils.toObject(nums));
    }

    /**
     * 输出json
     *
     * @param jsonObject json格式响应实体
     */
    public static JSONObject output(JSONObject jsonObject) {
        if (jsonObject == null || jsonObject.size() == 0) {
            output("json 对象是空的！");
            return jsonObject;
        }
        outputJsonStr(jsonObject.toString());
        return jsonObject;
    }

    /**
     * 输出map,默认使用JSON形式
     *
     * @param map
     */
    public static void output(Map map) {
        output(JSONObject.parseObject(JSON.toJSONString(map)));
    }

    /**
     * 以JSON格式输出
     *
     * @param o
     */
    public static void outputJson(Object o) {
        output(SourceCode.parse(o));
    }


    public static void outputJsonStr(String jsonStr) {
        jsonStr = jsonStr.replaceAll("\\\\/", OR);
        int level = 0;// 用户标记层级
        StringBuffer jsonResultStr = new StringBuffer(Pre);// 新建stringbuffer对象，用户接收转化好的string字符串
        int length = jsonStr.length();
        for (int i = 0; i < length; i++) {// 循环遍历每一个字符
            char piece = jsonStr.charAt(i);// 获取当前字符
            // 如果上一个字符是断行，则在本行开始按照level数值添加标记符，排除第一行
            if ('\n' == jsonResultStr.charAt(jsonResultStr.length() - 1)) {
                jsonResultStr.append(StringUtil.getSerialEmoji(level) + J);
                IntStream.range(0, level - 1).forEach(x -> jsonResultStr.append(Q));//没有采用sourcecode的getmanystring
            }
            char last = i == 0 ? '{' : jsonStr.charAt(i - 1);
            char next = i < length - 1 ? jsonStr.charAt(i + 1) : '}';
            switch (piece) {
                case ',':
                    // 如果是“,”，则断行
                    jsonResultStr.append(piece + (("\"0123456789le]}".contains(last + EMPTY) && "\"[{".contains(next + EMPTY)) ? LINE : EMPTY));
                    break;
                case '{':
                case '[':
                    // 如果字符是{或者[，则断行，level加1
                    jsonResultStr.append(piece + (":[{,".contains(last + EMPTY) && ",[{}]\"0123456789le".contains(next + EMPTY) ? LINE : EMPTY));
                    level++;
                    break;
                case '}':
                case ']':
                    // 如果是}或者]，则断行，level减1
//                    jsonResultStr.append(LINE);
                    jsonResultStr.append(("\"0123456789le]}{[,".contains(last + EMPTY) && "}],".contains(next + EMPTY) ? LINE : EMPTY));
                    level--;
                    jsonResultStr.append(level == 0 ? "" : StringUtil.getSerialEmoji(level) + J);
                    IntStream.range(0, level - 1).forEach(x -> jsonResultStr.append(Q));//没有采用sourcecode的getmanystring
                    jsonResultStr.append(piece);
                    break;
                default:
                    jsonResultStr.append(piece);
                    break;
            }
        }
        output(LINE + UP + " JSON " + UP + LINE + jsonResultStr.toString().replaceAll(LINE, LINE + Pre) + LINE + DOWN + " JSON " + DOWN);
    }

    public static void show(Map map) {
        new ConsoleFable(map);
    }

    public static void show(List<List<String>> rows) {
        new ConsoleFable(rows);
    }

    /**
     * 打印可能的json数据
     *
     * @param content
     */
    public static void showStr(String content) {
        try {
            if (content.contains("&")) output(SourceCode.getJson(content.split("&")));
            else output(JSONObject.parseObject(content));
        } catch (JSONException e) {
            output(content);
        }
    }

    static class ConsoleFable extends SourceCode {

        List<Integer> rowLength = new ArrayList<>();

        public static void show(Map map) {
            new ConsoleFable(map);
        }

        /**
         * 输出map
         *
         * @param map
         */
        private ConsoleFable(Map map) {
            Set set = map.keySet();
            int asInt0 = set.stream().mapToInt(key -> key.toString().length()).max().getAsInt();
            rowLength.add(asInt0 + 2);
            List<String> values = new ArrayList<>();
            set.forEach(key -> values.add(map.get(key).toString()));
            int asInt1 = values.stream().mapToInt(value -> value.length()).max().getAsInt();
            rowLength.add(asInt1 + 2);
            StringBuffer stringBuffer = new StringBuffer(LINE + getHeader());
            map.forEach((k, v) -> {
                stringBuffer.append(getCel(0, k.toString()));
                stringBuffer.append(getCel(1, v.toString()));
            });
            output(stringBuffer.append(LINE + getHeader()).toString());
        }

        /**
         * 输出list
         *
         * @param rows
         */
        private ConsoleFable(List<List<String>> rows) {
            for (int i = 0; i < rows.size(); i++) {
                List<String> line = rows.get(i);
                for (int j = 0; j < line.size(); j++) {
                    String s = line.get(j);
                    if (rowLength.size() <= j) rowLength.add(0);
                    if (rowLength.get(j) < s.length()) rowLength.set(j, s.length());
                }
            }
            rowLength = rowLength.stream().map(n -> n + 2).collect(Collectors.toList());
            StringBuffer stringBuffer = new StringBuffer(LINE + getHeader());
            for (int i = 0; i < rows.size(); i++) {
                List<String> line = rows.get(i);
                for (int j = 0; j < rowLength.size(); j++) {
                    stringBuffer.append(getCel(j, j < line.size() ? line.get(j) : EMPTY));
                }
            }
            output(stringBuffer.append(LINE + getHeader()).toString());
        }


        /**
         * 获取每一格的string
         *
         * @param colum   列
         * @param content 格内容
         * @return
         */
        public String getCel(int colum, String content) {
            return (colum == 0 ? LINE + PART : PART) + StringUtil.center(content, rowLength.get(colum)) + (rowLength.size() - colum == 1 ? EMPTY + PART : EMPTY);
        }

        /**
         * 获取头尾行
         *
         * @return
         */
        private String getHeader() {
            List<String> collect = rowLength.stream().map(size -> getManyString("-", size)).collect(Collectors.toList());
            return "+" + StringUtils.join(collect.toArray(), "+") + "+";
        }


    }


    public static String rgb2(String str) {
        return Stream.of(ArrayUtils.toObject(str.toCharArray())).map(f -> color(random(Stream.of(Ansi.Color.values()).filter(v -> v != Ansi.Color.BLACK).collect(Collectors.toList())), f)).collect(Collectors.joining());
    }

    public static String rgb(String str) {
        char[] array = str.toCharArray();
        List<Ansi.Color> collect = Stream.of(Ansi.Color.values()).filter(v -> v != Ansi.Color.BLACK).collect(Collectors.toList());
        Ansi ansi = ansi();
        for (int i = 0; i < array.length; i++) {
            ansi.fg(random(collect)).a(array[i]);
        }
        return ansi.reset().toString();
    }

    public static String color(Ansi.Color color, Object o) {
        return ansi().fg(color).a(o.toString()).reset().toString();
    }

    public static void black(String str) {
        System.out.println(ansi().fgBlack().a(str));
    }

    public static void blue(Object o) {
        output(color(Ansi.Color.BLUE, o));
    }

    public static void red(Object o) {
        output(color(Ansi.Color.RED, o));
    }

    public static void green(Object o) {
        output(color(Ansi.Color.GREEN, o));
    }

    public static void yellow(Object o) {
        output(color(Ansi.Color.YELLOW, o));
    }

    public static void white(Object o) {
        output(color(Ansi.Color.WHITE, o));
    }

    public static void black(Object o) {
        output(color(Ansi.Color.BLACK, o));
    }

    public static void gyan(Object o) {
        output(color(Ansi.Color.CYAN, o));
    }

    public static void magenta(Object o) {
        output(color(Ansi.Color.MAGENTA, o));
    }

}
