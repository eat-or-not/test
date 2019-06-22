package com.example.demo.page;


import com.example.demo.entity.Task;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Set;

public class Crawler {

    /**
     * 用种子（url数组）初始化
     * @param seeds
     */

    public void initCrawlerWithSeeds(Task[] seeds){
        for(int i=0;i<seeds.length;i++){
            Links.addUnvisitedUrlQueue(seeds[i]);
        }
    }

    /**
     * 从URL集中选取一个网址爬取，并且返回一个Page
     * @return
     */
    public Page getPage(Task task){
        String visitUrl=task.getUrl();
        if(visitUrl==null){
            return null;
        }
        Page page=RequestAndResponesTool.sendRequestAndGetRespones(visitUrl);
        return page;
    }

    /**
     * 爬取符合正则表达式的所有元素
     * @param page 需要爬取内容的Page
     * @param targetCssSelector 所需内容的标签正则表达式
     * @return
     */
    public Elements crawlingAllElements(Page page,String targetCssSelector){
        Elements es=PageParserTool.select(page,targetCssSelector);
        Links.addVisitedUrlSet(page.getUrl());
        return es;
    }

    /**
     * 返回符合正则表达式的第order个元素
     * @param page
     * @param targetCssSelector
     * @param order
     * @return
     */
    public Element crawlingElementWithOrder(Page page,String targetCssSelector,Integer order){
        Element es=PageParserTool.select(page,targetCssSelector,order);
        Links.addVisitedUrlSet(page.getUrl());
        return es;
    }

    /**
     * 爬取符合正则表达式的元素的某个属性
     * @param page
     * @param targetCssSelector
     * @param attrName
     * @return
     */
    public ArrayList<String> crawlingAttr(Page page,String targetCssSelector,String attrName){
        return PageParserTool.getAttrs(page,targetCssSelector,attrName);
    }






}
