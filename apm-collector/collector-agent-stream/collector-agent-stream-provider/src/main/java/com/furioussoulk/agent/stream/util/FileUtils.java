package com.furioussoulk.agent.stream.util;

import com.furioussoulk.apm.collector.core.util.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public enum FileUtils {
    /**
     * instance
     */
    INSTANCE;

    private final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    public String readLastLine(File file) {
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(file, "r");
            long length = randomAccessFile.length();
            if (length == 0) {
                return Const.EMPTY_STRING;
            } else {
                long position = length - 1;
                randomAccessFile.seek(position);
                while (position >= 0) {
                    if (randomAccessFile.read() == '\n') {
                        return randomAccessFile.readLine();
                    }
                    randomAccessFile.seek(position);
                    if (position == 0) {
                        return randomAccessFile.readLine();
                    }
                    position--;
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return Const.EMPTY_STRING;
    }

    public void writeAppendToLast(File file, RandomAccessFile randomAccessFile, String value) {
        if (randomAccessFile == null) {
            try {
                randomAccessFile = new RandomAccessFile(file, "rwd");
            } catch (FileNotFoundException e) {
                logger.error(e.getMessage(), e);
            }
        }
        try {
            long length = randomAccessFile.length();
            randomAccessFile.seek(length);
            randomAccessFile.writeBytes(System.lineSeparator());
            randomAccessFile.writeBytes(value);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
