

package com.furioussoulk.collector.stream.worker.collector.stream.worker.core.plugin;

import com.furioussoulk.collector.stream.worker.collector.stream.worker.core.logger.LogManager;
import com.furioussoulk.collector.stream.worker.collector.stream.worker.core.logger.api.ILogger;
import com.furioussoulk.collector.stream.worker.collector.stream.worker.core.exception.PathNotFoundException;
import com.furioussoulk.collector.stream.worker.collector.stream.worker.core.plugin.loader.AgentClassLoader;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Plugins finder.
 * Use {@link PluginResourcesResolver} to find all plugins,
 * and ask {@link PluginCfg} to load all plugin definitions.
 */
public class PluginBootstrap {
    private static final ILogger logger = LogManager.getLogger(PluginBootstrap.class);

    /**
     * load all plugins.
     *
     * @return plugin definition list.
     */
    public List<AbstractClassEnhancePluginDefine> loadPlugins() throws PathNotFoundException {
        AgentClassLoader.initDefaultLoader();

        PluginResourcesResolver resolver = new PluginResourcesResolver();
        List<URL> resources = resolver.getResources();

        if (resources == null || resources.size() == 0) {
            logger.info("no plugin files (skywalking-plugin.def) found, continue to start application.");
            return new ArrayList<AbstractClassEnhancePluginDefine>();
        }

        for (URL pluginUrl : resources) {
            try {
                PluginCfg.INSTANCE.load(pluginUrl.openStream());
            } catch (Throwable t) {
                logger.error(t, "plugin file [{}] init failure.", pluginUrl);
            }
        }

        List<PluginDefine> pluginClassList = PluginCfg.INSTANCE.getPluginClassList();

        List<AbstractClassEnhancePluginDefine> plugins = new ArrayList<AbstractClassEnhancePluginDefine>();
        for (PluginDefine pluginDefine : pluginClassList) {
            try {
                logger.debug("loading plugin class {}.", pluginDefine.getDefineClass());
                AbstractClassEnhancePluginDefine plugin =
                        (AbstractClassEnhancePluginDefine) Class.forName(pluginDefine.getDefineClass(),
                                true,
                                AgentClassLoader.getDefault())
                                .newInstance();
                plugins.add(plugin);
            } catch (Throwable t) {
                logger.error(t, "load plugin [{}] failure.", pluginDefine.getDefineClass());
            }
        }

        return plugins;

    }

}