package com.fun.config;

import org.apache.http.Consts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * 常量类
 */
public class Constant {

    private static Logger logger = LoggerFactory.getLogger(Constant.class);

    /*常用的常量*/
    public static final String LINE = "\r\n";

    public static final String TAB = "\t";

    public static final String EMPTY = "";

    public static final String OR = "/";

    public static final String PART = "|";

    public static final String SPACE_1 = " ";

    public static final int TEST_ERROR_CODE = -2;

    public static final String FILE_TYPE_LOG = ".log";

    /**
     * UTF-8字符编码格式
     */
    public static final Charset UTF_8 = Consts.UTF_8;

    /**
     * gb2312编码格式
     */
    public static final Charset GB2312 = Charset.forName("gb2312");

    /**
     * Unicode编码格式
     */
    public static final Charset UNICODE = Charset.forName("Unicode");

    /**
     * utf-16字符集
     */
    public static final Charset UTF_16 = Charset.forName("UTF-16");

    /**
     * ISO-8859-1编码格式
     */
    public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");

    /**
     * GBK编码格式
     */
    public static final Charset GBK = Charset.forName("GBK");

    /**
     * 默认字符集
     */
    public static Charset DEFAULT_CHARSET = UTF_8;

    /**
     * 当前工作目录
     */
    public static final String WORK_SPACE = new File(EMPTY).getAbsolutePath() + "/";

    /**
     * 测试数据存储目录
     */
    public static final String LONG_Path = WORK_SPACE + "long/";

    /**
     * 日志存存储目录
     */
    public static final String LOG_Path = WORK_SPACE + "log/";

    /**
     * 毫秒数
     */
    public static final long DAY = 86400000;

    /**
     * 反射方法执行用例时间间隔
     */
    public static final int EXCUTE_GAP_TIME = 10;

    /**
     * 本机ip，程序初始化会赋值
     */
    public static String LOCAL_IP = getLocalIp();

    /**
     * 本机用户名，程序初始化会赋值
     */
    public static String COMPUTER_USER_NAME = getComputerName();

    /**
     * 创建日志文件夹和数据存储文件夹
     */
    static {
        new File(Constant.LOG_Path).mkdir();
        new File(Constant.LONG_Path).mkdir();
        logger.info("当前用户：{}，IP：{}，工作目录：{}", Constant.COMPUTER_USER_NAME, Constant.LOCAL_IP, Constant.WORK_SPACE);
    }


    /**
     * 获取本机IP
     *
     * @return
     */
    public static String getLocalIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            logger.warn("获取本机IP失败！", e);
            return Constant.EMPTY;
        }
    }

    /**
     * 获取本机用户username
     *
     * @return
     */
    public static String getComputerName() {
        Properties properties = System.getProperties();
        Object name = properties.get("user.name");
        return name.toString();
    }

}
