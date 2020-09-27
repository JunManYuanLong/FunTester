# 如何在Linux命令行界面愉快进行性能测试


本人在做性能测试的过程中，遇到一个问题，测试机选了一台Linux服务器，只有命令行界面。执行测试用例不是非常的灵活，有时候我需要改一两个参数添加一些日志，都需要重新打包部署，虽然自动化构建比较方便，但感觉绕了一大圈，在经过一些简单尝试之后做好了两个方案，一个是针对单接口的压测，以配置文件形式完成每一个request的组装，然后通过调节并发的参数执行不同的测试用例，且支持多个请求一起压测；另外一个以groovy脚本形式执行用例，则需要在服务器上配置好groovy环境以及把项目打包后的jar包推送到groovy的lib目录下。

### 方案一：

从文本中读取request组装：

首先从文本读取组装request的类：


```
package com.fun.utils.request;

import com.fun.frame.SourceCode;
import com.fun.httpclient.FanLibrary;
import com.fun.profile.Constant;
import com.fun.utils.WriteRead;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpRequestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 从文件中读取接口相关参数，用来发送请求，实现接口请求的配置化
 * <p>从当前路径下获取后缀为.log的文件，以文件名为准读取文件内容</p>
 */
public class RequestFile extends SourceCode {

    private static Logger logger = LoggerFactory.getLogger(RequestFile.class);

    String url;

    /**
     * get对应get请求，post对应post请求表单参数，其他对应post请求json参数
     */
    JSONObject headers;

    String requestType;

    String name;

    JSONObject info;

    JSONObject params;

    /**
     * @param name
     */
    public RequestFile(String name) {
        this.name = name;
        getInfo();
        this.url = this.info.getString("url");
        requestType = this.info.getString("requestType");
        getParams();
        headers = JSONObject.fromObject(this.info.getString("headers"));
    }

    /**
     * 获取当前目录下的配置文件，以数字开头，后缀是.log的
     *
     * @param i
     */
    public RequestFile(int i) {
        this(i + Constant.EMPTY);
    }

    /**
     * 从配置文件中读取信息，组成一个json对象
     */
    private void getInfo() {
        String filePath = Constant.WORK_SPACE + name;
        logger.info("配置文件地址：" + filePath);
        this.info = WriteRead.readTxtByJson(filePath);
    }

    /**
     * 获取请求参数
     */
    private void getParams() {
        params = JSONObject.fromObject(info.getString("params"));
    }


    /**
     * 根据info组成请求
     *
     * @return
     */
    public HttpRequestBase getRequest() {
        HttpRequestBase requestBase = requestType.equalsIgnoreCase(Constant.REQUEST_TYPE_POST) ? FanLibrary.getHttpPost(this.url, this.params) : requestType.equalsIgnoreCase(Constant.REQUEST_TYPE_GET) ? FanLibrary.getHttpGet(this.url, this.params) : FanLibrary.getHttpPost(this.url, this.params.toString());
        FanLibrary.addHeaders(requestBase, headers);
        FanLibrary.setHeaderKey();
        output(FanLibrary.getHttpResponse(requestBase));
        return requestBase;
    }

}
```

然后是通过main方法实现参数化：

```
class PerformanceFromFile extends SourceCode {
    public static void main(String[] args) {
        MySqlTest.setFlag();
        def size = args.size();
        List<HttpRequestBase> list = new ArrayList<>()
        for (int i = 0; i < size - 1; i += 2) {
            def name = args[i]
            int thread = changeStringToInt(args[i + 1])
            def request = new RequestFile(name).getRequest()
            for (int j = 0; j < thread; j++) {
                list.add(request)
            }
        }
        int perTimes = changeStringToInt(args[size - 1])
        def concurrent = new Concurrent(list, perTimes)
        concurrent.start()
        FanLibrary.testOver()
    }
}

```

* 这里就不放Concurrent类的方法了，有兴趣的同学可以去翻一翻之前的文章。

执行用例的命令行：
`java -jar performance.jar test 10 login 10 1000`

解释一下，test脚本的请求分配10个线程，login脚本请求分配10个线程，每个线程执行1000次请求。下面是test的内容：

>url=http://127.0.0.1:8050/api/pad/user/login
requestType=peost
params={"uname":"81951375115","pwd":"QJ81KU2LV6z1X4nA+czzvqVZVDsQnjOIKt857kEbemcs/SJW8GXL+sjOcemH5GFIm6rKKpqIOrqp1z0DUig/9QJouhBp1OQnZbNlkXSS84+IOQS022kbsN9e51r+GeyZDCrr7WWLenZJcyIE1BRrMeq1EkWCBotzwegXUJjR6Qs="}
headers={requestId:88888888}

### 方案二：
这个就比较简单了，首先在服务器上配置好groovy环境，然后把接口功能测试和自动化测试项目的打包jar放到groovy的lib目录下即可。这里用到了Jenkins自动化构建，在后置脚本中增加一行mv或者cp文件的shell即可。

然后在服务器上新建一个目录存放groovy脚本，下面放一个test脚本内容：


```
import com.fun.httpclient.FanLibrary
import com.okayqa.studentapd.base.OkayBase
import com.fun.frame.excute.Concurrent

class T8 extends OkayBase{
    public static void main(String[] args) {
        def base = getBase()
        output(base.getLoginResponse())
        def get = FanLibrary.requests.get(FanLibrary.requests.size() - 1)
      //  new Concurrent(get,10,100).start()
        FanLibrary.testOver()
    }
}
```
然后在服务器上通过vim就可以灵活编辑脚本，执行不同的用例了，包括打点日志什么都是没有问题的。

执行方法：
`groovy test`

