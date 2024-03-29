package com.furioussoulk.apm.collector.server.jetty;

import com.furioussoulk.apm.collector.server.Server;
import com.furioussoulk.apm.collector.server.ServerException;
import com.furioussoulk.apm.collector.server.ServerHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlet.ServletMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import java.net.InetSocketAddress;

public class JettyServer implements Server {

    private final Logger logger = LoggerFactory.getLogger(JettyServer.class);

    private final String host;
    private final int port;
    private final String contextPath;
    private org.eclipse.jetty.server.Server server;
    private ServletContextHandler servletContextHandler;

    public JettyServer(String host, int port, String contextPath) {
        this.host = host;
        this.port = port;
        this.contextPath = contextPath;
    }

    @Override
    public String hostPort() {
        return host + ":" + port;
    }

    @Override
    public String serverClassify() {
        return "Jetty";
    }

    @Override
    public void initialize() throws ServerException {
        server = new org.eclipse.jetty.server.Server(new InetSocketAddress(host, port));

        servletContextHandler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        servletContextHandler.setContextPath(contextPath);
        logger.info("http server root context path: {}", contextPath);

        server.setHandler(servletContextHandler);
    }

    @Override
    public void addHandler(ServerHandler handler) {
        ServletHolder servletHolder = new ServletHolder();
        servletHolder.setServlet((HttpServlet) handler);
        servletContextHandler.addServlet(servletHolder, ((JettyHandler) handler).pathSpec());
    }

    @Override
    public void start() throws ServerException {
        logger.info("start server, host: {}, port: {}", host, port);
        try {
            for (ServletMapping servletMapping : servletContextHandler.getServletHandler().getServletMappings()) {
                logger.info("jetty servlet mappings: {} register by {}", servletMapping.getPathSpecs(), servletMapping.getServletName());
            }
            server.start();
        } catch (Exception e) {
            throw new JettyServerException(e.getMessage(), e);
        }
    }
}
