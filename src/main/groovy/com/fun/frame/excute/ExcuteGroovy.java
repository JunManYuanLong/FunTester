package com.fun.frame.excute;

import com.fun.frame.SourceCode;
import com.fun.utils.FileUtil;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * groovy脚本执行类，用户执行上传的groovy脚本，功能简单，使用未做封装，将就用一下
 */
public class ExcuteGroovy extends SourceCode {

    private static Logger logger = LoggerFactory.getLogger(ExcuteSource.class);

    private String path;

    private String name;

    private String[] args;//参数，暂时不支持参数

    private List<String> files = new ArrayList<>();//所有脚本

    private GroovyClassLoader loader = new GroovyClassLoader(getClass().getClassLoader());

    private GroovyObject groovyObject;//groovy对象

    private Class<?> groovyClass;//执行类


    public ExcuteGroovy(String path, String name) {
        this.path = path;
        this.name = name;
        getGroovyObject();
    }

    /**
     * 执行一个类的所有方法
     */
    public void excuteAllMethod(String path) {
        FileUtil.getAllFile(path);
        if (files == null)
            return;
        files.forEach((file) -> new ExcuteGroovy(file, EMPTY).excuteMethodByPath());
    }

    /**
     * 执行某个类的方法，需要做过滤
     *
     * @return
     */
    public void excuteMethodByName() {
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
    public void excuteMethodByPath() {
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