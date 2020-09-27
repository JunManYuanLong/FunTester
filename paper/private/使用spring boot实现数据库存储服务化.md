# 使用spring boot实现数据库存储服务化
<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---
2018年09月06日更新：

修复bug，8.22中代码已更新

-------------------------分割线-------------------------

2018年08月22日更新：

更新内容：添加通用的数据库存储类，添加了根据等待任务数动态创建辅助线程的功能。

两个类代码如下：

```
package com.fission.source.mysql;
 
import com.fission.source.profile.SqlConstant;
 
/**
 * mysql辅助线程，当任务数太满的时候启用
 */
public class AidThread extends Thread {
    @Override
    public void run() {
        MySqlObject.threadNum.incrementAndGet();
        MySqlObject object = new MySqlObject();
        while (true) {
            if (MySqlTest.sqls.size() < SqlConstant.MYSQL_MAX_WAIT_WORK) break;
            String sql = MySqlTest.getWork();
            if (sql == null) break;
            object.excuteUpdateSql(sql);
        }
        MySqlObject.threadNum.decrementAndGet();
        object.close ();
    }
}
```


```
package com.fission.source.mysql;
 
import com.fission.source.profile.SqlConstant;
import com.fission.source.source.Output;
import com.fission.source.source.SourceCode;
 
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicInteger;
 
/**
 * 辅助线程，处理sql任务
 */
public class MySqlObject {
    /**
     * 标记多少辅助线程存活数量
     */
    public static AtomicInteger threadNum = new AtomicInteger(0);
    /**
     * 标记连接使用
     */
    int updateTime;
    Connection connection;
    Statement statement;
 
    /**
     * 初始化连接方法
     */
    public MySqlObject() {
        getConnection();
    }
 
    /**
     * 获取当前辅助线程数
     *
     * @return
     */
    public static int getThreadNum() {
        return threadNum.get();
    }
 
    /**
     * 更新连接使用标记
     */
    void updateLastUpdate() {
        updateTime = SourceCode.getMark();
    }
 
    /**
     * 执行sql方法
     *
     * @param sql
     */
    void excuteUpdateSql(String sql) {
        getConnection();
        SqlBasic.excuteUpdateSql(connection, statement, sql);
        updateLastUpdate();
    }
 
    /**
     * 获取数据库连接
     */
    void getConnection() {
        try {
            if (SourceCode.getMark() - updateTime > SqlConstant.MYSQL_RECONNECTION_GAP || connection == null || connection.isClosed()) {
                connection = TestConnectionManage.getConnection(SqlConstant.TEST_SQL_URL, SqlConstant.TEST_USER, SqlConstant.TEST_PASS_WORD);
                statement = TestConnectionManage.getStatement(connection);
            }
        } catch (SQLException e) {
            Output.output("数据库连接获取失败！", e);
        } finally {
            updateLastUpdate();
        }
    }
 
    /**
     * 关闭对象方法
     */
    void close() {
        SqlBasic.mySqlOver(connection, statement);
    }
}
```
-----------------------------分割线--------------------------------

本人在设计接口测试框架的过程中，把各类的数据和记录都存放在数据库里面，这也导致了存储数据花费时间挺长的，在学习了spring boot框架之后，决定将数据库存储功能服务化。

思路如下：把需要执行的sql发送到一个服务里的消息队列里面，用线程安全的方法存取往数据库里面执行。

正常执行一次sql插入，差不多150ms左右，发送一次请求，10ms以内，这下子能降低很多执行时间，而且对于其他使用者来说，也是非常方便。数据库存储的线程是我自己写的，并没有使用spring boot的线程池，一来是因为测试需求不大，二来趁机练练手，学习一下队列、数据库、多线程的使用。主要原因是需要兼容功能测试框架，功能测试并不是依赖spring boot。

下面分享一下自己spring boot的代码：


```
@Controller
public class HelloController extends SourceCode {
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(HelloController.class);
	@Autowired
	private HttpServletRequest request;
 
	@RequestMapping(value = "/",method = RequestMethod.GET)
	@ResponseBody
	String home() {
		return "数据库存储服务启动了！";
	}
 
 
	@RequestMapping(value = "/mysql",method = RequestMethod.GET)
	@ResponseBody
	String update(@RequestParam("sql") String sql) {
		String sql1 = com.fission.source.source.SourceCode.urlDecoderText(sql);
		long a = System.currentTimeMillis();
		String ip = getIpAddr(request);
		MySqlTest.addWork(sql1);
		return "添加数据库任务成功！";
	}
}
```
下面是服务启动时执行的startrun类：


