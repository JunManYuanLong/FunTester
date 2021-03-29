package com.funtester.frame.thread;

import com.funtester.base.constaint.ThreadBase;
import com.funtester.base.constaint.ThreadLimitTimesCount;
import com.funtester.base.interfaces.IMySqlBasic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 数据库多线程类,update方法类，区别于querythread
 */
public class UpdateSqlThread extends ThreadLimitTimesCount<String> {

    private static final long serialVersionUID = 5808571085138930143L;

    private static Logger logger = LogManager.getLogger(UpdateSqlThread.class);

    IMySqlBasic base;

    public UpdateSqlThread(IMySqlBasic base, String sql, int times) {
        this.times = times;
        this.f = sql;
        this.base = base;
    }

    @Override
    protected void doing() {
        base.executeUpdateSql(f);
    }

    @Override
    protected void after() {
        super.after();
        base.mySqlOver();
    }

    @Override
    public ThreadBase clone() {
        return null;
    }

}
