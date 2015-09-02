package org.finra.test.datagen;

import org.finra.test.datagen.util.DisplayRuleUtil;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created on 9/1/2015.
 */
public class DataGenTest {
	@Test
	public void canReadRule() {
		try {
			List<ColumnDisplayRule> displayRules = DisplayRuleUtil.readDisplayRules();
			assertNotNull(displayRules);
			assertEquals(447, displayRules.size());
		}
		catch (Exception e){
			fail(e.getMessage());
		}
	}

    @Test
    public void canGenerateTestData() {
        TestDataRange range = new TestDataRange()
            .withSymbol("AAPL")
            .withFirms("MLCO", "MLEX")
            .withRelatedFirms(true)
            .withStartDate(new DateTime(2015,1,20,0,0,0).toDate())
            .withEndDate(new DateTime(2015, 1, 27, 23, 59, 59).toDate());
        try {
            List<Map<String, Object>> table = DataGenerator.generateTestData(range, 5);
            assertNotNull(table);
            assertEquals(5, table.size());
            Map<String, Object> row = table.get(0);
            assertTrue(row.size()> 400);
            int nonEmptyFieldCount = 0;
            for(String fieldName : row.keySet()){
                Object value = row.get(fieldName);
                if(value!=null && value.toString().length()>0) {
                    nonEmptyFieldCount++;
                }
            }
            assertTrue(nonEmptyFieldCount>100);
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }
}
