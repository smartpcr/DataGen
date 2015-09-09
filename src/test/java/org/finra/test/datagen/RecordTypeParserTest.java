package org.finra.test.datagen;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by xiaodongli on 9/8/15.
 */
public class RecordTypeParserTest {

    @Test
    public void canParseRecordType() {
        String recordTypeNames = "fo";
        RecordType recordType = RecordType.fromString(recordTypeNames);
        assertNotNull(recordType);
        assertEquals(RecordType.FirmOrder, recordType);
    }
}
