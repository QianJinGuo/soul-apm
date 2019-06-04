package com.furioussoulk.apm.collector.core.data;

import com.furioussoulk.apm.collector.core.define.Loader;
import com.furioussoulk.apm.core.exception.DefineException;
import com.furioussoulk.apm.collector.core.define.DefinitionLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public class StorageDefineLoader implements Loader<List<TableDefine>> {

    private final Logger logger = LoggerFactory.getLogger(StorageDefineLoader.class);

    @Override public List<TableDefine> load() throws DefineException {

        List<TableDefine> tableDefines = new LinkedList<>();

        StorageDefinitionFile definitionFile = new StorageDefinitionFile();
        logger.info("storage definition file name: {}", definitionFile.fileName());
        DefinitionLoader<TableDefine> definitionLoader = DefinitionLoader.load(TableDefine.class, definitionFile);
        for (TableDefine tableDefine : definitionLoader) {
            logger.info("loaded storage definition class: {}", tableDefine.getClass().getName());
            tableDefines.add(tableDefine);
        }
        return tableDefines;
    }
}
