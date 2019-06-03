package com.furioussoulk.delegate.p2;

import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Demo class
 *
 * @author 孙证杰
 * @email 200765821@qq.com
 * @date 2019/6/3 15:17
 */
public class LoggerInterceptor {

    public static List<String> log(@SuperCall Callable<List<String>> zuper) throws Exception {
        System.out.println("Calling database");
        try {
            return zuper.call();
        } finally {
            System.out.println("Returned from database");
        }
    }
}
