package com.fun.config;

import com.fun.frame.SourceCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

/**
 * 存放一些系统初始化的方法，可被外部调用
 */
public class SysInit extends SourceCode {

    private static Logger logger = LoggerFactory.getLogger(SysInit.class);

    /**
     * 创建日志文件夹和数据存储文件夹
     */
    static {
        new File(Constant.LOG_Path).mkdir();
        new File(Constant.LONG_Path).mkdir();
        logger.info("当前用户：{}，IP：{}，工作目录：{}", COMPUTER_USER_NAME, LOCAL_IP, WORK_SPACE);
    }

    /**
     * 获取本机IP
     *
     * @return
     */
    public static String getLocalIp() {
        InetAddress inetAddress = null;
        try {
            inetAddress = inetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            logger.warn("获取本机IP失败！", e);
        }
        String ip = inetAddress.getHostAddress();
        return ip;
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

    /**
     * 是否是黑名单的host
     * <p>先检验fv1314和本地local还有10.10.的地址，然后校验配置文件中的host name</p>
     *
     * @param name
     * @return
     */
    public static boolean isBlack(String name) {
        return name.contains("fv1314") || name.contains("10.10") || name.contains("local") || HttpClientConstant.BLACK_HOSTS.contains(name);
    }
}
