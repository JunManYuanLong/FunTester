package com.funtester.main

import com.funtester.frame.SourceCode
import com.funtester.frame.execute.Concurrent
import com.funtester.frame.thread.RequestThreadTimes
import com.funtester.httpclient.FunLibrary
import com.funtester.utils.request.RequestFile
import org.apache.http.client.methods.HttpRequestBase
/**
 * 从文本配置中读取request，进行压测的类
 */
class PerformanceFromFile extends SourceCode {
    static void main(String[] args) {
        FunLibrary.setSocketTimeOut(30)
        def size = args.size();
        List<HttpRequestBase> list = new ArrayList<>()
        for (int i = 0; i < size - 1; i += 2) {
            def name = args[i]
            int thread = changeStringToInt(args[i + 1])
            def request = new RequestFile(name).getRequest()
            for (int j = 0; j < thread; j++) {
                list.add(request)
            }
        }
        int perTimes = changeStringToInt(args[size - 1])
        List<RequestThreadTimes> thread = new ArrayList<>()
        for (int i = 0; i < list.size(); i++) {
            def get = list.get(i)
            def thread1 = new RequestThreadTimes(get, perTimes)
            thread.add(thread1)
        }
        def concurrent = new Concurrent(thread)
        concurrent.start()
        FunLibrary.testOver()
    }
}
