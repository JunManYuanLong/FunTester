package com.fun.moco


import com.github.dreamhead.moco.HttpServer
import com.github.dreamhead.moco.MocoMonitor
import com.github.dreamhead.moco.MocoRequestHit
import com.github.dreamhead.moco.RequestHit
import com.github.dreamhead.moco.Runner

import static com.github.dreamhead.moco.Moco.httpServer
import static com.github.dreamhead.moco.Moco.log

/**
 * 获取server的工具类，提供了计数监视器和日志监视器
 * 这里的继承关系为了更方便调用mocorequest和mocoresponse的静态方法
 */
class MocoServer extends MocoResponse {

    def array = []

/**
 * 获取httpserver对象，端口号12345
 * @return
 */
    static HttpServer getServer() {
        httpServer 12345, getLogMonitor()
    }

/**
 * 获取httpserver对象
 * @param port
 * @return
 */
    static HttpServer getServer(int port) {
        httpServer port, getLogMonitor()
    }

/**
 * 获取httpserver对象
 * @param mocoMonitors
 * @return
 */
    static HttpServer getServer(MocoMonitor mocoMonitors) {
        httpServer 12345, mocoMonitors
    }

/**
 * 获取httpserver对象
 * @param port 端口
 * @param logName 日志文件名
 * @param configs 配置
 * @return
 */
    static HttpServer getServer(final int port, String logName, MocoMonitor configs) {
        httpServer port, getLogMonitor(logName), configs
    }

    /**
     * 获取httpserver对象
     * @param port 端口
     * @param logName 日志文件名
     * @return
     */
    static HttpServer getServer(final int port, String logName) {
        httpServer port, getLogMonitor(logName)
    }

/**
 * 获取日志监视器，在log_path下面
 * @param logName
 * @return
 */
    static def getLogMonitor(String logName) {
        log LOG_Path + logName, DEFAULT_CHARSET
    }

/**
 * 获取日志监视器，默认在控制台显示
 * @return
 */
    static def getLogMonitor() {
        log()
    }

/**
 * 获取计数监视器，计数器在做测试的时候用到，确认服务启动且接口调用正常
 * @return
 */
    static RequestHit getHitMonitor() {
        MocoRequestHit.requestHit()
    }

/**
 * 启动所有服务
 * @param httpServer
 * @return
 */
    static MocoServer run(HttpServer... httpServer) {
        def server = new MocoServer()
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
