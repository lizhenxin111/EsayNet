package com.lzx.esaynet.net.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;

import com.lzx.esaynet.net.HttpGet;
import com.lzx.esaynet.net.HttpRunnable;
import com.lzx.esaynet.net.utils.PostMainThread;
import com.lzx.esaynet.net.RequestQueue;
import com.lzx.esaynet.net.image.cache.BaseCacheInterface;
import com.lzx.esaynet.net.listener.ProgressListener;

/**
 * Created by lizhe on 2017/10/24.
 */

public class ImageLoader {
    private BaseCacheInterface mCache;
    private RequestQueue mQueue;
    private HttpGet mHttpGet;

    public ImageLoader(BaseCacheInterface mCache, RequestQueue mQueue) {
        this.mCache = mCache;
        this.mQueue = mQueue;
        mHttpGet = new HttpGet();
    }


    public void load(final String link, final ProgressListener<Bitmap> listener){
        Log.d("IMAGE", "load");
        if (mCache != null){
            if (mCache.get(link) != null){
                Log.d("IMAGE", "load from cache");
                listener.onSuccess(mCache.get(link));
                return;
            }
        }

        Log.d("IMAGE", "load from net");
        HttpRunnable runnable = mHttpGet.getBytes(link, new ProgressListener<byte[]>() {
            @Override
            public void onProgress(int progress, int length) {
                listener.onProgress(progress, length);
            }

            @Override
            public void onStart() {
                listener.onStart();
            }

            @Override
            public void onSuccess(byte[] result) {
                if (mCache != null){
                    mCache.add(link, BitmapFactory.decodeByteArray(result, 0, result.length));
                }
                listener.onSuccess(BitmapFactory.decodeByteArray(result, 0, result.length));
            }

            @Override
            public void onFailed(String error) {
                listener.onFailed(error);
            }
        });
        mQueue.execute(runnable);
    }
}
