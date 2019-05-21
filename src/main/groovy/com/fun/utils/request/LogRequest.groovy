package com.fun.utils.request

import com.fun.frame.excute.Concurrent
import com.fun.frame.httpclient.FanRequest
import com.fun.frame.thead.RequestThread
import com.fun.utils.Regex
import net.sf.json.JSONObject

class LogRequest {
    public static void main(String[] args) {
        String re = ""
        def split = re.split("\\] \\[")[4]
        def regex = Regex.regexAll(re, "(REQHeader|REQParam)\\[\\{.+?\\}\\] ")
        def header = JSONObject.fromObject(regex[0].substring(10, regex[0].length() - 2))
        def params = JSONObject.fromObject(regex[1].substring(9, regex[1].length() - 2))
        def name = FanRequest.isPost().setHost("").setApiName(split)
        header.keySet().forEach { x -> name.addHeader(x, header.getString(x)) }
        params.keySet().forEach { x -> name.addJson(x, params.getString(x)) }
        new Concurrent(new RequestThread(name.getRequest(), 10), 10).start()
    }
}
