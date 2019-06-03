package com.furioussoulk.delegate.p4;

import com.furioussoulk.delegate.p2.MemoryDatabase;
import net.bytebuddy.implementation.bind.annotation.Pipe;

import java.util.List;

/**
 * @Pipe看起来只是把请求转发到的别的地方
 *
 * @author 孙证杰
 * @email 200765821@qq.com
 * @date 2019/6/3 15:28
 */
public class ForwardingLoggerInterceptor {

    // constructor omitted
    private MemoryDatabase memoryDatabase;

    public ForwardingLoggerInterceptor(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
    }

    public List<String> log(@Pipe Forwarder<List<String>, MemoryDatabase> pipe) {
        System.out.println("Calling database");
        try {
            return pipe.to(memoryDatabase);
        } finally {
            System.out.println("Returned from database");
        }
    }
}
