package com.fun.base.interfaces;

import org.apache.http.client.methods.HttpRequestBase;

import java.io.Serializable;

/**
 * 用来标记request,为了记录超时的请求
 */
public interface MarkRequest extends Serializable {

    /**
     * 用来标记base,删除header其中一项,添加一项
     *
     * @param request
     * @return
     */
    public String mark(HttpRequestBase request);


}
