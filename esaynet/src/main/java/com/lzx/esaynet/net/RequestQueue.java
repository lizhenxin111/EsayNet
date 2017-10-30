package com.lzx.esaynet.net;

import android.support.v4.util.ArrayMap;

import com.lzx.esaynet.net.image.cache.BaseCacheInterface;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lizhe on 2017/10/24.
 * 线程队列
 */

public class RequestQueue {
    private ExecutorService mService;

    public RequestQueue() {
        mService = Executors.newCachedThreadPool();
    }

    public void execute(HttpRunnable runnable){
        mService.execute(runnable);
    }

    public void execute(HttpRunnable runnable, BaseCacheInterface cache){
        if (cache.get(runnable.getName()) != null){
            return;
        }
        execute(runnable);
    }

    public void shutdown(){
        mService.shutdown();
    }
}
