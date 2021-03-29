package com.funtester.frame.execute;

import com.funtester.frame.SourceCode;
import com.funtester.utils.FileUtil;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * groovy脚本执行类，用户执行上传的groovy脚本，功能简单，使用未做封装，将就用一下
 */
public class ExecuteGroovy extends SourceCode {

    private static Logger logger = LogManager.getLogger(ExecuteSource.class);

    /**
     * 路径
     */
    private String path;

    /**
     * 文件名
     */
    private String name;

    /**
     * 所有的脚本文件
     */
    private List<String> files = new ArrayList<>();

    /**
     * Groovy类加载器
     */
    private GroovyClassLoader loader = new GroovyClassLoader(getClass().getClassLoader());

    /**
     * Groovy对象
     */
    private GroovyObject groovyObject;

    /**
     * 加载类
     */
    private Class<?> groovyClass;


    public ExecuteGroovy(String path, String name) {
        this.path = path;
        this.name = name;
        getGroovyObject();
    }

    /**
     * 执行一个类的所有方法
     */
    public void executeAllMethod(String path) {
        FileUtil.getAllFile(path);
        if (files == null)
            return;
        files.forEach((file) -> new ExecuteGroovy(file, EMPTY).executeMethodByPath());
    }

    /**
     * 执行某个类的方法，需要做过滤
     *
     * @return
     */
    public void executeMethodByName() {
        if (new File(path).isDirectory()) {
            logger.warn("文件类型错误!");
        }
        try {
            groovyObject.invokeMethod(name, null);
        } catch (Exception e) {
            logger.warn("执行" + name + "失败!", e);
        }
    }

    /**
     * 根据path执行相关方法
     */
    public void executeMethodByPath() {
        Method[] methods = groovyClass.getDeclaredMethods();//获取类方法，此处方法比较多，需过滤
        for (Method method : methods) {
            String methodName = method.getName();
            if (methodName.contains("test") || methodName.equals("main")) {
                groovyObject.invokeMethod(methodName, null);
            }
        }
    }

    /**
     * 获取groovy对象和执行类
     */
    public void getGroovyObject() {
        try {
            groovyClass = loader.parseClass(new File(path));//创建类
        } catch (IOException e) {
            logger.warn("groovy类加载失败！", e);
        }
        try {
            groovyObject = (GroovyObject) groovyClass.newInstance();//创建类对象
        } catch (InstantiationException e) {
            logger.warn("创建对象失败！", e);
        } catch (IllegalAccessException e) {
            logger.warn("非法异常！", e);
        }
    }

    /**
     * 获取文件下所有的groovy脚本,不支持递归查询
     *
     * @return
     */
    public List<String> getAllGroovyFile(String path) {
        File file = new File(path);
        if (file.isFile()) {
            files.add(path);
            return files;
        }
        File[] files1 = file.listFiles();
        int size = files1.length;
        for (int i = 0; i < size; i++) {
            String name = files1[i].getAbsolutePath();
            if (name.endsWith(".groovy"))
                files.add(name);
        }
        return files;
    }

}