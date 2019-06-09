package com.fun.main

import com.fun.base.bean.Result
import com.fun.moco.MocoResponse
import com.fun.moco.MocoServer

import static com.github.dreamhead.moco.Moco.from

/**
 * moco API的应用
 */
@SuppressWarnings("all")
class HttpServer extends MocoServer {
    public static void main(String[] args) {
        def server = getServer(getLogMonitor("1.log"))
        server.get(urlOnly("/test")).response(obRes(Result.success(getJson("324324=32432"))))
        server.get(urlOnly("/ss")).response(limit(textRes("dsfsd"),textRes("432432"),1000))

        server.get(urlOnly("/find")).response(random("43242=342","e343=fdsf","3242"))
        server.get(urlOnly("/qq")).response(cycle(getJson("3234=32423"),getJson("ew=32")))
        server.response(MocoResponse.textRes("hello word"))
        def run = run(server)


        waitForKey("fan")
        run.stop()
    }

    public static proxyNetdata(HttpServer server) {
        server.proxy(from("/").to("http://10.10.6.3:19999"))
    }
}
