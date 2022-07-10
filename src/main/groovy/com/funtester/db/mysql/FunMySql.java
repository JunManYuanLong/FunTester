package com.funtester.db.mysql;

import com.funtester.base.interfaces.IMySqlBasic;
import com.funtester.config.SqlConstant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * mysql操作的基础类
 * <p>用于存储数据，多用于爬虫</p>
 */
public class FunMySql extends SqlBase implements IMySqlBasic {

    private static final Logger logger = LogManager.getLogger(FunMySql.class);

    /**
     * {@link SqlConstant#FUN_SQL_URL}会替换IP到URL
     */
    String url;

    /**
     * 库
     */
    String database;

    /**
     * 用户
     */
    String user;

    /**
     * 密码
     */
    String password;

    Connection connection;

    Statement statement;

    /**
     * 私有构造方法
     *
     * @param url      连接地址,包括端口
     * @param database 库
     * @param user     用户名
     * @param password 密码
     */
    public FunMySql(String url, String database, String user, String password) {
        this.url = url;
        this.database = database;
        this.user = user;
        this.password = password;
        getConnection(database);
    }

    /**
     * 初始化连接
     */
    @Override
    public void getConnection() {
        getConnection(EMPTY);
    }

    /**
     * 执行sql语句，非query语句，并不关闭连接
     *
     * @param sql
     */
    @Override
    public void executeUpdateSql(String sql) {
        SqlBase.executeUpdateSql(connection, statement, sql);
    }

    /**
     * 查询功能
     *
     * @param sql
     * @return
     */
    @Override
    public ResultSet executeQuerySql(String sql) {
        return SqlBase.executeQuerySql(connection, statement, sql);
    }

    /**
     * 关闭query连接
     */
    @Override
    public void over() {
        SqlBase.close(connection, statement);
    }

    @Override
    public void getConnection(String database) {
        if (connection == null)
            connection = SqlBase.getConnection(SqlConstant.FUN_SQL_URL.replace("ip", url).replace("database", database), user, password);
        if (statement == null) statement = SqlBase.getStatement(connection);
        logger.info("连接成功,url:{}!", url);
    }

}
