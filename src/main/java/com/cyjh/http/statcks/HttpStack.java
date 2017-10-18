package com.cyjh.http.statcks;

import com.cyjh.http.base.CYJHRequest;
import com.cyjh.http.base.CYJHResponse;

/**
 *
 * Created by linbaosheng on 2017/9/14.
 */

public interface HttpStack {

    /**读取超时时间*/
    int READ_TIME_OUT = 30000;
    /**写入超时时间*/
    int WRITE_TIME_OUT = 30000;
    /**连接超时时间*/
    int CONNECT_TIME_OUT = 30000;
    /**
     * 执行Http请求
     *
     * @param request 待执行的请求
     * @return
     */
    CYJHResponse performRequest(CYJHRequest<?> request);
}
