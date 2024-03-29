package com.furioussoulk.collector.storage;

import com.furioussoulk.apm.collector.client.Client;
import com.furioussoulk.apm.collector.core.data.StorageDefineLoader;
import com.furioussoulk.apm.collector.core.data.TableDefine;
import com.furioussoulk.apm.collector.core.exception.DefineException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class StorageInstaller {

    private final Logger logger = LoggerFactory.getLogger(StorageInstaller.class);

    public final void install(Client client) throws StorageException {
        StorageDefineLoader defineLoader = new StorageDefineLoader();
        try {
            List<TableDefine> tableDefines = defineLoader.load();
            defineFilter(tableDefines);
            Boolean debug = System.getProperty("debug") != null;

            for (TableDefine tableDefine : tableDefines) {
                tableDefine.initialize();
                if (!isExists(client, tableDefine)) {
                    logger.info("table: {} not exists", tableDefine.getName());
                    createTable(client, tableDefine);
                } else if (debug) {
                    logger.info("table: {} exists", tableDefine.getName());
                    deleteTable(client, tableDefine);
                    createTable(client, tableDefine);
                }
            }
        } catch (DefineException e) {
            throw new StorageInstallException(e.getMessage(), e);
        }
    }

    protected abstract void defineFilter(List<TableDefine> tableDefines);

    protected abstract boolean isExists(Client client, TableDefine tableDefine) throws StorageException;

    protected abstract boolean deleteTable(Client client, TableDefine tableDefine) throws StorageException;

    protected abstract boolean createTable(Client client, TableDefine tableDefine) throws StorageException;
}
