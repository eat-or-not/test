package com.example.demo.page;

import com.example.demo.entity.Task;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Yang
 * 解析page类
 */
public class PageParserTool {
    /**
     * 通过选择器来获取页面
     */
    public static Elements select(Page page,String cssSelector){
        return page.getDoc().select(cssSelector);
    }

    /**
     * 从元素中再选择元素
     * @param elements
     * @param cssSelector
     * @return
     */
    public static Elements select(Elements elements,String cssSelector){
        return elements.select(cssSelector);
    }

    public static Element select(Elements elements,String cssSelector,int index){
        Elements eles=select(elements,cssSelector);
        int realIndex=index;
        if(index<0){
            realIndex=eles.size()+index;
        }
        System.out.println(realIndex+" error");
        return eles.get(realIndex);
    }
    /**
     * 根据css选择器来得到指定元素
     */
    public static Element select(Page page, String cssSelector, int index){
        Elements eles=select(page,cssSelector);
        int realIndex=index;
        if(index<0){
            realIndex=eles.size()+index;
        }
        return eles.get(realIndex);
    }

    /**
     * 获取满足选择器的元素中的链接。选择器CssSelector必须定位到具体的超链接
     * 例如想抽取id为content的div中的所有超链接，
     * 就必须将cssSelector定义为div[id=content]
     * 放入set中 防止重复
     */
    public  ArrayList<Task> getLinks(Task task, Elements es){
        Iterator iterator=es.iterator();
        ArrayList<Task> tasks=new ArrayList<>();
        while(iterator.hasNext()){
            Element element=(Element)iterator.next();
            if(element.hasAttr("href")){
                String url= element.attr("abs:href");
                //addTask(task,url);
                Task temp=this.getTask(task,url);
                tasks.add(temp);
            }else if(element.hasAttr("src")){
                String url= element.attr("abs:src");
                //addTask(task,url);
                Task temp=this.getTask(task,url);
                tasks.add(temp);
            }
        }
        return tasks;
    }

    /**
     * 获取页面中满足指定css选择器的所有元素的指定属性的集合
     * @param cssSelector 指定的标签
     * @param attrName 指定的属性
     */
    public static ArrayList<String> getAttrs(Page page,String cssSelector, String attrName){
        ArrayList<String> result=new ArrayList<String>();
        Elements eles=select(page,cssSelector);
        for(Element ele:eles){
            if(ele.hasAttr(attrName)){
                result.add(ele.attr(attrName));
            }
        }
        return result;
    }
    private static void addTask(Task task,String url){
        //形成真正的url
        String s=task.getUrl();
        String[] str=s.split("/");
        String trueUrl=str[0]+url;
        //组装成Task对象
        Task t=task;
        t.setUrl(trueUrl);
        t.setType(1);

        //将Task对象加入队列中
        Links.addUnvisitedUrlQueue(t);
    }

    public   Task getTask(Task task,String url){
        //形成真正的url
        String s=task.getRootUrl();
        String trueUrl=url;
        //组装成Task对象
        Task t=new Task();
        t.setRootUrl(task.getRootUrl());
        t.setUrl(trueUrl);
        t.setType(1);
        t.setFields(task.getFields());
        t.setId(task.getId());
        //将Task对象加入队列中
        return t;
    }
}
