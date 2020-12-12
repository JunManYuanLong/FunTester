package com.fun.main;

import com.fun.frame.SourceCode;
import com.fun.frame.execute.ExecuteSource;
import org.apache.commons.lang3.ArrayUtils;

public class ExecuteMethod extends SourceCode {

    public static void main(String[] args) {
        if (ArrayUtils.isEmpty(args)) args = new String[]{"com.fun.ztest.java.T.test", "java.lang.Integer", "1"};
        ExecuteSource.executeMethod(args);
    }


}
