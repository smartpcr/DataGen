package org.finra.test.datagen.dmapi;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.finra.test.datagen.DbType;
import org.finra.test.datagen.model.ExcelRecord;
import org.finra.test.datagen.model.Search;
import org.finra.test.datagen.schema.SeedFileParser;
import org.finra.test.datagen.util.ExcelUtil;
import org.finra.test.datagen.util.PropertyReader;
import org.finra.test.datagen.util.Records;
import org.finra.test.datagen.util.TableColumn;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

/**
 * Created by xiaodongli on 9/9/15.
 */
public class DmApiSteps {
	private List<ExcelRecord> excelRecords;

	@Given("^output excel file paths$")
	public void setupExcelOutput(List<ExcelRecord> excelRecords) {
		this.excelRecords = excelRecords;
	}

	@Given("^records in '(\\w+)'(.*)$")
	public void ensureRecordsInTable(final String tableName, String description, List<Map<String, Object>> records)
		throws IllegalAccessException, IOException, InvalidFormatException, SQLException {

		Preconditions.checkNotNull(tableName);
		Preconditions.checkArgument(records != null && records.size() > 0);
        validateColumns(tableName, records);

		List<TableColumn> columns = getTableStructure(tableName);
        assertNotNull(columns);
        List<Map<String, Object>> table = new LinkedList<>();
        for(Map<String, Object> record: records){
            Map<String, Object> row = new LinkedHashMap<>();
            Set<String> fieldsUpdated = new HashSet<>();
            for(TableColumn column : columns){
                if(record.containsKey(column.name)){
                    row.put(column.name, record.get(column.name));
                    fieldsUpdated.add(column.name);
                }
                else if(record.containsKey(column.name.toLowerCase())){
                    row.put(column.name, record.get(column.name.toLowerCase()));
                    fieldsUpdated.add(column.name.toLowerCase());
                }
                else if(record.containsKey(column.name.toUpperCase())){
                    row.put(column.name, record.get(column.name.toUpperCase()));
                    fieldsUpdated.add(column.name.toUpperCase());
                }
                else {
                    row.put(column.name, null);
                }
            }

            for(String fieldName : record.keySet()){
                if(fieldsUpdated.contains(fieldName)){
                    fieldsUpdated.remove(fieldName);
                }
            }
            assertThat(fieldsUpdated.size(), equalTo(0));
            table.add(row);
        }

		ExcelRecord excelRecord = null;
		try {
			excelRecord = Iterables.find(this.excelRecords, new Predicate<ExcelRecord>() {
				@Override
				public boolean apply(ExcelRecord excelRecord) {
					return excelRecord.tableName.equalsIgnoreCase(tableName);
				}
			});
		}
		catch (Throwable ignored){}
		assertNotNull(excelRecord);

        String excelFilePath = "out/"+excelRecord.excelFile;
        List<Map<String, Object>> existingTable = ExcelUtil.readSheetAsTable(excelFilePath, excelRecord.sheetName);
        if(existingTable!=null && existingTable.size()>0) {
            table = Records.toRecords(Records.union(Records.toCaseInsensitiveRecords(existingTable), Records.toCaseInsensitiveRecords(table)));
            ExcelUtil.writeSheet(excelFilePath, excelRecord.sheetName, table);
        }
        else {
            ExcelUtil.writeSheet(excelFilePath, excelRecord.sheetName, table);
        }
	}

    @Given("^records NOT in '(\\w+)'$")
    public void ensureRecordsNotExistInTable(final String tableName, List<Map<String, Object>> records){
	    Preconditions.checkNotNull(tableName);
	    Preconditions.checkArgument(records != null && records.size() > 0);
        validateColumns(tableName, records);

        ExcelRecord excelRecord = null;
        try {
            excelRecord = Iterables.find(this.excelRecords, new Predicate<ExcelRecord>() {
                @Override
                public boolean apply(ExcelRecord excelRecord) {
                    return excelRecord.tableName.equalsIgnoreCase(tableName);
                }
            });
        }
        catch (Throwable ignored){}
        assertNotNull(excelRecord);

        String excelFilePath = "out/"+excelRecord.excelFile;
        try {
            List<Map<String, Object>> table = ExcelUtil.readSheetAsTable(excelFilePath, excelRecord.sheetName);
            for(final Map<String, Object> record: records){
                Map<String, Object> found;
                try {
                    found = Iterables.find(table, new Predicate<Map<String, Object>>() {
                        @Override
                        public boolean apply(Map<String, Object> stringObjectMap) {
                            for(String fieldName : record.keySet()){
                                Object fieldValue = record.get(fieldName);
                                if(fieldValue==null)
                                    continue;
                                boolean match=false;
                                if(stringObjectMap.containsKey(fieldName) && stringObjectMap.get(fieldName).equals(fieldValue)){
                                    match=true;
                                }
                                if(!match && stringObjectMap.containsKey(fieldName.toUpperCase()) && stringObjectMap.get(fieldName.toUpperCase()).equals(fieldValue)){
                                    match=true;
                                }
                                if(!match && stringObjectMap.containsKey(fieldName.toLowerCase()) && stringObjectMap.get(fieldName.toLowerCase()).equals(fieldValue)){
                                    match=true;
                                }
                                if(!match)
                                    return false;
                            }
                            return true;
                        }
                    });
                }
                catch (Exception e) {
                    found = null;
                }
                if(found!=null){
                    fail("Record should not exist in excel file");
                }
            }
        }
        catch (Exception e) {
            fail("Unable to read excel records: " + e.getMessage());
        }
    }

    @When("^Run DM API query.*")
    public void runDmApiQuery(List<Search> searches) {

    }

    @Then("^I should get (\\d+) record[s]?.*")
    public void validateDmQueryResult(int expectedRecords) {

    }

	private List<TableColumn> getTableStructure(String tableName) {
		String seedFileName = PropertyReader.get(tableName+".seed");
		try {
            String seedFilePath = SeedFileParser.class.getClassLoader().getResource(seedFileName).getFile();
			return SeedFileParser.parseSeedFile(seedFilePath);
		} catch (IOException e) {
			fail(e.getMessage());
			return null;
		}
	}

	private void validateColumns(String tableName, List<Map<String, Object>> records) {
		List<TableColumn> columns = getTableStructure(tableName);
        assertNotNull(columns);
		for(int i=0;i<records.size();i++)
            for (final String fieldName : records.get(i).keySet()) {
                TableColumn columnFound;
                try {
                    columnFound = Iterables.find(columns, new Predicate<TableColumn>() {
                        @Override
                        public boolean apply(TableColumn tableColumn) {
                            return tableColumn.name.equalsIgnoreCase(fieldName);
                        }
                    });
                } catch (Exception e) {
                    columnFound = null;
                }
                assertNotNull("Unable to find column with name '" + fieldName + "'", columnFound);
                Object fieldValue = records.get(i).get(fieldName);
                if (fieldValue != null) {
                    assertTrue("value " + fieldValue + " cannot be assigned to field " + fieldName + " with data type " + columnFound.dbType,
                        DbType.assignableFrom(columnFound.dbType, columnFound.size, fieldValue));
                    if(columnFound.dbType== DbType.Varchar) {
                        assertTrue("Row " + i + ", column " + fieldName+ " data type not match or size too large",
                            fieldValue.toString().length()<=columnFound.size);
                    }
                }
            }
	}
}
