package com.funtester.httpclient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.funtester.base.exception.FailException;
import com.funtester.base.exception.ParamException;
import com.funtester.base.interfaces.IBase;
import com.funtester.config.Constant;
import com.funtester.config.HttpClientConstant;
import com.funtester.frame.SourceCode;
import com.funtester.utils.DecodeEncode;
import com.funtester.utils.Regex;
import com.funtester.utils.Time;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * 请求相关类，采用统一的静态方法，在登录后台管理页面是自动化设置cookie，其他公参由各自的base类实现header
 */
public class FunHttp extends SourceCode {

    private static Logger logger = LogManager.getLogger(FunHttp.class);

    /**
     * 打印日志的key
     */
    public static boolean LOG_KEY = true;

    /**
     * 是否需要处理响应头
     */
    public static boolean HEADER_HANDLE = false;

    /**
     * 异步请求打印日志的callback
     */
    public static final FutureCallback<HttpResponse> logCallback = new FutureCallback<HttpResponse>() {
        @Override
        public void completed(HttpResponse httpResponse) {
            HttpEntity entity = httpResponse.getEntity();
            String content = getContent(entity);
            logger.info("响应结果:{}", content);
        }

        @Override
        public void failed(Exception e) {
            logger.warn("响应失败", e);
        }

        @Override
        public void cancelled() {
            logger.warn("取消执行");
        }
    };

    /**
     * 方法已重载，获取{@link HttpGet}对象
     * <p>方法重载，主要区别参数，会自动进行urlencode操作</p>
     *
     * @param url  表示请求地址
     * @param args 表示传入数据
     * @return 返回get对象
     */
    public static HttpGet getHttpGet(String url, JSONObject args) {
        if (args == null || args.isEmpty()) return getHttpGet(url);
        String uri = url + changeJsonToArguments(args);
        return getHttpGet(uri);
    }

    /**
     * 获取{@link HttpGet},body携带请求参数,ES查询使用
     *
     * @param url
     * @param params
     * @return
     */
    public static HttpGetByBody getHttpGetWithBody(String url, JSONObject params) {
        HttpGetByBody httpGetByBody = new HttpGetByBody(url);
        if (params == null || params.isEmpty()) return httpGetByBody;
        httpGetByBody.setEntity(new StringEntity(params.toString(), DEFAULT_CHARSET.toString()));
        httpGetByBody.addHeader(HttpClientConstant.ContentType_JSON);
        return httpGetByBody;
    }

    /**
     * 方法已重载，获取{@link HttpGet}对象
     * <p>方法重载，主要区别参数，会自动进行urlencode操作</p>
     *
     * @param url 表示请求地址
     * @return 返回get对象
     */
    public static HttpGet getHttpGet(String url) {
        return new HttpGet(url);
    }

    /**
     * 获取{@link HttpPost}对象，以form表单提交数据
     * <p>方法重载，文字信息form表单提交，文件信息二进制流提交，具体参照文件上传的方法主食，post请求可以不需要参数，暂时不支持其他参数类型，如果是公参需要在url里面展示，需要传一个json对象，一般默认args为get公参，params为post请求参数</p>
     * 请求header参数类型为{@link HttpClientConstant#ContentType_FORM}
     *
     * @param url    请求地址
     * @param params 请求数据，form表单形式设置请求实体
     * @return 返回post对象
     */
    public static HttpPost getHttpPost(String url, JSONObject params) {
        HttpPost httpPost = getHttpPost(url);
        if (params != null && !params.isEmpty()) setFormHttpEntity(httpPost, params);
        httpPost.addHeader(HttpClientConstant.ContentType_FORM);
        return httpPost;
    }

    /**
     * 获取{@link HttpPost}对象，没有参数设置
     * <p>方法重载，文字信息form表单提交，文件信息二进制流提交，具体参照文件上传的方法主食，post请求可以不需要参数，暂时不支持其他参数类型，如果是公参需要在url里面展示，需要传一个json对象，一般默认args为get公参，params为post请求参数</p>
     *
     * @param url
     * @return
     */
    public static HttpPost getHttpPost(String url) {
        return new HttpPost(url);
    }

