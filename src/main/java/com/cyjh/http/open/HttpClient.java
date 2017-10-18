package com.cyjh.http.open;

import com.cyjh.http.base.CYJHRequest;
import com.cyjh.http.core.RequestQueue;

/**
 * http对外开放的
 * Created by linbaosheng on 2017/9/21.
 */

public class HttpClient {
    private RequestQueue mRequestQueue;

    /**
     * 初始化http
     * @param threadPoolSize 支持的线程数量
     */
    public void init(int threadPoolSize) {
        mRequestQueue = new RequestQueue(null,threadPoolSize);
        mRequestQueue.start();
    }

    /**
     * 销毁当前所有线程，并释放内存
     */
    public void destory() {
        mRequestQueue.stop();
        mRequestQueue.clear();
    }

    public void addRequest(CYJHRequest<?> cyjhRequest) {
        mRequestQueue.addRequest(cyjhRequest);
    }

    /*-------------------------------------------------------*/
    private volatile static HttpClient instance = null;

    private HttpClient() {
    }

    public static HttpClient getInstance() {
        if (instance == null) {
            synchronized (HttpClient.class) {
                if (instance == null)
                    instance = new HttpClient();
            }
        }
        return instance;
    }
      /*-------------------------------------------------------*/
}
