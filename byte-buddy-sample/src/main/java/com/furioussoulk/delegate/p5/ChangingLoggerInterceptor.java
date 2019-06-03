package com.furioussoulk.delegate.p5;

import com.furioussoulk.delegate.p2.MemoryDatabase;
import net.bytebuddy.implementation.bind.annotation.Super;

import java.util.List;

/**
 * Demo class
 *
 * @author 孙证杰
 * @email 200765821@qq.com
 * @date 2019/6/3 15:44
 */
public class ChangingLoggerInterceptor {

    public static List<String> log(String info, @Super MemoryDatabase zuper) throws Exception {
        System.out.println("Calling database");
        try {
            return zuper.load(info + " (logged access)");
        } finally {
            System.out.println("Returned from database");
        }
    }
}
