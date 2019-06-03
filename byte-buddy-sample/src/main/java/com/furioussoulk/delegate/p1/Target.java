package com.furioussoulk.delegate.p1;

/**
 * byte buddy根据方法签名找到对应的拦截方法,如果有两个方法签名一样就报错。
 * @author 孙证杰
 * @date 2019/6/3 15:03
 */
public class Target {
    public static String a(String name) {
        return "Hello " + name + "!";
    }

    public static String b(int i) {
        return Integer.toString(i);
    }

    public static String c(Object o) {
        return o.toString();
    }
}
