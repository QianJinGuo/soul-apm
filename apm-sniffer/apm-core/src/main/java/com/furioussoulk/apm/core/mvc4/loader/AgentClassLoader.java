

package com.furioussoulk.apm.core.mvc4.loader;

import com.furioussoulk.apm.core.exception.PathNotFoundException;
import com.furioussoulk.apm.core.logger.LogManager;
import com.furioussoulk.apm.core.logger.api.ILogger;
import com.furioussoulk.apm.core.util.AgentPackagePathUtil;
import com.furioussoulk.apm.core.mvc4.PluginBootstrap;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * The <code>AgentClassLoader</code> represents a classloader,
 * which is in charge of finding plugins and interceptors.
 */
public class AgentClassLoader extends ClassLoader {
    private static final ILogger logger = LogManager.getLogger(AgentClassLoader.class);
    /**
     * The default_method class loader for the agent.
     */
    private static AgentClassLoader DEFAULT_LOADER;

    private List<File> classpath;
    private List<Jar> allJars;
    private ReentrantLock jarScanLock = new ReentrantLock();

    public AgentClassLoader(ClassLoader parent) throws PathNotFoundException {
        super(parent);
        File agentDictionary = AgentPackagePathUtil.getPath();
        classpath = new LinkedList<File>();
        classpath.add(new File(agentDictionary, "plugins"));
        classpath.add(new File(agentDictionary, "activations"));
    }

    public static AgentClassLoader getDefault() {
        return DEFAULT_LOADER;
    }

    /**
     * Init the default_method
     *
     * @return
     * @throws PathNotFoundException
     */
    public static AgentClassLoader initDefaultLoader() throws PathNotFoundException {
        DEFAULT_LOADER = new AgentClassLoader(PluginBootstrap.class.getClassLoader());
        return getDefault();
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        List<Jar> allJars = getAllJars();
        String path = name.replace('.', '/').concat(".class");
        for (Jar jar : allJars) {
            JarEntry entry = jar.jarFile.getJarEntry(path);
            if (entry != null) {
                try {
                    URL classFileUrl = new URL("jar:file:" + jar.sourceFile.getAbsolutePath() + "!/" + path);
                    byte[] data = null;
                    BufferedInputStream is = null;
                    ByteArrayOutputStream baos = null;
                    try {
                        is = new BufferedInputStream(classFileUrl.openStream());
                        baos = new ByteArrayOutputStream();
                        int ch = 0;
                        while ((ch = is.read()) != -1) {
                            baos.write(ch);
                        }
                        data = baos.toByteArray();
                    } finally {
                        if (is != null)
                            try {
                                is.close();
                            } catch (IOException ignored) {
                            }
                        if (baos != null)
                            try {
                                baos.close();
                            } catch (IOException ignored) {
                            }
                    }
                    return defineClass(name, data, 0, data.length);
                } catch (MalformedURLException e) {
                    logger.error(e, "find class fail.");
                } catch (IOException e) {
                    logger.error(e, "find class fail.");
                }
            }
        }

        this.getClass().getClassLoader().loadClass(name);
        throw new ClassNotFoundException("Can't find " + name);
    }

    @Override
    protected URL findResource(String name) {
        List<Jar> allJars = getAllJars();
        for (Jar jar : allJars) {
            JarEntry entry = jar.jarFile.getJarEntry(name);
            if (entry != null) {
                try {
                    return new URL("jar:file:" + jar.sourceFile.getAbsolutePath() + "!/" + name);
                } catch (MalformedURLException e) {
                    continue;
                }
            }
        }
        return null;
    }

    @Override
    protected Enumeration<URL> findResources(String name) throws IOException {
        List<URL> allResources = new LinkedList<URL>();
        List<Jar> allJars = getAllJars();
        for (Jar jar : allJars) {
            JarEntry entry = jar.jarFile.getJarEntry(name);
            if (entry != null) {
                allResources.add(new URL("jar:file:" + jar.sourceFile.getAbsolutePath() + "!/" + name));
            }
        }

        final Iterator<URL> iterator = allResources.iterator();
        return new Enumeration<URL>() {
            @Override
            public boolean hasMoreElements() {
                return iterator.hasNext();
            }

            @Override
            public URL nextElement() {
                return iterator.next();
            }
        };
    }

    private List<Jar> getAllJars() {
        if (allJars == null) {
            jarScanLock.lock();
            try {
                if (allJars == null) {
                    allJars = new LinkedList<Jar>();
                    for (File path : classpath) {
                        if (path.exists() && path.isDirectory()) {
                            String[] jarFileNames = path.list(new FilenameFilter() {
                                @Override
                                public boolean accept(File dir, String name) {
                                    return name.endsWith(".jar");
                                }
                            });
                            for (String fileName : jarFileNames) {
                                try {
                                    File file = new File(path, fileName);
                                    Jar jar = new Jar(new JarFile(file), file);
                                    allJars.add(jar);
                                    logger.info("{} loaded.", file.toString());
                                } catch (IOException e) {
                                    logger.error(e, "{} jar file can't be resolved", fileName);
                                }
                            }
                        }
                    }
                }
            } finally {
                jarScanLock.unlock();
            }
        }

        return allJars;
    }

    private class Jar {
        private JarFile jarFile;
        private File sourceFile;

        private Jar(JarFile jarFile, File sourceFile) {
            this.jarFile = jarFile;
            this.sourceFile = sourceFile;
        }
    }
}
