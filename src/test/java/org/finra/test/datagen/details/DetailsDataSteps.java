package org.finra.test.datagen.details;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.finra.test.datagen.*;
import org.finra.test.datagen.model.NameValuePair;
import org.finra.test.datagen.schema.SeedFileParser;
import org.finra.test.datagen.track.UserMartTracking;
import org.finra.test.datagen.util.*;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.*;

import static org.finra.test.datagen.DbType.Varchar;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

/**
 * Created by xiaodongli on 9/8/15.
 */
public class DetailsDataSteps {
	private TestDataRange range;
    private int version;
    private int recordCount;
    private String userId;
    private String refId;
    private DateTime requestTime;

    // tracking
    private String trackExcelFile;
    private String trackSheet;
    private String trackConfigSheet;
	private String trackingDataSourceName;
	private String trackingSchemaName;
	private String trackingTableName;

    // data
    private String martExcelFile;
    private String martSheet;
    private String martConfigSheet;
//	private String martDataSourceName;
//	private String martSchemaName;
//	private String martTableName;

	List<ColumnDisplayRule> displayRules;
	private Map<String, HandleCopyValue> copyValueHandlers;

	@Given("^search parameters$")
	public void setupSearchParameters(List<NameValuePair> pairs) throws Throwable {
		this.range = new TestDataRange()
			.withSymbol(NameValuePair.getValue(pairs, "symbol"))
			.withFirms(NameValuePair.getValues(pairs, "firms"))
			.withStartDate(NameValuePair.getDateTime(pairs, "fromDate", "fromTime"))
			.withEndDate(NameValuePair.getDateTime(pairs, "toDate", "toTime"))
			.withRelatedFirms(NameValuePair.getBoolean(pairs, "allRelatedFirms"))
            .withRecordTypes(RecordType.valuesOf(NameValuePair.getValue(pairs, "recordTypes")))
			.withLastFirmOrderId(NameValuePair.getLong(pairs, "lastFirmOrderId"))
			.withLastExchangeOrderId(NameValuePair.getLong(pairs, "lastExchangeOrderId"))
            .withLastOffExchangeTradeId(NameValuePair.getLong(pairs, "lastOffExchangeTradeId"))
			.withFillerPercentage(NameValuePair.getPercent(pairs, "DefaultFieldValueFillerPercentage"))
			.withIncludeDerivedFields(NameValuePair.getBoolean(pairs, "includeDerivedFields"));

        this.version = NameValuePair.getInt(pairs, "version");
        this.recordCount = NameValuePair.getInt(pairs, "recordCount");
        this.userId = NameValuePair.getValue(pairs, "userId");
        this.refId = NameValuePair.getValue(pairs, "refId");
        this.requestTime = new DateTime().withYear(2022);

		this.displayRules = DisplayRuleUtil.readDisplayRules(this.version);
		this.copyValueHandlers = new HashMap<>();
		for(ColumnDisplayRule displayRule : this.displayRules) {
			if(displayRule.sourceFieldName!=null && displayRule.sourceFieldName.size()>0) {
				HandleCopyValue copyHandler = new CopyValueHandler(displayRule.diverFieldName, this.displayRules);
				this.copyValueHandlers.put(displayRule.diverFieldName, copyHandler);
			}
		}
	}

	@Given("^user tracking record$")
	public void setupTracking(List<NameValuePair> pairs) {
        this.trackExcelFile = NameValuePair.getValue(pairs, "excelFile");
        this.trackSheet = NameValuePair.getValue(pairs, "trackSheet");
        this.trackConfigSheet = NameValuePair.getValue(pairs, "configSheet");
		this.trackingDataSourceName = NameValuePair.getValue(pairs, "trackingDataSource");
		this.trackingSchemaName = NameValuePair.getValue(pairs, "trackingSchemaName");
		this.trackingTableName = NameValuePair.getValue(pairs, "trackingTableName");
    }

