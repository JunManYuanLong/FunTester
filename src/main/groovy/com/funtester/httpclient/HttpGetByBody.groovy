package com.funtester.httpclient

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase

import javax.annotation.concurrent.NotThreadSafe

/**
 * HttpDelete请求携带body参数
 */
@NotThreadSafe
class HttpGetByBody extends HttpEntityEnclosingRequestBase {

    static final String METHOD_NAME = "GET";

    /**
     * 获取方法（必须重载）
     *
     * @return
     */
    @Override
    String getMethod() {
        return METHOD_NAME;
    }

    /**
     * PS:不能照抄{@link org.apache.http.client.methods.HttpPost}
     * @param uri
     */
    HttpGetByBody(final String uri) {
        this(new URI(uri))
    }

    HttpGetByBody(final URI uri) {
        super();
        setURI(uri);
    }

    HttpGetByBody() {
        super();
    }
}
