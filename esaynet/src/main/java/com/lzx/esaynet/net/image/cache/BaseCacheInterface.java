package com.lzx.esaynet.net.image.cache;

import android.graphics.Bitmap;

/**
 * Created by lizhe on 2017/10/9.
 */

public interface BaseCacheInterface {
    /*
    * 以DiskLruCache为标准，不缓存图片而缓存io流。
    * 缓存InputStream的原因是为了照顾内存缓存的速度：
    *       因为内存缓存的使用频率远高于磁盘缓存，且需要快速完成存取，
    *       而且使用磁盘缓存自带线程，可以顺便完成一些耗时工作，
    *       所以在磁盘缓存的线程中将InputStream转为OutputStream。
    * */
    void add(String imageUrl, Bitmap bmp);
    Bitmap get(String imageUrl);
    boolean clear();
    int size();
    void close();
}
