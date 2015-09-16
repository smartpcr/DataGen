package org.finra.test.datagen.dmapi;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.finra.test.datagen.*;
import org.finra.test.datagen.model.*;
import org.finra.test.datagen.rule.RowContext;
import org.finra.test.datagen.schema.SeedFileParser;
import org.finra.test.datagen.util.DisplayRuleUtil;
import org.finra.test.datagen.util.PropertyReader;
import org.finra.test.datagen.util.StringFormat;
import org.finra.test.datagen.util.TableColumn;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

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
	public void ensureDataInOetOrders(final String tableName, List<Map<String, Object>> records)
		throws IllegalAccessException, IOException, InvalidFormatException, SQLException {

		Preconditions.checkNotNull(tableName);
		Preconditions.checkArgument(records!=null && records.size()>0);

		List<TableColumn> columns = getTableStructure(tableName);
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

		List<Field> fields = Arrays.asList(OetDetailRecord.class.getFields());
		List<ColumnDisplayRule> displayRules = DisplayRuleUtil.readDisplayRules();

		for(Map<String, Object> record : records) {

		}
	}

    @Given("^records NOT in '(\\w+)'$")
    public void ensureDataNotInFirmRef(String tableName, List<Map<String, Object>> records){
	    Preconditions.checkNotNull(tableName);
	    Preconditions.checkArgument(records!=null && records.size()>0);
    }

    @When("^Run DM API query.*")
    public void runDmApiQuery() {

    }

    @Then("^I should get (\\d+) record[s]?.*")
    public void validateDmQueryResult(int expectedRecords) {

    }

	private List<TableColumn> getTableStructure(String tableName) {
		String seedFileName = PropertyReader.get(tableName+".seed");
		try {
			return SeedFileParser.parseSeedFile(seedFileName);
		} catch (IOException e) {
			fail(e.getMessage());
			return null;
		}
	}
}
