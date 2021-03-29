package com.funtester.utils;

import com.alibaba.fastjson.JSONObject;
import com.funtester.base.exception.FailException;
import com.funtester.base.exception.ParamException;
import com.funtester.config.Constant;
import com.funtester.frame.SourceCode;
import groovy.lang.Tuple2;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件读写类,与{@link FileUtil}有功能上的重合,原因在与Java和Groovy的不兼容问题.
 */
public class RWUtil extends Constant {

    private static Logger logger = LogManager.getLogger(RWUtil.class);

    /**
     * 读取文件信息，返回json数据
     *
     * @param filePath
     * @return
     */
    public static JSONObject readTxtByJson(String filePath) {
        if (StringUtils.isEmpty(filePath) || !new File(filePath).exists() || new File(filePath).isDirectory())
            ParamException.fail("配置文件信息错误!" + filePath);
        logger.debug("读取文件名：{}", filePath);
        List<String> lines = readTxtFileByLine(filePath);
        JSONObject info = new JSONObject();
        lines.forEach(line -> {
            String[] split = line.split("=", 2);
            info.put(split[0], split[1]);
        });
        return info;
    }

    public static JSONObject readTxtByJson(String filePath, String filter) {
        if (StringUtils.isEmpty(filePath) || !new File(filePath).exists() || new File(filePath).isDirectory())
            ParamException.fail("配置文件信息错误!" + filePath);
        logger.debug("读取文件名：{}", filePath);
        List<String> lines = readTxtFileByLine(filePath, filter, false);
        JSONObject info = new JSONObject();
        lines.forEach(line -> {
            String[] split = line.split("=", 2);
            info.put(split[0], split[1]);
        });
        return info;
    }

    /**
     * 通过文件信息，返回string
     *
     * @param filePath
     * @return
     */
    public static String readTextByString(String filePath) {
        if (StringUtils.isEmpty(filePath) || !new File(filePath).exists() || new File(filePath).isDirectory())
            ParamException.fail("配置文件信息错误!" + filePath);
        logger.debug("读取文件名：{}", filePath);
        List<String> list = readTxtFileByLine(filePath);
        StringBuffer all = new StringBuffer();
        list.forEach(line -> all.append(line + Constant.LINE));
        return all.toString();
    }

    /**
     * 分行读取txt文档，默认使用utf-8编码格式
     *
     * @param filePath 文件路径
     * @return 返回list数组
     */
    public static List<String> readTxtFileByLine(String filePath) {
        return readTxtFileByLine(filePath, Constant.EMPTY, true);
    }

    /**
     * 分行读取txt文档，默认使用utf-8编码格式
     * <p>line.contains(content) == key</p>
     *
     * @param filePath 文件路径
     * @param content  过滤文本
     * @param key      是否包含
     * @return 返回list数组
     */
    public static List<String> readTxtFileByLine(String filePath, String content, boolean key) {
        if (StringUtils.isEmpty(filePath) || !new File(filePath).exists() || new File(filePath).isDirectory())
            ParamException.fail("文件信息错误!" + filePath);
        logger.debug("读取文件名：{}", filePath);
        List<String> lines = new ArrayList<>();
        try {
            String encoding = Constant.UTF_8.toString();
            File file = new File(filePath);
            if (file.isFile() && file.exists()) { // 判断文件是否存在
                FileInputStream fileInputStream = new FileInputStream(file);
                InputStreamReader read = new InputStreamReader(fileInputStream, encoding);// 考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    if (line.contains(content) == key)
                        lines.add(line);
                }
                bufferedReader.close();
                read.close();
                fileInputStream.close();
            } else {
                logger.warn("找不到指定的文件：{}", filePath);
            }
        } catch (Exception e) {
            logger.warn("读取文件内容出错", e);
        }
        return lines;
    }

    /**
     * 从配置文件中读取数字信息
     *
     * @param filePath
     * @return
     */
    public static List<Integer> readTxtFileByNumLine(String filePath) {
        return readTxtFileByLine(filePath, Constant.EMPTY, true).stream().map(x -> SourceCode.changeStringToInt(x)).collect(Collectors.toList());
    }

    /**
     * 从配置文件中读取数字信息
     *
     * @param filePath
     * @return
     */
    public static List<Double> readTxtFileByDoubleLine(String filePath) {
        return readTxtFileByLine(filePath, Constant.EMPTY, true).stream().map(x -> SourceCode.changeStringToDouble(x)).collect(Collectors.toList());
    }


    /**
     * 下载文件,目前只要针对图片
     *
     * @param url
     */
    public static void down(String url) {
        Tuple2 tuple2 = FileUtil.handlePicName(url);
        down(tuple2.getFirst().toString(), tuple2.getSecond().toString());
    }

    /**
     * 通过url下载图片
     *
     * @param url
     * @param name
     */
    public static void down(String url, String name) {
        File file = new File(name);
        logger.info("下载链接：{}，存储文件名：{}", url, file.getAbsolutePath());
        if (!file.exists())
            try {
                file.createNewFile();
            } catch (IOException e) {
                logger.warn("创建文件失败！", e);
            }
        try (InputStream is = new URL(url).openStream(); OutputStream os = new FileOutputStream(file)) {
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            logger.warn("下载文件失败！", e);
        }
    }

    /**
     * 复制文件
     *
     * @param oldPath 旧路径
     * @param newPath 新路径
     */
    public static void copyFile(String oldPath, String newPath) {
        logger.debug("源文件名：{}，目标文件名：{}", oldPath, newPath);
        int bytesum = 0;// 这个用来统计需要写入byte数组的长度
        int byteread = 0;// 这个用来接收read()方法的返回值，表示读取内容的长度
        File oldfile = new File(oldPath);// 获取源文件的file对象
        if (oldfile.exists()) {// 文件存在时
            try (InputStream inputStream = new FileInputStream(oldPath); FileOutputStream fileOutputStream = new FileOutputStream(newPath);) {
                byte[] buffer = new byte[1024];// 新建读取文件所用的数组
                // 此处用while循环每次按buffer读取文件直到读取完成
                while ((byteread = inputStream.read(buffer)) != -1) {// 如何读取到文件末尾
                    bytesum += byteread;// 此处计算读取长度，byteread表示每次读取的长度
                    fileOutputStream.write(buffer, 0, byteread);// 此方法第一个参数是byte数组，第二次参数是开始位置，第三个参数是长度
                }
                logger.info("文件：{}，总大小是：", oldfile, SourceCode.formatLong(bytesum));// 输出读取的总长度
                fileOutputStream.flush();// 强制缓存输出，防止数据丢失
            } catch (IOException e) {
                FailException.fail("复制文件出错!" + e.getMessage());
            }
        } else {
            logger.warn("文件不存在！");
        }
        // File oldfile2 = new File(oldPath);
        // oldfile2.delete();
    }

    /**
     * 写入文本信息，会自动新建文件
     *
     * @param file file对象，必须是存在的路径
     * @param text 写入的内容，如果file存在，续写
     */
    public static void writeText(File file, String text) {
        logger.debug("写入文件名：{}", file);
        if (!file.exists())
            try {
                file.createNewFile();
            } catch (IOException e) {
                logger.error("文件创建失败！", e);
            }
        try {
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bw1 = new BufferedWriter(fileWriter);
            bw1.write(text);// 将内容写到文件中
            bw1.flush();// 强制输出缓冲区内容
            bw1.close();// 关闭流
        } catch (IOException e) {
            logger.warn("写入文件失败！", e);
        }
    }


}
