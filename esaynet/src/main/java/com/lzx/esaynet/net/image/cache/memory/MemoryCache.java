package com.lzx.esaynet.net.image.cache.memory;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.lzx.esaynet.net.image.cache.BaseCacheInterface;
import com.lzx.esaynet.net.utils.StringUtils;

import java.io.InputStream;


/**
 * Created by lizhe on 2017/10/9.
 */

public class MemoryCache implements BaseCacheInterface {
    final LruCache<String, Bitmap> mMemoryCache;
    final LruCache<String, InputStream> memoryCache;

    public MemoryCache() {
        int memorySize = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = memorySize / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };
        memoryCache = new LruCache<String, InputStream>(cacheSize);
    }

    @Override
    public void add(String imageUrl, Bitmap bmp) {
        Log.d("Http", "Memory cache add");
        mMemoryCache.put(StringUtils.toHex(imageUrl), bmp) ;
    }

    public void add(String link, InputStream inputStream){
        Log.d("Http", "Memory cache add");
        memoryCache.put(StringUtils.toHex(link), inputStream);
    }

    @Override
    public Bitmap get(String imageUrl) {
        Log.d("Http", "Memory cache get");
        return mMemoryCache.get(StringUtils.toHex(imageUrl));
    }

    public InputStream get(String link, int i){
        return memoryCache.get(StringUtils.toHex(link));
    }

    @Override
    public boolean clear() {
        mMemoryCache.evictAll();
        return true;
    }

    @Override
    public int size() {
        return mMemoryCache.size();
    }

    @Override
    public void close() {
    }
}
