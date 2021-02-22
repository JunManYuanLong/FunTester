package com.funtester.db.mongodb;

import com.funtester.frame.SourceCode;

/**
 * mongo操作类的基础类
 */
@SuppressWarnings("all")
public class MongoBase extends SourceCode {
//
//    /**
//     * 获取服务地址list
//     *
//     * @param addresses
//     * @return
//     */
//    public static List<ServerAddress> getServers(ServerAddress... addresses) {
//        return Arrays.asList(addresses);
//    }
//
//    /**
//     * 获取服务地址
//     *
//     * @param host
//     * @param port
//     * @return
//     */
//    public static ServerAddress getServerAdress(String host, int port) {
//        return new ServerAddress(host, port);
//    }
//
//    /**
//     * 获取认证list
//     *
//     * @param credentials
//     * @return
//     */
//    public static List<MongoCredential> getCredentials(MongoCredential... credentials) {
//        return Arrays.asList(credentials);
//    }
//
//    /**
//     * 获取验证
//     *
//     * @param userName
//     * @param database
//     * @param password
//     * @return
//     */
//    public static MongoCredential getMongoCredential(String userName, String database, String password) {
//        return MongoCredential.createCredential(userName, database, password.toCharArray());
//    }
//
//    /**
//     * 获取mongo客户端
//     *
//     * @param addresses
//     * @param credentials
//     * @return
//     */
//    public static MongoClient getMongoClient(List<ServerAddress> addresses, List<MongoCredential> credentials) {
//        return new MongoClient(addresses, credentials);
//    }
//
//    /**
//     * 连接mongo数据库
//     *
//     * @param mongoClient
//     * @param databaseName
//     * @return
//     */
//    public static MongoDatabase getMongoDatabase(MongoClient mongoClient, String databaseName) {
//        return mongoClient.getDatabase(databaseName);
//    }
//
//    /**
//     * 连接mongo集
//     *
//     * @param mongoDatabase
//     * @param collectionName
//     * @return
//     */
//    public static MongoCollection<Document> getMongoCollection(MongoDatabase mongoDatabase, String collectionName) {
//        return mongoDatabase.getCollection(collectionName);
//    }
//
//    /**
//     * 关闭数据库连接
//     *
//     * @param mongoClient
//     */
//    public static void over(MongoClient mongoClient) {
//        mongoClient.close();
//    }
//
//    /**
//     * 获取mongo客户端对象，通过servers和credentials对象创建
//     *
//     * @param mongoObject
//     * @return
//     */
//    public static MongoClient getMongoClient(MongoObject mongoObject) {
//        MongoClient mongoClient = new MongoClient(getServers(getServerAdress(mongoObject.host, mongoObject.port)), getCredentials(getMongoCredential(mongoObject.user, mongoObject.database, mongoObject.password)));
//        return mongoClient;
//    }
//
//    /**
//     * 获取mongo客户端对象,通过uri方式连接
//     *
//     * @param mongoObject
//     * @return
//     */
//    public static MongoClient getMongoClientOnline(MongoObject mongoObject) {
//        String format = String.format("mongodb://%s:%s@%s:%d/%s", mongoObject.user, mongoObject.password, mongoObject.host, mongoObject.port, mongoObject.database);
//        return new MongoClient(new MongoClientURI(format));
//    }
//
//    /**
//     * 获取collection对象
//     *
//     * @param mongoObject
//     * @return
//     */
//    public static MongoCollection<Document> getCollection(MongoObject mongoObject, String collectionName) {
//        return getMongoClient(mongoObject).getDatabase(mongoObject.database).getCollection(collectionName);
//    }
//
//    /**
//     * 获取collection对象
//     *
//     * @param mongoObject
//     * @return
//     */
//    public static MongoCollection<Document> getCollectionOnline(MongoObject mongoObject, String collectionName) {
//        return getMongoClientOnline(mongoObject).getDatabase(mongoObject.database).getCollection(collectionName);
//    }

}
