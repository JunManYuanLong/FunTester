package com.funtester.db.mysql


import com.funtester.config.PoolConstant
import com.funtester.db.mysql.FunMySql
import org.apache.commons.pool2.BasePooledObjectFactory
import org.apache.commons.pool2.PooledObject
import org.apache.commons.pool2.impl.DefaultPooledObject
import org.apache.commons.pool2.impl.GenericObjectPool
import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
/**
 * 自定义MySQL连接池对象
 */
class MysqlPool extends PoolConstant {

    private static final Logger logger = LogManager.getLogger(MysqlPool.class);

    /**
     * {@link com.funtester.config.SqlConstant#FUN_SQL_URL}会替换IP到URL*/
    String url;

    /**
     * 库
     **/
    String database;

    /**
     * 用户
     **/
    String user;

    /**
     * 密码
     **/
    String password;

    private GenericObjectPool<FunMySql> pool

    MysqlPool(String url, String database, String user, String password) {
        this.url = url
        this.database = database
        this.user = user
        this.password = password
        init()
    }

    /**
     * 初始化连接池
     * @return
     */
    def init() {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(MAX);
        poolConfig.setMinIdle(MIN_IDLE);
        poolConfig.setMaxIdle(MAX_IDLE);
        poolConfig.setMaxWaitMillis(MAX_WAIT_TIME);
        poolConfig.setMinEvictableIdleTimeMillis(MAX_IDLE_TIME);
        pool = new GenericObjectPool<FunMySql>(new FunTester(), poolConfig);
    }


    /**
     * 借出对象
     * @return
     */
    FunMySql borrow() {
        try {
            return pool.borrowObject()
        } catch (e) {
            logger.warn("获取${FunMySql.class} 失败", e)
        } finally {
            new FunMySql(url, database, user, password)
        }
    }

    /**
     * 归还对象
     * @param funMySql
     * @return
     */
    def back(FunMySql funMySql) {
        pool.returnObject(funMySql)
    }

    /**
     * 执行update SQL
     * @param sql
     * @return
     */
    def execute(def sql) {
        def driver = borrow()
        try {
            driver.executeUpdateSql(sql)
        } catch (e) {
            logger.warn("执行:{}失败", sql)
        } finally {
            back(driver)
        }
    }

    /**
     * 执行查询SQL
     * @param sql
     * @return
     */
    def query(def sql) {
        def driver = borrow()
        try {
            return driver.executeQuerySql(sql)
        } catch (e) {
            logger.warn("执行:{}失败", sql)
        } finally {
            back(driver)
        }
    }

    /**
     * 池化工厂类
     */
    private class FunTester extends BasePooledObjectFactory<FunMySql> {

        @Override
        FunMySql create() throws Exception {
            return new FunMySql(url, database, user, password)
        }

        @Override
        PooledObject<FunMySql> wrap(FunMySql obj) {
            return new DefaultPooledObject<FunMySql>(obj)
        }

        @Override
        void destroyObject(PooledObject<FunMySql> p) throws Exception {
            p.getObject().over()
            super.destroyObject(p)
        }
    }
}