    /**
     * 获取{@link HttpPost}对象，{@link JSONObject}格式对象，传参时手动{@link JSONObject#toString()}方法,现在大多数情况下由{@link IBase}项目基础类完成
     * <p>新重载方法，适应{@link HttpPost}请求{@link JSONObject}传参，默认{@link Constant#DEFAULT_CHARSET}编码格式</p>
     * 请求header参数类型为{@link HttpClientConstant#ContentType_JSON}
     *
     * @param url
     * @param params
     * @return
     */
    public static HttpPost getHttpPost(String url, String params) {
        HttpPost httpPost = getHttpPost(url);
        if (StringUtils.isNotBlank(params))
            httpPost.setEntity(new StringEntity(params, DEFAULT_CHARSET.toString()));
        httpPost.addHeader(HttpClientConstant.ContentType_JSON);
        return httpPost;
    }

    /**
     * 获取 {@link HttpPost} 对象
     * <p>方法重载，文字信息{@link HttpClientConstant#ContentType_FORM}表单提交，文件信息二进制流提交，具体参照文件上传的方法主食，post请求可以不需要参数，暂时不支持其他参数类型，如果是公参需要在url里面展示，需要传一个{@link JSONObject}对象，一般默认args为{@link HttpGet}公参，params为{@link HttpPost}请求参数</p>
     *
     * @param url    请求地址
     * @param params 请求参数，其中二进制流必须是 file
     * @param file   文件
     * @return
     */
    public static HttpPost getHttpPost(String url, JSONObject params, File file) {
        if (file == null || !file.exists() || file.isDirectory())
            FailException.fail("file is not exists or file is directory");
        HttpPost httpPost = getHttpPost(url);
        if (params != null && !params.isEmpty()) setMultipartEntityEntity(httpPost, params, file);
//        httpPost.addHeader(HttpClientConstant.ContentType_FORM);//会自动处理
        return httpPost;
    }

    /**
     * 获取{@link HttpPut}请求,{@link JSONObject}传参格式
     *
     * @param url
     * @param params
     * @return
     */
    public static HttpPut getHttpPut(String url, String params) {
        HttpPut httpPut = getHttpPut(url);
        if (StringUtils.isNotBlank(params))
            httpPut.setEntity(new StringEntity(params, DEFAULT_CHARSET.toString()));
        httpPut.addHeader(HttpClientConstant.ContentType_JSON);
        return httpPut;
    }

    /**
     * 获取{@link HttpPut}请求,{@link JSONObject}表单格式
     *
     * @param url
     * @param params
     * @return
     */
    public static HttpPut getHttpPut(String url, JSONObject params) {
        HttpPut httpPut = getHttpPut(url);
        if (params != null && !params.isEmpty())
            setFormHttpEntity(httpPut, params);
        httpPut.addHeader(HttpClientConstant.ContentType_FORM);
        return httpPut;
    }

    /**
     * 获取{@link HttpPut}请求对象
     *
     * @param url
     * @return
     */
    public static HttpPut getHttpPut(String url) {
        return new HttpPut(url);
    }

    /**
     * 获取{@link HttpDelete}对象
     *
     * @param url
     * @return
     */
    public static HttpDelete getHttpDelete(String url) {
        return new HttpDelete(url);
    }

    /**
     * 获取{@link HttpDeleteByBody}对象,delete请求携带body参数
     *
     * @param url
     * @param params
     * @return
     */
    public static HttpDeleteByBody getHttpDelete(String url, JSONObject params) {
        HttpDeleteByBody httpDeleteWithBody = new HttpDeleteByBody(url);
        httpDeleteWithBody.setEntity(new StringEntity(params.toString(), DEFAULT_CHARSET.toString()));
        httpDeleteWithBody.addHeader(HttpClientConstant.ContentType_JSON);
        return httpDeleteWithBody;
    }


    /**
     * 获取{@link HttpPatch}对象
     *
     * @param url
     * @return
     */
    public static HttpPatch getHttpPatch(String url) {
        return new HttpPatch(url);
    }

    /**
     * 获取{@link HttpPatch}对象
     *
     * @param url
     * @param params
     * @return
     */
    public static HttpPatch getHttpPatch(String url, JSONObject params) {
        HttpPatch httpPatch = getHttpPatch(url);
        if (params != null && !params.isEmpty())
            httpPatch.setEntity(new StringEntity(params.toString(), DEFAULT_CHARSET.toString()));
        httpPatch.addHeader(HttpClientConstant.ContentType_JSON);
        return httpPatch;
    }