	@Given("^mart table record$")
	public void setupMart(List<NameValuePair> pairs) {
        this.martExcelFile = NameValuePair.getValue(pairs, "excelFile");
        this.martSheet = NameValuePair.getValue(pairs, "dataSheet");
        this.martConfigSheet = NameValuePair.getValue(pairs, "configSheet");
//		this.martDataSourceName = NameValuePair.getValue(pairs, "martDataSource");
//		this.martSchemaName = NameValuePair.getValue(pairs, "martSchemaName");
//		this.martTableName = NameValuePair.getValue(pairs, "martTableName");
	}

    @When("^generate tracking data$")
    public void generateTrackData() {
        String excelFilePath = "out/" + this.trackExcelFile;
        List<UserMartTracking> trackings = new LinkedList<>();
        UserMartTracking newTracking = new UserMartTracking();
        newTracking.rqst_user_id = this.userId;
        newTracking.user_rfrnc_id = this.refId;
        newTracking.rqst_ts = StringFormat.formatDateTime(this.requestTime.plusMinutes(1));
        newTracking.crit_from_dt=StringFormat.formatDate(this.range.getStartDate());
        newTracking.crit_to_dt=StringFormat.formatDate(this.range.getEndDate());
        newTracking.crit_from_tm=StringFormat.formatTime(this.range.getStartDate());
        newTracking.crit_to_tm=StringFormat.formatTime(this.range.getStartDate());
        newTracking.crit_issue_sym_id = this.range.getSymbol();
        newTracking.crit_firm_mp_id = Joiner.on(',').join(this.range.getFirms());
        newTracking.crit_alt_rltd_firm = this.range.getRelatedFirms()?"Y":"N";
        newTracking.trckg_stts_cd="CMPLT";
        newTracking.cmplt_ts= StringFormat.formatDateTime(this.requestTime.plusSeconds(10));
        newTracking.user_mart_creat_job_id = "";
        newTracking.user_mart_tmplt_id = String.valueOf(this.version);
        newTracking.user_mart_tbl_nm = this.userId+"_"+this.refId;
        newTracking.user_mart_rec_ct = String.valueOf(this.recordCount);
        newTracking.user_mart_view_nm = this.userId+"_"+this.refId+"_dflt_vw";
        newTracking.src_type_tx=RecordType.join(this.range.getRecordTypes());

        File excelFile = new File(excelFilePath);
        try {
            if(excelFile.exists()){
                List<Map<String, Object>> table = ExcelUtil.readSheetAsTable(excelFilePath, this.trackSheet);
                trackings = UserMartTracking.parseTable(table);
                UserMartTracking existingTracking = UserMartTracking.find(trackings, this.userId, this.refId);
                if(existingTracking==null){
                    trackings.add(newTracking);
                }
                else {
                    existingTracking.applyChanges(newTracking);
                }
            }
            else {
                trackings.add(newTracking);
            }

            List<Map<String, String>> table2 = new LinkedList<>();
            for(UserMartTracking tracking : trackings) {
                table2.add(tracking.getRecord());
            }
            ExcelUtil.writeSheet2(excelFilePath, this.trackSheet, table2);
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

	@When("^generate tracking config.*$")
	public void generateTrackConfig() {
		try {
			List<TableColumn> trackingColumns = DbBackup.getTableColumns(this.trackingDataSourceName, this.trackingSchemaName, this.trackingTableName);
			List<Map<String, Object>> configData = new LinkedList<>();
			for(TableColumn column : trackingColumns) {
				if(column.name.equalsIgnoreCase("xpr_fl"))
					continue;

				Map<String, Object> record = new LinkedHashMap<>();
				record.put("field", column.name);
				record.put("type", column.dbType.toString());
				record.put("format", null);
				record.put("default", null);
				configData.add(record);
			}
			String excelFilePath = "out/" + this.trackExcelFile;
			ExcelUtil.writeSheet(excelFilePath, this.trackConfigSheet, configData);
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@When("^generate mart data.*$")
	public void generateMartData() {
        try {
            List<Map<String, Object>> table = DataGenerator.generateTestData(this.range, this.recordCount, this.version);
            assertNotNull(table);
            assertThat(table.size(), equalTo(this.recordCount));

	        this.validateConcatenatedColumnSize(table, this.displayRules);
	        String excelFilePath = "out/" + this.martExcelFile;
	        ExcelUtil.writeSheet(excelFilePath, this.martSheet, table);
        }
        catch (Exception e){
            fail(e.getMessage());
        }
	}

	@When("^apply static data only when userId=\"([\\w]+)\" and refId=\"([\\w]+)\"")
	public void applyStaticData(String userId, String refId, List<Map<String, Object>> records) {
		if(this.userId.equals(userId) && this.refId.equals(refId) && records!=null && records.size()>0) {
			try {
				String excelFilePath = "out/" + this.martExcelFile;
				List<Map<String, Object>> table = ExcelUtil.readSheetAsTable(excelFilePath, this.martSheet);
				int staticDataIdx = 0;
				int tableRowIx = 0;
				int rowsUpdated = 0;
				while (staticDataIdx<records.size() && tableRowIx< table.size()) {
					String recordType = Records.getValue(records.get(staticDataIdx), "cmn_rec_type");
					String recordType2 = Records.getValue(table.get(tableRowIx), "cmn_rec_type");
					if(recordType.equalsIgnoreCase(recordType2)) {
						Records.applyChanges(table.get(tableRowIx), records.get(staticDataIdx), this.copyValueHandlers);
						staticDataIdx++;
						tableRowIx++;
						rowsUpdated++;
					}
					else {
						tableRowIx++;
					}
				}
				assertEquals(records.size(), rowsUpdated);
				ExcelUtil.writeSheet(excelFilePath, this.martSheet, table);
			}
			catch (Exception e){
				fail(e.getMessage());
			}
		}
	}

	@When("^generate mart config.*$")
	public void generateMartConfig() {
		try {
			URL seedfileUrl = SeedFileParser.class.getClassLoader().getResource("seed_v"+this.version+"/event_tmplt.seed");
			assertNotNull(seedfileUrl);
			List<TableColumn> columns = SeedFileParser.parseSeedFile(seedfileUrl.getFile());
			List<Map<String, Object>> configData = new LinkedList<>();
			for(TableColumn column : columns) {
				if(column.name.equalsIgnoreCase("mart_row_id"))
					continue;
				if(!this.range.getIncludeDerivedFields() && column.name.toLowerCase().startsWith("drvd_"))
					continue;

				Map<String, Object> record = new LinkedHashMap<>();
				record.put("field", column.name);
				record.put("type", column.dbType.toString());
				record.put("format", null);
				record.put("default", null);
				configData.add(record);
			}
			String excelFilePath = "out/" + this.martExcelFile;
			ExcelUtil.writeSheet(excelFilePath, this.martConfigSheet, configData);
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	private void validateConcatenatedColumnSize(List<Map<String, Object>> table, List<ColumnDisplayRule> displayRules){
		int recordIdx = 0;
		for(Map<String, Object> record : table) {
			recordIdx++;

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
								System.out.println(String.format("\n%s_%s: record #",this.userId, this.refId, recordIdx));
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
				System.out.println(String.format("\n%s_%s: record #",this.userId, this.refId, recordIdx));
				System.out.println("concatenated text for common fields exceed 1000 limit: " + concatedCommonFieldSize);
			}
			if(concatedFirmOrderFieldSize>1000) {
				System.out.println(String.format("\n%s_%s: record #",this.userId, this.refId, recordIdx));
				System.out.println("concatenated text for fo fields exceed 1000 limit: " + concatedFirmOrderFieldSize);
			}
			if(concatedExchangeOrderFieldSize>1000) {
				System.out.println(String.format("\n%s_%s: record #",this.userId, this.refId, recordIdx));
				System.out.println("concatenated text for eo fields exceed 1000 limit: " + concatedExchangeOrderFieldSize);
			}
			if(concatedOffExchangeTradeFieldSize>1000) {
				System.out.println(String.format("\n%s_%s: record #",this.userId, this.refId, recordIdx));
				System.out.println("concatenated text for oet fields exceed 1000 limit: " + concatedOffExchangeTradeFieldSize);
			}
		}
	}

	@Then("^user track record should be populated$")
	public void verifyTracking() {

	}

	@Then("^data mart table should be populated$")
	public void verifyDataMart() {

	}

	@Then("^mart config table should be populated$")
	public void verifyDataMartConfig() {

	}
}
