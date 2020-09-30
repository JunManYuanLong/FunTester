package com.fun.utils.xml

import com.fun.base.bean.AbstractBean

class Attr extends AbstractBean {

    private static final long serialVersionUID = -35484487563215649L

    String name

    String value

    public Attr(String name, String value) {
        this.name = name
        this.value = value
    }


}