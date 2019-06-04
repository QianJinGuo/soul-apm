package com.furioussoulk.apm.collector.core.agent;

import com.furioussoulk.apm.collector.core.core.conf.Config;
import com.furioussoulk.apm.collector.core.exception.PathNotFoundException;
import com.furioussoulk.apm.collector.core.core.logger.LogManager;
import com.furioussoulk.apm.collector.core.core.logger.api.ILogger;
import com.furioussoulk.apm.collector.core.core.util.AgentPackagePathUtil;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;

import java.io.File;
import java.io.IOException;

public enum InstrumentDebuggingClass {
    /**
     * instance
     */
    INSTANCE;

    private static final ILogger logger = LogManager.getLogger(InstrumentDebuggingClass.class);
    private File debuggingClassesRootPath;

    public void log(TypeDescription typeDescription, DynamicType dynamicType) {
        if (!Config.Agent.IS_OPEN_DEBUGGING_CLASS) {
            return;
        }

        /**
         * try to do I/O things in synchronized way, to avoid unexpected situations.
         */
        synchronized (INSTANCE) {
            try {
                if (debuggingClassesRootPath == null) {
                    try {
                        debuggingClassesRootPath = new File(AgentPackagePathUtil.getPath(), "/debugging");
                        if (!debuggingClassesRootPath.exists()) {
                            debuggingClassesRootPath.mkdir();
                        }
                    } catch (PathNotFoundException e) {
                        logger.error(e, "Can't find the root path for creating /debugging folder.");
                    }
                }

                try {
                    dynamicType.saveIn(debuggingClassesRootPath);
                } catch (IOException e) {
                    logger.error(e, "Can't save class {} to file." + typeDescription.getActualName());
                }
            } catch (Throwable t) {
                logger.error(t, "Save debugging classes fail.");
            }
        }
    }
}