```
@Component
public class StartRun implements CommandLineRunner {
	@Override
	public void run(String... args) throws Exception {
		TestConnectionManage.getUpdateConnection1();
		TestConnectionManage.getUpdateConnection2();
		TestConnectionManage.start();
	}
}
```
下面是框架里面的执行类:


```
package com.fission.source.mysql;
 
import com.fission.source.httpclient.ApiLibrary;
import com.fission.source.httpclient.RequestInfo;
import com.fission.source.profile.SqlConstant;
import com.fission.source.source.SourceCode;
import com.fission.source.utils.AlertOver;
import net.sf.json.JSONObject;
 
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
 
/**
 * 数据库读写类
 *
 * @Author [Vicky]
 * @Description
 * @UpdateRemark
 * @UpdateDate: [2018年1月10日 下午4:05:16]
 * @CreateDate: [2018年1月10日 下午4:05:16]
 */
public class MySqlTest extends SourceCode {
	/**
	 * 控台statement1和statement均衡
	 */
	static AtomicInteger key = new AtomicInteger(0);
 
	/**
	 * 存放数据库存储任务
	 */
	static LinkedBlockingDeque<String> sqls = new LinkedBlockingDeque<>();
 
	public static Connection getConnection0() {
		return connection0;
	}
 
	public static Statement getStatement0() {
		return statement0;
	}
 
	/**
	 * 用于查询
	 */
	static Connection connection0;
	/**
	 * 用于写入
	 */
	static Connection connection1;
	/**
	 * 用于写入
	 */
	static Connection connection2;
	static Statement statement0;
	static Statement statement1;
	static Statement statement2;
 
 
	/**
	 * 保存接口测试数据的方法
	 *
	 * @param host_name    host名字
	 * @param api_name     接口名称
	 * @param data_size    响应大小
	 * @param elapsed_time 响应时间
	 * @param status       响应状态码
	 * @param type         网络协议类型
	 * @param mark         标记
	 * @param method       请求方法
	 * @param code         响应code码
	 * @param loacalIp     本机ip
	 */
	private static void saveApiTestDate(String host_name, String api_name, long data_size, double elapsed_time, int status, String type, int mark, String method, int code, String loacalIp, String computerName) {
		if (isBlack(host_name)) return;
		String sql = String.format("INSERT INTO request_record (host_name,api_name,data_size,elapsed_time,status,type,mark, method,response_code,local_ip,computer_name) VALUES ('%s','%s',%d,%f,%d,'%s',%d,'%s',%d,'%s','%s');", host_name, api_name, data_size, elapsed_time, status, type, mark, method, code, loacalIp, computerName);
		sendWork(sql);
	}
 
	/**
	 * 新方法，报错requestinfo对象
	 *
	 * @param requestInfo
	 * @param data_size
	 * @param elapsed_time
	 * @param status
	 * @param mark
	 * @param code
	 * @param loacalIp
	 * @param computerName
	 */
	public static void saveApiTestDate(RequestInfo requestInfo, long data_size, double elapsed_time, int status, int mark, int code, String localIP, String computerName) {
		saveApiTestDate(requestInfo.getHost(), requestInfo.getApiName(), data_size, elapsed_time, status, requestInfo.getType(), mark, requestInfo.getMethod(), code, localIP, computerName);
	}
 
	/**
	 * 保存测试结果
	 *
	 * @param label  测试标记
	 * @param result 测试结果
	 */
	public static void saveTestResult(String label, JSONObject result) {
		String data = result.toString();
		Iterator<String> iterator = result.keys();
		int abc = 1;
		while (iterator.hasNext() && abc == 1) {
			String key = iterator.next().toString();
			String value = result.getString(key);
			if (value.equals("false")) abc = 2;
		}
		if (abc != 1) new AlertOver("用例失败！", label + "测试结果：" + abc + LINE + data).sendBusinessMessage();
		output(label + LINE + "测试结果：" + (abc == 1 ? "通过" : "失败") + LINE + data);
		String sql = String.format("INSERT INTO table (mark,test_mark,result,label,params,local_ip,computer_name) VALUES (%d,%d,%d,'%s','%s','%s','%s')", ApiLibrary.mark, ApiLibrary.test_mark, abc, label, data, LOCAL_IP, COMPUTER_USER_NAME);
		sql = sql.contains(NAJM_PACKAGE) ? sql.replace("table", NAJM_TABLE) : sql.contains(LAMMA_PACKAGE) ? sql.replace("table", LAMMA_TABLE) : sql.contains(READY_PACKAGE) ? sql.replace("table", READY_TABLE) : EMPTY;
		sendWork(sql);
	}
 
	/**
	 * 记录alertover警告
	 *
	 * @param requestInfo
	 * @param type
	 * @param title
	 * @param mark
	 * @param localIP
	 * @param computerName
	 */
	public static void saveAlertOverMessage(RequestInfo requestInfo, String type, String title, int mark, String localIP, String computerName) {
		String host_name = requestInfo.getHost();
		if (isBlack(host_name)) return;
		String sql = String.format("INSERT INTO alertover (type,title,host_name,api_name,local_ip,computer_name,mark) VALUES('%s','%s','%s','%s','%s','%s',%d);", type, title, host_name, requestInfo.getApiName(), localIP, computerName, mark);
		sendWork(sql);
	}
 
	/**
	 * 获取所有有效的用例类
	 *
	 * @return
	 */
	public static List<String> getAllCaseName() {
		List<String> list = new ArrayList<>();
		String sql = "SELECT *FROM case_class WHERE flag = 1 ORDER BY create_time DESC;";
		TestConnectionManage.getQueryConnection();
		ResultSet resultSet = SqlBasic.excuteQuerySql(connection0, statement0, sql);
		try {
			while (resultSet != null && resultSet.next()) {
				String className = resultSet.getString("class");
				list.add(className);
			}
		} catch (SQLException e) {
			output(sql, e);
		}
		return list;
	}
 
	/**
	 * 获取用例状态
	 *
	 * @param name
	 * @return
	 */
	public static boolean getCaseStatus(String name) {
		String sql = "SELECT flag FROM case_class WHERE class = \"" + name + "\";";
		TestConnectionManage.getQueryConnection();
		ResultSet resultSet = SqlBasic.excuteQuerySql(connection0, statement0, sql);
		try {
			if (resultSet != null && resultSet.next()) {
				int flag = resultSet.getInt(1);
				return flag == 1 ? true : false;
			}
		} catch (SQLException e) {
			output(sql, e);
		}
		return false;
	}
 
 
	/**
	 * 确保所有的储存任务都结束
	 */
	private static void check() {
		int size = sqls.size();
		while (sqls.size() != 0) {
			sleep(100);
		}
		TestConnectionManage.stopAllThread();
	}
 
	/**
	 * 执行sql语句，非query语句，并不关闭连接
	 *
	 * @param connection
	 * @param statement
	 * @param sql
	 */
	static void excuteUpdateSql(String sql, boolean key) {
		if (key) {
			TestConnectionManage.getUpdateConnection1();
			SqlBasic.excuteUpdateSql(connection1, statement1, sql);
			if (key) TestConnectionManage.updateLastUpdate1();
		} else {
			TestConnectionManage.getUpdateConnection2();
			SqlBasic.excuteUpdateSql(connection2, statement2, sql);
			TestConnectionManage.updateLastUpdate2();
		}
	}
 
	/**
	 * 发送数据库任务，暂时用请求服务器接口
	 *
	 * @param sql
	 * @return
	 */
	public static void sendWork(String sql) {
		if (sql == null || sql.isEmpty()) return;
		JSONObject argss = new JSONObject();
		argss.put("sql", sql);
		ApiLibrary.getHttpResponse(ApiLibrary.getHttpGet(SqlConstant.MYSQL_SERVER_PATH, argss));
	}
 
	/**
	 * 添加存储任务
	 *
	 * @param sql
	 * @return
	 */
	public static boolean addWork(String sql) {
		try {
			sqls.put(sql);
		} catch (InterruptedException e) {
			output("添加数据库存储任务失败！", e);
			return false;
		}
		return true;
	}
 
	/**
	 * 从任务池里面获取任务
	 *
	 * @return
	 */
	static String getWork() {
		String sql = null;
		try {
			sql = sqls.poll(SqlConstant.MYSQLWORK_TIMEOUT, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			output("获取存储任务失败！", e);
		} finally {
			return sql;
		}
	}
 
 
	/**
	 * 关闭数据库链接的方法，供外部使用
	 */
	public static void mySqlOver() {
		mySqlQueryOver();
	}
 
	/**
	 * 关闭update连接
	 */
	static void mySqlUpdateOver() {
		check();
		SqlBasic.mySqlOver(connection1, statement1);
		SqlBasic.mySqlOver(connection2, statement2);
	}
 
	/**
	 * 关闭query连接
	 */
	static void mySqlQueryOver() {
		SqlBasic.mySqlOver(connection0, statement0);
	}
 
}
```

> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---
<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>