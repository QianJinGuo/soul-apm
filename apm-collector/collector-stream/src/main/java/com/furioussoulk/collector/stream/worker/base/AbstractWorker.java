package com.furioussoulk.collector.stream.worker.base;

import com.furioussoulk.collector.stream.exception.WorkerException;
import com.furioussoulk.apm.collector.core.graph.Next;
import com.furioussoulk.apm.collector.core.graph.NodeProcessor;
import com.furioussoulk.apm.collector.core.module.ModuleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Demo class
 *
 * @author 孙证杰
 * @email 200765821@qq.com
 * @date 2019/6/4 11:14
 */
public abstract class AbstractWorker<INPUT, OUTPUT> implements NodeProcessor<INPUT, OUTPUT> {

    private final Logger logger = LoggerFactory.getLogger(AbstractWorker.class);

    private final ModuleManager moduleManager;

    public AbstractWorker(ModuleManager moduleManager) {
        this.moduleManager = moduleManager;
    }

    public final ModuleManager getModuleManager() {
        return moduleManager;
    }

    private Next<OUTPUT> next;

    /**
     * The data process logic in this method.
     *
     * @param message Cast the message object to a expect subclass.
     * @throws WorkerException Don't handle the exception, throw it.
     */
    protected abstract void onWork(INPUT message) throws WorkerException;
}
