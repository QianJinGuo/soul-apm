package com.furioussoulk.agent.stream.buffer;

import java.util.Properties;

public class BufferFileConfig {
    static int BUFFER_OFFSET_MAX_FILE_SIZE = 10 * 1024 * 1024;
    static int BUFFER_SEGMENT_MAX_FILE_SIZE = 10 * 1024 * 1024;
    static String BUFFER_PATH = "../buffer/";

    private static final String BUFFER_PATH_KEY = "buffer_file_path";
    private static final String BUFFER_OFFSET_MAX_FILE_SIZE_KEY = "buffer_offset_max_file_size";
    private static final String BUFFER_SEGMENT_MAX_FILE_SIZE_KEY = "buffer_segment_max_file_size";

    public static class Parser {

        public void parse(Properties config) {
            if (config.containsKey(BUFFER_PATH_KEY)) {
                BUFFER_PATH = config.getProperty(BUFFER_PATH_KEY);
            }

            if (config.containsKey(BUFFER_OFFSET_MAX_FILE_SIZE_KEY)) {
                String sizeStr = config.getProperty(BUFFER_OFFSET_MAX_FILE_SIZE_KEY).toUpperCase();
                if (sizeStr.endsWith("K")) {
                    int size = Integer.parseInt(sizeStr.replace("K", ""));
                    BUFFER_OFFSET_MAX_FILE_SIZE = size * 1024;
                } else if (sizeStr.endsWith("KB")) {
                    int size = Integer.parseInt(sizeStr.replace("KB", ""));
                    BUFFER_OFFSET_MAX_FILE_SIZE = size * 1024;
                } else if (sizeStr.endsWith("M")) {
                    int size = Integer.parseInt(sizeStr.replace("M", ""));
                    BUFFER_OFFSET_MAX_FILE_SIZE = size * 1024 * 1024;
                } else if (sizeStr.endsWith("MB")) {
                    int size = Integer.parseInt(sizeStr.replace("MB", ""));
                    BUFFER_OFFSET_MAX_FILE_SIZE = size * 1024 * 1024;
                } else {
                    BUFFER_OFFSET_MAX_FILE_SIZE = 10 * 1024 * 1024;
                }
            } else {
                BUFFER_OFFSET_MAX_FILE_SIZE = 10 * 1024 * 1024;
            }

            if (config.containsKey(BUFFER_SEGMENT_MAX_FILE_SIZE_KEY)) {
                String sizeStr = config.getProperty(BUFFER_SEGMENT_MAX_FILE_SIZE_KEY).toUpperCase();
                if (sizeStr.endsWith("K")) {
                    int size = Integer.parseInt(sizeStr.replace("K", ""));
                    BUFFER_SEGMENT_MAX_FILE_SIZE = size * 1024;
                } else if (sizeStr.endsWith("KB")) {
                    int size = Integer.parseInt(sizeStr.replace("KB", ""));
                    BUFFER_SEGMENT_MAX_FILE_SIZE = size * 1024;
                } else if (sizeStr.endsWith("M")) {
                    int size = Integer.parseInt(sizeStr.replace("M", ""));
                    BUFFER_SEGMENT_MAX_FILE_SIZE = size * 1024 * 1024;
                } else if (sizeStr.endsWith("MB")) {
                    int size = Integer.parseInt(sizeStr.replace("MB", ""));
                    BUFFER_SEGMENT_MAX_FILE_SIZE = size * 1024 * 1024;
                } else {
                    BUFFER_SEGMENT_MAX_FILE_SIZE = 1024 * 1024;
                }
            } else {
                BUFFER_SEGMENT_MAX_FILE_SIZE = 1024 * 1024;
            }
        }
    }
}
