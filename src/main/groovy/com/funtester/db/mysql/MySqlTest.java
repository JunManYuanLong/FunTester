package com.funtester.db.mysql;

import com.alibaba.fastjson.JSONObject;
import com.funtester.base.bean.PerformanceResultBean;
import com.funtester.base.bean.RecordBean;
import com.funtester.base.bean.RequestInfo;
import com.funtester.config.SqlConstant;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.Statement;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 数据库读写类
 * <p>
 * 用来存储接口请求信息的mysql数据库类
 * 打印请求信息的方法写在这里面，数据库服务的队列也在这里（可不用），暂时才用直接抛出sql语句完成记录功能
 * </p>
 */
public class MySqlTest extends SqlBase {

    private static Logger logger = LogManager.getLogger(MySqlTest.class);

    /**
     * 控台statement1和statement均衡
     */
    static AtomicInteger key = new AtomicInteger(0);

    /**
     * 存放数据库存储任务
     */
    static LinkedBlockingQueue<String> sqls = new LinkedBlockingQueue<>();

    public static Connection getConnection0() {
        return connection0;
    }

    public static Statement getStatement0() {
        return statement0;
    }

    /**
     * 用于查询
     */
    static Connection connection0;

    /**
     * 用于写入
     */
    static Connection connection1;

    /**
     * 用于写入
     */
    static Connection connection2;

    static Statement statement0;

    static Statement statement1;

    static Statement statement2;


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
        logger.info("请求uri：{},耗时：{} ms, {}", requestInfo.getUri(), expend_time, requestInfo.mark());
//        if (StringUtils.isEmpty(SqlConstant.REQUEST_TABLE) || SysInit.isBlack(requestInfo.getHost())) return;
//        String sql = String.format("INSERT INTO " + SqlConstant.REQUEST_TABLE + " (domain,api,data_size,expend_time,status,type,method,code,local_ip,local_name,create_time) VALUES ('%s','%s',%d,%d,%d,'%s','%s',%d,'%s','%s','%s');", requestInfo.getHost(), requestInfo.getApiName(), data_size, expend_time, status, requestInfo.getType(), requestInfo.getMethod().getName(), code, localIP, computerName, Time.getDate());
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
//        sendWork(sql);
    }

    /**
     * 保存性能测试结果的方法
     *
     * @param bean
     */
    public static void savePerformanceBean(PerformanceResultBean bean) {
        if (!StringUtils.isNoneEmpty(SqlConstant.PERFORMANCE_TABLE)) return;
        String sql = String.format("INSERT INTO " + SqlConstant.PERFORMANCE_TABLE + "(threads,total,rt,qps,error,fail,des,start_time,end_time) VALUES (%d,%d,%d,%f,%f,%f,'%s','%s','%s');", bean.getThreads(), bean.getTotal(), bean.getRt(), bean.getQps(), bean.getErrorRate(), bean.getFailRate(), bean.getMark(), bean.getStartTime(), bean.getEndTime());
        sendWork(sql);
    }

    /**
     * 保存测试结果
     *
     * @param label  测试标记
     * @param result 测试结果
     */
    public static void saveTestResult(String label, JSONObject result) {
//        if (SqlConstant.RESULT_TABLE == null) return;
//        String data = result.toString();
//        Iterator<String> iterator = result.keySet().iterator();
//        int abc = 1;
//        while (iterator.hasNext() && abc == 1) {
//            String key = iterator.next().toString();
//            String value = result.getString(key);
//            if (value.equals("false")) abc = 2;
//        }
//        if (abc != 1) new AlertOver("用例失败！", label + "测试结果：" + abc + LINE + data).sendBusinessMessage();
//        logger.info(label + LINE + "测试结果：" + (abc == 1 ? "通过" : "失败") + LINE + data);
//        String sql = String.format("INSERT INTO " + SqlConstant.RESULT_TABLE + " (result,label,params,local_ip,computer_name,create_time) VALUES (%d,'%s','%s','%s','%s','%s')", abc, label, data, LOCAL_IP, COMPUTER_USER_NAME, Time.getDate());
//        sendWork(sql);
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
//        String host_name = requestInfo.getHost();
//        if (SysInit.isBlack(host_name) || SqlConstant.ALERTOVER_TABLE == null) return;
//        String sql = String.format("INSERT INTO alertover (type,title,host_name,api_name,local_ip,computer_name,create_time) VALUES('%s','%s','%s','%s','%s','%s','%s');", type, title, host_name, requestInfo.getApiName(), localIP, computerName, Time.getDate());
//        sendWork(sql);
    }

    /**
     * 获取所有有效的用例类
     *
     * @return
     */
