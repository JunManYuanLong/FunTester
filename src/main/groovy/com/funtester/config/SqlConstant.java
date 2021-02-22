package com.funtester.config;


/**
 *
 */
public class SqlConstant {

    static PropertyUtils.Property propertyUtils = PropertyUtils.getProperties("mysql");

    static String getProperty(String name) {
        return propertyUtils.getProperty(name);
    }

    /**
     * 驱动名称
     */
    public static final String DRIVE = "com.mysql.cj.jdbc.Driver";

    /**
     * 数据库默认连接设置
     */
    public static final String SQLARGS = "?useUnicode=true&characterEncoding=utf-8&useOldAliasMetadataBehavior=true";

    /**
     * 测试数据库
     */
    public static final String TEST_SQL_URL = getProperty("test_mysql_url") + SQLARGS;

    public static final String TEST_USER = getProperty("user");

    public static final String TEST_PASS_WORD = getProperty("password");

    /**
     * 数据库账号
     */
    public static final String FUN_SQL_URL = "jdbc:mysql://ip/database" + SQLARGS;

    /**
     * 数据库存储服务接口地址
     */
    public static final String MYSQL_SERVER_PATH = getProperty("mysql_server_path");

    /**
     * 数据库连接重连间隔
     */
    public static final int MYSQL_RECONNECTION_GAP = 250;

    /**
     * 数据库存储任务每个线程最大等待数量
     */
    public static final int MYSQL_WORK_PER_THREAD = 30;

    /**
     * 最大等待数量，超过上限不再创建新的线程
     */
    public static final int MYSQL_MAX_WAIT_WORK = MYSQL_WORK_PER_THREAD * 50;

    /**
     * 获取数据库存储任务的超时时间，单位毫秒
     */
    public static final int MYSQLWORK_TIMEOUT = 200;

    /**
     * 默认request表名
     */
    public static String REQUEST_TABLE;

    /**
     * 默认result表名
     */
    public static String RESULT_TABLE;

    /**
     * 默认class表名
     */
    public static String CLASS_TABLE;

    /**
     * 默认性能测试表名
     */
    public static String PERFORMANCE_TABLE;

    /**
     * 默认的alertover表格
     */
    public static String ALERTOVER_TABLE;

    /**
     * 是否保存所有的请求到数据库
     */
    public static boolean flag = propertyUtils.getPropertyBoolean("flag");
}
