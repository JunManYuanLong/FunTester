package com.fun.dubbo;

import com.alibaba.dubbo.rpc.service.GenericService;
import com.fun.frame.SourceCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * dubbo泛化调用的方法
 */
public class DubboUtil extends SourceCode {

    public static Logger logger = LoggerFactory.getLogger(DubboUtil.class);

    public static DubboInvokeParams initDubboInvokeParams(DubboParamBase... params) {
        DubboInvokeParams invokeParams = new DubboInvokeParams(params.length);
        range(invokeParams.getLength()).forEach(x ->
                {
                    DubboParamBase param = params[x];
                    invokeParams.getTypes()[x] = param.getType();
                    invokeParams.getValues()[x] = param.getValue();
                }
        );
        return invokeParams;
    }

    public static Object getResponse(GenericService genericService, String methodName, DubboInvokeParams params) {
        return genericService.$invoke(methodName, params.getTypes(), params.getValues());
    }

    public static void main(String[] args) {
        Map<String, Object> getDataRequest = new HashMap<String, Object>();
        getDataRequest.put("orgId", "119");
        getDataRequest.put("orgType", "2");
        getDataRequest.put("reqId", "123456789");
        DubboInvokeParams dubboInvokeParams = initDubboInvokeParams(new DubboParamBase(Object.class, getDataRequest));
        DubboBase dubbo = new DubboBase("dubbo");
        GenericService genericService = dubbo.getGenericService("com.noriental.usersvr.service.okuser.SchoolYearService");

        Object response = getResponse(genericService, "findFutureYear", dubboInvokeParams);
        output(response.toString());

    }


}