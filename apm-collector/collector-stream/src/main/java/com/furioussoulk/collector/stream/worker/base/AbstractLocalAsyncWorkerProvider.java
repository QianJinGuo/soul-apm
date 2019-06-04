package com.furioussoulk.collector.stream.worker.base;

public abstract class AbstractLocalAsyncWorkerProvider<INPUT, OUTPUT, WORKER_TYPE extends AbstractLocalAsyncWorker<INPUT, OUTPUT>> extends AbstractWorkerProvider<INPUT, OUTPUT, WORKER_TYPE> {

    public abstract int queueSize();

//    private final QueueCreatorService<INPUT> queueCreatorService;
//
//    public AbstractLocalAsyncWorkerProvider(ModuleManager moduleManager,
//                                            QueueCreatorService<INPUT> queueCreatorService) {
//        super(moduleManager);
//        this.queueCreatorService = queueCreatorService;
//    }
//
//    @Override
//    public final WorkerRef create(WorkerCreateListener workerCreateListener) {
//        WORKER_TYPE localAsyncWorker = workerInstance(getModuleManager());
//        workerCreateListener.addWorker(localAsyncWorker);
//
//        LocalAsyncWorkerRef<INPUT, OUTPUT> localAsyncWorkerRef = new LocalAsyncWorkerRef<>(localAsyncWorker);
//        QueueEventHandler<INPUT> queueEventHandler = queueCreatorService.create(queueSize(), localAsyncWorkerRef);
//        localAsyncWorkerRef.setQueueEventHandler(queueEventHandler);
//        return localAsyncWorkerRef;
//    }
}
