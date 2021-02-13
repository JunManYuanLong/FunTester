package com.funtester.main;

import com.funtester.frame.SourceCode;
import com.funtester.frame.execute.ExecuteSource;
import org.apache.commons.lang3.ArrayUtils;

public class ExecuteMethod extends SourceCode {

    public static void main(String[] args) {
        if (ArrayUtils.isEmpty(args)) args = new String[]{"com.funtester.ztest.java.T.test", "java.lang.Integer", "1"};
        ExecuteSource.executeMethod(args);
    }


}
