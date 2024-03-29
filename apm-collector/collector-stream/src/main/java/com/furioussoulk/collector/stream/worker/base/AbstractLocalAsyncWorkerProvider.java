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

package com.furioussoulk.collector.stream.worker.base;

import org.skywalking.apm.collector.core.module.ModuleManager;
import org.skywalking.apm.collector.queue.base.QueueEventHandler;
import org.skywalking.apm.collector.queue.service.QueueCreatorService;

/**
 * @author peng-yongsheng
 */
public abstract class AbstractLocalAsyncWorkerProvider<INPUT, OUTPUT, WORKER_TYPE extends AbstractLocalAsyncWorker<INPUT, OUTPUT>> extends AbstractWorkerProvider<INPUT, OUTPUT, WORKER_TYPE> {

    public abstract int queueSize();

    private final QueueCreatorService<INPUT> queueCreatorService;

    public AbstractLocalAsyncWorkerProvider(ModuleManager moduleManager,
        QueueCreatorService<INPUT> queueCreatorService) {
        super(moduleManager);
        this.queueCreatorService = queueCreatorService;
    }

    @Override
    public final WorkerRef create(WorkerCreateListener workerCreateListener) {
        WORKER_TYPE localAsyncWorker = workerInstance(getModuleManager());
        workerCreateListener.addWorker(localAsyncWorker);

        LocalAsyncWorkerRef<INPUT, OUTPUT> localAsyncWorkerRef = new LocalAsyncWorkerRef<>(localAsyncWorker);
        QueueEventHandler<INPUT> queueEventHandler = queueCreatorService.create(queueSize(), localAsyncWorkerRef);
        localAsyncWorkerRef.setQueueEventHandler(queueEventHandler);
        return localAsyncWorkerRef;
    }
}
