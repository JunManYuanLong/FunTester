package com.funtester.frame.thread;

import com.funtester.base.constaint.ThreadLimitTimesCount;
import com.funtester.base.interfaces.IMySqlBasic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据库多线程类,update方法类，区别于querythread
 */
public class UpdateSqlThread extends ThreadLimitTimesCount<String> {

    private static Logger logger = LoggerFactory.getLogger(UpdateSqlThread.class);

    IMySqlBasic base;

    public UpdateSqlThread(IMySqlBasic base, String sql, int times) {
        this.times = times;
        this.t = sql;
        this.base = base;
    }

    @Override
    protected void doing() {
        base.executeUpdateSql(t);
    }

    @Override
    protected void after() {
        super.after();
        base.mySqlOver();
    }

}
