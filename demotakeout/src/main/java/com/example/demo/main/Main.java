package com.example.demo.main;

import com.example.demo.dao.TaskDao;
import com.example.demo.entity.Field;
import com.example.demo.entity.Task;
import com.example.demo.page.Crawler;
import com.example.demo.page.Links;
import com.example.demo.page.Page;
import com.example.demo.page.PageParserTool;
import com.example.demo.service.TaskService;
import com.example.demo.util.HBaseUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import sun.awt.image.ImageWatched;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    @Autowired
    private HBaseUtils hBaseUtils;


    public static void main(String[] args) throws IOException {
        Crawler crawler=new Crawler();
        Task task=new Task();
        task.setUrl("http://shfy.chinacourt.org/article/index/id/MzAqMDAwNTAwNCACAAA/page/1.shtml");
        Field field=new Field();
        field.setFieldUrl("//html/0/body/0/div[id='container']/div[id='layout']/div[id='main']/div[class='yui3-g']/div[class='content']/div[id='category']/div[class='yui3-u']/div[id='list']/ul/0/li/span/a");
        field.setFieldName("123");
        ArrayList<Field> fields=new ArrayList<>();
        fields.add(field);
        task.setFields(fields);
        //getElement(task);

    }

    public void getElement(Task task) throws IOException {
        Crawler crawler=new Crawler();
        //crawler.crawling(new String[]{"http://bjgy.chinacourt.org/paper.shtml"},1000,"a[title][target][href]");
        //crawler.initCrawlerWithSeeds(new String[]{"http://bjgy.chinacourt.org/paper.shtml"});
        //String str1="//div[id='article']/div[class='left']/div[class='show']/h1/0";
        System.out.println(task);
        Page page=crawler.getPage(task);//div[@id='container']/div[@class='clearfix cpws']/div[@class='bigtit']/h2[1]
        try {
            for (Field field : (task.getFields())) {
                String[] str;
                String str1 = field.getFieldUrl();
                String str2 = str1.replace("//", "");
                str = str2.split("/");
                for (int i = 0; i < str.length; i++) {
                    System.out.println(str[i]);
                }
                int begin = 1;
                Elements eles;
                if (!isNumeric(str[1])) {
                    eles = PageParserTool.select(page, str[0]);
                } else {

                    Element ele = PageParserTool.select(page, str[0], Integer.parseInt(str[1]));
                    eles = ele.getAllElements();
                    begin = 2;
                }
                //System.out.println(eles);
                for (int i = begin; i < str.length - 1; i++) {
                    if (isNumeric(str[i + 1])) {
                        System.out.println(Integer.parseInt(str[i + 1]));
                        Element ele = PageParserTool.select(eles, str[i], Integer.parseInt(str[i + 1]));
                        eles = ele.getAllElements();
                        //System.out.println(eles);
                        if (i < str.length - 2) {
                            i = i + 1;
                        }
                    } else {
                        eles = PageParserTool.select(eles, str[i]);
                        //System.out.println(eles);
                        if (i == str.length - 2) {
                            eles = PageParserTool.select(eles, str[i + 1]);
                            //System.out.println(eles);
                        }
                    }
                }
                System.out.println(eles);
                String s = "";
                for (Element ele : eles) {
                    System.out.println(task.getRootUrl());
                    System.out.println(field.getFieldName());
                    System.out.println(ele.text());

                hBaseUtils=new HBaseUtils();
                hBaseUtils.insertOneRecord("web",task.getUrl(),"content",field.getFieldName(),ele.text());
                String text=HBaseUtils.selectRow("web",task.getRootUrl());
                System.out.println(text);
                }
            }
        }
        catch (NullPointerException e)
        {

            System.out.println("NullPointerException!无法访问页面");
            System.out.println(e.getStackTrace());
        }
    }


    public ArrayList<Task> addUrls(Task task)
    {
        try {
            PageParserTool pageParserTool = new PageParserTool();
            Crawler crawler = new Crawler();
            //crawler.crawling(new String[]{"http://bjgy.chinacourt.org/paper.shtml"},1000,"a[title][target][href]");
            //crawler.initCrawlerWithSeeds(new String[]{"http://bjgy.chinacourt.org/paper.shtml"});
            //String str1="//div[id='article']/div[class='left']/div[class='show']/h1/0";
            Page page = crawler.getPage(task);//div[@id='container']/div[@class='clearfix cpws']/div[@class='bigtit']/h2[1]

            String[] str;
            String str1 = task.getTableUrl();
            //String str1=field.getFieldUrl();
            String str2 = str1.replace("//", "");
            str = str2.split("/");
            for (int i = 0; i < str.length; i++) {
                System.out.println(str[i]);
            }
            int begin = 1;
            Elements eles;
            if (!isNumeric(str[1])) {
                eles = PageParserTool.select(page, str[0]);
            } else {
                Element ele = PageParserTool.select(page, str[0], Integer.parseInt(str[1]));
                eles = ele.getAllElements();
                begin = 2;
            }
            //System.out.println(eles);
            for (int i = begin; i < str.length - 1; i++) {
                if (isNumeric(str[i + 1])) {
                    System.out.println(Integer.parseInt(str[i + 1]));
                    Element ele = PageParserTool.select(eles, str[i], Integer.parseInt(str[i + 1]));

                    eles = ele.getAllElements();
                    //System.out.println(eles);
                    if (i < str.length - 2) {
                        i = i + 1;
                    }
                } else {
                    eles = PageParserTool.select(eles, str[i]);
                    //System.out.println(eles);
                    if (i == str.length - 2) {
                        eles = PageParserTool.select(eles, str[i + 1]);
                        System.out.println(eles);
                    }
                }
            }

        return pageParserTool.getLinks(task,eles);
        }
        catch (NullPointerException e)
        {

            System.out.println("NullPointerException!无法访问页面");
            System.out.println(e.getStackTrace());
            return null;
        }
    }


    /**
     * 用于判断字符串是否为纯数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
        for (int i = str.length();--i>=0;){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }
}
