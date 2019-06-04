

package com.furioussoulk.apm.core.logger;

import com.furioussoulk.apm.core.conf.Config;
import com.furioussoulk.apm.core.logger.api.IWriter;
import com.furioussoulk.apm.core.util.AgentPackagePathUtil;
import com.furioussoulk.apm.core.exception.PathNotFoundException;
import com.furioussoulk.apm.common.util.StringUtil;


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
