package org.finra.test.datagen;

import com.google.common.base.Strings;
import org.finra.test.datagen.util.ExcelUtil;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Created on 9/1/2015.
 */
public class ExcelUtilTest {
	@Test
	public void canReadExcelFile() {
		final String sheetName = "filter3_100005";
		final String fileName = "saw_analyzer_filter_N_testdata.xlsx";
		URL url = this.getClass().getClassLoader().getResource(fileName);
		assertNotNull(url);
		String excelFilePath = url.getFile();

		try {
			List<Map<String, Object>> table = ExcelUtil.readSheetAsTable(excelFilePath, sheetName);
			assertNotNull(table);
			assertTrue(table.size() > 0);
			assertEquals(238, table.size());

			Map<String, Object> row1 = table.get(0);
			assertEquals(318, row1.size());


			String fieldName = "fo_oats_roe_id";
			assertTrue(row1.containsKey(fieldName));
			Object fieldValue = row1.get(fieldName);
			assertNotNull(fieldValue);
			assertTrue(fieldValue instanceof String);

			fieldName = "cmn_bd_nb";
			assertTrue(row1.containsKey(fieldName));
			fieldValue = row1.get(fieldName);
			assertNotNull(fieldValue);
			assertTrue(fieldValue instanceof String);

			fieldName = "cmn_event_dt";
			assertTrue(row1.containsKey(fieldName));
			fieldValue = row1.get(fieldName);
			assertNotNull(fieldValue);
			assertTrue(fieldValue instanceof String);

			fieldName = "cmn_event_tm";
			assertTrue(row1.containsKey(fieldName));
			fieldValue = row1.get(fieldName);
			assertNotNull(fieldValue);
			assertTrue(fieldValue instanceof String);

			fieldName = "fo_crctn_dltn_ts";
			assertTrue(row1.containsKey(fieldName));
			fieldValue = row1.get(fieldName);
			assertTrue(fieldValue instanceof String);
			assertTrue(Strings.isNullOrEmpty((String) fieldValue));

		}
		catch (Exception ex){
			fail(ex.getMessage());
		}
	}

	@Test
	public void canWriteExcel() {
		final String filePath = "test.xlsx";
		final String sheetName = "sheet1";
		List<Map<String, Object>> table = new ArrayList<Map<String, Object>>();
		Map<String, Class> columns = new HashMap<String, Class>();
		columns.put("Name", String.class);
		columns.put("Price", Double.class);
		columns.put("Quantity", Integer.class);
		columns.put("ReleaseDate", Date.class);
		for(int i = 0; i < 100; i++) {
			Map<String, Object> row = new HashMap<String, Object>();
			for(String fieldName : columns.keySet()) {
				Class dataType = columns.get(fieldName);
				Object value = createRandomValue(dataType);
				row.put(fieldName, value);
			}
			table.add(row);
		}
		try {
			ExcelUtil.writeSheet(filePath, sheetName, table);
			File file = new File(filePath);
			assertTrue(file.exists() && !file.isDirectory());

			List<Map<String, Object>> table2 = ExcelUtil.readSheetAsTable(filePath, sheetName);
			assertNotNull(table2);
			assertEquals(table.size(), table2.size());

			file.delete();
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
	}

	private Object createRandomValue(Class clazz) {
		if(clazz == String.class) {
			return "String";
		}
		else if(clazz == Integer.class){
			return 1;
		}
		else if(clazz == Double.class) {
			return 2.5;
		}
		else if(clazz == Date.class){
			return new Date();
		}
		return null;
	}
}
