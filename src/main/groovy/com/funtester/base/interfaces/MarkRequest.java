package com.funtester.base.interfaces;

import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;

/**
 * 专门用来标记HTTP请求的接口
 */
public interface MarkRequest extends MarkThread {

    /**
     * 标记请求对象
     *
     * @param requestBase
     * @return
     */
    public String mark(HttpUriRequestBase requestBase);


}
