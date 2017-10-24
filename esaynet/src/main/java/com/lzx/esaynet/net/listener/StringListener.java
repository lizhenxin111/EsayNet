package com.lzx.esaynet.net.listener;

/**
 * Created by lizhe on 2017/10/24.
 */

public interface StringListener extends HttpListener<String>{
    @Override
    void onSuccess(String result);

    @Override
    void onFailed(String error);
}
