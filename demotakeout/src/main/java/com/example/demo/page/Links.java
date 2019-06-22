package com.example.demo.page;

import com.example.demo.entity.Task;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * @author Yang
 * 有两个集合，用于存放已经访问过的url和还未访问过的url
 */
public class Links {
    //已经访问过的url集合，用set保证不重复
    private static Set visitedUrlSet=new HashSet();

    //待访问的url集合，主要考虑规定访问顺序和用不重复
    private static LinkedList unVisitedUrlQueue=new LinkedList();

    //获取已经访问过的URL数目
    public static int getVisitedUrlNum(){
        return visitedUrlSet.size();
    }

    //添加已访问过的URL
    public static void addVisitedUrlSet(String url){
        visitedUrlSet.add(url);
    }

    //移除访问过的URL
    public static void removeVisitedUrlSet(String url){
        visitedUrlSet.remove(url);
    }

    //获得 待访问的url集合
    public static LinkedList getUnVisitedUrlQueue(){
        return unVisitedUrlQueue;
    }
    //添加到待访问的集合中 保证每个URL只被访问一次
    public static void addUnvisitedUrlQueue(Task task){
        unVisitedUrlQueue.add(task);
    }

    public static Object removeHeadOfUnvisitedUrlQueue(){
        return unVisitedUrlQueue.removeFirst();
    }
    //判断未访问的URL队列是否为空
    public static boolean unVisitedUrlQueueIsEmpty(){
        return unVisitedUrlQueue.isEmpty();
    }

}
