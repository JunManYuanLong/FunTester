package com.funtester.httpclient;

import com.funtester.base.exception.FailException;
import com.funtester.config.Constant;
import com.funtester.frame.SourceCode;
import org.apache.hc.client5.http.DnsResolver;
import org.apache.hc.client5.http.HttpRequestRetryStrategy;
import org.apache.hc.client5.http.SystemDefaultDnsResolver;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManagerBuilder;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.nio.ssl.BasicClientTlsStrategy;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.http.protocol.HttpCoreContext;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static com.funtester.config.HttpClientConstant.*;

/**
 * 连接池管理类
 */
public class ClientManage {

    private static Logger logger = LogManager.getLogger(ClientManage.class);

    /**
     * 需要解析自定义域名的IP集合
     */
    public static List<InetAddress> ips = getAddress();

    /**
     * ssl验证
     */
    private static SSLContext sslContext = createIgnoreVerifySSL();

    /**
     * 本地DNS解析
     */
    private static DnsResolver dnsResolver = getDnsResolver();

    /**
     * 请求超时控制器
     */
    private static RequestConfig requestConfig = getRequestConfig();

    /**
     * 请求重试管理器
     */
    private static HttpRequestRetryStrategy httpRequestRetryHandler = getHttpRequestRetryHandler();

    /**
     * 连接池
     */
    private static PoolingHttpClientConnectionManager connManager = getPool();

    /**
     * 异步连接池
     */
    private static PoolingAsyncClientConnectionManager NconnManager = getNPool();

    /**
     * httpclient对象
     */
    public static CloseableHttpClient httpsClient = getCloseableHttpsClients();

    /**
     * 异步连接池
     */
    public static CloseableHttpAsyncClient httpAsyncClient = getCloseableHttpAsyncClient();


    static ConnectionConfig connectionConfig = ConnectionConfig.custom()// 设置连接配置
            .setConnectTimeout(Timeout.of(Duration.ofMillis(CONNECT_TIMEOUT))) // 设置连接超时
            .setSocketTimeout(Timeout.of(Duration.ofMillis(SOCKET_TIMEOUT))) // 设置 socket 超时
            .build();

