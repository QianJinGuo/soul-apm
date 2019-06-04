package com.furioussoulk.collector.stream.worker.collector.stream.worker.core.module;

import com.furioussoulk.collector.stream.worker.collector.stream.worker.core.exception.*;
import com.furioussoulk.collector.stream.worker.core.exception.*;
import com.furioussoulk.core.exception.*;
import com.furioussoulk.exception.*;

import java.util.*;

/**
 * The <code>ModuleManager</code> takes charge of all {@link Module}s in collector.
 *
 * @author 孙证杰
 * @email 200765821@qq.com
 * @date 2019/6/4 10:17
 */
public class ModuleManager {

    private Map<String, Module> loadedModules = new HashMap<>();

    /**
     * Init the given modules
     *
     * @param applicationConfiguration
     */
    public void init(ApplicationConfiguration applicationConfiguration)
            throws ModuleNotFoundException,
            ProviderNotFoundException,
            ServiceNotProvidedException,
            CycleDependencyException {

        String[] moduleNames = applicationConfiguration.moduleList();
        ServiceLoader<Module> moduleServiceLoader = ServiceLoader.load(Module.class);
        LinkedList<String> moduleList = new LinkedList(Arrays.asList(moduleNames));

        Iterator<Module> moduleIter = moduleServiceLoader.iterator();
        while(moduleIter.hasNext()){
            Module module = moduleIter.next();

            Iterator<String> moduleNameIter = moduleList.iterator();

            while(moduleNameIter.hasNext()){
                String moduleName = moduleNameIter.next();
                if (moduleName.equals(module.name())) {
                    Module newInstance;
                    try {
                        newInstance = module.getClass().newInstance();
                    } catch (InstantiationException e) {
                        throw new ModuleNotFoundException(e);
                    } catch (IllegalAccessException e) {
                        throw new ModuleNotFoundException(e);
                    }
                    newInstance.prepare(this, applicationConfiguration.getModuleConfiguration(moduleName));
                    loadedModules.put(moduleName, newInstance);
                    moduleList.remove(moduleName);
                }
            }
        }

        if (moduleList.size() > 0) {
            throw new ModuleNotFoundException(moduleList.toString() + " missing.");
        }

        BootstrapFlow bootstrapFlow = new BootstrapFlow(loadedModules, applicationConfiguration);
        bootstrapFlow.start(this, applicationConfiguration);
        bootstrapFlow.notifyAfterCompleted();
    }

    public boolean has(String moduleName) {
        return loadedModules.get(moduleName) != null;
    }

    public Module find(String moduleName) throws ModuleNotFoundRuntimeException {
        Module module = loadedModules.get(moduleName);
        if (module == null) {
            throw new ModuleNotFoundRuntimeException(moduleName + " missing.");
        }

        return module;
    }
}
