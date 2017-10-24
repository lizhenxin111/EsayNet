package com.lzx.esaynet.net;

import android.os.Handler;

import com.lzx.esaynet.net.listener.HttpListener;
import com.lzx.esaynet.net.listener.ProgressListener;

/**
 * Created by lizhe on 2017/10/24.
 */

public class PostMainThread {
    private Handler mHandler;

    public PostMainThread(Handler mHandler) {
        this.mHandler = mHandler;
    }

    public void postStart(final HttpListener listener) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                listener.onStart();
            }
        });
    }

    public void postFailed(final HttpListener listener, final String message) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                listener.onFailed(message);
            }
        });
    }

    public void postProgress(final HttpListener listener, final int progress, final int length) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ((ProgressListener) listener).onProgress(progress, length);
            }
        });
    }

    public void postSuccess(final HttpListener listener, final Object o) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                listener.onSuccess(o);
            }
        });
    }
}
