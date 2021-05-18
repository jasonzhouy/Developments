package com.it.hikvision.mobtool.tool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlTagTool {
    public static String delHTMLTag(String htmlStr) {
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式
        String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); //过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); //过滤html标签

//        htmlStr = htmlStr.replace(" ", "");
//        htmlStr = htmlStr.replaceAll("\\s*|\t|\r|\n", "");
//        htmlStr = htmlStr.replace("“", "");
//        htmlStr = htmlStr.replace("”", "");
//        htmlStr = htmlStr.replaceAll("　", "");
        htmlStr = htmlStr.replace("&nbsp;", "");

        return htmlStr.trim(); //返回文本字符串
    }
}
