package com.lzx.esaynet.net.image.cache.disk;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.lzx.esaynet.net.image.cache.BaseCacheInterface;
import com.lzx.esaynet.net.utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;


/**
 * Created by lizhe on 2017/10/9.
 */

public class DiskCache implements BaseCacheInterface {
    private DiskLruCache mDiskCache;
    private int maxSize;

    public DiskCache(Context context, String dirName, int maxSize) {
        this.maxSize = maxSize;
        try {
            File cacheDir = getCacheDir(context, dirName);
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            mDiskCache = DiskLruCache.open(cacheDir, getAppVersion(context), 1, maxSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return 1;
    }


    private File getCacheDir(Context context, String dirName){
        String path;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()){
            //SD卡存在且不可移除
            path = context.getExternalCacheDir().getPath();
        } else {
            path = context.getCacheDir().getPath();
        }
        return new File(path + File.separator + dirName);
    }

    @Override
    public void add(String link, Bitmap bmp) {
        Log.d("Http", "Disk cache add");
        DiskLruCache.Editor editor = null;
        try {
            editor = mDiskCache.edit(StringUtils.toHex(link));
            if (editor != null) {
                OutputStream outputStream = editor.newOutputStream(0);
                if (bmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)) {
                    editor.commit();
                } else {
                    editor.abort();
                }
            }
        } catch (IOException e) {
            Log.e("DiskCache", "#add: add error");
        }
    }

    @Override
    public Bitmap get(final String link) {
        String key = StringUtils.toHex(link);
        Log.d("Http", "get key : " + key);
        try {
            DiskLruCache.Snapshot snapshot = mDiskCache.get(key);
            if (snapshot != null){
                return BitmapFactory.decodeStream(snapshot.getInputStream(0));
            } else {
                Log.e("DiskCache", "#get: snapshot is null");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public boolean clear() {
        try {
            mDiskCache.delete();
            return true;
        } catch (IOException e) {
            Log.e("DiskCache", "#clear: clear error");
        }
        return false;
    }

    public void close(){
        if (!mDiskCache.isClosed()){
            try {
                mDiskCache.close();
            } catch (IOException e) {
                Log.e("DiskCache", "#close: close error");
            }
        }
    }

    public int size(){
        return (int) (mDiskCache.size() / 1024);
    }
}
