package com.cyjh.http.core;

import android.util.Log;

import com.cyjh.http.base.CYJHRequest;
import com.cyjh.http.base.CYJHResponse;
import com.cyjh.http.statcks.HttpStack;

import java.util.concurrent.BlockingQueue;

/**
 * 每次请求的执行线程
 * Created by linbaosheng on 2017/9/21.
 */

public class RequestExecuteRunnable extends Thread{

    private BlockingQueue<CYJHRequest<?>> mRequestQueue;
    private HttpStack httpStack;

    private boolean isStop = false;
    /**
     * 结果分发器,将结果投递到主线程
     */
    private ResponseDelivery mResponseDelivery = new ResponseDelivery();
    public RequestExecuteRunnable(BlockingQueue<CYJHRequest<?>> mRequestQueue, HttpStack httpStack) {
        this.mRequestQueue = mRequestQueue;
        this.httpStack = httpStack;
    }

    @Override
    public void run() {
        try{
            while (!isStop){
                final CYJHRequest<?> request = mRequestQueue.take();
                if (request.isCanceled()){
                    continue;
                }
                CYJHResponse cyjhResponse = null;
                if (!request.isCanceled()){
                    Log.i(RequestExecuteRunnable.class.getSimpleName(),"以执行完毕");
                    cyjhResponse = httpStack.performRequest(request);
                    request.analisisJson(new String(cyjhResponse.data));
                }
                //回调UI线程
                mResponseDelivery.deliveryResponse(request,cyjhResponse);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void quit(){
        isStop = true;
        interrupt();
    }
}
