package com.lzx.esaynet.net;

import android.accounts.NetworkErrorException;
import android.util.Log;

import com.lzx.esaynet.net.listener.ProgressListener;
import com.lzx.esaynet.net.listener.StringListener;
import com.lzx.esaynet.net.utils.CloseableUtils;
import com.lzx.esaynet.net.utils.PostMainThread;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by lizhe on 2017/10/24.
 * http请求
 */

public class HttpGet {
    public HttpGet() {
    }

    /**
     * 字符串请求
     * @param link      请求链接
     * @param listener  回掉函数
     * @return
     */
    public HttpRunnable getString(final String link, final StringListener listener) {

        return new HttpRunnable(link) {
            @Override
            public void runnableConstructorStart() {
                PostMainThread.postStart(listener);
            }

            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    URL url = new URL(link);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();
                    int stateCode = conn.getResponseCode();
                    Log.d("test", "code : " + stateCode);
                    if (stateCode / 100 == 2){      //请求成功，返回结果
                        PostMainThread.postSuccess(listener, streamToString(conn.getInputStream()));
                    } else {                        //请求失败，处理返回结果
                        handleRequestCode(stateCode);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        };
    }



    /**
     * 请求字节流。此类请求可以获得下载进度
     * @param link
     * @param listener
     * @return
     */
    public HttpRunnable getBytes(final String link, final ProgressListener<byte[]> listener){
        return new HttpRunnable(link) {
            @Override
            public void runnableConstructorStart() {
                PostMainThread.postStart(listener);
            }

            @Override
            public void run() {
                Log.d("getBytes", "start");
                HttpURLConnection conn = null;
                try {
                    URL url = new URL(link);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();
                    int stateCode = conn.getResponseCode();
                    Log.d("getBytes", "state code : " + stateCode + "   " + (stateCode/100));
                    if (stateCode / 100 == 2){      //请求成功，返回结果
                        PostMainThread.postSuccess(listener, streamToBytes(conn.getInputStream(), conn.getContentLength(), listener));
                    } else {                        //请求失败，处理返回结果
                        handleRequestCode(stateCode);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        };
    }



    /**
     * 处理http状态码
     * @param requestCode
     */
    private void handleRequestCode(int requestCode){
        try {
            switch (requestCode / 100){
                case 3:
                    break;
                case 4:
                    switch (requestCode % 400){
                        case 0:
                            throw new NetworkErrorException("400：Bad Request 请求错误");
                        case 1:
                            throw new NetworkErrorException("401：Unauthorized 未认证(该请求要求用户认证)");
                        case 3:
                            throw new NetworkErrorException("403：Forbidden 不明原因的禁止");
                        case 4:
                            throw new NetworkErrorException("404：Not Found 未找到");
                    }
                    break;
                case 5:
                    switch (requestCode % 500){
                        case 0:
                            throw new NetworkErrorException("500：Internal Server Error  服务器错误");
                        case 1:
                            throw new NetworkErrorException("501：Not Implemented  没有实现");
                        case 2:
                            throw new NetworkErrorException("502 Bad Gateway  错误的网关");
                        case 3:
                            throw new NetworkErrorException("503： Service Unavailable  服务器临时过载");
                    }
                    break;
                default:break;
            }
        } catch (NetworkErrorException e) {
            e.printStackTrace();
        }
    }


    /**
     * 输入流转换为String字符串
     * @param inputStream
     * @return
     */
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

    private byte[] streamToBytes(InputStream inputStream, int length, ProgressListener listener){
        Log.d("getBytes", "streamToBytes");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] bytes = new byte[length > 100 ? length / 100 : 1];
        int rl, progress = 0;
        try {
            while ((rl = inputStream.read(bytes)) > 0){
                progress += rl;
                PostMainThread.postProgress(listener, progress, length);
                bos.write(bytes, 0, rl);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.close(bos);
        }
        return null;
    }
}