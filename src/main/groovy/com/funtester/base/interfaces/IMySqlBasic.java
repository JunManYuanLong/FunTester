package com.funtester.base.interfaces;

import java.sql.ResultSet;

/**
 * 项目数据库执行类接口
 */
public interface IMySqlBasic {
    /**
     * 执行查询sql
     *
     * @param sql
     * @return
     */
    ResultSet executeQuerySql(String sql);

    /**
     * 执行修改sql
     *
     * @param sql
     */
    void executeUpdateSql(String sql);

    /**
     * 关闭数据库连接
     */
    void over();

    /**
     * 初始化数据库连接
     *
     * @param database
     */
    void getConnection(String database);

    /**
     * 初始化数据库连接
     */
    void getConnection();
}