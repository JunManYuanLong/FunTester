package com.funtester.db.mongodb;


/**
 * mongo数据库配置对象，针对单个数据服务，单个身份验证
 */
public class MongoObject extends MongoBase {
//
//    String host;
//
//    int port;
//
//    String user;
//
//    String password;
//
//    String database;
//
//    MongoClient mongoClient;
//
//    /**
//     * 创建测试数据连接
//     *
//     * @param host
//     * @param port
//     * @param user
//     * @param password
//     * @param database
//     */
//    public MongoObject(String host, int port, String user, String password, String database) {
//        this.host = host;
//        this.port = port;
//        this.user = user;
//        this.password = password;
//        this.database = database;
//        this.mongoClient = getMongoClient(this);
//    }
//
//    /**
//     * 创建线上数据库连接
//     *
//     * @param port
//     * @param host
//     * @param user
//     * @param password
//     * @param database
//     */
//    public MongoObject(int port, String host, String user, String password, String database) {
//        this.host = host;
//        this.port = port;
//        this.user = user;
//        this.password = password;
//        this.database = database;
//        this.mongoClient = getMongoClientOnline(this);
//    }
//
//    /**
//     * 获取colletion对象
//     *
//     * @param collectionName
//     * @return
//     */
//    public MongoCollection<Document> getMongoCollection(String collectionName) {
//        MongoClient mongoClientOnline = getMongoClientOnline(this);
//        return mongoClientOnline.getDatabase(database).getCollection(collectionName);
//    }
//
//
//    /**
//     * 关闭连接
//     */
//    public void over() {
//        over(this.mongoClient);
//    }
//
//    @Override
//    public MongoObject clone() {
//        return new MongoObject(this.host, this.port, this.user, this.password, this.database);
//    }
//
//    public MongoObject clone2() {
//        return new MongoObject(this.port, this.host, this.user, this.password, this.database);
//    }

}
