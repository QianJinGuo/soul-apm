package com.furioussoulk.apm.collector.core.module;

import java.util.HashMap;
import java.util.Properties;

/**
 * Modulization configurations. The {@link ModuleManager} is going to start, lookup, start modules based on this.
 *
 * @author 孙证杰
 * @email 200765821@qq.com
 * @date 2019/6/4 10:08
 */
public class ApplicationConfiguration {
    private HashMap<String, ModuleConfiguration> modules = new HashMap<>();

    public String[] moduleList() {
        return modules.keySet().toArray(new String[0]);
    }

    public ModuleConfiguration addModule(String moduleName) {
        ModuleConfiguration newModule = new ModuleConfiguration();
        modules.put(moduleName, newModule);
        return newModule;
    }

    public boolean has(String moduleName) {
        return modules.containsKey(moduleName);
    }

    public ModuleConfiguration getModuleConfiguration(String name) {
        return modules.get(name);
    }

    /**
     * The configurations about a certain module.
     */
    public class ModuleConfiguration {
        private HashMap<String, ProviderConfiguration> providers = new HashMap<>();

        private ModuleConfiguration() {
        }

        public Properties getProviderConfiguration(String name) {
            return providers.get(name).getProperties();
        }

        public boolean has(String name) {
            return providers.containsKey(name);
        }

        public ModuleConfiguration addProviderConfiguration(String name, Properties properties) {
            ProviderConfiguration newProvider = new ProviderConfiguration(properties);
            providers.put(name, newProvider);
            return this;
        }
    }

    /**
     * The configuration about a certain provider of a module.
     */
    public class ProviderConfiguration {
        private Properties properties;

        ProviderConfiguration(Properties properties) {
            this.properties = properties;
        }

        public Properties getProperties() {
            return properties;
        }
    }
}
