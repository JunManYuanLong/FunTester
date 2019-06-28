package com.fun.config

import com.fun.frame.SourceCode
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * 读取配置工具
 */
class PropertyUtils extends SourceCode {
    static Logger logger = LoggerFactory.getLogger(PropertyUtils.class)

    /**
     * 获取指定.properties配置文件中所以的数据
     * @param propertyName
     *        调用方式：
     *            1.配置文件放在resource源包下，不用加后缀
     *              PropertiesUtil.getAllMessage("message")
     *            2.放在包里面的
     *              PropertiesUtil.getAllMessage("com.test.message")
     * @return
     */
    static Property getProperties(String propertyName) {
        try {
            new Property(ResourceBundle.getBundle(propertyName.trim()))
        } catch (MissingResourceException e) {
            logger.warn("找不到配置文件", e)
            new Property()
        }
    }

/**
 * 配置项
 */
    static class Property {
        Map<String, String> properties = new HashMap<>()

        Property(ResourceBundle resourceBundle) {
            def set = resourceBundle.keySet()
            for (def key in set) {
                properties.put key, resourceBundle.getString(key)
            }
        }

        String getProperty(String name) {
            if (contain(name)) properties.get(name)
        }

        int getPropertyInt(String name) {
            changeStringToInt(properties.get(name))
        }

        boolean getPropertyBoolean(String name) {
            changeStringToBoolean(properties.get(name))
        }

        int size() {
            properties.size()
        }

/**
 * 输出所以配置项
 * @return
 */
        def printAll() {
            output properties
        }

/**
 * 是否有配置项
 * @param key
 * @return
 */
        boolean contain(def key) {
            boolean var = properties.containsKey key asBoolean()
            if (!var) logger.error("配置{}未发现！", key)
            var
        }
    }
}
