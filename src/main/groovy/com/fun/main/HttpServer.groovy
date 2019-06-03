package com.fun.main

import com.fun.base.bean.Result
import com.fun.moco.MocoResponse
import com.fun.moco.MocoServer

/**
 * moco API的应用
 */
@SuppressWarnings("all")
class HttpServer extends MocoServer {
    public static void main(String[] args) {
        def server = getServer(getLogMonitor("1.log"))
        server.get(urlOnly("/test")).response(obResponse(Result.success(getJson("324324=32432"))))
        server.get(urlOnly("/ss")).response(limit(contentResponse("dsfsd"),contentResponse("432432"),1000))

        server.response(MocoResponse.contentResponse("hello word"))
        def run = run(server)


        waitForKey("fan")
        run.stop()
    }
}
