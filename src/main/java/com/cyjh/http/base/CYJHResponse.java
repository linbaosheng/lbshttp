package com.cyjh.http.base;

import java.util.Map;



/**
 *
 * Created by linbaosheng on 2017/9/15.
 */

public class CYJHResponse<T> {
    public int statusCode;
    public byte[] data;
    public Map<String, String> headers;
    public boolean notModified;
    public T result;
    public CYJHResponse(int statusCode, byte[] data, Map<String, String> headers, boolean notModified) {
        this.statusCode = statusCode;
        this.data = data;
        this.headers = headers;
        this.notModified = notModified;
    }

    public CYJHResponse(byte[] data) {
//        this(200, data, Collections.emptyMap(), false);
    }

    public CYJHResponse(byte[] data, Map<String, String> headers) {
        this(200, data, headers, false);
    }
}
