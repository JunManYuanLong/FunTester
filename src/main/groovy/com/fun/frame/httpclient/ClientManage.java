package com.fun.frame.httpclient;

import com.fun.config.HttpClientConstant;
import com.fun.frame.SourceCode;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
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
public class ClientManage extends SourceCode {

    private static Logger logger = LoggerFactory.getLogger(ClientManage.class);

    /**
     * 请求超时控制器
     */
    public static RequestConfig requestConfig = getRequestConfig();

    /**
     * 请求重试管理器
     */
    private static HttpRequestRetryHandler httpRequestRetryHandler = getHttpRequestRetryHandler();

    /**
     * 连接池
     */
    private static PoolingHttpClientConnectionManager connManager = getPool();

    /**
     * httpclient对象
     */
    public static CloseableHttpClient httpsClient = getCloseableHttpsClients();

    /**
     * 获取连接池
     *
     * @return
     */
    private static PoolingHttpClientConnectionManager getPool() {
        PoolingHttpClientConnectionManager connManager = null;
        // 采用绕过验证的方式处理https请求
        SSLContext sslcontext = createIgnoreVerifySSL();
        // 设置协议http和https对应的处理socket链接工厂的对象
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create().register("http", PlainConnectionSocketFactory.INSTANCE).register("https", new SSLConnectionSocketFactory(sslcontext)).build();
        connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        // 消息约束
        MessageConstraints messageConstraints = MessageConstraints.custom().setMaxHeaderCount(HttpClientConstant.MAX_HEADER_COUNT).setMaxLineLength(HttpClientConstant.MAX_LINE_LENGTH).build();
        // 连接设置
        ConnectionConfig connectionConfig = ConnectionConfig.custom().setMalformedInputAction(CodingErrorAction.IGNORE).setUnmappableInputAction(CodingErrorAction.IGNORE).setCharset(DEFAULT_CHARSET).setMessageConstraints(messageConstraints).build();
        connManager.setDefaultConnectionConfig(connectionConfig);
        connManager.setMaxTotal(HttpClientConstant.MAX_TOTAL_CONNECTION);
        connManager.setDefaultMaxPerRoute(HttpClientConstant.MAX_PER_ROUTE_CONNECTION);
        HttpClients.custom().setConnectionManager(connManager);
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
            logger.warn("创建套接字失败！", e);
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
                logger.warn("请求发生错误！", exception);
                if (executionCount > HttpClientConstant.TRY_TIMES) return false;
                if (exception instanceof NoHttpResponseException) {
                    logger.warn("没有响应异常");
                    return true;
                } else if (exception instanceof ConnectTimeoutException) {
                    logger.warn("连接超时，重试");
                    return true;
                } else if (exception instanceof SSLHandshakeException) {
                    logger.warn("本地证书异常");
                    return false;
                } else if (exception instanceof InterruptedIOException) {
                    logger.warn("IO中断异常");
                    return true;
                } else if (exception instanceof UnknownHostException) {
                    logger.warn("找不到服务器异常");
                    return false;
                } else if (exception instanceof SSLException) {
                    logger.warn("SSL异常");
                    return false;
                } else if (exception instanceof HttpHostConnectException) {
                    logger.warn("主机连接异常");
                    return false;
                } else if (exception instanceof SocketException) {
                    logger.warn("socket异常");
                    return false;
                } else {
                    logger.warn("未记录的请求异常：{}", exception.getClass());
                }
                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，则重试
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    return true;
                }
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
     *
     * @param timeout
     * @param accepttime
     * @param retrytime
     */
    public static void init(int timeout, int accepttime, int retrytime) {
        HttpClientConstant.CONNECT_REQUEST_TIMEOUT = timeout;
        HttpClientConstant.CONNECT_TIMEOUT = timeout;
        HttpClientConstant.SOCKET_TIMEOUT = timeout;
        HttpClientConstant.MAX_ACCEPT_TIME = accepttime;
        HttpClientConstant.TRY_TIMES = retrytime;
        requestConfig = getRequestConfig();
        httpsClient = getCloseableHttpsClients();
        httpRequestRetryHandler = getHttpRequestRetryHandler();
    }


}
