package com.fun.base.interfaces;

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
    ResultSet excuteQuerySql(String sql);

    /**
     * 执行查询sql
     *
     * @param database
     * @param sql
     * @return
     */
    ResultSet excuteQuerySql(String database, String sql);

    /**
     * 执行修改sql
     *
     * @param sql
     */
    void excuteUpdateSql(String sql);

    /**
     * 执行查询sql
     *
     * @param database
     * @param sql
     */
    void excuteUpdateSql(String database, String sql);

    /**
     * 关闭数据库连接
     */
    void mySqlOver();

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