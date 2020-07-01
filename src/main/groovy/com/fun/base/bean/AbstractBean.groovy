package com.fun.base.bean

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.fun.frame.Save
import com.fun.frame.SourceCode
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.BeanUtils

/**
 * bean的基类
 */
abstract class AbstractBean implements Serializable {

    private static final long serialVersionUID = -1595942567071159847L;

    static final Logger logger = LoggerFactory.getLogger(AbstractBean.class)

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
        logger.info(this.getClass().toString() + "：" + this.toString());
    }

    def initFrom(String str) {
        JSONObject.parseObject(str, this.getClass())
    }

    def initFrom(Object str) {
        initFrom(JSON.toJSON(str))
    }

    def copyFrom(AbstractBean source) {
        BeanUtils.copyProperties(source, this)
    }

    def copyTo(AbstractBean target) {
        BeanUtils.copyProperties(this, target)
    }

    @Override
    String toString() {
        JSONObject.toJSONString(this)
    }
}
