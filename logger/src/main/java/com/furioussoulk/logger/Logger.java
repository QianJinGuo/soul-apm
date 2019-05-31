package com.furioussoulk.logger;

import com.furioussoulk.marker.Marker;

/**
 * 日志
 *
 * @author 孙证杰-YCKJ1697
 * @email sunzhengjie@cloudwalk.cn
 * @date 2019/5/31 13:01
 */
public class Logger {

    private static boolean DEV_MODE = true;

    public static void debug(Marker marker, String message) {
        debug(marker,message,null);
    }

    public static void debug(Marker marker, String message, Throwable ex) {
        if (marker == null) {
            return;
        }

        if (DEV_MODE) {
            System.out.println(marker.toString() + "message:" + message);
            if(ex != null){
                ex.printStackTrace();
            }
        }
    }
}
