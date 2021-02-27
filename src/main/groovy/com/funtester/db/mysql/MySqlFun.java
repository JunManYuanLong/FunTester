package com.funtester.db.mysql;

/**
 * mysql操作的基础类
 * <p>用于存储数据，多用于爬虫</p>
 */
@Deprecated
public class MySqlFun extends SqlBase {
//public class MySqlFun extends SqlBase implements IMySqlBasic {

//    String url;
//
//    String database;
//
//    String user;
//
//    String password;
//
//    Connection connection;
//
//    Statement statement;
//
//    /**
//     * 私有构造方法
//     */
//    public MySqlFun(String url, String database, String user, String password) {
//        this.url = url;
//        this.database = database;
//        this.user = user;
//        this.password = password;
//        getConnection(database);
//    }
//
//    /**
//     * 初始化连接
//     */
//    @Override
//    public void getConnection() {
//        getConnection(EMPTY);
//    }
//
//    /**
//     * 执行sql语句，非query语句，并不关闭连接
//     *
//     * @param sql
//     */
//    @Override
//    public void executeUpdateSql(String sql) {
//        executeUpdateSql(EMPTY, sql);
//    }
//
//    /**
//     * 执行sql语句，非query语句，并不关闭连接
//     *
//     * @param database
//     * @param sql
//     */
//    @Override
//    public void executeUpdateSql(String database, String sql) {
//        getConnection(database);
//        SqlBase.executeUpdateSql(connection, statement, sql);
//    }
//
//    /**
//     * 查询功能
//     *
//     * @param sql
//     * @return
//     */
//    @Override
//    public ResultSet executeQuerySql(String sql) {
//        return SqlBase.executeQuerySql(connection, statement, sql);
//    }
//
//    @Override
//    public ResultSet executeQuerySql(String database, String sql) {
//        getConnection(database);
//        return executeQuerySql(sql);
//    }
//
//    /**
//     * 关闭query连接
//     */
//    @Override
//    public void mySqlOver() {
//        SqlBase.mySqlOver(connection, statement);
//    }
//
//    @Override
//    public void getConnection(String database) {
//        connection = SqlBase.getConnection(SqlConstant.FUN_SQL_URL.replace("ip", url).replace("database", database), user, password);
//        statement = SqlBase.getStatement(connection);
//    }

}
