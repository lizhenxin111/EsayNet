package com.lzx.esaynet.net.listener;

/**
 * Created by lizhe on 2017/10/24.
 */

public interface ProgressListener<T> extends HttpListener {

    void onProgress(int progress, int length);
}
