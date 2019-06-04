

package com.furioussoulk.apm.collector.core.core.logger;

import com.furioussoulk.apm.collector.core.core.conf.Config;
import com.furioussoulk.apm.collector.core.core.logger.api.IWriter;
import com.furioussoulk.apm.collector.core.core.util.AgentPackagePathUtil;
import com.furioussoulk.apm.collector.core.exception.PathNotFoundException;
import com.furioussoulk.apm.collector.core.common.util.StringUtil;


public class WriterFactory {
    public static IWriter getLogWriter() {
        if (AgentPackagePathUtil.isPathFound()) {
            if (StringUtil.isEmpty(Config.Logging.DIR)) {
                try {
                    Config.Logging.DIR = AgentPackagePathUtil.getPath() + "/logs";
                } catch (PathNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return FileWriter.get();
        } else {
            return SystemOutWriter.INSTANCE;
        }
    }
}
