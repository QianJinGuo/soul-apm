package com.furioussoulk.util;

import com.furioussoulk.exception.PathNotFoundException;
import com.furioussoulk.logger.LogManager;
import com.furioussoulk.logger.api.ILogger;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class AgentPackagePathUtil {

    private static final ILogger logger = LogManager.getLogger(AgentPackagePathUtil.class);

    private static File AGENT_PACKAGE_PATH;

    public static File getPath() throws PathNotFoundException {
        if (AGENT_PACKAGE_PATH == null) {
            AGENT_PACKAGE_PATH = findPath();
        }
        return AGENT_PACKAGE_PATH;
    }

    public static boolean isPathFound() {
        return AGENT_PACKAGE_PATH != null;
    }

    private static File findPath() throws PathNotFoundException {
        String classResourcePath = AgentPackagePathUtil.class.getName().replaceAll("\\.", "/") + ".class";

        URL resource = ClassLoader.getSystemClassLoader().getResource(classResourcePath);
        if (resource != null) {
            String urlString = resource.toString();

            logger.debug("The beacon class location is {}.", urlString);

            int insidePathIndex = urlString.indexOf('!');
            boolean isInJar = insidePathIndex > -1;

            if (isInJar) {
                urlString = urlString.substring(urlString.indexOf("file:"), insidePathIndex);
                File agentJarFile = null;
                try {
                    agentJarFile = new File(new URL(urlString).getFile());
                } catch (MalformedURLException e) {
                    logger.error(e, "Can not locate agent jar file by url:" + urlString);
                }
                if (agentJarFile != null && agentJarFile.exists()) {
                    return agentJarFile.getParentFile();
                }
            } else {
                String classLocation = urlString.substring(urlString.indexOf("file:"), urlString.length() - classResourcePath.length());
                return new File(classLocation);
            }
        }

        logger.error("Can not locate agent jar file.");
        throw new PathNotFoundException("Can not locate agent jar file.");
    }
}
