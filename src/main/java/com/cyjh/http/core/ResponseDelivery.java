package com.cyjh.http.core;

import android.os.Handler;
import android.os.Looper;

import com.cyjh.http.base.CYJHRequest;
import com.cyjh.http.base.CYJHResponse;

import java.util.concurrent.Executor;

/**
 * 请求结果投递类，投递给UI线程
 * Created by linbaosheng on 2017/9/23.
 */

public class ResponseDelivery implements Executor{
    private Handler mResponseHandler = new Handler(Looper.getMainLooper());
    /**
     * 处理请求结果,将其执行在UI线程
     *
     * @param request
     * @param response
     */
    public void deliveryResponse(final CYJHRequest<?> request, final CYJHResponse response) {
        Runnable respRunnable = new Runnable() {
            @Override
            public void run() {
                request.deliveryResponse(response);
            }
        };
        execute(respRunnable);
    }

    @Override
    public void execute(Runnable command) {
        mResponseHandler.post(command);
    }
}
