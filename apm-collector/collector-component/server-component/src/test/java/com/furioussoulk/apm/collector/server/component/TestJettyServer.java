package com.furioussoulk.apm.collector.server.component;

import com.furioussoulk.apm.collector.server.ServerException;
import com.furioussoulk.apm.collector.server.jetty.JettyServer;
import org.junit.Test;

/**
 * Demo class
 *
 * @author 孙证杰
 * @email 200765821@qq.com
 * @date 2019/6/5 19:35
 */
public class TestJettyServer {


    @Test
    public void runJettyServer() throws ServerException {
        JettyServer server = new JettyServer("localhost", 8080, "D:\\sdocs");
        server.initialize();
        server.start();
    }
}
