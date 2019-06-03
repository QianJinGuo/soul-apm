package com.furioussoulk.queue;

import java.util.concurrent.ThreadFactory;

/**
 * 后台线程工厂
 *
 * @author 孙证杰
 * @date 2019/6/3 13:15
 */
public enum DaemonThreadFactory implements ThreadFactory {

    INSTANCE;

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
    }}
