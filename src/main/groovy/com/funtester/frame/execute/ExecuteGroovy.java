package com.funtester.frame.execute;

import com.funtester.frame.SourceCode;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyShell;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * groovy脚本执行类，用户执行上传的groovy脚本，功能简单，使用未做封装，将就用一下
 */
public class ExecuteGroovy extends SourceCode {

    private static Logger logger = LogManager.getLogger(ExecuteSource.class);

    /**
     * Groovy类加载器
     */
    private static GroovyClassLoader loader = new GroovyClassLoader(ExecuteGroovy.class.getClassLoader());

    /**
     * 获取类所有方法
     *
     * @param path
     * @return
     */
    public static List<String> getAllMethodName(String path) {
        List<String> names = new ArrayList<>();
        try {
            Class<?> groovyClass = loader.parseClass(new File(path));
            Method[] declaredMethods = groovyClass.getDeclaredMethods();
            names = Arrays.asList(declaredMethods).stream().map(f -> f.getName()).collect(Collectors.toList());
        } catch (IOException e) {
            logger.warn("获取类对象 {} 所有方法失败!", path, e);
            e.printStackTrace();
        }
        return names;
    }

    /**
     * 获取groovy对象和执行类
     *
     * @param path 类文件路径
     * @param name 方法名
     * @param args 貌似只支持一个参数,这里默认{@link String}
     */
    public static void executeMethod(String path, String name, Object... args) {
        try {
            Class<?> groovyClass = loader.parseClass(new File(path));//创建类
            GroovyObject groovyObject = (GroovyObject) groovyClass.newInstance();//创建类对象
            groovyObject.invokeMethod(name, args);
        } catch (IOException | ReflectiveOperationException e) {
            logger.warn("获取类对象 {} 失败!", path, e);
            fail();
        }
    }

    public static void executeMethod(String path, String name) {
        executeMethod(path, name, null);
    }


    /**
     * 直接执行Groovy脚本
     *
     * @param filepath
     * @return
     */
    public static void excuteScript(String filepath) {
        GroovyShell groovyShell = new GroovyShell();
        try {
            groovyShell.evaluate(new File(filepath));
        } catch (IOException e) {
            logger.warn("执行脚本 {} 失败!", filepath, e);
            fail();
        }
    }

}