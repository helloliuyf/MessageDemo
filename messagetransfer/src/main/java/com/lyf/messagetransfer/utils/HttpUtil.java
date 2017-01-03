package com.lyf.messagetransfer.utils;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONObject;

import java.io.File;

import cz.msebera.android.httpclient.Header;


/**
 * Created by lyf on 2016/12/19 0019.
 * e_mail:helloliuyf@163.com
 */

public class HttpUtil {

    private static HttpUtil instance = new HttpUtil();
    private AsyncHttpClient httpClient;

    private HttpUtil() {
        httpClient = new AsyncHttpClient();
    }

    public static HttpUtil getInstance() {
        return instance;
    }

    /**
     * 从网络返回String数据
     * @param context
     * @param url
     * @param callBack
     */
    public void getStringFromNet(Context context, String url, final CallBackDepdependString callBack){
        httpClient.get(context, url, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (callBack != null) {
                    callBack.onSuccess(responseString);
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (callBack != null) {
                    callBack.onFail(responseString);
                }
            }
        });
    }

    /**
     *  这个是从网络返回byte数据
     * @param context
     * @param url
     * @param callBack
     */
    public void getByteFromNet(Context context, String url, final CallBackDepdependByte callBack) {
        httpClient.get(context, url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (callBack != null) {
                    callBack.onSuccess(responseBody);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (callBack != null) {
                    callBack.onFail(responseBody);
                }
            }
        });
    }

    /**
     * 请求网络返回的是json数据
     * @param context
     * @param url
     * @param callBack
     */
    public void getJsonFromNet(Context context, String url, final CallBackDepdependJson callBack) {
        httpClient.get(context, url, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if(callBack != null){
                    callBack.onSuccess(response);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (callBack != null) {
                    callBack.onFail(errorResponse);
                }
            }

        });
    }

    /**
     * 將一個文件上传到服务器
     * @param url
     * @param path
     * @param key
     * @throws Exception
     */
    public int[] postFileToNet(String url,String path,String key)throws Exception{
        final int[] statusCodes = {-1};
        File file = new File(path);
        if(file.exists() && file.length()>0){
            RequestParams params = new RequestParams();
            params.put(key,file);
            httpClient.post(url, params ,new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    statusCodes[0] = statusCode;
                    ToastUtil.showToast("文件上传成功");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                    statusCodes[0] = statusCode;
                    ToastUtil.showToast("文件上传失败");
                }
            });
        }else{
            ToastUtil.showToast("文件不存在");
        }
        return statusCodes;
    }

    public void postString (String url,String json,String key){
        RequestParams params = new RequestParams();
        params.put(key,json);
        httpClient.post(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (responseString != null) {
                    ToastUtil.showToast("上传成功");
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (responseString != null) {
                    ToastUtil.showToast("上传失败");
                }
            }
        });
    }


    /**
     * 返回值是String的回调接口
     */
    public interface CallBackDepdependString {

        void onSuccess(String successString);
        void onFail(String failString);

    }

    /**
     * 返回值是Byte的回调接口
     */
    public interface CallBackDepdependByte {

        void onSuccess(byte[] successResult);
        void onFail(byte[] failResult);

    }

    /**
     * 返回值是Json的回调接口
     */
    public interface CallBackDepdependJson {
        void onSuccess(JSONObject successJson);
        void onFail(JSONObject failJson);
    }
}
