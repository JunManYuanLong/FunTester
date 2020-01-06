package com.fun.base.interfaces;

import org.apache.http.client.methods.HttpRequestBase;

public interface MarkRequest {

    /**
     * 用来标记base,删除header其中一项,添加一项
     *
     * @param base
     * @return
     */
    public String mark(HttpRequestBase base);


}
