package com.funtester.utils.request;

import com.funtester.config.Constant;
import com.funtester.config.RequestType;
import com.funtester.httpclient.FunLibrary;
import com.funtester.utils.RWUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * 从文件中读取接口相关参数，用来发送请求，实现接口请求的配置化
 * <p>从当前路径下获取后缀为.log的文件，以文件名为准读取文件内容</p>
 */
public class RequestFile extends FunLibrary {

    private static Logger logger = LogManager.getLogger(RequestFile.class);

    String url;

    /**
     * get对应get请求，post对应post请求表单参数，其他对应post请求json参数
     */
    JSONObject headers;

    RequestType requestType;

    String name;

    JSONObject info;

    JSONObject params;

    /**
     * @param name
     */
    public RequestFile(String name) {
        this.name = name;
        getInfo();
        this.url = this.info.getString("url");
        requestType = RequestType.getRequestType(this.info.getString("requestType"));
        getParams();
        headers = JSONObject.parseObject(this.info.getString("headers"));
    }

    /**
     * 获取当前目录下的配置文件，以数字开头，后缀是.log的
     *
     * @param i
     */
    public RequestFile(int i) {
        this(i + Constant.EMPTY);
    }

    /**
     * 从配置文件中读取信息，组成一个json对象
     */
    private void getInfo() {
        String filePath = Constant.WORK_SPACE + this.name;
        logger.info("配置文件地址：" + filePath);
        this.info = RWUtil.readTxtByJson(filePath);
    }

    /**
     * 获取请求参数
     */
    private void getParams() {
        params = JSONObject.parseObject(info.getString("params"));
    }


    /**
     * 根据info组成请求
     *
     * @return
     */
    public HttpRequestBase getRequest() {
        HttpRequestBase requestBase;
        switch (this.requestType) {
            case GET:
                requestBase = getHttpGet(this.url, this.params);
                break;
            case POST:
                requestBase = getHttpPost(this.url, this.params);
                break;
            default:
                requestBase = getHttpPost(this.url, this.params.toString());
                break;
        }
        this.headers.keySet().forEach(x -> requestBase.addHeader(getHeader(x.toString(), headers.getString(x.toString()))));
        output(getHttpResponse(requestBase));
        return requestBase;
    }
}
