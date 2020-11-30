package com.fun.utils.xml

import com.fun.base.bean.AbstractBean
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings

/**
 * 节点信息
 */
@SuppressFBWarnings("SE_TRANSIENT_FIELD_NOT_RESTORED")
class NodeInfo extends AbstractBean implements Serializable{

    private static final long serialVersionUID = 568896512159847L

    List<Attr> attrs

    List<NodeInfo> children


}

