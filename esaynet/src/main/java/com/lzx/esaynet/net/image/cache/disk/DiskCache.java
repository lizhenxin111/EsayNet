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

    public DiskCache(Context context, String dirName, int maxSize) {
        try {
            File cacheDir = getDiskCacheDir(context, dirName);
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            mDiskCache = DiskLruCache.open(cacheDir, getAppVersion(context), 1, maxSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }


    public File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    @Override
    public void add(String link, Bitmap bmp) {
        Log.d("Http", "Disk cache add");
        try {
            DiskLruCache.Editor editor = mDiskCache.edit(StringUtils.toHex(link));
            if (editor != null) {
                OutputStream outputStream = editor.newOutputStream(0);
                if (bmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)) {
                    editor.commit();
                } else {
                    editor.abort();
                }
                mDiskCache.flush();
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
