package com.funtester.base.bean

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.funtester.config.Constant
import com.funtester.frame.Save
import com.funtester.frame.SourceCode
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
/**
 * bean的基类，提供了一些基本的方法
 */
abstract class AbstractBean extends Constant{

    static final Logger logger = LogManager.getLogger(AbstractBean.class)

    /**
     * 将bean转化为json，为了进行数据处理和打印
     *
     * @return
     */
    JSONObject toJson() {
        JSONObject.parseObject(JSONObject.toJSONString(this))
    }

    /**
     * 文本形式保存
     */
    def save() {
        Save.saveJson(this.toJson(), this.getClass().toString() + SourceCode.getMark());
    }

    /**
     * 控制台打印，使用WARN记录，以便查看
     */
    def print() {
        logger.warn(this.getClass().toString() + "：" + this.toString());
    }

    /**
     * 从json字符串中初始化bean
     * @param str
     * @return
     */
    def initFrom(String str) {
        JSONObject.parseObject(str, this.getClass())
    }

    /**
     * 从json字符串中初始化bean
     * @param str
     * @return
     */
    def initFrom(Object str) {
        initFrom(JSON.toJSONString(str))
    }

    /**
     * 从bean中初始化bean
     * @param source
     * @param clazz
     * @return
     */
    static <T> T copyFrom(AbstractBean source ,Class<T> clazz) {
        JSON.parseObject(JSON.toJSONString(source), clazz)
    }

    /**
     * 这里bean的属性必需是可以访问的,不然会返回空json串
     * @return
     */
    @Override
    String toString() {
        JSONObject.toJSONString(this)
    }

    /**
     * 克隆bean
     * @return
     */
    @Override
    protected Object clone() {
        initFrom(this)
    }

}
