package com.furioussoulk.apm.collector.core.util;

import org.junit.Assert;
import org.junit.Test;

public class ColumnNameUtilsTest {
    @Test
    public void testRename() {
        Assert.assertEquals("newAttributeNameFromColumnName",
            ColumnNameUtils.INSTANCE.rename("new_attribute_name_from_column_name"));
    }
}
