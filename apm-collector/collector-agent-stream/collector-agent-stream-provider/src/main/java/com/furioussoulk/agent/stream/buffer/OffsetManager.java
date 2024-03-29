package com.furioussoulk.agent.stream.buffer;

import com.furioussoulk.agent.stream.util.FileUtils;
import com.furioussoulk.apm.collector.core.util.CollectionUtils;
import com.furioussoulk.apm.collector.core.util.Const;
import com.furioussoulk.apm.collector.core.util.TimeBucketUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public enum OffsetManager {
    /**
     * instance
     */
    INSTANCE;

    private final Logger logger = LoggerFactory.getLogger(OffsetManager.class);

    private static final String OFFSET_FILE_PREFIX = "offset";
    private File offsetFile;
    private Offset offset;
    private boolean initialized = false;
    private RandomAccessFile randomAccessFile = null;
    private String lastOffsetRecord = Const.EMPTY_STRING;

    public synchronized void initialize() throws IOException {
        if (!initialized) {
            this.offset = new Offset();
            File dataPath = new File(BufferFileConfig.BUFFER_PATH);
            if (dataPath.mkdirs()) {
                createOffsetFile();
            } else {
                File[] offsetFiles = dataPath.listFiles(new PrefixFileNameFilter());
                if (CollectionUtils.isNotEmpty(offsetFiles) && offsetFiles.length > 0) {
                    for (int i = 0; i < offsetFiles.length; i++) {
                        if (i != offsetFiles.length - 1) {
                            offsetFiles[i].delete();
                        } else {
                            offsetFile = offsetFiles[i];
                        }
                    }
                } else {
                    createOffsetFile();
                }
            }
            String offsetRecord = FileUtils.INSTANCE.readLastLine(offsetFile);
            offset.deserialize(offsetRecord);
            initialized = true;

            Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(this::flush, 10, 3, TimeUnit.SECONDS);
        }
    }

    private void createOffsetFile() throws IOException {
        String timeBucket = String.valueOf(TimeBucketUtils.INSTANCE.getSecondTimeBucket(System.currentTimeMillis()));
        String offsetFileName = OFFSET_FILE_PREFIX + "_" + timeBucket + "." + Const.FILE_SUFFIX;
        offsetFile = new File(BufferFileConfig.BUFFER_PATH + offsetFileName);
        this.offset.getWriteOffset().setWriteFileName(Const.EMPTY_STRING);
        this.offset.getWriteOffset().setWriteFileOffset(0);
        this.offset.getReadOffset().setReadFileName(Const.EMPTY_STRING);
        this.offset.getReadOffset().setReadFileOffset(0);
        this.flush();
    }

    public void flush() {
        String offsetRecord = offset.serialize();
        if (!lastOffsetRecord.equals(offsetRecord)) {
            if (offsetFile.length() >= BufferFileConfig.BUFFER_OFFSET_MAX_FILE_SIZE) {
                nextFile();
            }
            FileUtils.INSTANCE.writeAppendToLast(offsetFile, randomAccessFile, offsetRecord);
            lastOffsetRecord = offsetRecord;
        }
    }

    private void nextFile() {
        String timeBucket = String.valueOf(TimeBucketUtils.INSTANCE.getSecondTimeBucket(System.currentTimeMillis()));
        String offsetFileName = OFFSET_FILE_PREFIX + "_" + timeBucket + "." + Const.FILE_SUFFIX;
        File newOffsetFile = new File(BufferFileConfig.BUFFER_PATH + offsetFileName);
        offsetFile.delete();
        offsetFile = newOffsetFile;
        this.flush();
    }

    public String getReadFileName() {
        return offset.getReadOffset().getReadFileName();
    }

    public long getReadFileOffset() {
        return offset.getReadOffset().getReadFileOffset();
    }

    public void setReadOffset(long readFileOffset) {
        offset.getReadOffset().setReadFileOffset(readFileOffset);
    }

    public void setReadOffset(String readFileName, long readFileOffset) {
        offset.getReadOffset().setReadFileName(readFileName);
        offset.getReadOffset().setReadFileOffset(readFileOffset);
    }

    public String getWriteFileName() {
        return offset.getWriteOffset().getWriteFileName();
    }

    public long getWriteFileOffset() {
        return offset.getWriteOffset().getWriteFileOffset();
    }

    public void setWriteOffset(String writeFileName, long writeFileOffset) {
        offset.getWriteOffset().setWriteFileName(writeFileName);
        offset.getWriteOffset().setWriteFileOffset(writeFileOffset);
    }

    public void setWriteOffset(long writeFileOffset) {
        offset.getWriteOffset().setWriteFileOffset(writeFileOffset);
    }

    class PrefixFileNameFilter implements FilenameFilter {
        @Override
        public boolean accept(File dir, String name) {
            return name.startsWith(OFFSET_FILE_PREFIX);
        }
    }
}
