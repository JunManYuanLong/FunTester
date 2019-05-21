package com.fun.frame;

import com.fun.base.bean.BaseBean;
import com.fun.config.Constant;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("all")
public class Output extends Constant {

    private static Logger logger = LoggerFactory.getLogger(Output.class);

    /**
     * 输出带有信息的异常
     *
     * @param object
     * @param e
     */
    public static void output(Object object, Exception e) {
        output(object);
        output(e);
    }

    public static void output(BaseBean bean) {
        output(bean.toJson());
    }

    /**
     * 输出异常
     *
     * @param e
     */
    public static void output(Exception e) {
        StackTraceElement[] stackTrace = e.getStackTrace();
        for (int i = 0; i < stackTrace.length; i++) {
            logger.warn(stackTrace[i].toString());
        }
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
     * 输出
     *
     * @param object
     */
    public static void output(Object... object) {
        if (object == null || object.length == 0) {
            logger.warn("怎么空了呢！");
        } else if (object.length == 1) {
            if (object[0] instanceof List) {
                output(((List<Object>) object[0]).toArray());
            } else {
                output(object[0].toString());
            }
        } else {
            for (int i = 0; i < object.length; i++) {
                output("第" + (i + 1) + "个：" + object[i]);
            }
        }
    }

    public static void output(Map map) {
        if (map == null || map.isEmpty()) {
            logger.warn("怎么空了呢！");
        } else {
            show(map);
        }
    }

    /**
     * 输出json数组
     *
     * @param jsonArray
     */
    public static void output(JSONArray jsonArray) {
        JSONObject jsonObject = new JSONObject();
        for (int i = 0; i < jsonArray.size(); i++) {
            jsonObject.put(i + 1, jsonArray.get(i));
        }
        output(jsonObject);
    }

    /**
     * 输出数组
     *
     * @param arrays
     */
    public static void output(Number[] arrays) {
        if (arrays == null)
            return;
        int length = arrays.length;
        for (int i = 0; i < length; i++) {
            output(arrays[i] + "");
        }
    }

    /**
     * 输出json
     *
     * @param jsonObject json格式响应实体
     */
    public static void output(JSONObject jsonObject) {
        if (jsonObject == null || jsonObject.isEmpty()) {
            output("json 对象是空的！");
            return;
        }
        String start = SourceCode.getManyString(SPACE_1, 4);
        String jsonStr = jsonObject.toString();// 先将json对象转化为string对象
        jsonStr = jsonStr.replaceAll("\\\\/", OR);
        int level = 0;// 用户标记层级
        StringBuffer jsonResultStr = new StringBuffer("＞  ");// 新建stringbuffer对象，用户接收转化好的string字符串
        for (int i = 0; i < jsonStr.length(); i++) {// 循环遍历每一个字符
            char piece = jsonStr.charAt(i);// 获取当前字符
            // 如果上一个字符是断行，则在本行开始按照level数值添加标记符，排除第一行
            if (i != 0 && '\n' == jsonResultStr.charAt(jsonResultStr.length() - 1)) {
                for (int k = 0; k < level; k++) {
                    jsonResultStr.append(start);
                }
            }
            switch (piece) {
                case ',':
                    // 如果是“,”，则断行
                    char last = jsonStr.charAt(i - 1);
                    if ("\"0123456789le]}".contains(last + EMPTY)) jsonResultStr.append(piece + LINE);
                    break;
                case '{':
                case '[':
                    // 如果字符是{或者[，则断行，level加1
                    jsonResultStr.append(piece + LINE);
                    level++;
                    break;
                case '}':
                case ']':
                    // 如果是}或者]，则断行，level减1
                    jsonResultStr.append(LINE);
                    level--;
                    for (int k = 0; k < level; k++) {
                        jsonResultStr.append(start);
                    }
                    jsonResultStr.append(piece);
                    break;
                default:
                    jsonResultStr.append(piece);
                    break;
            }
        }
        output(LINE + "↘ ↘ ↘ ↘ ↘ ↘ ↘ ↘ json ↙ ↙ ↙ ↙ ↙ ↙ ↙ ↙ ↙ ↙ ↙ ↙" + LINE + jsonResultStr.toString().replaceAll(LINE, LINE + "＞  ") + LINE + "↘ ↘ ↘ ↘ ↘ ↘ ↘ ↘ json ↙ ↙ ↙ ↙ ↙ ↙ ↙ ↙ ↙ ↙ ↙ ↙");
    }

    public static void show(Map map) {
        new ConsoleTable(map);
    }

    public static void show(List<List<String>> rows) {
        new ConsoleTable(rows);
    }

    static class ConsoleTable extends SourceCode {

        List<Integer> rowLength = new ArrayList<>();

        public static void show(Map map) {
            new ConsoleTable(map);
        }

        public static void show(List<List<String>> rows) {
            new ConsoleTable(rows);
        }

        /**
         * 输出map
         *
         * @param map
         */
        private ConsoleTable(Map map) {
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
        private ConsoleTable(List<List<String>> rows) {
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
            Integer integer = rowLength.get(colum);
            int i = integer - content.length();
            return (colum == 0 ? LINE + PART : PART) + getManyString(SPACE_1, i / 2) + content + getManyString(SPACE_1, i - i / 2) + (rowLength.size() - colum == 1 ? PART : EMPTY);
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
}
