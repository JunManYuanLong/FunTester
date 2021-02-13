package com.funtester.utils.xml


import com.funtester.base.bean.AbstractBean
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings

/**
 * 节点属性信息
 */
@SuppressFBWarnings("SE_TRANSIENT_FIELD_NOT_RESTORED")
class Attr extends AbstractBean implements Serializable{

    private static final long serialVersionUID = -35484487563215649L

    String name

    String value

    public Attr(String name, String value) {
        this.name = name
        this.value = value
    }


}