package com.furioussoulk.collector.stream.worker.timer;

import com.furioussoulk.collector.stream.worker.collector.stream.worker.core.module.ModuleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Demo class
 *
 * @author 孙证杰
 * @email 200765821@qq.com
 * @date 2019/6/4 11:10
 */
public class PersistenceTimer {

    private final Logger logger = LoggerFactory.getLogger(PersistenceTimer.class);

    public void start(ModuleManager moduleManager, List<PersistenceWorker> persistenceWorkers) {
        logger.info("persistence timer start");
        //TODO timer value config
//        final long timeInterval = EsConfig.Es.Persistence.Timer.VALUE * 1000;
        final long timeInterval = 3;
        IBatchDAO batchDAO = moduleManager.find(StorageModule.NAME).getService(IBatchDAO.class);
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> extractDataAndSave(batchDAO, persistenceWorkers), 1, timeInterval, TimeUnit.SECONDS);
    }
}
