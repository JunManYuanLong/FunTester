package com.funtester.frame.execute;

import com.funtester.frame.SourceCode;
import groovy.lang.*;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.groovy.control.CompilationFailedException;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * groovy脚本执行类，用户执行上传的groovy脚本，功能简单，使用未做封装，将就用一下
 * <p>
 * GroovyClassLoader
 * 用 Groovy 的 GroovyClassLoader ，动态地加载一个脚本并执行它的行为。GroovyClassLoader是一个定制的类装载器，
 * 负责解释加载Java类中用到的Groovy类。
 * <p>
 * GroovyShell
 * GroovyShell允许在Java类中（甚至Groovy类）求任意Groovy表达式的值。您可使用Binding对象输入参数给表达式，
 * 并最终通过GroovyShell返回Groovy表达式的计算结果。
 * <p>
 * GroovyScriptEngine
 * GroovyShell多用于推求对立的脚本或表达式，如果换成相互关联的多个脚本，使用GroovyScriptEngine会更好些。
 * GroovyScriptEngine从您指定的位置（文件系统，URL，数据库，等等）加载Groovy脚本，并且随着脚本变化而重新加载它们。
 * 如同GroovyShell一样，GroovyScriptEngine也允许您传入参数值，并能返回脚本的值。
 */
public class ExecuteGroovy extends SourceCode {

    private static Logger logger = LogManager.getLogger(ExecuteSource.class);

    /**
     * Groovy类加载器
     * 为啥要每次new一个GroovyClassLoader，而不是所有的脚本持有一个？
     * <p>
     * 因为如果脚本重新加载了，这时候就会有新老两个class文件，如果通过一个classloader持有的话，这样在GC扫描的时候，会认为老的类还在存活，导致回收不掉，所以每次new一个就能解决这个问题了。
     */
    private static GroovyClassLoader getLoader() {
        return new GroovyClassLoader(ExecuteGroovy.class.getClassLoader());
    }

    /**
     * 获取类所有方法
     *
     * @param path
     * @return
     */
    public static List<String> getAllMethodName(String path) {
        List<String> names = new ArrayList<>();
        try {
            Class<?> groovyClass = getLoader().parseClass(new File(path));
            Method[] declaredMethods = groovyClass.getDeclaredMethods();
            names = Arrays.asList(declaredMethods).stream().map(f -> f.getName()).collect(Collectors.toList());
        } catch (IOException e) {
            logger.warn("执行 {} 失败!", path, e);
            e.printStackTrace();
        }
        return names;
    }

    /**
     * 获取groovy对象和执行类
     *
     * @param path 类文件路径
     * @param name 方法名
     * @param args 这里默认{@link String}
     */
    public static Object executeFileMethod(String path, String name, Object... args) {
        try {
            Class<?> groovyClass = getLoader().parseClass(new File(path));//创建类
            GroovyObject groovyObject = (GroovyObject) groovyClass.newInstance();//创建类对象
            groovyObject.invokeMethod(name, args);
        } catch (IOException | ReflectiveOperationException e) {
            logger.warn("执行 {} 失败!", path, e);
            fail();
        }
        return null;
    }

    public static Object executeFileMethod(String path, String name) {
        return executeFileMethod(path, name, MetaClassImpl.EMPTY_ARGUMENTS);
    }

    /**
     * 使用Groovyshell重写
     *
     * @param path
     * @param name
     * @param args
     * @return
     */
    public static Object executeFileMethod2(String path, String name, Object... args) {
        GroovyShell groovyShell = new GroovyShell();
        Script script = null;
        try {
            script = groovyShell.parse(new File(path));
        } catch (IOException e) {
            logger.warn("执行 {} 失败!", path, e);
            fail();
        }
        return script.invokeMethod(name, args);
    }


    public static Object executeScriptMethod(String content, String name, Object... args) {
        try {
            Class<?> groovyClass = getLoader().parseClass(content);//创建类
            GroovyObject groovyObject = (GroovyObject) groovyClass.newInstance();//创建类对象
            return groovyObject.invokeMethod(name, args);
        } catch (ReflectiveOperationException | CompilationFailedException e) {
            logger.warn("执行 {} 失败!", content, e);
            fail();
        }
        return null;
    }

    public static void executeScriptMethod(String content, String name) {
        executeScriptMethod(content, name, MetaClassImpl.EMPTY_ARGUMENTS);
    }

