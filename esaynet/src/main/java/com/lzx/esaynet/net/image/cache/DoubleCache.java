package com.lzx.esaynet.net.image.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.lzx.esaynet.net.image.cache.disk.DiskCache;
import com.lzx.esaynet.net.image.cache.memory.MemoryCache;

/**
 * Created by lizhe on 2017/10/11.
 */

public class DoubleCache implements BaseCacheInterface {
    private BaseCacheInterface mMemoryCache;
    private BaseCacheInterface mDiskCache;

    public DoubleCache(Context context, String diskCacheName, int maxDiskCacheSize) {
        mMemoryCache = new MemoryCache();
        mDiskCache = new DiskCache(context, diskCacheName, maxDiskCacheSize);
    }

    @Override
    public void add(String imageUrl, Bitmap bmp) {
        Log.d("Http", "double cache add");
        mMemoryCache.add(imageUrl, bmp);
        mDiskCache.add(imageUrl, bmp);
    }

    @Override
    public Bitmap get(String imageUrl) {
        Log.d("Http", "double cache get");
        Bitmap bitmap = mMemoryCache.get(imageUrl);
        if (bitmap == null){
            bitmap = mDiskCache.get(imageUrl);
        }
        return bitmap;
    }

    @Override
    public boolean clear() {
         clearMemoryCache();
        return clearDiskCache();
    }

    public void clearMemoryCache() {
        mMemoryCache.clear();
    }

    public boolean clearDiskCache(){
        return mDiskCache.clear();
    }

    public void close(){
        mMemoryCache.close();
        if (mDiskCache != null){
            mDiskCache.close();
        }
    }



    public int size(){
        return (int) ((mDiskCache.size() + mMemoryCache.size()) / 1024);
    }
}
