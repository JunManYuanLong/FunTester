package com.fun.dubbo;

import com.alibaba.dubbo.rpc.service.GenericService;
import com.fun.frame.SourceCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * dubbo泛化调用的方法
 */
public class DubboUtil extends SourceCode {

    public static Logger logger = LoggerFactory.getLogger(DubboUtil.class);

    public static DubboInvokeParams initDubboInvokeParams(DubboParamBase... params) {
        DubboInvokeParams invokeParams = new DubboInvokeParams(params.length);
        range(invokeParams.getLength()).forEach(x ->
                {
                    invokeParams.getTypes()[x] = params[x].getType();
                    invokeParams.getValues()[x] = params[x].getValue();
                }
        );
        return invokeParams;
    }

    public static Object getResponse(GenericService genericService, String methodName, DubboInvokeParams params) {
        return genericService.$invoke(methodName, params.getTypes(), params.getValues());
    }

}