    /**
     * 使用Groovyshell重写
     *
     * @param content
     * @param name
     * @param args
     * @return
     */
    public static Object executeScriptMethod2(String content, String name, Object... args) {
        GroovyShell groovyShell = new GroovyShell();
        Script parse = groovyShell.parse(content);
        return parse.invokeMethod(name, args);
    }

    /**
     * @param filepath
     * @return
     */
    public static Object excuteFile(String filepath) {
        return excuteFile(filepath, new Binding());
    }

    /**
     * 直接执行Groovy脚本,传参用{@link Binding}
     *
     * @param filepath
     * @param binding
     * @return
     */
    public static Object excuteFile(String filepath, Binding binding) {
        GroovyShell groovyShell = new GroovyShell(binding);
        try {
            return groovyShell.evaluate(new File(filepath));
        } catch (IOException e) {
            logger.warn("执行 {} 失败!", filepath, e);
            fail();
        }
        return null;
    }

    /**
     * 直接执行文本脚本
     *
     * @param script
     * @return
     */
    public static Object executeScript(String script) {
        return executeScript(script, new Binding());
    }


    /**
     * 直接执行脚本,传参用{@link Binding}
     *
     * @param script
     * @param binding
     * @return
     */
    public static Object executeScript(String script, Binding binding) {
        GroovyShell groovyShell = new GroovyShell(binding);
        return groovyShell.evaluate(script);
    }

    /**
     * 测试类,暂时不用
     */
    private static class FunTester {

        public static Object test() throws IOException, ResourceException, ScriptException, IllegalAccessException, InstantiationException, javax.script.ScriptException {
            /*下面是ScriptEngineManager的使用*/
            ScriptEngineManager factory = new ScriptEngineManager();
            ScriptEngine groovy = factory.getEngineByName("groovy");
            Bindings bind = groovy.createBindings();
            bind.put("name", DEFAULT_STRING);
            bind.put("age", 12);
            bind.put("fun", getJson("name=FunTester", "age=12"));
            groovy.eval("def sayHello(name,age){return 'Hello,I am ' + name + ',age' + age;}", bind);
            groovy.eval("def test(){return fun.toString()}", bind);

            /*下面是GroovyScriptEngine的使用*/
            GroovyScriptEngine groovyScriptEngine = new GroovyScriptEngine("");
            Class a = groovyScriptEngine.loadScriptByName("a.groovy");
            Class b = groovyScriptEngine.loadScriptByName("b.groovy");
            GroovyObject groovyObject = (GroovyObject) a.newInstance();
            groovyObject.invokeMethod("test", null);
            Binding binding = new Binding();
            binding.setVariable("name", DEFAULT_STRING);
            binding.setVariable("age", 12);
            groovyScriptEngine.run("a.groovy", binding);
            groovyScriptEngine.run("b.groovy", binding);
            return binding.getVariable("name");
        }

    }

    public static void cache(String scriptClass) throws IllegalAccessException, InstantiationException {
        GroovyClassLoader groovyClassLoader = new GroovyClassLoader(FunTester.class.getClassLoader());
        Class<?> groovyClass = null;
        String classKey = String.valueOf(scriptClass.hashCode());
//先从缓存里面去Class文件
        if (GroovyScriptClassCache.newInstance().containsKey(classKey)) {
            groovyClass = GroovyScriptClassCache.newInstance().getClassByKey(classKey);
        } else {
            groovyClass = groovyClassLoader.parseClass(scriptClass);
            GroovyScriptClassCache.newInstance().putClass(classKey, groovyClass);
        }

        GroovyObject go = (GroovyObject) groovyClass.newInstance();
    }

    public static class GroovyScriptClassCache {

        private static final Map<String, Class<?>> GROOVY_SCRIPT_CLASS_CACHE = new HashMap<String, Class<?>>();

        private GroovyScriptClassCache() {
        }

        private static GroovyScriptClassCache instance = new GroovyScriptClassCache();

        public static GroovyScriptClassCache newInstance() {
            return instance;
        }

        public Class<?> getClassByKey(String key) {
            return GROOVY_SCRIPT_CLASS_CACHE.get(key);
        }

        public void putClass(String key, Class<?> clazz) {
            GROOVY_SCRIPT_CLASS_CACHE.put(key, clazz);
        }

        public boolean containsKey(String key) {
            return GROOVY_SCRIPT_CLASS_CACHE.containsKey(key);
        }

    }

}