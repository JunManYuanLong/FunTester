package com.fun.frame;

import com.fun.utils.Regex;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用验证方法封装
 */
public class Verify extends SourceCode {

    private static Logger logger = LoggerFactory.getLogger(Verify.class);
    /**
     * 断言的json对象
     */
    private JSONObject verifyJson;
    /**
     * 断言的code码
     */
    private int code;
    /**
     * 断言的json对象分行解析
     */
    private List<String> lines = new ArrayList<>();

    public Verify(JSONObject jsonObject) {
        this.verifyJson = jsonObject;
        this.lines = parseJsonLines(jsonObject);
    }

    /**
     * 获取 code
     *
     * @return
     */
    public int getCode() {
        try {
            return verifyJson.getInt("code");
        } catch (JSONException e) {
            logger.warn("获取responseCode失败！", e);
        } finally {
            return TEST_ERROR_CODE;
        }
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
                return deleteCharFromString(key + ":", line);
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
     * @return返回 Boolean 值
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
                    String lineValue = deleteCharFromString(key, line);
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
                    String lineValue = deleteCharFromString(key, line);
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
        if (a == '[')
            return true;
        return false;
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
                    String lineValue = deleteCharFromString(key, line);
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
}
