package com.funtester.utils

import com.funtester.frame.SourceCode
import org.apache.commons.lang3.StringUtils

class LogUtil extends SourceCode {

    static FunLog getLog(String line) {
        if (StringUtils.isNotBlank(line) && line.startsWith("\"/"))
            new FunLog(line)
        else new FunLog()
    }

    private static class FunLog {

        FunLog() {
        }

        FunLog(String line) {
            def split = line.split("\",\"")
            this.url = split[0] - "\""
            this.host = split[0]
            this.time = split[5] as int
        }

        String url

        String host

        int time

    }
}
