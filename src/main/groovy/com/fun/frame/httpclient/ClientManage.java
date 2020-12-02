package com.fun.frame.httpclient;

import com.fun.base.exception.FailException;
import com.fun.config.Constant;
import com.fun.config.HttpClientConstant;
import com.fun.utils.Regex;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.CodingErrorAction;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

/**
 * 连接池管理类
 */
@SuppressFBWarnings({"MS_SHOULD_BE_FINAL", "MS_PKGPROTECT"})
public class ClientManage {

    private static Logger logger = LoggerFactory.getLogger(ClientManage.class);

    /**
     * ssl验证
     */
    private static SSLContext sslContext = createIgnoreVerifySSL();

    /**
     * 请求超时控制器
     */
    private static RequestConfig requestConfig = getRequestConfig();

    /**
     * 请求重试管理器
     */
    private static HttpRequestRetryHandler httpRequestRetryHandler = getHttpRequestRetryHandler();

    /**
     * 连接池
     */
    private static PoolingHttpClientConnectionManager connManager = getPool();

    /**
     * 异步连接池
     */
    private static PoolingNHttpClientConnectionManager NconnManager = getNPool();

    /**
     * httpclient对象
     */
    public static CloseableHttpClient httpsClient = getCloseableHttpsClients();

    /**
     * 异步连接池
     */
    public static CloseableHttpAsyncClient httpAsyncClient = getCloseableHttpAsyncClient();

    /**
     * 获取连接池
     *
     * @return
     */
    private static PoolingHttpClientConnectionManager getPool() {
        PoolingHttpClientConnectionManager connManager = null;
        // 采用绕过验证的方式处理https请求
        // 设置协议http和https对应的处理socket链接工厂的对象
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create().register("http", PlainConnectionSocketFactory.INSTANCE).register("https", new SSLConnectionSocketFactory(sslContext)).build();
        connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        // 消息约束
        MessageConstraints messageConstraints = MessageConstraints.custom().setMaxHeaderCount(HttpClientConstant.MAX_HEADER_COUNT).setMaxLineLength(HttpClientConstant.MAX_LINE_LENGTH).build();
        // 连接设置
        ConnectionConfig connectionConfig = ConnectionConfig.custom().setMalformedInputAction(CodingErrorAction.IGNORE).setUnmappableInputAction(CodingErrorAction.IGNORE).setCharset(Constant.DEFAULT_CHARSET).setMessageConstraints(messageConstraints).build();
        connManager.setDefaultConnectionConfig(connectionConfig);
        connManager.setMaxTotal(HttpClientConstant.MAX_TOTAL_CONNECTION);
        connManager.setDefaultMaxPerRoute(HttpClientConstant.MAX_PER_ROUTE_CONNECTION);
        return connManager;
    }

    private static PoolingNHttpClientConnectionManager getNPool() {
        IOReactorConfig ioReactorConfig = IOReactorConfig.custom().setIoThreadCount(Runtime.getRuntime().availableProcessors()).setConnectTimeout(HttpClientConstant.CONNECT_REQUEST_TIMEOUT).setSoTimeout(HttpClientConstant.SOCKET_TIMEOUT).build();
        ConnectingIOReactor ioReactor = null;
        try {
            ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
        } catch (IOReactorException e) {
            logger.error("创建连接响应器失败!", e);
        }
        MessageConstraints messageConstraints = MessageConstraints.custom().setMaxHeaderCount(HttpClientConstant.MAX_HEADER_COUNT).setMaxLineLength(HttpClientConstant.MAX_LINE_LENGTH).build();
        PoolingNHttpClientConnectionManager connManager = new PoolingNHttpClientConnectionManager(ioReactor);
        ConnectionConfig connectionConfig = ConnectionConfig.custom().setMalformedInputAction(CodingErrorAction.IGNORE).setUnmappableInputAction(CodingErrorAction.IGNORE).setCharset(Constant.DEFAULT_CHARSET).setMessageConstraints(messageConstraints).build();
        connManager.setDefaultConnectionConfig(connectionConfig);
        connManager.setMaxTotal(HttpClientConstant.MAX_TOTAL_CONNECTION);
        connManager.setDefaultMaxPerRoute(HttpClientConstant.MAX_PER_ROUTE_CONNECTION);
        return connManager;
    }

