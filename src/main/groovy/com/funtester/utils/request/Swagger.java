package com.funtester.utils.request;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.funtester.config.RequestType;
import com.funtester.httpclient.FunLibrary;
import com.funtester.utils.Regex;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * swagger文档解析类
 */
public class Swagger extends FunLibrary {

    /**
     * 关键字，用于url前
     */
    String key;

    /**
     * swagger文档地址
     */
    String swaggerPath;

    /**
     * 构造方法中接口地址类别
     */
    String name;

    /**
     * swagger地址所有类别
     */
    List<String> names = new ArrayList<>();

    /**
     * 某类别所有接口地址
     */
    List<String> urls = new ArrayList<>();

    /**
     * swagger文档转换成的json对象
     */
    JSONObject swagger = new JSONObject();

    /**
     * 所有接口地址的json对象
     */
    JSONObject paths = new JSONObject();

    /**
     * 对应构造方法中url的request对象
     */
    Request request = new Request();

    public Request getRequest() {
        return request;
    }

    public List<Request> getAllRequests() {
        return allRequests;
    }

    /**
     * 对应构造方法中name的所有request对象
     */
    List<Request> allRequests = new ArrayList<>();

    /**
     * 获取某一类的接口的request对象
     *
     * @param swaggerPath
     * @param name
     */
    public Swagger(String swaggerPath, String name) {
        this.swaggerPath = swaggerPath;
        this.name = name;
        build();
    }

    /**
     * 获取某一类的某一个接口的request对象
     *
     * @param swaggerPath
     * @param name
     * @param url
     */
    public Swagger(String swaggerPath, String name, String url) {
        this.swaggerPath = swaggerPath;
        this.name = name;
        build();
        request = getRequest(url);
    }


    public String getKey() {
        this.key = Regex.regexAll(this.swaggerPath, "/((?!/).)*/swagger.json").get(0);
        this.key = this.key.replace(OR, EMPTY).replace("swagger.json", EMPTY);
        if (this.key.contains(":")) this.key = EMPTY;
        return this.key;
    }

    /**
     * 获取name下所有接口的request对象
     */
    private void getRequests() {
        this.urls.forEach(url -> {
            Request request = getRequest(url);
            if (request != null) allRequests.add(request);
        });
    }

    /**
     * 初始化处理方法
     */
    public void build() {
        swagger = getHttpResponse(getHttpGet(this.swaggerPath));
        getKey();
        getNames();
        getPaths();
        getUrls();
        getRequests();

    }

    /**
     * 获取某一个url地址的请求request对象
     *
     * @param url 接口地址
     * @return
     */
    private Request getRequest(String url) {
        Request request = new Request();
        request.setUrl((OR + key + url).replace("//", "/"));
        JSONObject json1 = paths.getJSONObject(url);
        JSONObject json2 = new JSONObject();
        if (json1.containsKey("get")) {
            request.setType(RequestType.GET);
            json2 = json1.getJSONObject("get");
        } else if (json1.containsKey("post")) {
            request.setType(RequestType.POST);
            json2 = json1.getJSONObject("post");
        }
        String tags = json2.get("tags").toString();
        if (!tags.contains(name)) return null;
        String apiName = json2.getString("operationId");
        request.setApiName(apiName);
        String desc = json2.getString("summary");
        request.setDesc(desc);
        JSONArray json3 = json2.getJSONArray("parameters");
        JSONObject json5 = new JSONObject();
        JSONObject json6 = new JSONObject();
        json3.forEach(json -> {//获取参数，区分query和formdata
            JSONObject json4 = (JSONObject) json;
            String in = json4.getString("in");
            if (in.equals("query")) {
                boolean required = json4.getBoolean("required");
                if (required) {
                    String format = json4.getString("type");
                    String name = json4.getString("name");
                    json5.put(name, format);
                }
            } else if (in.equals("formData")) {
                boolean required = json4.getBoolean("required");
                if (required) {
                    String format = json4.getString("type");
                    String name = json4.getString("name");
                    json6.put(name, format);
                }
            }
        });
        request.setArgs(json5);
        request.setParams(json6);
        return request;
    }

    /**
     * 获取name下所有接口的地址
     */
    private void getUrls() {
        Set keySet = paths.keySet();
        keySet.forEach(key -> urls.add(key.toString()));
    }


    /**
     * 获取所有name
     */
    private void getNames() {
        JSONArray tags = swagger.getJSONArray("tags");
        tags.forEach(info -> {
            JSONObject name = (JSONObject) info;
            names.add(name.getString("name"));
        });
    }

    /**
     * 获取所有的接口地址
     */
    private void getPaths() {
        paths = swagger.getJSONObject("paths");
    }


}
