package com.fun.frame.thead;

import com.fun.base.constaint.ThreadLimitTimes;
import com.fun.frame.httpclient.ClientManage;
import com.fun.frame.httpclient.FanLibrary;
import com.fun.frame.httpclient.GCThread;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * http请求多线程类
 */
public class RequestThreadTimes extends ThreadLimitTimes {

    static Logger logger = LoggerFactory.getLogger(RequestThreadTimes.class);

    /**
     * 请求
     */
    public HttpRequestBase request;

    /**
     * 单请求多线程多次任务构造方法
     *
     * @param request 被执行的请求
     * @param times   每个线程运行的次数
     */
    public RequestThreadTimes(HttpRequestBase request, int times) {
        this.request = request;
        this.times = times;
    }

    @Override
    public void before() {
        GCThread.starts();
    }

    @Override
    protected void doing() throws Exception {
        getResponse(request);
    }

    @Override
    protected void after() {
        GCThread.stop();
    }

    @Override
    public boolean status() {
        return false;
    }

    /**
     * 多次执行某个请求，但是不记录日志，记录方法用 loglong
     * <p>此方法只适应与单个请求的重复请求，对于有业务联系的请求暂时不能适配</p>
     *
     * @param request 请求
     * @throws IOException
     */
    void getResponse(HttpRequestBase request) throws IOException {
        CloseableHttpResponse response = ClientManage.httpsClient.execute(request);
        String content = FanLibrary.getContent(response);
        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
            logger.warn("响应状态码：{},响应内容：{}", content, response.getStatusLine());
        response.close();
    }
}
