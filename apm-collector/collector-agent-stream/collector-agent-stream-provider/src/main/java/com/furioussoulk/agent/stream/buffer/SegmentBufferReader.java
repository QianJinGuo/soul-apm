package com.furioussoulk.agent.stream.buffer;

import com.furioussoulk.agent.stream.parser.SegmentParse;
import com.furioussoulk.apm.collector.core.module.ModuleManager;
import com.furioussoulk.apm.collector.core.util.CollectionUtils;
import com.furioussoulk.apm.collector.core.util.Const;
import com.furioussoulk.apm.collector.core.util.StringUtils;
import com.furioussoulk.network.proto.UpstreamSegment;
import com.google.protobuf.CodedOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public enum SegmentBufferReader {
    /**
     * instance
     */
    INSTANCE;

    private final Logger logger = LoggerFactory.getLogger(SegmentBufferReader.class);
    private InputStream inputStream;
    private ModuleManager moduleManager;

    public void initialize(ModuleManager moduleManager) {
        this.moduleManager = moduleManager;
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(this::preRead, 3, 3, TimeUnit.SECONDS);
    }

    private void preRead() {
        String readFileName = OffsetManager.INSTANCE.getReadFileName();
        if (StringUtils.isNotEmpty(readFileName)) {
            File readFile = new File(BufferFileConfig.BUFFER_PATH + readFileName);
            if (readFile.exists()) {
                deleteTheDataFilesBeforeReadFile(readFileName);
                long readFileOffset = OffsetManager.INSTANCE.getReadFileOffset();
                read(readFile, readFileOffset);
                readEarliestCreateDataFile();
            } else {
                deleteTheDataFilesBeforeReadFile(readFileName);
                readEarliestCreateDataFile();
            }
        } else {
            readEarliestCreateDataFile();
        }
    }

    private void deleteTheDataFilesBeforeReadFile(String readFileName) {
        File[] dataFiles = new File(BufferFileConfig.BUFFER_PATH).listFiles(new PrefixFileNameFilter());

        long readFileCreateTime = getFileCreateTime(readFileName);
        for (File dataFile : dataFiles) {
            long fileCreateTime = getFileCreateTime(dataFile.getName());
            if (fileCreateTime < readFileCreateTime) {
                dataFile.delete();
            } else if (fileCreateTime == readFileCreateTime) {
                break;
            }
        }
    }

    private long getFileCreateTime(String fileName) {
        fileName = fileName.replace(SegmentBufferManager.DATA_FILE_PREFIX + "_", Const.EMPTY_STRING);
        fileName = fileName.replace("." + Const.FILE_SUFFIX, Const.EMPTY_STRING);
        return Long.valueOf(fileName);
    }

    private void readEarliestCreateDataFile() {
        String readFileName = OffsetManager.INSTANCE.getReadFileName();
        File[] dataFiles = new File(BufferFileConfig.BUFFER_PATH).listFiles(new PrefixFileNameFilter());

        if (CollectionUtils.isNotEmpty(dataFiles)) {
            if (dataFiles[0].getName().equals(readFileName)) {
                return;
            }
        }

        for (File dataFile : dataFiles) {
            logger.debug("Reading segment buffer data file, file name: {}", dataFile.getAbsolutePath());
            OffsetManager.INSTANCE.setReadOffset(dataFile.getName(), 0);
            if (!read(dataFile, 0)) {
                break;
            }
        }
    }

    private boolean read(File readFile, long readFileOffset) {
        try {
            inputStream = new FileInputStream(readFile);
            inputStream.skip(readFileOffset);

            String writeFileName = OffsetManager.INSTANCE.getWriteFileName();
            long endPoint = readFile.length();
            if (writeFileName.equals(readFile.getName())) {
                endPoint = OffsetManager.INSTANCE.getWriteFileOffset();
            }

            while (readFile.length() > readFileOffset && readFileOffset < endPoint) {
                UpstreamSegment upstreamSegment = UpstreamSegment.parser().parseDelimitedFrom(inputStream);
                SegmentParse parse = new SegmentParse(moduleManager);
                if (!parse.parse(upstreamSegment, SegmentParse.Source.Buffer)) {
                    return false;
                }

                final int serialized = upstreamSegment.getSerializedSize();
                readFileOffset = readFileOffset + CodedOutputStream.computeUInt32SizeNoTag(serialized) + serialized;
                logger.debug("read segment buffer from file: {}, offset: {}, file length: {}", readFile.getName(), readFileOffset, readFile.length());
                OffsetManager.INSTANCE.setReadOffset(readFileOffset);
            }

            inputStream.close();
            if (!writeFileName.equals(readFile.getName())) {
                readFile.delete();
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    class PrefixFileNameFilter implements FilenameFilter {
        @Override
        public boolean accept(File dir, String name) {
            return name.startsWith(SegmentBufferManager.DATA_FILE_PREFIX);
        }
    }
}
