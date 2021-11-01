package com.funtester.frame.thread;

import com.funtester.base.constaint.FixedThread;
import com.funtester.base.interfaces.IMySqlBasic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

/**
 * 数据库多线程类，query方法类，区别于updatethread
 */
public class QuerySqlThread extends FixedThread {

    private static final long serialVersionUID = 879371247008746883L;

    private static Logger logger = LogManager.getLogger(QuerySqlThread.class);

    String sql;

    IMySqlBasic base;

    public QuerySqlThread(IMySqlBasic base, String sql, int times) {
        this.limit = times;
        this.sql = sql;
        this.base = base;
    }

    @Override
    public void before() {
        base.getConnection();
    }

    @Override
    protected void doing() throws SQLException {
        base.executeQuerySql(sql);
    }

    @Override
    protected void after() {
        super.after();
        base.over();
    }

    @Override
    public FixedThread clone() {
        return null;
    }


}
