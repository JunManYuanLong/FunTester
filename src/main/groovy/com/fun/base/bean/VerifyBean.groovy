package com.fun.base.bean

import com.alibaba.fastjson.JSON
import com.fun.base.exception.ParamException
import com.fun.config.VerifyType
import com.fun.utils.JsonUtil
import com.fun.utils.Regex
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import static com.fun.config.Constant.REG_PART
/**
 * 验证对象类
 */
class VerifyBean extends AbstractBean implements Serializable, Cloneable {

    private static Logger logger = LoggerFactory.getLogger(VerifyBean.class)

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

    boolean verify() {
        isVerify = true
        try {
            switch (type) {
                case VerifyType.CONTAIN:
                    result = value.contains(verify)
                    break
                case VerifyType.REGEX:
                    result = Regex.isRegex(value, verify)
                    break
                case VerifyType.JSONPATH:
                    def split = verify.split(REG_PART, 2)
                    def path = split[0]
                    def v = split[1]
                    def instance = JsonUtil.getInstance(JSON.parseObject(value))
                    result = instance.getVerify(path).fit(v)
                    break
                case VerifyType.HANDLE:
                    def sp = verify.split(REG_PART, 2)
                    def path = sp[0]
                    def ve = sp[1]
                    def instance = JsonUtil.getInstance(JSON.parseObject(value))
                    result = instance.getVerify(path).fitFun(ve)
                    break
                default:
                    ParamException.fail("验证类型参数错误!")
            }
        } catch (Exception e) {
            logger.warn("验证出现问题!", e)
            result = false
        } finally {
            logger.info("verify对象 {} ,验证结果: {}", verify, result)
            result
        }
    }

    @Override
    public VerifyBean clone() throws CloneNotSupportedException {
        new VerifyBean(this.verify, this.value, this.des)
    }
}
