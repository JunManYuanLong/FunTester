package com.funtester.utils;

import java.util.List;

/**
 * 生成表格封装类
 */
public class WriteHtml {

    /**
     * 获取表格头部信息
     *
     * @param list
     * @return
     */
    private static String getTable(List<Object> list) {
        StringBuffer start = new StringBuffer("<table align=\"center\"  border='1' style='table-layout:fixed;font-size:16px;'><thead><tr style='word-wrap:break-word;word-break:break-all'>");
        start.append("<td>序号</td>");
        for (int i = 0; i < list.size(); i++) {
            start.append("<td>" + list.get(i).toString() + "</td>");
        }
        String end = "</tr></thead><tbody>";
        return start + end;
    }

    /**
     * 拼接整个页面
     *
     * @param result
     * @param title
     * @return
     */
    public static String createWebReport(List<List<Object>> result, String title) {
        String starttext = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'></head><h1 style='text-align:center'>" + title + "</h1>" + getTable(result.get(0));
        String endtext = "<script src=\"http://blog.fv1314.xyz/blog/js/bubbly.js\"></script><script src=\"http://blog.fv1314.xyz/blog/js/home.js\"></script></tbody></table></body></html>";
        StringBuffer sheet = new StringBuffer(starttext);
        for (int i = 1; i < result.size(); i++) {
            List<Object> objects = result.get(i);
            sheet.append("<tr>");
            sheet.append("<td style='word-wrap:break-word;word-break:break-all'>" + i + "</td>");
            sheet.append(getTr(objects));
            sheet.append("</tr>");
        }
        sheet.append(endtext);
        return sheet.toString();
    }

    /**
     * 获取行信息
     *
     * @param tr
     * @return
     */
    private static StringBuffer getTr(List<Object> tr) {
        StringBuffer body = new StringBuffer();
        for (int i = 0; i < tr.size(); i++) {
            body.append("<td style='word-wrap:break-word;word-break:break-all'>" + tr.get(i).toString() + "</td>");
        }
        return body;
    }


}
