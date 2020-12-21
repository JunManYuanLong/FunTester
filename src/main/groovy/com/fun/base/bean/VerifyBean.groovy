package com.fun.base.bean

import com.fun.config.VerifyType

/**
 * 验证对象类
 */
class VerifyBean extends AbstractBean implements Serializable {

    private static final long serialVersionUID = -1595942567071153982L;

    String verifyType

    String value

    String des

    String result

    /**
     * 获取验证类型的枚举类
     * @return
     */
    VerifyType getVerfiType() {
        VerifyType.getRequestType(verifyType)
    }

}
