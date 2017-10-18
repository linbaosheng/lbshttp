package com.cyjh.http.statcks;

import com.cyjh.http.base.CYJHRequest;
import com.cyjh.http.base.CYJHResponse;
import com.cyjh.http.tools.HttpUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;

/**
 * HttpUrlConnection请求
 * Created by linbaosheng on 2017/9/17.
 */

public class HttpUrlConnStack implements HttpStack {
    @Override
    public CYJHResponse performRequest(CYJHRequest<?> request) {
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = createUrlConnection(request.getRequestUrl());
            //设置请求头
            //设置参数
            setRequestParams(urlConnection, request);
            //https配置

            //开始请求
            return fetchResponse(urlConnection);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    protected void setRequestParams(HttpURLConnection urlConnection, CYJHRequest<?> request) throws ProtocolException {
        urlConnection.setRequestMethod(request.getRequestMethod().toString());
        byte[] body = request.getBody();
        if (body != null) { // 说明走的是POST方法
            //具体的处理
        }
    }

    /**
     * 获取reponse
     *
     * @param urlConnection 连接器
     * @return 返回值
     */
    private CYJHResponse fetchResponse(HttpURLConnection urlConnection) throws IOException {
        int responseCode = urlConnection.getResponseCode();  //猜测这里就开始执行连接了
//        if (responseCode == -1) {
//            throw new IOException("Could not retrieve response code from HttpUrlConnection.");
//        }
        CYJHResponse cyjhResponse = new CYJHResponse(responseCode, HttpUtils
                .getBytesByInputStream(urlConnection.getInputStream()),null,false);
        return cyjhResponse;
    }

    /**
     * 创建URL Connection对象
     *
     * @param urlPath
     * @return
     * @throws IOException
     */
    private HttpURLConnection createUrlConnection(String urlPath) throws IOException {
        URL url = new URL(urlPath);
        URLConnection urlConnection = url.openConnection();
        urlConnection.setConnectTimeout(CONNECT_TIME_OUT);
        urlConnection.setReadTimeout(READ_TIME_OUT);
        urlConnection.setDoInput(true);
        urlConnection.setUseCaches(false);
        return (HttpURLConnection) urlConnection;
    }

    //    private void configHttps(CYJHRequest<?> request) {
//        if (request.isHttps()) {
//            SSLSocketFactory sslFactory = mConfig.getSslSocketFactory();
//            // 配置https
//            if (sslFactory != null) {
//                HttpsURLConnection.setDefaultSSLSocketFactory(sslFactory);
//                HttpsURLConnection.setDefaultHostnameVerifier(mConfig.getHostnameVerifier());
//            }
//
//        }
//    }
}
