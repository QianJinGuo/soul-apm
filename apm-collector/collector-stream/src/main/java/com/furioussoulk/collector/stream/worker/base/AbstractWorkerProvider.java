package com.furioussoulk.collector.stream.worker.base;

public abstract class AbstractWorkerProvider<INPUT, OUTPUT, WORKER_TYPE extends AbstractWorker<INPUT, OUTPUT>> implements Provider {

//    private final ModuleManager moduleManager;
//
//    public AbstractWorkerProvider(ModuleManager moduleManager) {
//        this.moduleManager = moduleManager;
//    }
//
//    public final ModuleManager getModuleManager() {
//        return moduleManager;
//    }
//
//    public abstract WORKER_TYPE workerInstance(ModuleManager moduleManager);
}
