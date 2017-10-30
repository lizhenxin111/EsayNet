package com.lzx.esaynet.net;

/**
 * Created by lizhe on 2017/10/24.
 * 封装线程单元
 */

public abstract class HttpRunnable implements Runnable {
    private String name;

    public HttpRunnable(String name) {
        runnableConstructorStart();
        this.name = name;
    }

    public abstract void runnableConstructorStart();

    public String getName(){
        return name;
    }

    @Override
    public abstract void run();
}
