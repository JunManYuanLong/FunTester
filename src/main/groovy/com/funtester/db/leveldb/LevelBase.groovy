package com.funtester.db.leveldb

import com.funtester.frame.SourceCode
import com.funtester.utils.Regex
import org.iq80.leveldb.DB
import org.iq80.leveldb.DBIterator
import org.iq80.leveldb.Options
import org.iq80.leveldb.impl.Iq80DBFactory

import static org.iq80.leveldb.impl.Iq80DBFactory.bytes

/**
 * LevelDB数据库操作类
 */
class LevelBase extends SourceCode {

    static Iq80DBFactory factory = Iq80DBFactory.factory

    static Options options = new Options()

    static LevelBase FunBase

    static def Instance() {
        if (FunBase == null) {
            FunBase = new LevelBase(DEFAULT_STRING)
        }
        FunBase
    }

    DB db

    /**
     * 创建对象
     * @param path
     */
    LevelBase(String path) {
        db = factory.open(new File(getLongFile(path)), options)
    }

    /**
     * 获取value
     * @param key
     * @return
     */
    def get(String key) {
        new String(db.get(bytes(key)), DEFAULT_CHARSET)
    }

    /**
     * 获取int类型
     * @param key
     * @return
     */
    def getInt(String key) {
        return changeStringToInt(get(key))
    }

    /**
     * 存储数据
     * @param key
     * @param value
     * @return
     */
    def put(String key, String value) {
        db.put(bytes(key), bytes(value))
    }

    /**
     * 在原来的结果上追加
     * @param key
     * @param content
     * @return
     */
    def append(String key, String content) {
        put(key, get(key) + content)
    }

    /**
     * 在原来的结果中删除
     * @param key
     * @param content
     * @return
     */
    def cut(String key, String content) {
        put(key, get(key) - content)
    }

    /**
     * put int类型的值
     * @param key
     * @param value
     * @return
     */
    def putInt(String key, int value) {
        db.put(bytes(key), bytes(value + EMPTY))
    }

    /**
     * 自增
     * @param key
     * @return
     */
    def incr(String key) {
        add(key, 1)
    }

    /**
     * 自减
     * @param key
     * @return
     */
    def decr(String key) {
        minus(key, 1)
    }

    /**
     * value 值增加
     * @param key
     * @param i
     * @return
     */
    def add(String key, int i) {
        synchronized (this) {
            putInt(key, getInt(key) + i)
        }
    }

    /**
     * value 值减少
     * @param key
     * @param i
     * @return
     */
    def minus(String key, int i) {
        add(key, -i)
    }

    /**
     * 删除某个值
     * @param key
     * @return
     */
    def del(String key) {
        db.delete(bytes(key))
    }

    /**
     * 获取所有key
     * @return
     */
    def keys() {
        def keys = []
        DBIterator iterator = db.iterator();
        while (iterator.hasNext()) {
            Map.Entry<byte[], byte[]> next = iterator.next();
            keys << next.key
        }
        return keys
    }

    /**
     * 获取所有key
     * @return
     */
    def keys(def regex) {
        def keys = []
        DBIterator iterator = db.iterator();
        while (iterator.hasNext()) {
            Map.Entry<byte[], byte[]> next = iterator.next();
            keys << next.key
        }
        return keys.findAll {Regex.isMatch(it, regex)}
    }

    /**
     * 关闭
     * @return
     */
    def close() {
        db.close()
    }

}
