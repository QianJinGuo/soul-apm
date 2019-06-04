
package com.furioussoulk.collector.stream.worker.base;

import com.furioussoulk.apm.collector.core.module.ModuleManager;

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
