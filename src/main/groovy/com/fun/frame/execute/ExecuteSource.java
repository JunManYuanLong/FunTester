package com.fun.frame.execute;

import com.fun.base.exception.FailException;
import com.fun.config.Constant;
import com.fun.frame.SourceCode;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@SuppressFBWarnings({"NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE", "NP_NULL_ON_SOME_PATH_EXCEPTION"})
public class ExecuteSource extends SourceCode {

    private static Logger logger = LoggerFactory.getLogger(ExecuteSource.class);

    /**
     * 执行包内所有类的非 main 方法
     *
     * @param packageName
     */
    public static void executeAllMethodInPackage(String packageName) {
        // String packageName = "najm.base";
        List<String> classNames = getClassName(packageName);
        if (classNames != null) {
            for (String className : classNames) {
                String path = packageName + "." + className;
                executeAllMethod(path);// 执行所有方法
            }
        }
    }


    /**
     * 执行一个类的方法内所有的方法，非 main，执行带参方法的代码已被注释
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
            e.printStackTrace();
        }
        Class<?> class1 = object.getClass();
        Method[] methods = class1.getDeclaredMethods();
        for (Method method : methods) {
            if (!method.getName().startsWith("testDemo")) // 排除 mian 方法
                continue;
//			output(class1.getName() + method.getName());
//			第一个参数写的是方法名,第二个\第三个\...写的是方法参数列表中参数的类型
//			try {
//				method = c.getMethod(method.getName());
//			} catch (NoSuchMethodException e) {
//				e.printStackTrace();
//			}
            try {
                method.invoke(object);
            } catch (IllegalAccessException e) {
                logger.warn("非法访问导致反射方法执行失败！", e);
            } catch (InvocationTargetException e) {
                logger.warn("反射调用目标异常导致方法执行失败！", e);
            } catch (Exception e) {
                logger.warn("反射方法执行失败！", e);
            } finally {
                sleep(Constant.EXECUTE_GAP_TIME);
            }
        }
    }

    /**
     * 获取当前类的所有用例方法名，已过滤
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
        Class<?> class1 = object.getClass();
        Method[] all = class1.getDeclaredMethods();
        for (int i = 0; i < all.length; i++) {
            String str = all[i].getName();
            if (str.startsWith("testDemo")) methods.add(str);
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