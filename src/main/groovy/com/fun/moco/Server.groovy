package com.fun.moco

import com.fun.frame.SourceCode
import com.github.dreamhead.moco.HttpServer
import com.github.dreamhead.moco.MocoMonitor
import com.github.dreamhead.moco.MocoRequestHit
import com.github.dreamhead.moco.Runner

import static com.github.dreamhead.moco.Moco.httpServer
import static com.github.dreamhead.moco.Moco.log

/**
 * 获取server的工具类，提供了计数监视器和日志监视器
 */
class Server extends SourceCode {

    def array = new ArrayList()

/**
 * 默认方法，端口号12345
 * @return
 */
    static HttpServer getServer() {
        httpServer 12345, getLogMonitor()
    }

    static HttpServer getServer(int port) {
        httpServer port, getLogMonitor()
    }

    static HttpServer getServer(int port, MocoMonitor... mocoMonitors) {
        httpServer port, mocoMonitors
    }

/**
 * 获取日志监视器
 * @param name
 * @return
 */
    static def getLogMonitor(String name) {
        log name, DEFAULT_CHARSET
    }

/**
 * 获取日志监视器，默认在控制台显示
 * @return
 */
    static def getLogMonitor() {
        log()
    }

/**
 * 获取计数监视器
 * @return
 */
    static def getHitMonitor() {
        MocoRequestHit.requestHit()
    }

/**
 * 启动所有服务
 * @param httpServer
 * @return
 */
    static Server run(HttpServer... httpServer) {
        def server = new Server()
        httpServer.each { x -> server.array << Runner.runner(x) }
        server.start()
    }

/**
 * 启动服务
 * @return
 */
    def start() {
        array.each { x -> x.start() }
        this
    }

/**
 * 结束服务
 * @return
 */
    def stop() {
        array.each { x -> x.stop() }
    }
}
