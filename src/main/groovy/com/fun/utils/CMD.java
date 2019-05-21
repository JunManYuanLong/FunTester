package com.fun.utils;

import com.fun.frame.SourceCode;
import com.fun.frame.Output;
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
     * 执行cmd命令，控制台信息编码方式GBK
     *
     * @param cmd 需要执行的命令
     */
    public static int execCmdByGBK(String cmd) {
        logger.info("执行命令：",cmd);
        try {
            Process p = Runtime.getRuntime().exec("cmd /c " + cmd);
            InputStreamReader inputStreamReader = new InputStreamReader(p.getInputStream(), Charset.forName("GBK"));
            BufferedReader br = new BufferedReader(inputStreamReader);
            String line = null;
            while ((line = br.readLine()) != null) {
                Output.output(line);
            }
            br.close();// 此处有依赖，先关闭br
            inputStreamReader.close();
            return 0;
        } catch (IOException e) {
            logger.warn("执行" + cmd + "失败！", e);
            return 1;
        }
    }

    /**
     * 执行cmd命令，注意Mac 系统添加环境路径
     *
     * @param cmd 需要执行的命令
     */
    public static int execCmd(String cmd) {
        logger.info("执行命令：",cmd);
        try {
            Process p = Runtime.getRuntime().exec(cmd);// 通过runtime类执行cmd命令
            // 正确输出流
            InputStream input = p.getInputStream();// 创建并实例化输入字节流
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));// 先通过inputstreamreader进行流转化，在实例化bufferedreader，接收内容
            String line = "";
            while ((line = reader.readLine()) != null) {// 循环读取
                Output.output(line);// 输出
            }
            reader.close();// 此处reader依赖于input，应先关闭
            input.close();
            // 错误输出流
            InputStream errorInput = p.getErrorStream();// 创建并实例化输入字节流
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorInput));// 先通过inputstreamreader进行流转化，在实例化bufferedreader，接收内容
            String eline = "";
            while ((eline = errorReader.readLine()) != null) {// 循环读取
                Output.output(eline);// 输出
            }
            errorReader.close();// 此处有依赖关系，先关闭errorReader
            errorInput.close();
            return 0;
        } catch (IOException e) {
            logger.warn("执行" + cmd + "失败！", e);
            return 1;
        }
    }
}
