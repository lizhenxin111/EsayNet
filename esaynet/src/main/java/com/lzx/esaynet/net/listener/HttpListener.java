package com.lzx.esaynet.net.listener;

/**
 * Created by lizhe on 2017/10/24.
 * 网络请求的回掉函数
 */

public interface HttpListener<T>{
    void onStart();
    void onSuccess(T result);
    void onFailed(String error);
}
