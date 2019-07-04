package com.fun.db.mysql;

import com.fun.config.SqlConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

/**
 * 测试数据存储数据库连接管理类
 * <p>放弃使用该方式存储，换成springboot数据库服务</p>
 */
@Deprecated
public class TestConnectionManage extends SqlBase {

    static Logger logger = LoggerFactory.getLogger(TestConnectionManage.class);

    public static ExcuteThread excuteThread1 = new ExcuteThread(true);


    public static ExcuteThread excuteThread2 = new ExcuteThread(false);


    /**
     * 记录query的最后调用时间时间
     */
    private static int lastQuery;

    /**
     * 记录update的最后调用时间
     */
    private static int lastUpdate1;

    /**
     * 记录update的最后调用时间
     */
    private static int lastUpdate2;

    public static void start() {
        getUpdateConnection1();
        getUpdateConnection2();
        excuteThread1.start();
        excuteThread2.start();
    }

    static void getQueryConnection() {
        try {
            if (getMark() - lastQuery > SqlConstant.MYSQL_RECONNECTION_GAP || MySqlTest.connection0 == null || MySqlTest.connection0.isClosed())
                MySqlTestInitQuery();
        } catch (SQLException e) {
            logger.warn("数据库连接获取失败！", e);
        }
    }

    public static void getUpdateConnection1() {
        try {
            if (getMark() - lastUpdate1 > SqlConstant.MYSQL_RECONNECTION_GAP || MySqlTest.connection1 == null || MySqlTest.connection1.isClosed())
                MySqlTestInitUpdate(true);
        } catch (SQLException e) {
            logger.warn("数据库连接获取失败！", e);
        }
    }

    public static void getUpdateConnection2() {
        try {
            if (getMark() - lastUpdate2 > SqlConstant.MYSQL_RECONNECTION_GAP || MySqlTest.connection2 == null || MySqlTest.connection2.isClosed())
                MySqlTestInitUpdate(false);
        } catch (SQLException e) {
            logger.warn("数据库连接获取失败！", e);
        }
    }

    static void updateLastQuery() {
        lastQuery = getMark();
    }

    static void updateLastUpdate1() {
        lastUpdate1 = getMark();
    }

    static void updateLastUpdate2() {
        lastUpdate2 = getMark();
    }

    /**
     * 连接初始化，last自动赋值
     */
    private static void MySqlTestInitQuery() {
        updateLastQuery();
        MySqlTest.mySqlQueryOver();
        MySqlTest.connection0 = getConnection(SqlConstant.TEST_SQL_URL, SqlConstant.TEST_USER, SqlConstant.TEST_PASS_WORD);
        MySqlTest.statement0 = getStatement(MySqlTest.connection0);
    }


    /**
     * 连接初始化，last自动赋值
     */
    private static void MySqlTestInitUpdate(boolean key) {
        if (key) {
            updateLastUpdate1();
            MySqlTest.connection1 = getConnection(SqlConstant.TEST_SQL_URL, SqlConstant.TEST_USER, SqlConstant.TEST_PASS_WORD);
            MySqlTest.statement1 = getStatement(MySqlTest.connection1);
        } else {
            updateLastUpdate2();
            MySqlTest.connection2 = getConnection(SqlConstant.TEST_SQL_URL, SqlConstant.TEST_USER, SqlConstant.TEST_PASS_WORD);
            MySqlTest.statement2 = getStatement(MySqlTest.connection2);
        }
    }


    /**
     * 结束所有sql任务线程
     */
    public static void stopAllThread() {
        ExcuteThread.threadKey = true;
    }

}

/**
 * 多线程类，用于消耗mysqltest里sqls中的数据库任务
 */
@Deprecated
class ExcuteThread extends Thread {

    /**
     * 分配连接
     */
    boolean key;

    /**
     * 结束标志
     */
    static boolean threadKey = false;

    ExcuteThread(boolean key) {
        this.key = key;
    }

    @Override
    public void run() {
        while (true) {
            if (threadKey) break;
            String sql = MySqlTest.getWork();
            if (sql == null) continue;
            TestConnectionManage.logger.info("辅助线程执行SQL：{}", sql);
            MySqlTest.excuteUpdateSql(sql, key);
        }
    }

}