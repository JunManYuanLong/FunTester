package com.funtester.config;

import com.funtester.frame.Output;
import org.apache.http.Consts;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * 常量类
 */
public class Constant {

    private static Logger logger = LogManager.getLogger(Constant.class);

    /*常用的常量*/
    public static final String LINE = "\r\n";

    public static final String TAB = "\t";

    public static final String EMPTY = "";

    public static final String COMMA = ",";

    public static final String UNKNOW = "?";

    public static final String OR = "/";

    public static final String PART = "|";

    public static final String EQUAL = "=";

    /**
     * 正则表达式中用到的{@link Constant#PART}
     */
    public static final String REG_PART = "\\|";

    public static final String SPACE_1 = " ";

    public static final String CONNECTOR = "_";

    private static final String[] PERCENT = {SPACE_1, "▁", "▂", "▃", "▄", "▅", "▅", "▇", "█"};

    /**
     * 此处前七处等高,第八个元素不等高,不能正常使用
     */
    private static final String[] PARTS = {SPACE_1, "▏", "▎", "▍", "▌", "▋", "▊", "▉", "█"};

    /**
     * 统计性能数据的分桶数
     */
    public static final int BUCKET_SIZE = 32;

    /**
     * 默认线程池的大小
     */
    public static int POOL_SIZE = 8;

    /**
     * 动态模型中线程增长步长
     */
    public static int THREAD_STEP = 1;

    /**
     * 动态模型中,QPS增长步长
     */
    public static int QPS_STEP = 1;

    /**
     * 性能测试中统计输出间隔
     */
    public static int COUNT_INTERVAL = 5;

    /**
     * 幸运数字
     */
    public static int LUCKY_NUM = 8;

    /**
     * QPS模型中每个任务QPS
     */
    public static int TIMES_PER_TASK = 200;

    /**
     * Event模型中默认消费者数量
     */
    @Deprecated
    public static int CONSUMER_SIZE = 1024;

    /**
     * ringbuffer大小
     */
    public static int RINGBUFFER_SIZE = 256 * 256;

    /**
     * 线程池最大等待队列长度
     */
    public static final int MAX_WAIT_TASK = 10_0000;

    /**
     * 统计数据中的数量限制,小于该限制无法绘图
     */
    public static final int DRAW_LIMIT = BUCKET_SIZE * BUCKET_SIZE;

    /**
     * 读写配置文件过滤的文本
     */
    public static final String FILTER = "#";

    public static final String DEFAULT_STRING = "FunTester";

    /**
     * 默认控制台输入标记
     */
    public static String INTPUT_KEY = "F";

    public static final String RESPONSE_CONTENT = "content";

    public static final String EXCEPTION = "exception";

    public static final int TEST_ERROR_CODE = -2;

    public static final long DEFAULT_LONG = 0L;

    public static final Properties SYSTEM_INFO = getSysInfo();

    private static Properties getSysInfo() {
        return System.getProperties();
    }

    /**
     * 校验IP+port的正确性
     */
    public static final String HOST_REGEX = "((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))):([0-9]|[1-9]\\d{1,3}|[1-5]\\d{4}|6[0-4]\\d{4}|65[0-4]\\d{2}|655[0-2]\\d|6553[0-5])";

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
     * 压测数据存放地址
     */
    public static final String DATA_Path = LONG_Path + "data/";

    /**
     * 毫秒数
     */
    public static final long DAY = 86400000;

    /**
     * 反射方法执行用例时间间隔
     */
    public static final int EXECUTE_GAP_TIME = 10;

    /**
     * 性能测试启动时间
     */
    public static double RUNUP_TIME = 30.0;

    /**
     * 单个线程执行的最大QPS任务速率
     */
    public static int QPS_PER_THREAD = 250;

    /**
     * 固定QPS启动之前运行的次数控制时间,总次数会等于QPS*该时间
     */
    public static int PREFIX_RUN = 10;

    /**
     * 通用循环间隔时间,单位s,使用double防止Groovy脚本出错
     */
    public static double LOOP_INTERVAL = 3.0;

    /**
     * 单次步长增长间隔
     */
    public static double STEP_INTERVAL = 0.2;

    /**
     * 线程最大空闲时间
     */
    public static int ALIVE_TIME = LUCKY_NUM;

    /**
     * 线程池核心线程数
     */
    public static int THREADPOOL_CORE = 200;

    /**
     * 线程池最大线程数
     */
    public static int THREADPOOL_MAX = 3600;

    /**
     * 关闭线程池最大等待时间
     */
    public static int WAIT_TERMINATION_TIMEOUT = 10;

    /**
     * 本机用户名，程序初始化会赋值
     */
    public static final String COMPUTER_USER_NAME = SYSTEM_INFO.getOrDefault("user.name", DEFAULT_STRING).toString();

    public static final String JAVA_VERSION = SYSTEM_INFO.get("java.version").toString();

    public static final String SYS_ENCODING = SYSTEM_INFO.get("file.encoding").toString();

    public static final String SYS_VERSION = SYSTEM_INFO.get("os.version").toString();

    public static final String SYS_NAME = SYSTEM_INFO.get("os.name").toString();

    /**
     * 直接获取long目录下的文件
     *
     * @param fileName
     * @return
     */
    public static String getLongFile(String fileName) {
        return LONG_Path + fileName;
    }

    public static String getPercent(int i) {
        return PERCENT[i % 9];
    }

    public static String getPart(int i) {
        return PARTS[i % 9];
    }

    private static String FunTester = "\n" +
            "  ###### #     #  #    # ####### ######  #####  ####### ######  #####\n" +
            "  #      #     #  ##   #    #    #       #         #    #       #    #\n" +
            "  ####   #     #  # #  #    #    ####    #####     #    ####    #####\n" +
            "  #      #     #  #  # #    #    #            #    #    #       #   #\n" +
            "  #       #####   #    #    #    ######  #####     #    ######  #    #\n";

    /**
     * 创建日志文件夹和数据存储文件夹
     */
    static {
//        new File(LOG_Path).mkdir();
        new File(LONG_Path).mkdir();
//        File file = new File(REQUEST_Path);
//        File mark = new File(MARK_Path);
//        File data = new File(DATA_Path);
//        file.mkdir();
//        mark.mkdir();
//        data.mkdir();
//        List<String> allFile = FileUtil.getAllFile(DATA_Path);
//        allFile.addAll(FileUtil.getAllFile(MARK_Path));
//        allFile.addAll(FileUtil.getAllFile(REQUEST_Path));
//        allFile.stream().map(y -> new File(y)).forEach(x -> {
//            if (Time.getTimeStamp() - x.lastModified() > 3 * DAY) x.delete();
//        });
        logger.info("当前用户：{}，工作目录：{},系统编码格式:{},系统{}版本:{}", COMPUTER_USER_NAME, WORK_SPACE, SYS_ENCODING, SYS_NAME, SYS_VERSION);
        logger.info(Output.rgb(FunTester));
    }

}
