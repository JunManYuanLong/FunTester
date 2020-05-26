package com.fun.moco.support


import com.github.dreamhead.moco.ResponseHandler
import com.github.dreamhead.moco.handler.AbstractResponseHandler
import com.github.dreamhead.moco.internal.SessionContext
import com.github.dreamhead.moco.util.Idles

import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit

import static com.google.common.base.Preconditions.checkArgument
/**
 * 固定QPS的接口实现类
 */
class QPSHandler extends AbstractResponseHandler {


    private static final Semaphore semaphore = new Semaphore(1, true);
    /**
     * 访问间隔
     */
    private final int gap

    private final ResponseHandler handler

    private QPSHandler(ResponseHandler handler, int gap) {
        this.gap = gap
        this.handler = handler
    }

    public static ResponseHandler newSeq(final ResponseHandler handler, int gap) {
        checkArgument(handler != null, "responsehandler 不能为空!");
        return new QPSHandler(handler, gap);
    }


/**
 * 具体实现,这里采用微秒,实验证明微秒更准确
 * @param context
 */
    @Override
    void writeToResponse(SessionContext context) {
        semaphore.acquire()
        Idles.idle(gap * 1000, TimeUnit.MICROSECONDS)
        handler.writeToResponse(context)
        semaphore.release()
    }

}
