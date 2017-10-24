package com.lzx.esaynet.net.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;

import com.lzx.esaynet.net.HttpRunnable;
import com.lzx.esaynet.net.PostMainThread;
import com.lzx.esaynet.net.RequestQueue;
import com.lzx.esaynet.net.image.cache.BaseCacheInterface;
import com.lzx.esaynet.net.listener.ProgressListener;
import com.lzx.esaynet.net.utils.CloseableUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by lizhe on 2017/10/24.
 */

public class ImageLoader {
    private BaseCacheInterface mCache;
    private RequestQueue mQueue;
    private PostMainThread mainThread;
    public ImageLoader(BaseCacheInterface mCache, RequestQueue mQueue) {
        this.mCache = mCache;
        this.mQueue = mQueue;
        mainThread = new PostMainThread(new Handler());
    }


    public void load(final String link, final ProgressListener listener){
        if (mCache.get(link) != null){
            listener.onSuccess(mCache.get(link));
        }

        HttpRunnable runnable = new HttpRunnable(link) {
            @Override
            public String getName() {
                return null;
            }

            @Override
            public void run() {
                mainThread.postStart(listener);
                HttpURLConnection conn = null;
                try {
                    URL url = new URL(link);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    Bitmap bitmap = streamToBitmap(conn.getInputStream(), conn.getContentLength(), listener);
                    mainThread.postSuccess(listener, bitmap);
                    mCache.add(link, bitmap);
                } catch (MalformedURLException e) {
                    //e.printStackTrace();
                } catch (IOException e) {
                    //e.printStackTrace();
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        };
        mQueue.execute(runnable);
    }

    private Bitmap streamToBitmap(InputStream inputStream, int length, ProgressListener listener) {
        int progress = 0;
        int l = -1;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] bytes = new byte[10];
        try {
            while ((l = inputStream.read(bytes)) != -1) {
                progress += l;
                mainThread.postProgress(listener, (int) progress, length);
                bos.write(bytes, 0, l);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.close(bos);
        }
        return BitmapFactory.decodeByteArray(bos.toByteArray(), 0, bos.size());
    }
}
