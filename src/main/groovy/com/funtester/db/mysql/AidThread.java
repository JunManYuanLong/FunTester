package com.funtester.db.mysql;

import com.funtester.frame.SourceCode;

/**
 * mysql辅助线程，当任务数太满的时候启用
 * <p>已经启用，单独写了基于springboot的sql存储服务</p>
 */
@Deprecated
public class AidThread extends SourceCode {
//public class AidThread extends SourceCode implements Runnable {

//    private static Logger logger = LogManager.getLogger(AidThread.class);
//
//    @Override
//    public void run() {
//        MySqlObject object = new MySqlObject();
//        MySqlObject.threadNum.incrementAndGet();
//        while (true) {
//            if (object.statement == null || MySqlTest.getWaitWorkNum() < SqlConstant.MYSQL_WORK_PER_THREAD) break;
//            String sql = MySqlTest.getWork();
//            if (sql == null) break;
//            logger.info("辅助线程执行SQL：{}", sql);
//            object.executeUpdateSql(sql);
//        }
//        MySqlObject.threadNum.decrementAndGet();
//        object.close();
//    }

}
