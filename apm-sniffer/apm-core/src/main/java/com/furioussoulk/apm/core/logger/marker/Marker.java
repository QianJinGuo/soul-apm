package com.furioussoulk.apm.core.logger.marker;

/**
 * 日志标记
 *
 * @author 孙证杰
 * @date 2019/5/31 13:03
 */
public class Marker {
    public String mark1;
    public String mark2;
    public String mark3;

    public Marker(String mark1, String mark2, String mark3) {
        this.mark1 = mark1;
        this.mark2 = mark2;
        this.mark3 = mark3;
    }

    public Marker(String mark1, String mark2) {
        this.mark1 = mark1;
        this.mark2 = mark2;
    }

    public Marker(String mark1) {
        this.mark1 = mark1;
    }

    @Override
    public String toString() {
        return "Marker{" +
                "mark1='" + mark1 + '\'' +
                ", mark2='" + mark2 + '\'' +
                ", mark3='" + mark3 + '\'' +
                '}';
    }
}
