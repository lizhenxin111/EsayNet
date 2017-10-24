package com.lzx.esaynet.net;

/**
 * Created by lizhe on 2017/10/24.
 */

public abstract class HttpRunnable implements Runnable {
    private String name;

    public HttpRunnable(String name) {
        this.name = name;
    }

    public abstract String getName();

    @Override
    public abstract void run();
}
