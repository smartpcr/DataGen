package org.finra.test.datagen;

import org.finra.test.datagen.util.DisplayRuleUtil;
import org.finra.test.datagen.util.ExcelUtil;
import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.finra.test.datagen.DbType.Varchar;
import static org.junit.Assert.*;

/**
 * Created on 9/1/2015.
 */
@Ignore
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
	        final int totalRecords = 50;
            List<Map<String, Object>> table = DataGenerator.generateTestData(range, totalRecords);
            assertNotNull(table);
            assertEquals(totalRecords, table.size());
            Map<String, Object> row = table.get(0);
            assertTrue(row.size()> 75);
            int nonEmptyFieldCount = 0;
            for(String fieldName : row.keySet()){
                Object value = row.get(fieldName);
                if(value!=null && value.toString().length()>0) {
                    nonEmptyFieldCount++;
                }
            }
            assertTrue(nonEmptyFieldCount > 50);

	        final String filePath = "out/test_data.xlsx";
	        final String sheetName = String.format("%s_%d", "tstanalyst1", 100008);
	        ExcelUtil.writeSheet(filePath, sheetName, table);
	        File file = new File(filePath);
	        assertTrue(file.exists() && !file.isDirectory());

	        List<Map<String, Object>> table2 = ExcelUtil.readSheetAsTable(filePath, sheetName);
	        assertNotNull(table2);
	        assertEquals(table.size(), table2.size());
	        List<ColumnDisplayRule> displayRules = DisplayRuleUtil.readDisplayRules();
	        Map<RecordType, Integer> maxSizes = new HashMap();
	        for(ColumnDisplayRule displayRule : displayRules) {

		        if (displayRule.diverDataType != null && displayRule.diverDataType.dbType!=null) {
			        int maxStringSize = 0;
			        switch (displayRule.diverDataType.dbType) {
				        case BigInt:
					        maxStringSize = 19;
					        break;
				        case Date:
					        maxStringSize = 10;
					        break;
				        case Int:
					        maxStringSize = 10;
					        break;
				        case Timestamp:
					        maxStringSize = 26;
					        break;
				        case Decimal:
					        maxStringSize = displayRule.diverDataType.precision;
					        break;
				        default:
					        maxStringSize = displayRule.diverDataType.size;
					        break;
			        }
			        if (maxSizes.containsKey(displayRule.recordType)) {
				        maxSizes.put(displayRule.recordType, maxSizes.get(displayRule.recordType) + maxStringSize);
			        } else {
				        maxSizes.put(displayRule.recordType, maxStringSize);
			        }
		        }
	        }
	        for(RecordType recType : maxSizes.keySet()){
		        System.out.println(String.format("%s: \t%d", recType.toString(), maxSizes.get(recType)));
	        }

	        int recordIdx = 0;
	        for(Map<String, Object> record : table2) {
		        recordIdx++;
		        System.out.println("\nrecord #" + recordIdx);

		        int concatedCommonFieldSize = 0;
		        int concatedFirmOrderFieldSize = 0;
		        int concatedExchangeOrderFieldSize = 0;
		        int concatedOffExchangeTradeFieldSize = 0;
		        for(ColumnDisplayRule rule : displayRules){
			        if(record.containsKey(rule.diverFieldName)) {
				        Object value = record.get(rule.diverFieldName);
				        if(value!=null) {
					        if(rule.diverDataType!=null && rule.diverDataType.dbType== Varchar){
						        int allowedSize = rule.diverDataType.size;
						        if(value.toString().length()>allowedSize){
							        System.out.println(String.format(
								        "field [%s] value '%s' is exceed its allowed length: %d",
								        rule.diverFieldName, value, allowedSize));
						        }
					        }
					        switch (rule.recordType){
						        case Common:
							        concatedCommonFieldSize += value.toString().length();
							        break;
						        case FirmOrder:
							        concatedFirmOrderFieldSize += value.toString().length();
							        break;
						        case ExchangeOrder:
							        concatedExchangeOrderFieldSize += value.toString().length();
							        break;
						        case OffExchangeTrade:
							        concatedOffExchangeTradeFieldSize += value.toString().length();
							        break;
					        }
				        }
			        }
		        }

		        if(concatedCommonFieldSize>1000) {
			        System.out.println("concatenated text for common fields exceed 1000 limit: " + concatedCommonFieldSize);
		        }
		        if(concatedFirmOrderFieldSize>1000) {
			        System.out.println("concatenated text for fo fields exceed 1000 limit: " + concatedFirmOrderFieldSize);
		        }
		        if(concatedExchangeOrderFieldSize>1000) {
			        System.out.println("concatenated text for eo fields exceed 1000 limit: " + concatedExchangeOrderFieldSize);
		        }
		        if(concatedOffExchangeTradeFieldSize>1000) {
			        System.out.println("concatenated text for dmapi.details fields exceed 1000 limit: " + concatedOffExchangeTradeFieldSize);
		        }
	        }
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }
}
