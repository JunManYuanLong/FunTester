package com.funtester.frame.thread

import com.funtester.base.interfaces.IFunController
import com.funtester.db.redis.RedisBase
import com.funtester.frame.SourceCode
import com.funtester.frame.execute.FunQpsConcurrent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import redis.clients.jedis.StreamEntryID
import redis.clients.jedis.params.XReadParams

/**
 * 基于Redis的控制台实现
 */
class RedisController extends SourceCode implements IFunController {

    private static final Logger logger = LogManager.getLogger(RedisController.class);

    def host, ip, auth

    boolean stop = true

    int target

    int duration

    FunQpsConcurrent concurrent

    RedisController(FunQpsConcurrent concurrent) {
        this.concurrent = concurrent
    }

    @Override
    void run() {
        def base = new RedisBase(host, ip, auth)
        base.index = 1
        Map<String, StreamEntryID> entry = ["FunTester": StreamEntryID.newInstance()]
        output(base.exists("hickwall_update_target_qps_stream"))
        def block = XReadParams.xReadParams().count(10).block(10000)
        while (stop) {
            noError {
                def xread = base.xread(block, entry)
                xread.each {
                    it.value.each {
                        def fields = it.getFields()
                        show(fields)
                        if (fields.get("flowReplayId") == 8) {
                            this.target = fields.get("targetQps") as int
                            this.duration = fields.get("duration") as int
                            add()
                        }
                    }

                }
            }
        }
        base.close()
    }

    @Override
    void add() {
        def qps = this.concurrent.qps
        duration.times {
            this.concurrent.qps += (target - qps) / duration
            sleep(1.0)
        }
        concurrent.qps = target
    }

    @Override
    void reduce() {

    }

    @Override
    void over() {
        FunQpsConcurrent.key = false
        logger.info("Redis controller over")
    }

}