package com.funtester.utils.xml

import com.funtester.base.bean.AbstractBean

/**
 * 节点信息
 */
class NodeInfo extends AbstractBean implements Serializable {

    private static final long serialVersionUID = 568896512159847L

    List<Attr> attrs

    List<NodeInfo> children


}

