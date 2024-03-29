package com.furioussoulk.bytebuddy.sample.delegate.p5;

import com.furioussoulk.bytebuddy.sample.delegate.p2.LoggerInterceptor;
import com.furioussoulk.bytebuddy.sample.delegate.p2.MemoryDatabase;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Demo class
 *
 * @author 孙证杰
 * @email 200765821@qq.com
 * @date 2019/6/3 15:43
 */
public class LoggingMemoryDatabase extends MemoryDatabase {

    private class LoadMethodSuperCall implements Callable {

        private final String info;

        private LoadMethodSuperCall(String info) {
            this.info = info;
        }

        @Override
        public Object call() throws Exception {
            return LoggingMemoryDatabase.super.load(info);
        }
    }

    @Override
    public List<String> load(String info) throws Exception {
        return LoggerInterceptor.log(new LoadMethodSuperCall(info));
    }
}
