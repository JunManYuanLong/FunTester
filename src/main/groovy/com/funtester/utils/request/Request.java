package com.funtester.utils.request;

import com.alibaba.fastjson.JSONObject;
import com.funtester.config.RequestType;
import com.funtester.frame.SourceCode;
import com.funtester.utils.Regex;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 从swagger文档中读取到的一个请求的所有信息
 */
public class Request extends SourceCode {

    /**
     * 请求的url
     */
    private String url;

    public String getUrl() {
        return url;
    }

    public RequestType getType() {
        return type;
    }

    public String getApiName() {
        return apiName;
    }

    public String getDesc() {
        return desc;
    }

    public JSONObject getArgs() {
        return args;
    }

    public JSONObject getParams() {
        return params;
    }

    public StringBuffer getStringBuffer() {
        return stringBuffer;
    }

    /**
     * 请求类型
     */
    RequestType type;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setType(RequestType type) {
        this.type = type;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setArgs(JSONObject args) {
        this.args = args;
    }

    public void setParams(JSONObject params) {
        this.params = params;
    }

    public void setStringBuffer(StringBuffer stringBuffer) {
        this.stringBuffer = stringBuffer;
    }

    public void setCode(StringBuffer code) {
        this.code = code;
    }

    /**
     * 接口名称
     */
    private String apiName;

    /**
     * 接口描述
     */
    private String desc;

    /**
     * restful参数
     */
    List<String> restfulArgs = new ArrayList<>();

    /**
     * query参数
     */
    JSONObject args = new JSONObject();

    /**
     * formdata参数
     */
    JSONObject params = new JSONObject();

    /**
     * 参数替换字符串，用户想方法里面添加参数
     */
    StringBuffer stringBuffer = new StringBuffer();

    /**
     * 代码文本
     */
    StringBuffer code = new StringBuffer();

    /**
     * 如果遇到post请求，fromdata参数为空时，url里面直接拼接请求字符串
     */
    boolean postNoParams = false;


    /**
     * 拼接json参数
     *
     * @param i 0：get请求；1：post请求
     */
    private void spliceArgs(int i) {
        String type = i == 1 ? "params" : "args";
        this.code.append(LINE + TAB + TAB + "JSONObject " + type + " = new JSONObject();");
        Set keySet = i == 0 ? args.keySet() : params.keySet();
        keySet.forEach(key -> {
            collectArgs(key.toString(), params.getString(key.toString()));
            this.code.append(LINE + TAB + TAB + type + ".put(\"" + key.toString() + "\", " + key.toString() + ");");
        });
    }


    /**
     * 收集参数，拼接往json传参的代码行
     *
     * @param key
     * @param value
     */
    private void collectArgs(String key, String value) {
        if (value.equals("string")) this.stringBuffer.append("String " + key.toString() + ",");
        if (value.equals("integer")) this.stringBuffer.append("int " + key.toString() + ",");
    }

    /**
     * 收集restful参数，处理url
     */
    private void collectRestfulArgs() {
        if (this.url.contains("{")) {//restful公参处理，并提取到restfulargs里面
            List<String> regexAll = Regex.regexAll(this.url, "\\{[^}]+\\}");
            regexAll.forEach(regex -> {
                regex = regex.replace("{", EMPTY).replace("}", EMPTY);
                this.restfulArgs.add(regex);
            });
        }
    }

    /**
     * 拼接url
     *
     * @return
     */
    private String spliceUrl() {
        collectRestfulArgs();
        this.url = this.url.contains("{") ? this.url : this.url + "\"";
        this.url = "\"" + this.url.replace("}/{", "+OR+").replace("{", "\"+").replace("}", EMPTY);
        return TAB + TAB + "String url = HOST + " + this.url + ";";
    }

    /**
     * 拼接get请求
     */
    private void spliceGet() {
        if (!this.args.isEmpty()) {
            spliceArgs(0);
            this.code.append(LINE + TAB + TAB + "HttpGet httpGet = getHttpGet(url, args);");//拼接获取请求方法
        } else {
            this.code.append(LINE + TAB + TAB + "HttpGet httpGet = getHttpGet(url);");//拼接获取请求方法
        }
        this.code.append(LINE + TAB + TAB + "JSONObject response = getHttpResponseEntityByJson(httpGet);");//拼接发送请求获取响应的方法
    }

    /**
     * 拼接post请求
     */
    private void splicePost() {
        if (!this.args.isEmpty()) spliceArgs(0);
        if (!this.params.isEmpty()) spliceArgs(1);
        if (this.args.isEmpty()) {//处理为空的情况
            if (!this.params.isEmpty()) {
                this.code.append(LINE + TAB + TAB + "HttpPost httpPost = getHttpPost(url, params);");
            } else {
                this.code.append(LINE + TAB + TAB + "HttpPost httpPost = getHttpPost(url);");
            }
        } else {
            if (!this.params.isEmpty()) {
                this.code.append(LINE + TAB + TAB + "HttpPost httpPost = getHttpPost(url, args, params);");
            } else {
                this.postNoParams = true;
            }
        }
        this.code.append(LINE + TAB + TAB + "JSONObject response = getHttpResponseEntityByJson(httpPost);");
    }

    /**
     * 拼接响应后代码行
     *
     * @return
     */
    private String spliceEnd() {
        restfulArgs.forEach(key -> stringBuffer.append("int " + key.toString() + ","));//在方法中添加参数类型的名称
        this.code.append(LINE + TAB + TAB + "output(response);");//拼接输出响应
        this.code.append(LINE + TAB + TAB + "return response;");//返回响应
        this.code.append(LINE + TAB + "}");
        return this.code.toString().replace("() {", "(" + stringBuffer.toString() + ") {").replace(",)", ")");//替换参数类型和名称
    }

    /**
     * 把request对象变成代码的方法
     *
     * @return
     */
    public String magic() {
        this.code.append(TAB + "/**\n\t * " + desc + "\n\t *\n\t * @return\n\t */" + LINE);
        this.code.append(TAB + "public JSONObject " + apiName + "() {" + LINE);//新建方法行
        String urlLine = spliceUrl();
        this.code.append(urlLine);
        if (restfulArgs.size() > 0) restfulArgs.forEach(arg -> args.remove(arg));//将公参从args里面删除
        if (this.type == RequestType.GET) spliceGet();
        if (this.type == RequestType.POST) splicePost();
        String finalCode = spliceEnd();
        if (this.postNoParams)
            finalCode = finalCode.replace(urlLine, urlLine.replace(";", EMPTY) + " + changeJsonToArguments(args)");
        return finalCode;
    }


}
