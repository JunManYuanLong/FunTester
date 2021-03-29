package com.funtester.base.bean

import com.alibaba.fastjson.JSON
import com.funtester.base.exception.ParamException
import com.funtester.config.VerifyType
import com.funtester.utils.JsonUtil
import com.funtester.utils.Regex
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

/**
 * 验证对象类
 */
class VerifyBean extends AbstractBean implements Serializable, Cloneable {

    private static Logger logger = LogManager.getLogger(VerifyBean.class)

    private static final long serialVersionUID = -1595942567071153982L;

    VerifyType type

    /**
     * 验证语法
     */
    String verify
    /**
     * 待验证内容
     */
    String value

    String des

    boolean isVerify

    boolean result;

    VerifyBean(String verify, String value, String des) {
        this.value = value
        this.des = des
        def split = verify.split(REG_PART, 2)
        this.verify = split[1]
        this.type = VerifyType.getRequestType(split[0])
    }

    /**
     * 用于进行自身验证,会进行isverify和result记录
     * @return
     */
    boolean verify() {
        if (isVerify && result) return result
        isVerify = true
        result = verify(value)
        result
    }

    /**
     * 用于进行对象外String验证,不会修改对象属性
     * @param val
     * @return
     */
    boolean verify(String val) {
        boolean res
        try {
            switch (type) {
                case VerifyType.CONTAIN:
                    res = val.contains(verify)
                    return res
                case VerifyType.REGEX:
                    res = Regex.isRegex(val, verify)
                    return res
                case VerifyType.JSONPATH:
                    def split = verify.split(REG_PART, 2)
                    def path = split[0]
                    def v = split[1]
                    def instance = JsonUtil.getInstance(JSON.parseObject(val))
                    res = instance.getVerify(path).fit(v)
                    return res
                case VerifyType.HANDLE:
                    def sp = verify.split(REG_PART, 2)
                    def path = sp[0]
                    def ve = sp[1]
                    def instance = JsonUtil.getInstance(JSON.parseObject(val))
                    res = instance.getVerify(path).fitFun(ve)
                    return res
                default:
                    ParamException.fail("验证类型参数错误!")
            }
        } catch (Exception e) {
            logger.warn("验证出现问题: {}", e.getMessage())
            res = false
        } finally {
            /*这里Groovy可以这么写,但是Java不能这么写,因为需要有返回值*/
            logger.info("verify对象 {} ,验证结果: {}", verify, res)
        }
    }

    @Override
    def print() {
        logger.info("{} 验证结果: {}", des, result)
    }

    @Override
    VerifyBean clone() {
        new VerifyBean(this.verify, this.value, this.des)
    }


}
