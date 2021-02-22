package com.funtester.frame.thread;

import com.funtester.base.constaint.ThreadBase;
import com.funtester.base.interfaces.MarkThread;
import com.funtester.frame.SourceCode;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 用于非单纯的http请求以及非HTTP请求,没有httprequestbase对象的标记方法,自己实现的虚拟类,可用户标记header固定字段或者随机参数,使用T作为参数载体,目前只能使用在T为string类才行
 */
public class FixedQpsParamMark extends SourceCode implements MarkThread, Cloneable, Serializable {

    private static final long serialVersionUID = 2135701056209833015L;

    public static AtomicInteger num = new AtomicInteger(10000);

    /**
     * 用于标记执行线程
     */
    String name;

    @Override
    public String mark(ThreadBase threadBase) {
        return name + num.getAndIncrement();
    }

    @Override
    public FixedQpsParamMark clone() {
        FixedQpsParamMark paramMark = new FixedQpsParamMark(this.name);
        return paramMark;
    }

    private FixedQpsParamMark() {
        name = EMPTY;
    }

    public FixedQpsParamMark(String name) {
        this();
        this.name = name;
    }


}
