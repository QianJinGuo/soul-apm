package com.furioussoulk.agent.stream.buffer;

public class Offset {

    private static final String SPLIT_CHARACTER = ",";
    private ReadOffset readOffset;
    private WriteOffset writeOffset;

    public Offset() {
        readOffset = new ReadOffset();
        writeOffset = new WriteOffset();
    }

    public String serialize() {
        return readOffset.getReadFileName() + SPLIT_CHARACTER + String.valueOf(readOffset.getReadFileOffset())
            + SPLIT_CHARACTER + writeOffset.getWriteFileName() + SPLIT_CHARACTER + String.valueOf(writeOffset.getWriteFileOffset());
    }

    public void deserialize(String value) {
        String[] values = value.split(SPLIT_CHARACTER);
        if (values.length == 4) {
            this.readOffset.readFileName = values[0];
            this.readOffset.readFileOffset = Long.parseLong(values[1]);
            this.writeOffset.writeFileName = values[2];
            this.writeOffset.writeFileOffset = Long.parseLong(values[3]);
        }
    }

    public ReadOffset getReadOffset() {
        return readOffset;
    }

    public WriteOffset getWriteOffset() {
        return writeOffset;
    }

    public static class ReadOffset {
        private String readFileName;
        private long readFileOffset = 0;

        public String getReadFileName() {
            return readFileName;
        }

        public long getReadFileOffset() {
            return readFileOffset;
        }

        public void setReadFileName(String readFileName) {
            this.readFileName = readFileName;
        }

        public void setReadFileOffset(long readFileOffset) {
            this.readFileOffset = readFileOffset;
        }
    }

    public static class WriteOffset {
        private String writeFileName;
        private long writeFileOffset = 0;

        public String getWriteFileName() {
            return writeFileName;
        }

        public long getWriteFileOffset() {
            return writeFileOffset;
        }

        public void setWriteFileName(String writeFileName) {
            this.writeFileName = writeFileName;
        }

        public void setWriteFileOffset(long writeFileOffset) {
            this.writeFileOffset = writeFileOffset;
        }
    }
}
