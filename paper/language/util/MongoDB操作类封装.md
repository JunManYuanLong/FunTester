# MongoDB操作类封装
<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---

最近接到一个需求，要做MongoDB打点数据的统计，在学习过MongoDB的操作之后，封装了一个MongoDB的操作类，分为两部分，基本思想是参照了自己写过的mysql的操作类。一个是基本的操作类，包括所有基本操作的静态方法，还有一个是mongoobject，就是具体操作的实现类。

以后再写如何用spring boot写一个简单的统计服务。

mongobase代码如下：

```
package com.fun.mongodb;
 
import com.fun.frame.SourceCode;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
 
import java.util.Arrays;
import java.util.List;
 
/**
 * mongo操作类的基础类
 */
public class MongoBase extends SourceCode {
 
    /**
     * 获取服务地址list
     *
     * @param addresses
     * @return
     */
    public static List<ServerAddress> getServers(ServerAddress... addresses) {
        return Arrays.asList(addresses);
    }
 
    /**
     * 获取服务地址
     *
     * @param host
     * @param port
     * @return
     */
    public static ServerAddress getServerAdress(String host, int port) {
        return new ServerAddress(host, port);
    }
 
    /**
     * 获取认证list
     *
     * @param credentials
     * @return
     */
    public static List<MongoCredential> getCredentials(MongoCredential... credentials) {
        return Arrays.asList(credentials);
    }
 
    /**
     * 获取验证
     *
     * @param userName
     * @param database
     * @param password
     * @return
     */
    public static MongoCredential getMongoCredential(String userName, String database, String password) {
        return MongoCredential.createCredential(userName, database, password.toCharArray());
    }
 
    /**
     * 获取mongo客户端
     *
     * @param addresses
     * @param credentials
     * @return
     */
    public static MongoClient getMongoClient(List<ServerAddress> addresses, List<MongoCredential> credentials) {
        return new MongoClient(addresses, credentials);
    }
 
    /**
     * 连接mongo数据库
     *
     * @param mongoClient
     * @param databaseName
     * @return
     */
    public static MongoDatabase getMongoDatabase(MongoClient mongoClient, String databaseName) {
        return mongoClient.getDatabase(databaseName);
    }
 
    /**
     * 连接mongo集
     *
     * @param mongoDatabase
     * @param collectionName
     * @return
     */
    public static MongoCollection<Document> getMongoCollection(MongoDatabase mongoDatabase, String collectionName) {
        return mongoDatabase.getCollection(collectionName);
    }
 
    /**
     * 关闭数据库连接
     *
     * @param mongoClient
     */
    public static void MongoOver(MongoClient mongoClient) {
        mongoClient.close();
    }
 
    /**
     * 获取mongo客户端对象，通过servers和credentials对象创建
     *
     * @param mongoObject
     * @return
     */
    public static MongoClient getMongoClient(MongoObject mongoObject) {
        MongoClient mongoClient = new MongoClient(getServers(getServerAdress(mongoObject.host, mongoObject.port)), getCredentials(getMongoCredential(mongoObject.user, mongoObject.database, mongoObject.password)));
        return mongoClient;
    }
 
    /**
     * 获取mongo客户端对象,通过uri方式连接
     *
     * @param mongoObject
     * @return
     */
    public static MongoClient getMongoClientOnline(MongoObject mongoObject) {
        String format = String.format("mongodb://%s:%s@%s:%d/%s", mongoObject.user, mongoObject.password, mongoObject.host, mongoObject.port, mongoObject.database);
        return new MongoClient(new MongoClientURI(format));
    }
 
    /**
     * 获取collection对象
     *
     * @param mongoObject
     * @return
     */
    public static MongoCollection<Document> getCollection(MongoObject mongoObject, String collectionName) {
        return getMongoClient(mongoObject).getDatabase(mongoObject.database).getCollection(collectionName);
    }
 
    /**
     * 获取collection对象
     *
     * @param mongoObject
     * @return
     */
    public static MongoCollection<Document> getCollectionOnline(MongoObject mongoObject, String collectionName) {
        return getMongoClientOnline(mongoObject).getDatabase(mongoObject.database).getCollection(collectionName);
    }
 
}
```
mongoobject的代码如下：


```
package com.fun.mongodb;
 
 
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
 
/**
 * mongo数据库配置对象，针对单个数据服务，单个身份验证
 */
public class MongoObject extends MongoBase {
 
    String host;
 
    int port;
 
    String user;
 
    String password;
 
    String database;
 
    MongoClient mongoClient;
 
    /**
     * 创建数据连接
     *
     * @param host
     * @param port
     * @param user
     * @param password
     * @param database
     */
    public MongoObject(String host, int port, String user, String password, String database) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.database = database;
        this.mongoClient = getMongoClient(this);
    }
 
    /**
     * 创建数据库连接
     *
     * @param port
     * @param host
     * @param user
     * @param password
     * @param database
     */
    public MongoObject(int port, String host, String user, String password, String database) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.database = database;
        this.mongoClient = getMongoClientOnline(this);
    }
 
    /**
     * 获取colletion对象
     *
     * @param collectionName
     * @return
     */
    public MongoCollection<Document> getMongoCollection(String collectionName) {
        MongoClient mongoClientOnline = getMongoClientOnline(this);
        return mongoClientOnline.getDatabase(database).getCollection(collectionName);
    }
 
 
    /**
     * 关闭连接
     */
    public void over() {
        MongoOver(this.mongoClient);
    }
}
 @Override
    public MongoObject clone() {
        return new MongoObject(this.host, this.port, this.user, this.password, this.database);
    }

    public MongoObject clone2() {
        return new MongoObject(this.port, this.host, this.user, this.password, this.database);
    }
```
具体效果非常不错，测试代码如下：


```
  public static void main(String[] args) {
        MongoObject ready = new MongoObject("*****", 5117, "fission_record", "fission_record", "fission_record");
        MongoCollection<Document> app = ready.getMongoCollection("app_logs_20181109");
        Document first = app.find().first();
        output(first);
        ready.over();
    }
```

打印内容如下：

> Document{{_id=5be4ce052ce01b21b6c26a64, _class=com.fission.next.record.bean.AppRecordBean, user_id=5482, action_type={"gameId":2,"userId":"5482"}, action_extern=DataSta_Game_Starts, client_version=15, client_ip=114.5.146.239, client_imei=UNKNOWN, client_dev=xiaomi-Redmi 5 Plus, client_type=200, server_time=1541721601655, os_name=200, os_version=15, client_time=1541721577025}}

[一起来~FunTester](https://gitee.com/fanapi/tester/blob/okay/readme.markdown)

<br></br>
> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)


<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---


<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>