//    public static List<String> getAllCaseName() {
//        List<String> list = new ArrayList<>();
//        if (SqlConstant.CLASS_TABLE == null) return list;
//        String sql = "SELECT * FROM " + SqlConstant.CLASS_TABLE + " WHERE flag = 1 ORDER BY create_time DESC;";
//        TestConnectionManage.getQueryConnection();
//        ResultSet resultSet = executeQuerySql(connection0, statement0, sql);
//        try {
//            while (resultSet != null && resultSet.next()) {
//                String className = resultSet.getString("class");
//                list.add(className);
//            }
//        } catch (SQLException e) {
//            logger.warn(sql, e);
//        }
//        return list;
//    }

    /**
     * 获取用例状态
     *
     * @param name
     * @return
     */
//    public static boolean getCaseStatus(String name) {
//        if (SqlConstant.CLASS_TABLE == null) return false;
//        String sql = "SELECT flag FROM " + SqlConstant.CLASS_TABLE + " WHERE class = \"" + name + "\";";
//        TestConnectionManage.getQueryConnection();
//        ResultSet resultSet = executeQuerySql(connection0, statement0, sql);
//        try {
//            if (resultSet != null && resultSet.next()) {
//                int flag = resultSet.getInt(1);
//                return flag == 1 ? true : false;
//            }
//        } catch (SQLException e) {
//            logger.warn(sql, e);
//        }
//        return false;
//    }


    /**
     * 确保所有的储存任务都结束
     */
//    private static void check() {
//        while (sqls.size() != 0) {
//            sleep(100);
//        }
//        TestConnectionManage.stopAllThread();
//    }

    /**
     * 执行sql语句，非query语句，并不关闭连接
     *
     * @param sql
     * @param key
     */
//    static void executeUpdateSql(String sql, boolean key) {
//        int size = getWaitWorkNum();
//        if (size % 3 == 1 && size > MySqlObject.getThreadNum() * (SqlConstant.MYSQL_WORK_PER_THREAD + 1) && size < SqlConstant.MYSQL_MAX_WAIT_WORK)
//            new Thread(new AidThread()).start();
//        if (key) {
//            TestConnectionManage.getUpdateConnection1();
//            executeUpdateSql(connection1, statement1, sql);
//            TestConnectionManage.updateLastUpdate1();
//        } else {
//            TestConnectionManage.getUpdateConnection2();
//            executeUpdateSql(connection2, statement2, sql);
//            TestConnectionManage.updateLastUpdate2();
//        }
//    }

    /**
     * 发送数据库任务，暂时用请求服务器接口
     *
     * @param sql
     * @return
     */
    public static void sendWork(String sql) {
//        if (!SqlConstant.flag) return;
//        logger.debug("记录SQL：{}", sql);
//        FunLibrary.noHeader();
//        JSONObject argss = new JSONObject();
//        argss.put("sql", DecodeEncode.urlEncoderText(sql));
//        FunLibrary.getHttpResponse(FunLibrary.getHttpPost(SqlConstant.MYSQL_SERVER_PATH, argss));
    }

    /**
     * 添加请求记录
     *
     * @param requestBean
     */
    public static void sendWork(RecordBean requestBean) {
//        FunLibrary.noHeader();
//        if (SqlConstant.flag)
//            FunLibrary.getHttpResponse(FunLibrary.getHttpPost(SqlConstant.MYSQL_SERVER_PATH, requestBean.toJson()));
    }

    /**
     * 添加存储任务，数据库存储服务用
     *
     * @param sql
     * @return
     */
    public static boolean addWork(String sql) {
//        try {
//            sqls.put(sql);
//        } catch (InterruptedException e) {
//            logger.warn("添加数据库存储任务失败！", e);
//            return false;
//        }
        return true;
    }

    /**
     * 从任务池里面获取任务
     *
     * @return
     */
    static String getWork() {
//        String sql = null;
//        try {
//            sql = sqls.poll(SqlConstant.MYSQLWORK_TIMEOUT, TimeUnit.MILLISECONDS);
//        } catch (InterruptedException e) {
//            logger.warn("获取存储任务失败！", e);
//        } finally {
//            return sql;
//        }
        return EMPTY;
    }

    /**
     * 获取等待任务数
     *
     * @return
     */
    public static int getWaitWorkNum() {
        return sqls.size();
    }

    /**
     * 提供外部查询功能
     *
     * @param sql
     * @return
     */
//    public static ResultSet executeQuerySql(String sql) {
//        TestConnectionManage.getQueryConnection();
//        return executeQuerySql(connection0, statement0, sql);
//    }


    /**
     * 关闭数据库链接的方法，供外部使用
     */
//    public static void mySqlOver() {
//        mySqlQueryOver();
//    }

    /**
     * 关闭update连接
     */
//    static void mySqlUpdateOver() {
//        check();
//        mySqlOver(connection1, statement1);
//        mySqlOver(connection2, statement2);
//    }

    /**
     * 关闭query连接
     */
//    public static void mySqlQueryOver() {
//        mySqlOver(connection0, statement0);
//    }


}