    /**
     * 获取连接池管理器
     *
     * @return
     */
    private static PoolingHttpClientConnectionManager getPool() {
        PoolingHttpClientConnectionManager connManager = PoolingHttpClientConnectionManagerBuilder.create()
                .setSSLSocketFactory(new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE))
                .setDnsResolver(dnsResolver)
                .build();
        connManager.setDefaultConnectionConfig(connectionConfig);
        connManager.setMaxTotal(MAX_TOTAL_CONNECTION);
        connManager.setDefaultMaxPerRoute(MAX_PER_ROUTE_CONNECTION);
        return connManager;
    }

    /**
     * 获取异步连接池
     *
     * @return
     */
    private static PoolingAsyncClientConnectionManager getNPool() {
        PoolingAsyncClientConnectionManager connectionManager = PoolingAsyncClientConnectionManagerBuilder.create()
                .setTlsStrategy(new BasicClientTlsStrategy(sslContext))
                .setDnsResolver(dnsResolver)
                .build();
        connectionManager.setDefaultMaxPerRoute(MAX_TOTAL_CONNECTION); // 设置每个路由的最大连接数
        connectionManager.setMaxTotal(MAX_TOTAL_CONNECTION); // 设置最大总连接数
        connectionManager.setDefaultConnectionConfig(connectionConfig);// 设置默认连接配置
        return connectionManager;
    }

    /**
     * 初始化DNS配置IP
     *
     * @return
     */
    private static List<InetAddress> getAddress() {
        try {

            return Arrays.asList(
                    InetAddress.getByName("127.0.0.1"),
                    InetAddress.getByName("0.0.0.0")
            );
        } catch (Exception e) {
            FailException.fail("DNS IP解析失败!");
        }
        return null;
    }

    /**
     * 重写Java自定义DNS解析器,负载均衡
     *
     * @return
     */
    private static DnsResolver getDnsResolver() {
        return new SystemDefaultDnsResolver() {
            @Override
            public InetAddress[] resolve(final String host) throws UnknownHostException {
                if (host.equalsIgnoreCase("fun.tester")) {
                    InetAddress random = SourceCode.random(ips);
                    logger.info(random);
                    return new InetAddress[]{random};
                } else {
                    return super.resolve(host);
                }
            }
        };
    }

    /**
     * 获取SSL套接字对象 重点重点：设置tls协议的版本
     *
     * @return SSLContext
     */
    private static SSLContext createIgnoreVerifySSL() {
        SSLContext sslContext = null;// 创建套接字对象
        try {
            sslContext = SSLContext.getInstance(SSL_VERSION);// 指定TLS版本
        } catch (NoSuchAlgorithmException e) {
            FailException.fail("创建套接字失败！" + e.getMessage());
        }
        // 实现X509TrustManager接口，用于绕过验证
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                                           String paramString) {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                                           String paramString) {
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
     * @return HttpRequestRetryStrategy
     */
    private static HttpRequestRetryStrategy getHttpRequestRetryHandler() {
        return new HttpRequestRetryStrategy() {
            @Override
            public boolean retryRequest(HttpRequest httpRequest, IOException e, int i, HttpContext httpContext) {
                boolean log = log(e, i, httpContext);
                if (log) logger.warn("请求发生重试! 次数: {}", i);
                return log;
            }

            @Override
            public boolean retryRequest(HttpResponse httpResponse, int i, HttpContext httpContext) {
                // 根据响应进行重试
                return false;
            }

            @Override
            public TimeValue getRetryInterval(HttpResponse httpResponse, int i, HttpContext httpContext) {
                return null;
            }

            /**绕一圈,记录重试信息,避免错误日志影响观感
             * @param exception 异常
             * @param executionCount 重试次数
             * @param context 上下文
             * @return
             */
            private boolean log(IOException exception, int executionCount, HttpContext context) {
                if (executionCount + 1 > TRY_TIMES) return false;
                logger.warn("请求发生错误:{}", exception.getMessage());
                HttpClientContext clientContext = HttpClientContext.adapt(context);
                final Object request = clientContext.getAttribute(HttpCoreContext.HTTP_REQUEST);
                if (request instanceof HttpUriRequest) {
                    HttpUriRequest uriRequest = (HttpUriRequestBase) request;
                    logger.warn("请求失败接口URI:{}", uriRequest.getRequestUri());
                }
                if (exception instanceof NoHttpResponseException) {
                    return true;
                } else if (exception instanceof InterruptedIOException) {
                    return true;
                } else if (exception instanceof UnknownHostException) {
                    return false;
                } else if (exception instanceof SSLException) {
                    return false;
                } else if (exception instanceof SocketException) {
                    return false;
                } else {
                    logger.warn("未记录的请求异常:{}", exception.getClass().getName());
                }
                // 如果请求是幂等的，则不重试,HttpEntityEnclosingRequest类以及子类都是非幂等性的
                if (!(request instanceof HttpEntityContainer)) {
                    return false;
                }
                return true;
            }
        };
    }

    /**
     * 通过连接池获取https协议请求对象
     * <p>
     * 增加默认的请求控制器，和请求配置，连接控制器，取消了cookiestore，单独解析响应set-cookie和发送请求的header，适配多用户同时在线的情况
     * </p>
     *
     * @return CloseableHttpClient
     */
    private static CloseableHttpAsyncClient getCloseableHttpAsyncClient() {
        return HttpAsyncClients.custom()
                .setConnectionManager(NconnManager)
                .setDefaultRequestConfig(requestConfig)
                .disableCookieManagement()
                .build();
    }

    /**
     * 获取HttpClient对象
     * <p>
     * 增加默认的请求控制器，和请求配置，连接控制器，取消了cookiestore，单独解析响应set-cookie和发送请求的header，适配多用户同时在线的情况
     * </p>
     *
     * @return CloseableHttpClient
     */
    private static CloseableHttpClient getCloseableHttpsClients() {
        return HttpClients.custom()
                .setConnectionManager(connManager)
                .setRetryStrategy(httpRequestRetryHandler)
                .setDefaultRequestConfig(requestConfig)
                .disableCookieManagement() // 取消cookiestore
                .build();
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
        return RequestConfig.custom().setConnectionRequestTimeout(Timeout.ofMilliseconds(CONNECT_REQUEST_TIMEOUT)).setCookieSpec("ignoreCookies").setRedirectsEnabled(false).build();
    }

    /**
     * 获取代理配置项
     *
     * @param ip
     * @param port
     * @return
     */
    public static RequestConfig getProxyRequestConfig(String ip, int port) {
        return RequestConfig.custom()
                .setConnectionRequestTimeout(Timeout.ofMilliseconds(CONNECT_TIMEOUT))
                .setCookieSpec("ignoreCookies").setRedirectsEnabled(false)
                .setProxy(new HttpHost(ip, port)).build();
    }

    /**
     * 请求拦截器
     *
     * @return
     */
    public static HttpRequestInterceptor getHttpRequestInterceptor() {
        return new HttpRequestInterceptor() {
            @Override
            public void process(HttpRequest httpRequest, EntityDetails entityDetails, HttpContext httpContext) throws HttpException, IOException {
                logger.debug("请求拦截器成功!");
            }

        };
    }


    /**
     * 响应拦截器
     *
     * @return
     */
    public static HttpResponseInterceptor getHttpResponseInterceptor() {
        return new HttpResponseInterceptor() {
            @Override
            public void process(HttpResponse httpResponse, EntityDetails entityDetails, HttpContext httpContext)  {
                logger.debug("响应拦截器成功!");
            }
        };
    }

    /**
     * 回收资源方法，关闭过期连接，关闭超时连接，用于另起线程回收连接池连接
     */
    public static void recyclingConnection() {
        connManager.closeExpired();
        connManager.closeIdle(TimeValue.ofSeconds(IDLE_TIMEOUT));
    }

    /**
     * 启动异步请求客户端
     */
    public static void startAsync() {
        httpAsyncClient.start();
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
     */
    public static void init(int timeout, int accepttime, int retrytimes) {
        CONNECT_REQUEST_TIMEOUT = timeout * 1000;
        CONNECT_TIMEOUT = timeout * 1000;
        SOCKET_TIMEOUT = timeout * 1000;
        MAX_ACCEPT_TIME = accepttime * 1000;
        TRY_TIMES = retrytimes < 1 ? Constant.TEST_ERROR_CODE : retrytimes;
        httpsClient = getCloseableHttpsClients();
        httpRequestRetryHandler = getHttpRequestRetryHandler();
    }


}
