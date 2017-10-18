package com.cyjh.http.core;

import com.cyjh.http.base.CYJHRequest;
import com.cyjh.http.statcks.HttpStack;
import com.cyjh.http.statcks.HttpStackFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 请求队列
 * Created by linbaosheng on 2017/9/21.
 */

public class RequestQueue {
    /**
     * 请求队列 [ Thread-safe ]
     */
    private BlockingQueue<CYJHRequest<?>> mRequestQueue = new LinkedBlockingQueue<>();
    /**
     * 默认的核心数
     */
    public static int DEFAULT_CORE_NUMS = Runtime.getRuntime().availableProcessors() + 1;

    private HttpStack mHttpStack;
    /**
     * CPU核心数 + 1个分发线程数
     */
    private int mDispatcherNums = DEFAULT_CORE_NUMS;
    /**
     * 请求的序列化生成器
     */
    private AtomicInteger mSerialNumGenerator = new AtomicInteger(0);

    private RequestExecuteRunnable[] requestExecuteRunnables;

    public RequestQueue(HttpStack mHttpStack, int mDispatcherNums) {
        this.mDispatcherNums = mDispatcherNums;
        this.mHttpStack = mHttpStack != null ? mHttpStack : HttpStackFactory.createHttpStack();
    }

    public void start(){
        stop();
        startExecutors();
    }

    private final void startExecutors(){
        requestExecuteRunnables = new RequestExecuteRunnable[mDispatcherNums];
        for (int i = 0; i < mDispatcherNums; i++) {
            requestExecuteRunnables[i] = new RequestExecuteRunnable(mRequestQueue,mHttpStack);
            requestExecuteRunnables[i].start();
        }
    }

    public void stop(){
        if (requestExecuteRunnables != null && requestExecuteRunnables.length > 0) {
            for (int i = 0; i < requestExecuteRunnables.length; i++) {
                requestExecuteRunnables[i].quit();
            }
        }
    }
    /**添加请求体*/
    public void addRequest(CYJHRequest<?> request){
        request.setSerialNumber(generateSerialNumber());
        mRequestQueue.add(request);
    }

    public void clear(){
        mRequestQueue.clear();
    }

    /**
     * 为每个请求生成一个系列号
     *
     * @return 序列号
     */
    private int generateSerialNumber() {
        return mSerialNumGenerator.incrementAndGet();
    }
}
