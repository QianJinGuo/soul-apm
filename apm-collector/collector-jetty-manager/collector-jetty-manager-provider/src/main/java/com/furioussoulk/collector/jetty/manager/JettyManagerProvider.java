/*
 * Copyright 2017, OpenSkywalking Organization All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Project repository: https://github.com/OpenSkywalking/skywalking
 */

package com.furioussoulk.collector.jetty.manager;

import com.furioussoulk.collector.jetty.manager.service.JettyManagerServiceImpl;
import org.skywalking.apm.collector.core.module.Module;
import org.skywalking.apm.collector.core.module.ModuleProvider;
import org.skywalking.apm.collector.core.module.ServiceNotProvidedException;
import org.skywalking.apm.collector.jetty.manager.service.JettyManagerService;
import org.skywalking.apm.collector.server.ServerException;
import org.skywalking.apm.collector.server.jetty.JettyServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author peng-yongsheng
 */
public class JettyManagerProvider extends ModuleProvider {

    private final Logger logger = LoggerFactory.getLogger(JettyManagerProvider.class);

    private Map<String, JettyServer> servers = new HashMap<>();

    @Override public String name() {
        return "jetty";
    }

    @Override public Class<? extends Module> module() {
        return JettyManagerModule.class;
    }

    @Override public void prepare(Properties config) throws ServiceNotProvidedException {
        this.registerServiceImplementation(JettyManagerService.class, new JettyManagerServiceImpl(servers));
    }

    @Override public void start(Properties config) throws ServiceNotProvidedException {

    }

    @Override public void notifyAfterCompleted() throws ServiceNotProvidedException {
        servers.values().forEach(server -> {
            try {
                server.start();
            } catch (ServerException e) {
                logger.error(e.getMessage(), e);
            }
        });
    }

    @Override public String[] requiredModules() {
        return new String[0];
    }
}
