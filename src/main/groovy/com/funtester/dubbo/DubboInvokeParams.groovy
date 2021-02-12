package com.funtester.dubbo

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings

@SuppressFBWarnings("EI_EXPOSE_REP")
class DubboInvokeParams {

    int length

    String[] types = new String[length]

    Object[] values = new Object[length]

    DubboInvokeParams(int length) {
        this.length = length;
    }

}
