package com.cyjh.http.statcks;

/**
 * httpstack工厂类，用于扩展
 * Created by linbaosheng on 2017/9/21.
 */

public class HttpStackFactory {

    public static HttpStack createHttpStack(){
        return new HttpUrlConnStack();
    }
}
