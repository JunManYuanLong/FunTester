package com.funtester.db.mysql;

/**
 * 辅助线程，处理sql任务
 * <p>不再使用该方式存储数据库数据</p>
 */
@Deprecated
public class MySqlObject {
//    /**
//     * 标记多少辅助线程存活数量
//     */
//    public static AtomicInteger threadNum = new AtomicInteger(0);
//    /**
//     * 标记连接使用
//     */
//    int updateTime;
//    Connection connection;
//    Statement statement;
//
//    /**
//     * 初始化连接方法
//     */
//    public MySqlObject() {
//        getConnection();
//    }
//
//    /**
//     * 获取当前辅助线程数
//     *
//     * @return
//     */
//    public static int getThreadNum() {
//        return threadNum.get();
//    }
//
//    /**
//     * 更新连接使用标记
//     */
//    void updateLastUpdate() {
//        updateTime = SourceCode.getMark();
//    }
//
//    /**
//     * 执行sql方法
//     *
//     * @param sql
//     */
//    void executeUpdateSql(String sql) {
//        getConnection();
//        SqlBase.executeUpdateSql(connection, statement, sql);
//        updateLastUpdate();
//    }
//
//    /**
//     * 获取数据库连接
//     */
//    void getConnection() {
//        try {
//            if (SourceCode.getMark() - updateTime > SqlConstant.MYSQL_RECONNECTION_GAP || connection == null || connection.isClosed()) {
//                connection = TestConnectionManage.getConnection(SqlConstant.TEST_SQL_URL, SqlConstant.TEST_USER, SqlConstant.TEST_PASS_WORD);
//                statement = TestConnectionManage.getStatement(connection);
//            }
//        } catch (SQLException e) {
//            Output.output("数据库连接获取失败！", e);
//        } finally {
//            updateLastUpdate();
//        }
//    }
//
//    /**
//     * 关闭对象方法
//     */
//    void close() {
//        SqlBase.mySqlOver(connection, statement);
//    }
}