package com.funtester.frame.thread;

import com.funtester.base.constaint.ThreadBase;
import com.funtester.base.interfaces.MarkThread;
import com.funtester.frame.SourceCode;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 用于非单纯的http请求以及非HTTP请求,没有httprequestbase对象的标记方法,自己实现的虚拟类,可用户标记header固定字段或者随机参数,使用T作为参数载体,目前只能使用在T为string类才行
 */
public class ParamMark extends SourceCode implements MarkThread, Cloneable, Serializable {

    private static final long serialVersionUID = -5532592151245141262L;

    public static AtomicInteger threadName = new AtomicInteger(getRandomIntRange(1000, 9000));

    /**
     * 用于标记执行线程
     */
    String name;

    int num = getRandomIntRange(100, 999) * 1000;

    @Override
    public String mark(ThreadBase threadBase) {
        return name + num++;
    }

    @Override
    public ParamMark clone() {
        ParamMark paramMark = new ParamMark();
        return paramMark;
    }

    public ParamMark() {
        this.name = threadName.getAndIncrement() + EMPTY;
    }

    public ParamMark(String name) {
        this();
        this.name = name;
    }


}
