package com.funtester.config;

import com.funtester.frame.SourceCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 存放一些系统初始化的方法，可被外部调用
 */
public class SysInit extends SourceCode{

    private static Logger logger = LogManager.getLogger(SysInit.class);

    /**
     * 是否是黑名单的host
     * <p>先检验fv1314和本地local还有10.10.的地址，然后校验配置文件中的host name</p>
     *
     * @param name
     * @return
     */
    public static boolean isBlack(String name) {
        return name.contains("10.10") || name.contains("local") || HttpClientConstant.BLACK_HOSTS.contains(name);
    }
}
