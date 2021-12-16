package com.funtester.utils


import java.lang.reflect.Field
import java.lang.reflect.Method

/**
 * 私有变量访问工具类,可用于final修饰的变量
 */
class PriUtil {

    /***
     * 获取私有成员变量的值
     *
     */
    static <F> F get(Object instance, String name, Class<F> f) {
        Field field = instance.getClass().getDeclaredField(name);
        field.setAccessible(true); // 参数值为true，禁止访问控制检查
        return (F) field.get(instance);
    }

    /***
     * 设置私有成员变量的值
     *
     */
    static void set(Object instance, String fileName, Object value) {
        Field field = instance.getClass().getDeclaredField(fileName);
        field.setAccessible(true);
        field.set(instance, value);
    }

    /***
     * 访问私有方法
     *
     */
    static <F> F call(Object instance, String name, Class[] parameterTypes, Object[] params) {
        Method method = instance.getClass().getDeclaredMethod(name, parameterTypes);
        method.setAccessible(true);
        return (F) method.invoke(instance, params);
    }

    /**
     * 获取static变量
     * @param c
     * @param name
     * @param f
     * @return
     */
    static <F> F get(Class c, String name, Class<F> f) {
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getType() == f && field.getName().equals(name))
                return (F) field.get(c);
        }
    }

    /**
     * 设置static变量
     * @param c
     * @param name
     * @param f
     */
    static void set(Class c, String name, Object f) {
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getName().equals(name))
                field.set(c, f)
        }
    }

    /**
     * 调用私有static方法
     * @param c
     * @param name
     * @param r
     * @param parameterTypes
     * @param params
     * @return
     */
    static <F> Object call(Class c, String name, Class[] parameterTypes, Object[] params) {
        Method method = c.getMethod(name, parameterTypes);
        method.setAccessible(true);
        return (F) method.invoke(null, params);
    }

}
