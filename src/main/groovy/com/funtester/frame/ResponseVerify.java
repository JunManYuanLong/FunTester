package com.funtester.frame;

import com.alibaba.fastjson.JSONObject;
import com.funtester.httpclient.FunLibrary;
import com.funtester.utils.Regex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 通用验证方法封装
 */
public class ResponseVerify extends SourceCode {

    private static Logger logger = LogManager.getLogger(ResponseVerify.class);

    /**
     * 断言的json对象
     */
    private JSONObject verifyJson;

    /**
     * 断言的code码
     */
    private int code;

    /**
     * 获取所有lines
     *
     * @return
     */
    public List<String> getLines() {
        return lines;
    }

    /**
     * 断言的json对象分行解析
     */
    private List<String> lines = new ArrayList<>();

    public ResponseVerify(JSONObject jsonObject) {
        this.verifyJson = jsonObject;
        this.lines = parseJsonLines(jsonObject);
    }

    /**
     * 获取 code
     * <p>这里的requestinfo主要的目的是为了拦截一些不必要的checkcode验证的，主要有black_host名单提供，在使用时，注意requestinfo的非空校验</p>
     *
     * @return
     */
    public int getCode() {
        return FunLibrary.getiBase().checkCode(verifyJson, null);
    }


    /**
     * 校验code码是否正确,==0
     *
     * @return
     */
    public boolean isRight() {
        return 0 == this.getCode();
    }

    /**
     * 获取节点值
     *
     * @param key 节点
     * @return 返回节点值
     */
    public String getValue(String key) {
        int size = lines.size();
        for (int i = 0; i < size; i++) {
            String line = lines.get(i);
            if (line.startsWith(key + ":"))
                return line.replaceFirst(key + ":", EMPTY);
        }
        return EMPTY;
    }

    /**
     * 校验是否包含文本
     *
     * @param text 需要校验的文本
     * @return 返回 Boolean 值
     */
    public boolean isContains(String... text) {
        boolean result = true;
        String content = verifyJson.toString();
        int length = text.length;
        for (int i = 0; i < length; i++) {
            if (!result) break;
            result = content.contains(text[i]) & result;
        }
        return result;
    }

    /**
     * 校验节点值为数字
     *
     * @param value 节点名
     * @return 返回 Boolean 值
     */
    public boolean isNum(String... value) {
        boolean result = true;
        int length = value.length;
        for (int i = 0; i < length; i++) {
            String key = value[i] + ":";
            if (!verifyJson.toString().contains(value[i]) || !result)
                return false;
            for (int k = 0; k < lines.size(); k++) {
                String line = lines.get(k);
                if (line.startsWith(key)) {
                    String lineValue = line.replaceFirst(key, EMPTY);
                    result = isNumber(lineValue) & result;
                }
            }
        }
        return result;
    }

    /**
     * 校验节点值不为空
     *
     * @param keys 节点名
     * @return 返回 Boolean 值，为空返回false，不为空返回true
     */
    public boolean notNull(String... keys) {
        boolean result = true;
        for (int i = 0; i < keys.length; i++) {
            String key = keys[i] + ":";
            if (!verifyJson.toString().contains(keys[i]) || !result)
                return false;
            for (int k = 0; k < lines.size(); k++) {
                String line = lines.get(k);
                if (line.startsWith(key)) {
                    String lineValue = line.replaceFirst(key, EMPTY);
                    result = lineValue != null & !lineValue.isEmpty() & result;
                }
            }
        }
        return result;
    }

    /**
     * 验证是否为列表，根据字段后面的符号是否是[
     *
     * @param key 返回体的字段值
     * @return
     */
    public boolean isArray(String key) {
        String json = verifyJson.toString();
        int index = json.indexOf(key);
        char a = json.charAt(index + key.length() + 2);
        return a == '[';
    }

    /**
     * 验证是否是json，根据后面跟的符号是否是{
     *
     * @param key 返回体的字段值
     * @return
     */
    public boolean isJson(String key) {
        String json = verifyJson.toString();
        int index = json.indexOf(key);
        char a = json.charAt(index + key.length() + 2);
        if (a == '{')
            return true;
        return false;
    }

    /**
     * 是否是Boolean值
     *
     * @return
     */
    public boolean isBoolean(String... value) {
        boolean result = true;
        int length = value.length;
        for (int i = 0; i < length; i++) {
            String key = value[i] + ":";
            if (!verifyJson.toString().contains(value[i]) || !result)
                return false;
            for (int k = 0; k < lines.size(); k++) {
                String line = lines.get(k);
                if (line.startsWith(key)) {
                    String lineValue = line.replaceFirst(key, EMPTY);
                    result = Regex.isRegex(lineValue, "^(false)|(true)$") & result;
                }
            }
        }
        return result;
    }

    /**
     * 验证正则匹配结果
     *
     * @param regex
     * @return
     */
    public boolean isRegex(String regex) {
        String text = verifyJson.toString();
        return Regex.isRegex(text, regex);
    }

    /**
     * 解析json信息
     *
     * @param response json格式的响应实体
     * @return json每个字段和值，key:value形式
     */
    public static List<String> parseJsonLines(JSONObject response) {
        String jsonStr = response.toString();// 先将json对象转化为string对象
        jsonStr = jsonStr.replaceAll(",", LINE);
        jsonStr = jsonStr.replaceAll("\"", EMPTY);
        jsonStr = jsonStr.replaceAll("\\\\/", OR);
        jsonStr = jsonStr.replaceAll("\\{", LINE);
        jsonStr = jsonStr.replaceAll("\\[", LINE);
        jsonStr = jsonStr.replaceAll("}", LINE);
        jsonStr = jsonStr.replaceAll("]", LINE);
        List<String> jsonLines = Arrays.asList(jsonStr.split(LINE));
        return jsonLines;
    }
}
