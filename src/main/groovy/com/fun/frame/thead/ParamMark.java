package com.fun.frame.thead;

import com.fun.base.constaint.ThreadBase;
import com.fun.base.interfaces.MarkParams;
import com.fun.base.interfaces.MarkThread;
import com.fun.frame.SourceCode;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 用于非单纯的http请求以及非HTTP请求,没有httprequestbase对象的标记方法,自己实现的虚拟类,可用户标记header固定字段或者随机参数,使用T作为参数载体,目前只能使用在T为string类才行
 */
public class ParamMark extends SourceCode implements MarkParams, Cloneable, Serializable {

    private static final long serialVersionUID = -5532592151245141262L;

    public static AtomicInteger threadName = new AtomicInteger(100);

    String name;

    int num = 100_0000;

    @Override
    public String mark() {
        return null;
    }

    @Override
    public String mark(ThreadBase threadBase) {
        String m = name + num++;
        threadBase.t = m;
        return m;
    }

    @Override
    public MarkThread clone() {
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
