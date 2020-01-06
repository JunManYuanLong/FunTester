package com.fun.frame.thead;

import com.fun.base.constaint.ThreadLimitTimeCount;
import com.fun.base.interfaces.MarkRequest;
import com.fun.config.Constant;
import com.fun.config.HttpClientConstant;
import com.fun.frame.Save;
import com.fun.frame.excute.Concurrent;
import com.fun.frame.httpclient.ClientManage;
import com.fun.frame.httpclient.FanLibrary;
import com.fun.frame.httpclient.FunRequest;
import com.fun.frame.httpclient.GCThread;
import com.fun.utils.Time;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * http请求多线程类
 */
public class RequestThreadTime extends ThreadLimitTimeCount {

    private static final long serialVersionUID = -4210938493934016518L;

    static Logger logger = LoggerFactory.getLogger(RequestThreadTime.class);

    /**
     * 请求
     */
    public HttpRequestBase request;

    public MarkRequest mark;

    public List<String> marks = new ArrayList<>();

    public static Vector<String> requestMark = new Vector<>();


    /**
     * 单请求多线程多次任务构造方法
     *
     * @param request 被执行的请求
     * @param time    每个线程运行的次数
     */
    public RequestThreadTime(HttpRequestBase request, int time) {
        this.request = request;
        this.time = time;
        this.mark = new MarkRequest() {
            @Override
            public String mark(HttpRequestBase base) {
                return EMPTY;
            }
        };
    }

    protected RequestThreadTime() {
        super();
    }

    @Override
    public void before() {
        super.before();
        GCThread.starts();
    }

    @Override
    protected void doing() throws Exception {
        CloseableHttpResponse response = ClientManage.httpsClient.execute(request);
        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            String content = FanLibrary.getContent(response);
            logger.warn("响应状态码：{},响应内容：{}", content, response.getStatusLine());
        }
        response.close();
    }

    @Override
    protected void after() {
        requestMark.addAll(marks);
        GCThread.stop();
        synchronized (RequestThreadTimes.class) {
            if (countDownLatch.getCount() == 0) Save.saveStringList(requestMark, Constant.DEFAULT_STRING);
        }
    }

    @Override
    public void run() {
        try {
            before();
            List<Long> t = new ArrayList<>();
            long ss = Time.getTimeStamp();
            long et = ss;
            while (true) {
                try {
                    String m = this.mark.mark(request);
                    long s = Time.getTimeStamp();
                    doing();
                    long e = Time.getTimeStamp();
                    excuteNum++;
                    long diff = e - s;
                    t.add(diff);
                    if (diff > HttpClientConstant.MAX_ACCEPT_TIME) marks.add(diff + CONNECTOR + m);
                    if ((et - ss) > time || status()) break;
                } catch (Exception e) {
                    logger.warn("执行任务失败！", e);
                    errorNum++;
                }
            }
            long ee = Time.getTimeStamp();
            logger.info("执行次数：{}, 失败次数: {},总耗时: {} s", excuteNum, errorNum, (ee - ss) / 1000 + 1);
            Concurrent.allTimes.addAll(t);
        } catch (Exception e) {
            logger.warn("执行任务失败！", e);
        } finally {
            if (countDownLatch != null)
                countDownLatch.countDown();
            after();
        }

    }

    @Override
    public RequestThreadTime clone() {
        RequestThreadTime threadTime = new RequestThreadTime();
        threadTime.time = this.time;
        threadTime.request = FunRequest.cloneRequest(request);
        threadTime.mark = this.mark;
        return threadTime;
    }


}
