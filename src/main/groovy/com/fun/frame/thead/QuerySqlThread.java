package com.fun.frame.thead;

import com.fun.base.constaint.ThreadLimitTimesCount;
import com.fun.base.interfaces.IMySqlBasic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

/**
 * 数据库多线程类，query方法类，区别于updatethread
 */
public class QuerySqlThread extends ThreadLimitTimesCount {

    private static Logger logger = LoggerFactory.getLogger(QuerySqlThread.class);

    String sql;

    IMySqlBasic base;

    public QuerySqlThread(IMySqlBasic base, String sql, int times) {
        this.times = times;
        this.sql = sql;
        this.base = base;
    }

    @Override
    public void before() {
        base.getConnection();
    }

    @Override
    protected void doing() throws SQLException {
        base.excuteQuerySql(sql);
    }

    @Override
    protected void after() {
        base.mySqlOver();
    }


}
