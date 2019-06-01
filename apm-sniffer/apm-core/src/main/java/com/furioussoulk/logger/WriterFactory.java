

package com.furioussoulk.logger;

import com.furioussoulk.conf.Config;
import com.furioussoulk.exception.PathNotFoundException;
import com.furioussoulk.logger.api.IWriter;
import com.furioussoulk.util.AgentPackagePathUtil;
import com.furioussoulk.util.StringUtil;


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
