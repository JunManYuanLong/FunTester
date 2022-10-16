package com.funtester.httpclient

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase

import javax.annotation.concurrent.NotThreadSafe

/**
 * HttpDelete请求携带body参数
 */
@NotThreadSafe
class HttpDeleteByBody extends HttpEntityEnclosingRequestBase {

    static final String METHOD_NAME = "DELETE";

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
    HttpDeleteByBody(final String uri) {
        this(new URI(uri))
    }

    HttpDeleteByBody(final URI uri) {
        super();
        setURI(uri);
    }

    HttpDeleteByBody() {
        super();
    }
}
