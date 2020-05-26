package com.fun.moco.support


import com.github.dreamhead.moco.ResponseHandler
import com.github.dreamhead.moco.handler.AbstractResponseHandler
import com.github.dreamhead.moco.internal.SessionContext

import java.util.concurrent.TimeUnit

import static com.google.common.base.Preconditions.checkArgument

/**
 * 延迟响应API拓展ResponseHandler
 */
class DelayHandler extends AbstractResponseHandler {

    /**
     * 延迟时间
     */
    private final int time

    private final ResponseHandler handler

    private DelayHandler(ResponseHandler handler, int time) {
        this.time = time
        this.handler = handler
    }

    public static ResponseHandler newSeq(final ResponseHandler handler, int time) {
        checkArgument(handler != null, "responsehandler 不能为空!");
        return new DelayHandler(handler, time);
    }

/**
 *
 * @param context
 */
    @Override
    void writeToResponse(SessionContext context) {
        com.github.dreamhead.moco.util.Idles.idle(time, TimeUnit.MILLISECONDS)
        handler.writeToResponse(context)
    }

}
