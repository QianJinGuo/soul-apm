package com.furioussoulk.default_method.constructor;

/**
 * Demo class
 *
 * @author 孙证杰
 * @email 200765821@qq.com
 * @date 2019/6/3 17:56
 */
public class Source {

    private String name;

    public Source() {
        System.out.println("construct Source.class");
    }

    public Source(String name) {
        this.name = name;
    }
}
