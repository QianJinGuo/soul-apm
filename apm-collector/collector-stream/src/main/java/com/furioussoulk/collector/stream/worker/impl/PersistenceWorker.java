package com.furioussoulk.collector.stream.worker.impl;

import com.furioussoulk.apm.collector.core.data.Data;
import com.furioussoulk.apm.collector.core.graph.Next;
import com.furioussoulk.apm.collector.core.module.ModuleManager;
import com.furioussoulk.collector.stream.exception.WorkerException;
import com.furioussoulk.collector.stream.worker.base.AbstractLocalAsyncWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Demo class
 *
 * @author 孙证杰
 * @email 200765821@qq.com
 * @date 2019/6/4 11:22
 */
public class PersistenceWorker <INPUT extends Data, OUTPUT extends Data> extends AbstractLocalAsyncWorker<INPUT, OUTPUT> {


    private final Logger logger = LoggerFactory.getLogger(PersistenceWorker.class);

    public PersistenceWorker(ModuleManager moduleManager) {
        super(moduleManager);
    }

    @Override
    protected void onWork(INPUT message) throws WorkerException {

    }

    @Override
    public int id() {
        return 0;
    }

    @Override
    public void process(INPUT input, Next<OUTPUT> next) {

    }

//    private final DataCache dataCache;
//    private final IBatchDAO batchDAO;


}
