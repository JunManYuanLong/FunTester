package com.fun.config;

import com.fun.base.exception.FailException;
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

    public static final String CONNECTOR = "_";

    public static final String DEFAULT_STRING = "FunTester";

    public static final int TEST_ERROR_CODE = -2;

    public static final Properties SYSTEM_INFO = getSysInfo();

    private static Properties getSysInfo() {
        return System.getProperties();
    }

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
     * request日志记录目录
     */
    public static final String REQUEST_Path = LONG_Path + "request/";

    /**
     * 标记请求地址
     */
    public static final String MARK_Path = LONG_Path + "mark/";

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
    public static String COMPUTER_USER_NAME = SYSTEM_INFO.getOrDefault("user.name", DEFAULT_STRING).toString();

    public static String JAVA_VERSION = SYSTEM_INFO.get("java.version").toString();

    public static String SYS_ENCODING = SYSTEM_INFO.get("file.encoding").toString();

    public static String SYS_VERSION = SYSTEM_INFO.get("os.version").toString();

    public static String SYS_NAME = SYSTEM_INFO.get("os.name").toString();


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
            return EMPTY;
        }
    }

    /**
     * 直接获取long目录下的文件
     *
     * @param fileName
     * @return
     */
    public static String getLongFile(String fileName) {
        return LONG_Path + fileName;
    }

    /**
     * 创建日志文件夹和数据存储文件夹
     */
    static {
        new File(LOG_Path).mkdir();
        new File(LONG_Path).mkdir();
        File file = new File(REQUEST_Path);
        File mark = new File(MARK_Path);
        file.mkdir();
        mark.mkdir();
        int length = file.listFiles().length;
        int markNum = mark.listFiles().length;
        if (length > 200) FailException.fail("request日志记录量过多!");
        if (markNum > 200) FailException.fail("mark日志记录量过多!");
        logger.info("当前用户：{}，IP：{}，工作目录：{},系统编码格式:{},系统{}版本:{}", COMPUTER_USER_NAME, LOCAL_IP, WORK_SPACE, SYS_ENCODING, SYS_NAME, SYS_VERSION);
    }

}
