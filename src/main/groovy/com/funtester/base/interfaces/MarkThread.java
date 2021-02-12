package com.funtester.base.interfaces;

import com.funtester.base.constaint.ThreadBase;

/**
 * 用来标记thread,为了记录超时的请求
 */
public interface MarkThread {

    /**
     * 标记线程任务
     *
     * @param threadBase
     * @return
     */
    public String mark(ThreadBase threadBase);

    public MarkThread clone();


}
