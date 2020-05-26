package com.fun.moco.support


import com.github.dreamhead.moco.ResponseHandler
import com.github.dreamhead.moco.handler.AbstractResponseHandler
import com.github.dreamhead.moco.internal.SessionContext

import static com.google.common.base.Preconditions.checkArgument

class DelayHandler extends AbstractResponseHandler {

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

    @Override
    void writeToResponse(SessionContext context) {
        sleep(time)
        handler.writeToResponse(context)
    }

}
