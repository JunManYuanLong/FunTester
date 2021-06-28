package com.funtester.frame.execute;

import com.alibaba.fastjson.JSON;
import com.funtester.base.exception.FailException;
import com.funtester.config.Constant;
import com.funtester.frame.SourceCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 主要用来执行反射任务
 */
public class ExecuteSource extends SourceCode {

    private static Logger logger = LogManager.getLogger(ExecuteSource.class);

    /**
     * 执行包内所有类的非 main 方法
     *
     * @param packageName
     */
    @Deprecated
    public static void executeAllMethodInPackage(String packageName) {
        List<String> classNames = getClassName(packageName);
        if (classNames != null) {
            for (String className : classNames) {
                String path = packageName + "." + className;
                executeAllMethod(path);// 执行所有方法
            }
        }
    }


    /**
     * 执行一个类的方法内所有的方法，非 main，执行带参方法的代码过滤
     *
     * @param path 类名
     */
    public static void executeAllMethod(String path) {
        Class<?> c = null;
        Object object = null;
        try {
            c = Class.forName(path);
            object = c.newInstance();
        } catch (Exception e) {
            logger.warn("获取 {} 对象失败!", path, e);
        }
        Method[] methods = c.getDeclaredMethods();
        for (Method method : methods) {
            try {
                method.invoke(object);
            } catch (Exception e) {
                logger.warn("方法 {} 执行失败！", method.getName(), e);
            } finally {
                sleep(Constant.EXECUTE_GAP_TIME);
            }
        }
    }

    /**
     * 执行方法
     * <p>防止编译报错,用list绕一圈</p>
     *
     * @param params
     */
    public static Object executeMethod(List<String> params) {
        Object[] objects = params.subList(1, params.size()).toArray();
        return executeMethod(params.get(0), objects);
    }

    /**
     * 执行方法
     * <p>防止编译报错,用list绕一圈</p>
     *
     * @param params
     */
    public static Object executeMethod(String[] params) {
        return executeMethod(Arrays.asList(params));
    }

    /**
     * 执行具体的某一个方法,提供内部方法调用
     * <p>重载方法如果参数是基础数据类型会报错</p>
     *
     * @param path
     * @param paramsType
     */
    public static Object executeMethod(String path, Object... paramsType) {
        int length = paramsType.length;
        if (length % 2 == 1) FailException.fail("参数个数错误,应该是偶数");
        String className = path.substring(0, path.lastIndexOf("."));
        String methodname = path.substring(className.length() + 1);
        Class<?> c = null;
        Object object = null;
        try {
            c = Class.forName(className);
            object = c.newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            logger.warn("创建实例对象时错误:{}", className, e);
        }
        Method[] methods = c.getDeclaredMethods();
        for (Method method : methods) {
            if (!method.getName().equalsIgnoreCase(methodname)) continue;
            try {
                Class[] classs = new Class[length / 2];
                for (int i = 0; i < paramsType.length; i += 2) {
                    classs[i / 2] = Class.forName(paramsType[i].toString());//此处基础数据类型的参数会导致报错,但不影响下面的调用
                }
                method = c.getMethod(method.getName(), classs);
            } catch (NoSuchMethodException | ClassNotFoundException e) {
                logger.warn("方法属性处理错误!");
            }
            try {
                Object[] ps = new Object[length / 2];
                for (int i = 1; i < paramsType.length; i += 2) {
                    String name = paramsType[i - 1].toString();
                    String param = paramsType[i].toString();
                    Object p = param;
                    if (name.contains("Integer")) {
                        p = Integer.parseInt(param);
                    } else if (name.contains("JSON")) {
                        p = JSON.parseObject(param);
                    }
                    ps[i / 2] = p;
                }
                method.invoke(object, ps);
            } catch (IllegalAccessException | InvocationTargetException e) {
                logger.warn("反射执行方法失败:{}", path, e);
            }
            break;
        }
        return null;
    }

    /**
     * 获取当前类的所有用例方法名
     *
     * @param path
     * @return
     */
    public static List<String> getAllMethodName(String path) {
        List<String> methods = new ArrayList<>();
        Class<?> c = null;
        Object object = null;
        try {
            c = Class.forName(path);
            object = c.newInstance();
        } catch (Exception e) {
            FailException.fail("初始化对象失败:" + path);
        }
        Method[] all = c.getDeclaredMethods();
        for (int i = 0; i < all.length; i++) {
            String str = all[i].getName();
            methods.add(str);
        }
        return methods;
    }

    /**
     * 获取某包下所有类
     *
     * @param packageName 包名
     * @return 类的完整名称
     */
    public static List<String> getClassName(String packageName) {
        List<String> fileNames = new ArrayList<>();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();// 获取当前位置
        String packagePath = packageName.replace(".", Constant.OR);// 转化路径，Linux 系统
        URL url = loader.getResource(packagePath);// 具体路径
        if (url == null || !"file".equals(url.getProtocol())) {
            FailException.fail("获取包路径失败!");
        }
        File file = new File(url.getPath());
        File[] childFiles = file.listFiles();
        for (File childFile : childFiles) {
            String path = childFile.getPath();
            if (path.endsWith(".class")) {
                path = path.substring(path.lastIndexOf(OR) + 1, path.lastIndexOf("."));
                fileNames.add(path);
            }
        }
        return fileNames;
    }


}