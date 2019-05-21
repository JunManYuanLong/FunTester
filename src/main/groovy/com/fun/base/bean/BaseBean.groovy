package com.fun.base.bean

import com.fun.frame.Save
import com.fun.frame.SourceCode
import net.sf.json.JSONObject
/**
 * bean的基类
 */
abstract class BaseBean extends SourceCode {
    /**
     * 将bean转化为json，为了进行数据处理和打印
     *
     * @return
     */
    JSONObject toJson() {
        return BeanUtil.toJson(this);
    }

    /**
     * 文本形式保存
     */
    def save() {
        Save.saveJson(this.toJson(), this.getClass().toString() + SourceCode.getMark());
    }

    /**
     * 控制台打印
     */
    def print() {
        output(this.toJson());
    }

    String toString() {
        return this.toJson().toString();
    }
}
