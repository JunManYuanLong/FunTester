package com.funtester.db.mysql;

import com.funtester.frame.SourceCode;

/**
 * 数据库基础类，主要公共的获取连接和操作对象
 */
public class SqlBase extends SourceCode {

//    private static Logger logger = LogManager.getLogger(SqlBase.class);
//
//    /**
//     * 获取数据库连接
//     *
//     * @param url      地址，包括端口
//     * @param user     用户名
//     * @param passowrd 密码
//     * @return
//     */
//    public static Connection getConnection(String url, String user, String passowrd) {
//        logger.debug("连接数据库url：{}，user：{}，password：{}", url, user, passowrd);
//        try {
//            Class.forName(SqlConstant.DRIVE);
//        } catch (ClassNotFoundException e) {
//            logger.warn("加载驱动程序失败！", e);
//        }
//        try {
//            return DriverManager.getConnection(url, user, passowrd);
//        } catch (SQLException e) {
//            logger.warn("数据库连接失败！", e);
//        }
//        return null;
//    }
//
//    /**
//     * 获取statement对象
//     *
//     * @param connection
//     * @return
//     */
//    public static Statement getStatement(Connection connection) {
//        try {
//            return connection.createStatement();
//        } catch (SQLException e) {
//            logger.warn("获取数据库连接失败！", e);
//        } catch (ExceptionInInitializerError e) {
//            logger.warn("初始化失败!", e);
//        }
//        return null;
//    }
//
//    /**
//     * 执行sql语句,查询语句，返回ResultSet，并不关闭连接
//     *
//     * @param connection
//     * @param statement
//     * @param sql
//     * @return
//     */
//    public static ResultSet executeQuerySql(Connection connection, Statement statement, String sql) {
//        logger.debug("执行的SQL：{}", sql);
//        try {
//            if (connection != null && !connection.isClosed()) {
//                ResultSet resultSet = statement.executeQuery(sql);
//                return resultSet;
//            }
//        } catch (SQLException e) {
//            logger.warn(sql, e);
//        }
//        return null;
//    }
//
//    /**
//     * 执行sql语句，非query语句，不关闭连接
//     *
//     * @param connection
//     * @param statement
//     * @param sql
//     */
//    public static void executeUpdateSql(Connection connection, Statement statement, String sql) {
//        logger.debug("执行的SQL：{}", sql);
//        try {
//            if (!connection.isClosed()) statement.executeUpdate(sql);
//        } catch (SQLException e) {
//            logger.warn(sql, e);
//        }
//    }
//
//    /**
//     * 关闭数据库资源
//     *
//     * @param connection
//     * @param statement
//     */
//    public static void mySqlOver(Connection connection, Statement statement) {
//        try {
//            if (connection == null || connection.isClosed()) return;
//            statement.close();
//            connection.close();
//        } catch (SQLException e) {
//            logger.warn("关闭数据库链接失败！", e);
//        }
//    }


}
