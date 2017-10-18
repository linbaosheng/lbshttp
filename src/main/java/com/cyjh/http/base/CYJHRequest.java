package com.cyjh.http.base;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 请求实体类
 * Created by linbaosheng on 2017/9/15.
 */

public class CYJHRequest<T> {

    /**
     * http请求方法枚举,这里我们只有GET, POST, PUT, DELETE四种
     *
     * @author mrsimple
     */
    public enum HttpMethod {
        GET("GET"),
        POST("POST"),
        PUT("PUT"),
        DELETE("DELETE");

        /** http request type */
        private String mHttpMethod = "";

        private HttpMethod(String method) {
            mHttpMethod = method;
        }

        @Override
        public String toString() {
            return mHttpMethod;
        }
    }
    /**
     * Default encoding for POST or PUT parameters. See
     * {@link #getParamsEncoding()}.
     */
    private static final String DEFAULT_PARAMS_ENCODING = "UTF-8";

    private RequestListener<T> mRequestListener;
    /**请求地址*/
    private String requestUrl;
    /**
     * 请求的方法
     */
    private HttpMethod mHttpMethod = HttpMethod.GET;
    /**
     * 是否取消该请求
     */
    private boolean isCancel = false;
    /**
     * 请求序列号
     */
    protected int mSerialNum = 0;
    /**最终结果*/
    private T result;
    public CYJHRequest(String requestUrl, HttpMethod mHttpMethod, RequestListener<T> mRequestListener) {
        this.requestUrl = requestUrl;
        this.mHttpMethod = mHttpMethod;
        this.mRequestListener = mRequestListener;
    }

    public String getRequestUrl() {
        return requestUrl;
    }
    /**设置请求地址*/
    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }
    protected String getParamsEncoding() {
        return DEFAULT_PARAMS_ENCODING;
    }
    /**
     * 判断是否是https的
     * @return
     */
    public boolean isHttps(){
        return false;
    }
    /**请求头信息*/
    public String getBodyContentType() {
        return "application/x-www-form-urlencoded; charset=" + getParamsEncoding();
    }

    /**
     * 获取请求类型：GET POST
     * @return
     */
    public HttpMethod getRequestMethod(){
        return mHttpMethod;
    }
    /**
     * 将参数转换为Url编码的参数串
     */
    private byte[] encodeParameters(Map<String, String> params, String paramsEncoding) {
        StringBuilder encodedParams = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
                encodedParams.append('&');
            }
            return encodedParams.toString().getBytes(paramsEncoding);
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
        }
    }

    /**
     * 获取请求参数，主要用于POST请求的
     * @return
     */
    public byte[] getBody(){
        return null;
    }
    /**这个是用户主动取消*/
    public void cancel(){
        isCancel = true;
    }
    /**相当于加了一层防护*/
    public boolean isCanceled(){
        return isCancel;
    }

    public void setSerialNumber(int mSerialNum) {
        this.mSerialNum = mSerialNum;
    }

    /**
     * JSON解析
     * @param json
     * @return
     */
    public T analisisJson(String json){
        if (mRequestListener != null && !isCancel){
            return result = mRequestListener.analisisJson(json);
        }
        return null;
    }

    public void deliveryResponse(CYJHResponse response) {
        if (mRequestListener != null && !isCancel){
            mRequestListener.onSuccess(result);
        }
    }

    public void deliveryError(CYJHHttpError error){
        if (mRequestListener != null && !isCancel){
            mRequestListener.onError(error);
        }
    }
    /**
     * 网络请求回调监听，会被执行在UI线程
     * @param <T>
     */
    public interface RequestListener<T>{
        void onSuccess(T t);

        void onError(CYJHHttpError error);
        /**
         * 解析JSON数据，这块交给用户自己去解析
         * @param json
         */
        T analisisJson(String json);
    }
}
