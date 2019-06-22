package com.example.demo.page;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Yang
 * 发送请求并得到回应
 */
public class RequestAndResponesTool {

    public static Page sendRequestAndGetRespones(String url){
        Page page=null;
        //1.生成HttpClinet对象并设置参数
        HttpClient httpClient=new HttpClient();
        //设置http连接超时 5s
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
        //2.生成GetMethod对象并设置参数
        GetMethod getMethod=new GetMethod(url);
        //设置get请求超时5s
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT,5000);
        //设置请求重试处理
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler());
        //3.执行 http get请求
        try{
            int statusCode=httpClient.executeMethod(getMethod);
            if(statusCode!= HttpStatus.SC_OK){
                System.err.println("Method failed: "+getMethod.getStatusLine());
            }
            //处理http响应内容
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(getMethod.getResponseBodyAsStream()));
            StringBuffer stringBuffer=new StringBuffer();
            String str="";
            while((str=bufferedReader.readLine())!=null){
                stringBuffer.append(str);
            }
            String html=stringBuffer.toString();
            page=new Page(url,html);
        }catch (HttpException e){
            //发生http异常，可能是协议不对或者返回的内容不对
            System.out.println("HttpException!");
            e.printStackTrace();
        }catch (IOException e){
            //发生网络异常
            System.out.println("IOException!");
            e.printStackTrace();
        }finally {
            //释放链接
            getMethod.releaseConnection();
        }
        return page;
    }
}
