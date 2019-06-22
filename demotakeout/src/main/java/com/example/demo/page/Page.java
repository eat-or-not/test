package com.example.demo.page;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


import java.io.UnsupportedEncodingException;

/**
 * @author Yang
 *
 */
public class Page {

    /**
     * 网页源码
     */
    private String html;
    /**
     * 解析过的网页
     */
    private Document doc;
    /**
     * 字符编码
     */
    private String url;
    private String contentType;

    public Page( String url,String html) {
        this.url = url;
        this.html=html;
    }

    public String getUrl() {
        return url;
    }

    public String getContentType() {
        return contentType;
    }

    public String getHtml() {
        //如果该类的html不为空，直接返回
        return this.html;
    }
    /**
     * 得到文档
     */
    public Document getDoc(){
        if(doc!=null){
            return doc;
        }
        try{
            this.doc= Jsoup.parse(getHtml(),url);
            return doc;
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
}
