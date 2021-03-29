package com.funtester.utils;

import com.funtester.config.Constant;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.Charset;

/**
 * 执行命令的类
 */
public class CMD extends Constant {

    private static Logger logger = LogManager.getLogger(CMD.class);

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
    public static int execCmd(String cmd, Charset charset) {
        return execCmd(cmd, charset, false, EMPTY);
    }

    public static int execCmd(String cmd, boolean filter, String mark) {
        return execCmd(cmd, DEFAULT_CHARSET, false, EMPTY);
    }

    public static int execCmd(String cmd, Charset charset, boolean filter, String mark) {
        logger.info("执行命令：{}", cmd);
        Process p = null;// 通过runtime类执行cmd命令
        try {
            p = Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            logger.error("cmd：{}命令错误", e);
            return 1;
        }
        try (InputStream input = p.getInputStream();
             InputStreamReader inputStreamReader = new InputStreamReader(input, charset);
             BufferedReader reader = new BufferedReader(inputStreamReader);
             InputStream errorInput = p.getErrorStream();
             InputStreamReader streamReader = new InputStreamReader(errorInput, charset.name());
             BufferedReader errorReader = new BufferedReader(streamReader)) {
            String line = EMPTY;
            while ((line = reader.readLine()) != null) {// 循环读取
                if (!filter || (line.contains(mark))) logger.info(line);
            }
            String eline = EMPTY;
            while ((eline = errorReader.readLine()) != null) {// 循环读取
                logger.info(eline);// 输出
            }
            return 0;
        } catch (IOException e) {
            logger.warn("执行命令:{}失败！", cmd, e);
            p.destroy();
            return 1;
        }
    }

    /**
     * 获取文本信息的最后几行，用户查看日志
     *
     * @param path
     * @param num
     * @return
     */
    public static String catFile(String path, int num) {
        logger.info("查询的文件：{}", path);
        if (StringUtils.isEmpty(path)) return EMPTY;
        File file = new File(path);
        if (!file.exists() || file.isDirectory()) return EMPTY;
        StringBuffer stringBuffer = new StringBuffer();
        String command = "tail -n " + num + SPACE_1 + path;
        logger.debug("执行命令：{}", command);
        try (InputStream input = Runtime.getRuntime().exec(command).getInputStream();
             InputStreamReader inputStreamReader = new InputStreamReader(input, DEFAULT_CHARSET);
             BufferedReader reader = new BufferedReader(inputStreamReader);) {
            String line = EMPTY;
            while ((line = reader.readLine()) != null) {// 循环读取
                stringBuffer.append(line + LINE);
            }
        } catch (IOException e) {
            logger.error("获取：{}文件信息失败！", path, e);
        } finally {
            return stringBuffer.toString();
        }
    }

}