    /**
     * 设置{@link HttpPost}接口上传表单，默认的编码格式
     * 默认编码格式{@link Constant#DEFAULT_CHARSET}
     *
     * @param request
     * @param params  参数
     */
    private static void setFormHttpEntity(HttpEntityEnclosingRequestBase request, JSONObject params) {
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        params.keySet().forEach(x -> formparams.add(new BasicNameValuePair(x, params.getString(x))));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, DEFAULT_CHARSET);
        request.setEntity(entity);
    }

    /**
     * 设置二进制流实体，params 里面参数值为 {@link HttpClientConstant#FILE_UPLOAD_KEY}
     *
     * @param request
     * @param params  请求参数
     * @param file    文件
     */
    private static void setMultipartEntityEntity(HttpEntityEnclosingRequestBase request, JSONObject params, File file) {
        logger.debug("上传文件名：{}", file.getAbsolutePath());
        String fileName = file.getName();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            logger.warn("读取文件失败！", e);
        }
        Iterator<String> keys = params.keySet().iterator();// 遍历 params 参数和值
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();// 新建MultipartEntityBuilder对象
        while (keys.hasNext()) {
            String key = keys.next();
            String value = params.getString(key);
            if (value.equalsIgnoreCase(HttpClientConstant.FILE_UPLOAD_KEY)) {
                builder.addBinaryBody(key, inputStream, ContentType.create(HttpClientConstant.CONTENTTYPE_MULTIPART_FORM), fileName);// 设置流参数
            } else {
                StringBody body = new StringBody(value, ContentType.create(HttpClientConstant.CONTENTTYPE_TEXT, DEFAULT_CHARSET));// 设置普通参数
                builder.addPart(key, body);
            }
        }
        HttpEntity entity = builder.build();
        request.setEntity(entity);
    }

    /**
     * 响应结束之后，处理响应头信息，如set-cookien内容
     *
     * @param response 响应内容
     * @return
     */
    private static JSONObject afterResponse(CloseableHttpResponse response) {
        if (!HEADER_HANDLE) return null;
        Header[] allHeaders = response.getAllHeaders();
        JSONObject hs = new JSONObject();
        JSONObject cookie = new JSONObject();
        for (int i = 0; i < allHeaders.length; i++) {
            Header header = allHeaders[i];
            if (header.getName().equals(HttpClientConstant.SET_COOKIE)) {
                String[] split = header.getValue().split(EQUAL, 2);
                cookie.put(split[0], split[1]);
                continue;
            }
            hs.compute(header.getName(), (x, y) -> {
                if (y == null) {
                    return header.getValue();
                } else {
                    return hs.getString(header.getName()) + PART + header.getValue();
                }
            });
        }
        if (!cookie.isEmpty()) hs.put(HttpClientConstant.COOKIE, cookie);
        return hs;
    }

    /**
     * 根据解析好的content，转化{@link JSONObject}对象
     *
     * @param content
     * @return
     */
    private static JSONObject getJsonResponse(String content, JSONObject headers) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (StringUtils.isBlank(content)) ParamException.fail("响应为空!");
            jsonObject = JSONObject.parseObject(content);
        } catch (JSONException e) {
            jsonObject = new JSONObject() {{
                put(RESPONSE_CONTENT, content);
            }};
        } finally {
            if (headers != null && !headers.isEmpty()) jsonObject.put(HttpClientConstant.HEADERS, headers);
            return jsonObject;
        }
    }


    /**
     * 解析{@link HttpEntity},不区分请求还是响应
     *
     * @param entity
     * @return
     */
    public static String getContent(HttpEntity entity) {
        String content = EMPTY;
        try {
            if (entity != null) content = EntityUtils.toString(entity, DEFAULT_CHARSET);// 用string接收响应实体
            EntityUtils.consume(entity);// 消耗响应实体，并关闭相关资源占用
        } catch (Exception e) {
            logger.warn("解析响应实体异常！", e);
        }
        return content;
    }

    /**
     * 获取响应状态，暂不处理{@link HttpStatus#SC_MOVED_TEMPORARILY}
     *
     * @param response
     * @param res
     * @return
     */
    public static int getStatus(CloseableHttpResponse response, JSONObject res) {
        int status = response.getStatusLine().getStatusCode();
//        if (status == HttpStatus.SC_MOVED_TEMPORARILY) {
//            res.put("location", response.getFirstHeader("Location").getValue());
//        }
        res.put(DEFAULT_STRING, status);
        return status;
    }

    /**
     * 获取响应实体
     * <p>会自动设置cookie，但是需要各个项目再自行实现cookie管理</p>
     * <p>该方法只会处理文本信息，对于文件处理可以调用两个过期的方法解决</p>
     *
     * @param request 请求对象
     * @return 返回json类型的对象
     */
    public static JSONObject getHttpResponse(HttpRequestBase request) {
        JSONObject res = new JSONObject();
        long start = 0l;
        if (LOG_KEY) start = Time.getTimeStamp();
        try (CloseableHttpResponse response = ClientManage.httpsClient.execute(request)) {
            res.putAll(getJsonResponse(getContent(response.getEntity()), afterResponse(response)));
            int status = getStatus(response, res);
            if (LOG_KEY)
                logger.info("请求uri：{} , 耗时：{} ms , HTTPcode: {}", request.getURI(), Time.getTimeStamp() - start, status, res);
        } catch (Exception e) {
            res.put(EXCEPTION, e.getMessage());
            FunRequest funRequest = FunRequest.initFromRequest(request);
            funRequest.setResponse(res);
            logger.warn("请求失败 {} ,内容:{} ", e.getMessage(), funRequest.toString());
        }
        return res;
    }

    /**
     * 解析response，使用char数组，注意编码格式
     * <p>自定义解析响应实体的方法，暂不采用</p>
     *
     * @param response 传入的response，非closedresponse
     * @return string类型的response
     */
    @Deprecated
    private static String parseResponeEntityByChar(HttpResponse response) {
        StringBuffer buffer = new StringBuffer();// 创建并实例化stringbuffer，存放响应信息
        try (InputStream input = response.getEntity().getContent(); InputStreamReader reader = new InputStreamReader(input, DEFAULT_CHARSET)) {
            char[] buff = new char[1024];// 创建并实例化字符数组
            int length = 0;// 声明变量length，表示读取长度
            while ((length = reader.read(buff)) != -1) {// 循环读取字符输入流
                String x = new String(buff, 0, length);// 获取读取到的有效内容
                buffer.append(x);// 将读取到的内容添加到stringbuffer中
            }
        } catch (IOException e) {
            logger.warn("解析响应实体失败！", e);
        }
        return buffer.toString();
    }

    /**
     * 从响应解析到文件
     *
     * @param response
     * @param file
     */
    @Deprecated
    private static void parseResponeByFile(HttpResponse response, File file) {
        int bytesum = 0;// 这个用来统计需要写入byte数组的长度
        int byteread = 0;// 这个用来接收read()方法的返回值，表示读取内容的长度
        try (InputStream inputStream = response.getEntity().getContent(); FileOutputStream fileOutputStream = new FileOutputStream(file);) {
            byte[] buffer = new byte[1024];// 新建读取文件所用的数组
            // 此处用while循环每次按buffer读取文件直到读取完成
            while ((byteread = inputStream.read(buffer)) != -1) {// 如何读取到文件末尾
                bytesum += byteread;// 此处计算读取长度，byteread表示每次读取的长度
                fileOutputStream.write(buffer, 0, byteread);// 此方法第一个参数是byte数组，第二次参数是开始位置，第三个参数是长度
            }
        } catch (IOException e) {
            logger.warn("解析响应实体失败！", e);
        }
    }

    /**
     * 把json数据转化为参数，为get请求和post请求stringentity的时候使用
     *
     * @param argument 请求参数，json数据类型，map类型，可转化
     * @return 返回拼接参数后的地址
     */
    public static String changeJsonToArguments(JSONObject argument) {
        return argument == null || argument.isEmpty() ? EMPTY : argument.keySet().stream().filter(x -> argument.get(x) != null).map(x -> x.toString() + EQUAL + DecodeEncode.urlEncoderText(argument.getString(x.toString()))).collect(Collectors.joining("&", UNKNOW, EMPTY)).toString();
    }

    /**
     * 通过json对象信息，生成cookie的header
     *
     * @param cookies
     * @return
     */
    public static Header getCookies(JSONObject cookies) {
        return getHeader(HttpClientConstant.COOKIE, cookies == null || cookies.isEmpty() ? EMPTY : cookies.keySet().stream().map(x -> x.toString() + EQUAL + cookies.get(x).toString()).collect(Collectors.joining(";")).toString());
    }

    /**
     * 生成header
     *
     * @param name
     * @param value
     * @return
     */
    public static Header getHeader(String name, String value) {
        return new BasicHeader(name, value);
    }

    /**
     * 将header转成json对象
     *
     * @param headers
     * @return
     */
    public static JSONObject header2Json(List<Header> headers) {
        return new JSONObject() {{
            headers.forEach(x -> put(x.getName(), x.getValue()));
        }};
    }

    /**
     * 简单发送请求,此处不用{@link CloseableHttpResponse#close()}也能释放连接
     *
     * @param request
     */
    public static String executeSimlple(HttpRequestBase request) throws IOException {
        CloseableHttpResponse response = ClientManage.httpsClient.execute(request);
        return getContent(response.getEntity());
    }

    /**
     * 只发送要求,不解析响应
     * 此处不用{@link CloseableHttpResponse#close()}也能释放连接
     *
     * @param request
     * @throws IOException
     */
    public static void executeOnly(HttpRequestBase request) throws IOException {
        CloseableHttpResponse response = ClientManage.httpsClient.execute(request);
        EntityUtils.consume(response.getEntity());// 消耗响应实体，并关闭相关资源占用
    }

    /**
     * 设置代理请求
     *
     * @param request
     * @param adress
     */
    public static void setProxy(HttpRequestBase request, String adress) {
        request.setConfig(getProxyConfig(adress));
    }

    /**
     * 设置代理请求
     *
     * @param request
     * @param ip
     * @param port
     */
    public static void setProxy(HttpRequestBase request, String ip, int port) {
        setProxy(request, ip + ":" + port);
    }

    /**
     * 通过IP和端口获取代理配置对象
     *
     * @param adress
     * @return
     */
    public static RequestConfig getProxyConfig(String adress) {
        if (StringUtils.isBlank(adress) || !Regex.isMatch(adress, Constant.HOST_REGEX))
            ParamException.fail("adress格式错误:" + adress);
        String[] split = adress.split(":", 2);
        return ClientManage.getProxyRequestConfig(split[0], changeStringToInt(split[1]));
    }

    /**
     * 异步发送请求
     *
     * @param request
     */
    public static Future<HttpResponse> executeSync(HttpRequestBase request) {
        return executeSync(request, null);
    }

    /**
     * 异步发送请求获取响应Demo
     * <p>经过测试没卵用</p>
     *
     * @param request
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static JSONObject executeSyncWithResponse(HttpRequestBase request) {
        Future<HttpResponse> execute = executeSync(request, null);
        try {
            HttpResponse response = execute.get();
            String content = getContent(response.getEntity());
            return getJsonResponse(content, null);
        } catch (Exception e) {
            logger.error("异步请求获取响应失败!", e);
        }
        return new JSONObject();
    }

    /**
     * 异步请求,打印日志
     *
     * @param request
     */
    public static Future executeSyncWithLog(HttpRequestBase request) {
        return executeSync(request, logCallback);
    }

    /**
     * 异步请求,返回响应,引入第二个参数{@link JSONObject}
     *
     * @param request
     * @param response
     */
    public static Future executeSyncWithResponse(HttpRequestBase request, JSONObject response) {
        return executeSync(request, new FunTester(response));
    }

    /**
     * 异步执行
     *
     * @param request
     * @param callback
     */
    public static Future<HttpResponse> executeSync(HttpRequestBase request, FutureCallback callback) {
        return ClientManage.httpAsyncClient.execute(request, callback);
    }

    /**
     * 异步请求,异步解析响应的FutureCallback实现类
     */
    private static class FunTester implements FutureCallback<HttpResponse> {

        public FunTester(JSONObject response) {
            this.response = response;
        }

        JSONObject response;

        @Override
        public void completed(HttpResponse result) {
            HttpEntity entity = result.getEntity();
            String content = getContent(entity);
            response = JSON.parseObject(content);
        }

        @Override
        public void failed(Exception e) {
            logger.warn("响应失败", e);
        }

        @Override
        public void cancelled() {
            logger.warn("取消执行");
        }

    }

    /**
     * 结束测试，关闭连接池
     */
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                ClientManage.httpsClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                ClientManage.httpAsyncClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

    }

    /**
     * 初始化连接池和各类管理器
     *
     * @param timeout
     * @param accepttime
     * @param retrytimes
     */
    public synchronized static void init(int timeout, int accepttime, int retrytimes) {
        ClientManage.init(timeout, accepttime, retrytimes, null, 0);
    }

    public synchronized static void init(int timeout, int accepttime, int retrytimes, String ip, int port) {
        ClientManage.init(timeout, accepttime, retrytimes, ip, port);
    }


}