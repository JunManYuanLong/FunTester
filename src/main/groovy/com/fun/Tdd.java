package com.fun;

import com.fun.frame.httpclient.FanLibrary;
import net.sf.json.JSONObject;
import org.apache.http.Header;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Tdd extends FanLibrary {

    public static void main(String[] args) {

        List<Integer> list = Arrays.asList(32, 432, 432, 432, 4, 2, 423, 4, 23);
        Map<Boolean, List<Integer>> collect = list.stream().collect(Collectors.groupingBy(x -> x > 10));
        output(collect);
//        collect.keySet().stream().3
//        def collect = list.stream().≥collect(Collectors.groupingBy{x->x.toString().length()})
        Map<Integer, List<Integer>> collect1 = list.stream().collect(Collectors.groupingBy(x -> x.toString().length()));

        JSONObject ss = new JSONObject();
        ss.put("323", "32432");
        ss.put("13323", "32432");
        ss.put("1323323", "32432");
        Header cookies = getCookies(ss);
        output(cookies);
        String s = changeJsonToArguments(ss);
        output(s);


        output(1==1 || 21==2?"w":"3");
        double percent = getPercent(11, 11);
        output(percent);
        testOver();
    }
}