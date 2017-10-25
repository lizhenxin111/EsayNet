package com.lzx.esaynet.net.utils;

import android.os.Handler;
import android.os.Looper;

import com.lzx.esaynet.net.listener.HttpListener;
import com.lzx.esaynet.net.listener.ProgressListener;

/**
 * Created by lizhe on 2017/10/24.
 */

public class PostMainThread {


    public static void postStart(final HttpListener listener) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                listener.onStart();
            }
        });
    }

    public static void postFailed(final HttpListener listener, final String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                listener.onFailed(message);
            }
        });
    }

    public static void postProgress(final HttpListener listener, final int progress, final int length) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                ((ProgressListener) listener).onProgress(progress, length);
            }
        });
    }

    public static void postSuccess(final HttpListener listener, final Object o) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                listener.onSuccess(o);
            }
        });
    }
}
