package com.furioussoulk.agent.stream.buffer;

import com.furioussoulk.apm.collector.core.module.ModuleManager;
import com.furioussoulk.apm.collector.core.util.Const;
import com.furioussoulk.apm.collector.core.util.StringUtils;
import com.furioussoulk.apm.collector.core.util.TimeBucketUtils;
import com.furioussoulk.network.proto.UpstreamSegment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public enum SegmentBufferManager {
    /**
     * instance
     */
    INSTANCE;

    private final Logger logger = LoggerFactory.getLogger(SegmentBufferManager.class);

    public static final String DATA_FILE_PREFIX = "data";
    private FileOutputStream outputStream;

    public synchronized void initialize(ModuleManager moduleManager) {
        logger.info("segment buffer initialize");
        try {
            OffsetManager.INSTANCE.initialize();
            if (new File(BufferFileConfig.BUFFER_PATH).mkdirs()) {
                newDataFile();
            } else {
                String writeFileName = OffsetManager.INSTANCE.getWriteFileName();
                if (StringUtils.isNotEmpty(writeFileName)) {
                    File dataFile = new File(BufferFileConfig.BUFFER_PATH + writeFileName);
                    if (dataFile.exists()) {
                        outputStream = new FileOutputStream(new File(BufferFileConfig.BUFFER_PATH + writeFileName), true);
                    } else {
                        newDataFile();
                    }
                } else {
                    newDataFile();
                }
            }
            SegmentBufferReader.INSTANCE.initialize(moduleManager);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public synchronized void writeBuffer(UpstreamSegment segment) {
        try {
            segment.writeDelimitedTo(outputStream);
            long position = outputStream.getChannel().position();
            if (position > BufferFileConfig.BUFFER_SEGMENT_MAX_FILE_SIZE) {
                newDataFile();
            } else {
                OffsetManager.INSTANCE.setWriteOffset(position);
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void newDataFile() throws IOException {
        logger.debug("create new segment buffer file");
        String timeBucket = String.valueOf(TimeBucketUtils.INSTANCE.getSecondTimeBucket(System.currentTimeMillis()));
        String writeFileName = DATA_FILE_PREFIX + "_" + timeBucket + "." + Const.FILE_SUFFIX;
        File dataFile = new File(BufferFileConfig.BUFFER_PATH + writeFileName);
        dataFile.createNewFile();
        OffsetManager.INSTANCE.setWriteOffset(writeFileName, 0);
        try {
            if (outputStream != null) {
                outputStream.close();
            }
            outputStream = new FileOutputStream(dataFile);
            outputStream.getChannel().position(0);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public synchronized void flush() {
    }
}