    /**
     * 获取SSL套接字对象 重点重点：设置tls协议的版本
     *
     * @return
     */
    private static SSLContext createIgnoreVerifySSL() {
        SSLContext sslContext = null;// 创建套接字对象
        try {
            sslContext = SSLContext.getInstance(HttpClientConstant.SSL_VERSION);// 指定TLS版本
        } catch (NoSuchAlgorithmException e) {
            FailException.fail("创建套接字失败！" + e.getMessage());
        }
        // 实现X509TrustManager接口，用于绕过验证
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                                           String paramString) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                                           String paramString) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        try {
            sslContext.init(null, new TrustManager[]{trustManager}, null);// 初始化sslContext对象
        } catch (KeyManagementException e) {
            logger.warn("初始化套接字失败！", e);
        }
        return sslContext;
    }

    /**
     * 获取重试控制器
     *
     * @return
     */
    private static HttpRequestRetryHandler getHttpRequestRetryHandler() {
        return new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                logger.warn("请求发生错误:{}", exception.getMessage(), exception);
                if (executionCount > HttpClientConstant.TRY_TIMES) return false;
                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequestBase request = clientContext.getAttribute("http.request", HttpRequestBase.class);
                logger.error(FunRequest.initFromRequest(request).toString());
                if (exception instanceof NoHttpResponseException) {
//                    logger.warn("没有响应异常");
                    return true;
                } else if (exception instanceof ConnectTimeoutException) {
//                    logger.warn("连接超时，重试");
                    return true;
                } else if (exception instanceof SSLHandshakeException) {
//                    logger.warn("本地证书异常");
                    return false;
                } else if (exception instanceof InterruptedIOException) {
//                    logger.warn("IO中断异常");
                    return true;
                } else if (exception instanceof UnknownHostException) {
//                    logger.warn("找不到服务器异常");
                    return false;
                } else if (exception instanceof SSLException) {
//                    logger.warn("SSL异常");
                    return false;
                } else if (exception instanceof HttpHostConnectException) {
//                    logger.warn("主机连接异常");
                    return false;
                } else if (exception instanceof SocketException) {
//                    logger.warn("socket异常");
                    return false;
                } else {
                    logger.warn("未记录的请求异常:", exception);
                }

                // 如果请求是幂等的，则不重试
//                if (!(request instanceof HttpEntityEnclosingRequest)) {
//                    return false;
//                }
                return false;
            }
        };
    }

    /**
     * 通过连接池获取https协议请求对象
     * <p>
     * 增加默认的请求控制器，和请求配置，连接控制器，取消了cookiestore，单独解析响应set-cookie和发送请求的header，适配多用户同时在线的情况
     * </p>
     *
     * @return
     */
    private static CloseableHttpAsyncClient getCloseableHttpAsyncClient() {
        return HttpAsyncClients.custom().setConnectionManager(NconnManager).setSSLHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER).setSSLContext(sslContext).build();
    }

    private static CloseableHttpClient getCloseableHttpsClients() {
        return HttpClients.custom().setConnectionManager(connManager).setRetryHandler(httpRequestRetryHandler).setDefaultRequestConfig(requestConfig).build();
    }

    /**
     * 获取请求超时控制器
     * <p>
     * cookieSpec:即cookie策略。参数为cookiespecs的一些字段。作用：
     * 1、如果网站header中有set-cookie字段时，采用默认方式可能会被cookie reject，无法写入cookie。将此属性设置成CookieSpecs.STANDARD_STRICT可避免此情况。
     * 2、如果要想忽略cookie访问，则将此属性设置成CookieSpecs.IGNORE_COOKIES。
     * </p>
     *
     * @return
     */
    private static RequestConfig getRequestConfig() {
        return RequestConfig.custom().setConnectionRequestTimeout(HttpClientConstant.CONNECT_REQUEST_TIMEOUT).setConnectTimeout(HttpClientConstant.CONNECT_TIMEOUT).setSocketTimeout(HttpClientConstant.SOCKET_TIMEOUT).setCookieSpec(CookieSpecs.IGNORE_COOKIES).setRedirectsEnabled(false).build();
    }

    /**
     * 获取代理配置项
     *
     * @param ip
     * @param port
     * @return
     */
    public static RequestConfig getProxyRequestConfig(String ip, int port) {
        return RequestConfig.custom().setConnectionRequestTimeout(HttpClientConstant.CONNECT_REQUEST_TIMEOUT).setConnectTimeout(HttpClientConstant.CONNECT_TIMEOUT).setSocketTimeout(HttpClientConstant.SOCKET_TIMEOUT).setCookieSpec(CookieSpecs.IGNORE_COOKIES).setRedirectsEnabled(false).setProxy(new HttpHost(ip, port)).build();
    }


    /**
     * 回收资源方法，关闭过期连接，关闭超时连接，用于另起线程回收连接池连接
     */
    public static void recyclingConnection() {
        connManager.closeExpiredConnections();
        connManager.closeIdleConnections(HttpClientConstant.IDLE_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    /**
     * 重新初始化连接池,用于临时改变超时和超时标准线的重置
     * <p>
     * 会重置请求控制器,重置连接池和重试控制器
     * </p>
     * 时间单位s,默认配置单位ms,自动乘以1000
     *
     * @param timeout
     * @param accepttime
     * @param retrytimes
     * @param ip
     * @param port
     */
    public static void init(int timeout, int accepttime, int retrytimes, String ip, int port) {
        HttpClientConstant.CONNECT_REQUEST_TIMEOUT = timeout * 1000;
        HttpClientConstant.CONNECT_TIMEOUT = timeout * 1000;
        HttpClientConstant.SOCKET_TIMEOUT = timeout * 1000;
        HttpClientConstant.MAX_ACCEPT_TIME = accepttime * 1000;
        HttpClientConstant.TRY_TIMES = retrytimes;
        requestConfig = StringUtils.isNoneBlank(ip) && Regex.isMatch(ip + ":" + port, "((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))):([0-9]|[1-9]\\d{1,3}|[1-5]\\d{4}|6[0-4]\\d{4}|65[0-4]\\d{2}|655[0-2]\\d|6553[0-5])") ? getProxyRequestConfig(ip, port) : getRequestConfig();
        httpsClient = getCloseableHttpsClients();
        httpRequestRetryHandler = getHttpRequestRetryHandler();
    }


}
