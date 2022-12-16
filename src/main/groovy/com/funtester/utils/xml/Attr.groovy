package com.funtester.utils.xml

import com.funtester.base.bean.AbstractBean

/**
 * 节点属性信息
 */
class Attr extends AbstractBean implements Serializable {

    private static final long serialVersionUID = -35484487563215649L

    String name

    String value

    Attr(String name, String value) {
        this.name = name
        this.value = value
    }


}