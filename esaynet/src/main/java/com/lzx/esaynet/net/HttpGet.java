package com.lzx.esaynet.net;

import android.os.Handler;

import com.lzx.esaynet.net.listener.StringListener;
import com.lzx.esaynet.net.utils.CloseableUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by lizhe on 2017/10/24.
 */

public class HttpGet {

    private PostMainThread mainThread;

    public HttpGet() {
        mainThread = new PostMainThread(new Handler());
    }

    public HttpRunnable getString(final String link, final StringListener listener) {

        return new HttpRunnable(link) {
            @Override
            public String getName() {
                return link;
            }

            @Override
            public void run() {
                mainThread.postStart(listener);
                HttpURLConnection conn = null;
                try {
                    URL url = new URL(link);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    String result = streamToString(conn.getInputStream());
                    mainThread.postSuccess(listener, result);
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
    }

    private String streamToString(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        StringBuilder builder = new StringBuilder();
        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.close(reader);
        }
        return builder.toString();
    }
}