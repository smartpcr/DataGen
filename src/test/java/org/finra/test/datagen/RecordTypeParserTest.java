package org.finra.test.datagen;

<<<<<<< HEAD
=======
import org.junit.Ignore;
>>>>>>> 034eb2c493cc943c6f71b860ed7003c81563ba74
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by xiaodongli on 9/8/15.
 */
<<<<<<< HEAD
=======
@Ignore
>>>>>>> 034eb2c493cc943c6f71b860ed7003c81563ba74
public class RecordTypeParserTest {

    @Test
    public void canParseRecordType() {
        String recordTypeNames = "fo";
        RecordType recordType = RecordType.fromString(recordTypeNames);
        assertNotNull(recordType);
        assertEquals(RecordType.FirmOrder, recordType);
    }
}
