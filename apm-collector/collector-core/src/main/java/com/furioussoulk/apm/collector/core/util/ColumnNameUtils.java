package com.furioussoulk.apm.collector.core.util;

public enum ColumnNameUtils {
    /**
     * instance
     */
    INSTANCE;

    public String rename(String columnName) {
        StringBuilder renamedColumnName = new StringBuilder();
        char[] chars = columnName.toLowerCase().toCharArray();

        boolean findUnderline = false;
        for (char character : chars) {
            if (character == '_') {
                findUnderline = true;
            } else if (findUnderline) {
                renamedColumnName.append(String.valueOf(character).toUpperCase());
                findUnderline = false;
            } else {
                renamedColumnName.append(character);
            }
        }
        return renamedColumnName.toString();
    }
}
