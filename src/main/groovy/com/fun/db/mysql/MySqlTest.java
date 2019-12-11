package com.fun.db.mysql;

import com.fun.base.bean.PerformanceResultBean;
import com.fun.base.bean.RecordBean;
import com.fun.base.bean.RequestInfo;
import com.fun.config.SqlConstant;
import com.fun.config.SysInit;
import com.fun.frame.httpclient.FanLibrary;
import com.fun.utils.DecodeEncode;
import com.fun.utils.Time;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据库读写类
 * <p>
 * 用来存储接口请求信息的mysql数据库类
 * 打印请求信息的方法写在这里面，数据库服务的队列也在这里（可不用），暂时才用直接抛出sql语句完成记录功能
 * </p>
 */
public abstract class MySqlTest {

    private static Logger logger = LoggerFactory.getLogger(MySqlTest.class);

    /**
     * 新方法，报错requestinfo对象
     *
     * @param requestInfo  请求信息
     * @param data_size
     * @param expend_time
     * @param status
     * @param mark
     * @param code
     * @param localIP
     * @param computerName
     */
    public static void saveApiTestDate(RequestInfo requestInfo, int data_size, long expend_time, int status, int mark, int code, String localIP, String computerName) {
        logger.debug("请求信息：{}", requestInfo.toString());
        if (SysInit.isBlack(requestInfo.getHost())) return;
        logger.info("请求uri：{},耗时：{} ms", requestInfo.getUri(), expend_time);
        String sql = String.format("INSERT INTO " + SqlConstant.REQUEST_TABLE + " (domain,api,data_size,expend_time,status,type,method,code,local_ip,local_name,create_time) VALUES ('%s','%s',%d,%d,%d,'%s','%s',%d,'%s','%s','%s');", requestInfo.getHost(), requestInfo.getApiName(), data_size, expend_time, status, requestInfo.getType(), requestInfo.getMethod().getName(), code, localIP, computerName, Time.getDate());
//        RecordBean requestBean = new RecordBean();
//        requestBean.setApi(requestInfo.getApiName());
//        requestBean.setDomain(requestInfo.getHost());
//        requestBean.setType(requestInfo.getType());
//        requestBean.setExpend_time(expend_time);
//        requestBean.setData_size(data_size);
//        requestBean.setStatus(status);
//        requestBean.setMethod(requestInfo.getMethod().getName());
//        requestBean.setCode(code);
//        requestBean.setLocal_ip(localIP);
//        requestBean.setLocal_name(computerName);
//        requestBean.setCreate_time(Time.getDate());
//        RecordBean.get().setApi(requestInfo.getApiName()).setDomain(requestInfo.getHost()).setType(requestInfo.getType()).setExpend_time(expend_time).setData_size(data_size).setStatus(status).setMethod(requestInfo.getMethod().getName()).setCode(code).setLocal_ip(localIP).setLocal_name(computerName).setCreate_time(Time.getDate());
        sendWork(sql);
    }

    /**
     * 保存性能测试结果的方法
     *
     * @param bean
     */
    public static void savePerformanceBean(PerformanceResultBean bean) {
        if (StringUtils.isNoneEmpty(SqlConstant.PERFORMANCE_TABLE)) return;
        String sql = String.format("INSERT INTO " + SqlConstant.PERFORMANCE_TABLE + "(threads,total,rt,qps,des,start_time,end_time) VALUES (%d,%d,%d,%f,'%s','%s','%s');", bean.getThreads(), bean.getTotal(), bean.getRt(), bean.getQps(), bean.getDesc(), bean.getStartTime(), bean.getEndTime());
        logger.info("记录性能测试数据：{}", bean.toJson());
        sendWork(sql);
    }

    /**
     * 记录alertover警告
     *
     * @param requestInfo
     * @param type
     * @param title
     * @param localIP
     * @param computerName
     */
    public static void saveAlertOverMessage(RequestInfo requestInfo, String type, String title, String localIP, String computerName) {
        String host_name = requestInfo.getHost();
        if (SysInit.isBlack(host_name) || SqlConstant.ALERTOVER_TABLE == null) return;
        String sql = String.format("INSERT INTO alertover (type,title,host_name,api_name,local_ip,computer_name,create_time) VALUES('%s','%s','%s','%s','%s','%s','%s');", type, title, host_name, requestInfo.getApiName(), localIP, computerName, Time.getDate());
        sendWork(sql);
    }

    /**
     * 发送数据库任务，暂时用请求服务器接口
     *
     * @param sql
     * @return
     */
    public static void sendWork(String sql) {
        logger.debug("记录SQL：{}", sql);
        if (!SqlConstant.flag) return;
        FanLibrary.noHeader();
        JSONObject argss = new JSONObject();
        argss.put("sql", DecodeEncode.urlEncoderText(sql));
        FanLibrary.getHttpResponse(FanLibrary.getHttpPost(SqlConstant.MYSQL_SERVER_PATH, argss));
    }

    /**
     * 添加请求记录
     *
     * @param requestBean
     */
    public static void sendWork(RecordBean requestBean) {
        FanLibrary.noHeader();
        if (SqlConstant.flag)
            FanLibrary.getHttpResponse(FanLibrary.getHttpPost(SqlConstant.MYSQL_SERVER_PATH, requestBean.toJson()));
    }


}
