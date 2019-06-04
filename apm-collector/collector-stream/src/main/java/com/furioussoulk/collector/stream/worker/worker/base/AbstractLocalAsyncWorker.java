
package com.furioussoulk.collector.stream.worker.worker.base;

import com.furioussoulk.collector.stream.worker.collector.stream.worker.core.module.ModuleManager;

/**
 * The <code>AbstractLocalAsyncWorker</code> implementations represent workers,
 * which receive local asynchronous message.
 *
 */
public abstract class AbstractLocalAsyncWorker<INPUT, OUTPUT> extends AbstractWorker<INPUT, OUTPUT> {

    public AbstractLocalAsyncWorker(ModuleManager moduleManager) {
        super(moduleManager);
    }
}
