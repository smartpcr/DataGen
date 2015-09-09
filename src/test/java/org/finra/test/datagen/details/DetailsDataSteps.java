package org.finra.test.datagen.details;

import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.finra.test.datagen.DataGenerator;
import org.finra.test.datagen.RecordType;
import org.finra.test.datagen.TestDataRange;
import org.finra.test.datagen.model.NameValuePair;
import org.finra.test.datagen.track.UserMartTracking;
import org.finra.test.datagen.util.ExcelUtil;
import org.finra.test.datagen.util.StringFormat;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.io.File;
import java.nio.file.Files;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by xiaodongli on 9/8/15.
 */
public class DetailsDataSteps {
	private TestDataRange range;
    private int version;
    private int recordCount;
    private String userId;
    private String refId;
    private Date requestTime;

    // tracking
    private String trackExcelFile;
    private String trackSheet;
    private String trackConfigSheet;

    // data
    private String martExcelFile;
    private String martSheet;
    private String martConfigSheet;

	@Given("^search parameters$")
	public void setupSearchParameters(List<NameValuePair> pairs) throws Throwable {
		this.range = new TestDataRange()
			.withSymbol(NameValuePair.getValue(pairs, "symbol"))
			.withFirms(NameValuePair.getValues(pairs, "firms"))
			.withStartDate(NameValuePair.getDateTime(pairs, "fromDate", "fromTime"))
			.withRelatedFirms(NameValuePair.getBoolean(pairs, "allRelatedFirms"))
            .withRecordTypes(RecordType.valuesOf(NameValuePair.getValue(pairs, "recordTypes")))
			.withLastFirmOrderId(NameValuePair.getLong(pairs, "lastFirmOrderId"))
			.withLastExchangeOrderId(NameValuePair.getLong(pairs, "lastExchangeOrderId"))
            .withLastOffExchangeTradeId(NameValuePair.getLong(pairs, "lastOffExchangeTradeId"));

        this.version = NameValuePair.getInt(pairs, "version");
        this.recordCount = NameValuePair.getInt(pairs, "recordCount");
        this.userId = NameValuePair.getValue(pairs, "userId");
        this.refId = NameValuePair.getValue(pairs, "refId");
        this.requestTime = (new DateTime()).withYear(2022).toDate();

	}

	@Given("^user tracking record$")
	public void setupTracking(List<NameValuePair> pairs) {
        this.trackExcelFile = NameValuePair.getValue(pairs, "excelFile");
        this.trackSheet = NameValuePair.getValue(pairs, "trackSheet");
        this.trackConfigSheet = NameValuePair.getValue(pairs, "configSheet");
    }

	@Given("^mart table record$")
	public void setupOutput(List<NameValuePair> pairs) {
        this.martExcelFile = NameValuePair.getValue(pairs, "excelFile");
        this.martSheet = NameValuePair.getValue(pairs, "dataSheet");
        this.martConfigSheet = NameValuePair.getValue(pairs, "configSheet");
	}

    @When("^generate tracking data$")
    public void generateTrackData() {
        String excelFilePath = "out/" + this.trackExcelFile;
        List<UserMartTracking> trackings = new LinkedList<>();
        UserMartTracking newTracking = new UserMartTracking();
        newTracking.rqst_user_id = this.userId;
        newTracking.user_rfrnc_id = this.refId;
        newTracking.rqst_ts = StringFormat.formatDateTime(this.requestTime);
        newTracking.crit_from_dt=StringFormat.formatDate(this.range.getStartDate());
        newTracking.crit_to_dt=StringFormat.formatDate(this.range.getEndDate());
        newTracking.crit_from_tm=StringFormat.formatTime(this.range.getStartDate());
        newTracking.crit_to_tm=StringFormat.formatTime(this.range.getStartDate());
        newTracking.crit_issue_sym_id = this.range.getSymbol();
        newTracking.crit_firm_mp_id = String.join(",", this.range.getFirms());
        newTracking.crit_alt_rltd_firm = this.range.getRelatedFirms()?"Y":"N";
        newTracking.trckg_stts_cd="CMPLT";
        newTracking.cmplt_ts= StringFormat.formatDateTime(new DateTime(this.requestTime).plusSeconds(10));
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

	@When("^generate detail data$")
	public void generateMartData() {
        try {
            List<Map<String, Object>> table = DataGenerator.generateTestData(this.range, this.recordCount, this.version);
            assertNotNull(table);
            assertThat(table.size(), equalTo(this.recordCount));
        }
        catch (Exception e){
            fail(e.getMessage());
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
