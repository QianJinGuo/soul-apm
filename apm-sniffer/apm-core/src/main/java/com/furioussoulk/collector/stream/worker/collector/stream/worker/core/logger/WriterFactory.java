

package com.furioussoulk.collector.stream.worker.collector.stream.worker.core.logger;

import com.furioussoulk.collector.stream.worker.collector.stream.worker.core.conf.Config;
import com.furioussoulk.collector.stream.worker.collector.stream.worker.core.logger.api.IWriter;
import com.furioussoulk.collector.stream.worker.collector.stream.worker.core.util.AgentPackagePathUtil;
import com.furioussoulk.collector.stream.worker.collector.stream.worker.core.exception.PathNotFoundException;
import com.furioussoulk.collector.stream.worker.collector.stream.worker.core.util.StringUtil;


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
