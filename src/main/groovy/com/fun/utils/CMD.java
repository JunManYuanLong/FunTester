package com.fun.utils;

import com.fun.frame.SourceCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * 执行命令的类
 */
public class CMD extends SourceCode {

    public static Logger logger = LoggerFactory.getLogger(CMD.class);

    /**
     * 执行cmd命令，控制台信息编码方式
     *
     * @param cmd 需要执行的命令
     */
    public static int execCmd(String cmd) {
        return execCmd(cmd, DEFAULT_CHARSET);
    }

    /**
     * 执行cmd命令，注意Mac 系统添加环境路径
     *
     * @param cmd 需要执行的命令
     */
    public static int execCmd(String cmd,Charset charset) {
        logger.info("执行命令：", cmd);
        Process p = null;// 通过runtime类执行cmd命令
        try {
            p = Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            logger.error("cmd：{}命令错误",e);
            return 1;
        }
        try (InputStream input = p.getInputStream(); BufferedReader reader = new BufferedReader(new InputStreamReader(input,charset)); InputStream errorInput = p.getErrorStream(); BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorInput))) {
            String line = "";
            while ((line = reader.readLine()) != null) {// 循环读取
                output(line);// 输出
            }
            String eline = "";
            while ((eline = errorReader.readLine()) != null) {// 循环读取
                output(eline);// 输出
            }
            return 0;
        } catch (IOException e) {
            logger.warn("执行" + cmd + "失败！", e);
            return 1;
        }
    }
}
