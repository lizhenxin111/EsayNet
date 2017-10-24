package com.lzx.esaynet.net;

import android.support.v4.util.ArrayMap;

import com.lzx.esaynet.net.image.cache.BaseCacheInterface;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lizhe on 2017/10/24.
 */

public class RequestQueue {
    private ExecutorService mService;
    private ArrayMap<String, HttpRunnable> mRunnables;

    public RequestQueue() {
        mService = Executors.newCachedThreadPool();
        mRunnables = new ArrayMap<>();
    }

    public void execute(HttpRunnable runnable){
        if (mRunnables.get((String)(runnable.getName())) == null){
            mRunnables.put(runnable.getName(), runnable);
            mService.execute(runnable);
        } /*else {
            mService.execute(mRunnables.get((String)(runnable.getName())));
        }*/
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
