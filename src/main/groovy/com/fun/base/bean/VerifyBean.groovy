package com.fun.base.bean

import com.fun.config.VerifyType
import com.fun.utils.Time

import java.util.concurrent.atomic.AtomicLong

/**
 * 验证对象类
 */
class VerifyBean extends AbstractBean implements Serializable {

    private static final long serialVersionUID = -1595942567071153982L;

    private static AtomicLong markNum = new AtomicLong(Time.getTimeStamp())

    long mark

    VerifyType type

    String verify

    String value

    String des

    String result

    VerifyBean(String verify, String value, String des, String result) {
        this.verify = verify
        this.value = value
        this.des = des
        this.result = result
        this.type = VerifyType.getRequestType(verify)
        mark = markNum.getAndIncrement()
    }

}
