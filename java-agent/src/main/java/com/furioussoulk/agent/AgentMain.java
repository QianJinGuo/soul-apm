package com.furioussoulk.agent;

import com.furioussoulk.logger.Logger;
import com.furioussoulk.marker.Marker;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.util.jar.JarFile;

/**
 * main入口
 *
 * @author 孙证杰-YCKJ1697
 * @email sunzhengjie@cloudwalk.cn
 * @date 2019/5/31 12:57
 */
public class AgentMain {

    public static void premain(String args, Instrumentation ins) {
        init(args, ins);
    }

    public synchronized static void init(String args, Instrumentation ins) {
        if (Boolean.getBoolean("ElasticApm.attached")) {
            // agent is already attached; don't attach twice
            // don't fail as this is a valid case
            // for example, Spring Boot restarts the application in dev mode
            Logger.debug(new Marker("AgentMain"),"init");
            return;
        }
        try {
            FileSystems.getDefault();
            final File agentJarFile = getAgentJarFile();
            try (JarFile jarFile = new JarFile(agentJarFile)) {
                ins.appendToBootstrapClassLoaderSearch(jarFile);
            }
            // invoking via reflection to make sure the class is not loaded by the system classloader,
            // but only from the bootstrap classloader
            Class.forName("co.elastic.apm.agent.bci.ElasticApmAgent", true, null)
                    .getMethod("initialize", String.class, Instrumentation.class, File.class)
                    .invoke(null, args, ins, agentJarFile);
            System.setProperty("ElasticApm.attached", Boolean.TRUE.toString());
        } catch (Exception e) {
            System.err.println("Failed to start agent");
            e.printStackTrace();
        }
    }

    private static File getAgentJarFile() throws URISyntaxException {
        final File agentJar = new File(AgentMain.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        if (!agentJar.getName().endsWith(".jar")) {
            throw new IllegalStateException("Agent is not a jar file: " + agentJar);
        }
        return agentJar.getAbsoluteFile();
    }
}
