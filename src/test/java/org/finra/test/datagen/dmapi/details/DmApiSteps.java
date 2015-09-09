package org.finra.test.datagen.dmapi.details;

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
import org.finra.test.datagen.util.DisplayRuleUtil;
import org.finra.test.datagen.util.StringFormat;
import org.finra.test.datagen.util.TableColumn;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by xiaodongli on 9/9/15.
 */
public class DmApiSteps {
    @Given("^records in firm_ref.*")
    public void ensureDataInFirmRef(List<FirmRefRecord> records){

    }

    @Given("^records NOT in firm_ref.*")
    public void ensureDataNotInFirmRef(List<FirmRefRecord> records){

    }

    @Given("^records in issue_ref.*")
    public void ensureDataInIssueRef(List<IssueRefRecord> records) {

    }

    @Given("^records in OATS new orders.*")
    public void ensureDataInOatsNewOrders(List<OatsNewOrderRecord> records) {

    }

    @Given("^records in OATS processed orders.*")
    public void ensureDataInOatsProcessedOrders(List<OatsProcessedOrderRecord> records) {

    }

    @Given("^records in NED orders.*")
    public void ensureDataInNedOrders(List<NedOrderRecord> records) {

    }

    @Given("^records in OET orders.*")
    public void ensureDataInOetOrders(List<OetDetailRecord> records) throws IllegalAccessException, IOException, InvalidFormatException, SQLException {
        final String excelFile="";
        final String oetSheet = "";
        List<Field> fields = Arrays.asList(OetDetailRecord.class.getFields());
        List<ColumnDisplayRule> displayRules = DisplayRuleUtil.readDisplayRules();

        for(OetDetailRecord record : records) {
            Preconditions.checkNotNull(record.ORGNL_EXCTN_DT);
            Preconditions.checkNotNull(record.REC_UNIQUE_ID);
            Preconditions.checkNotNull(record.ISSUE_SYM_ID);
            Date execDate = StringFormat.getDate(record.ORGNL_EXCTN_DT);
            long uniqId = Long.parseLong(record.REC_UNIQUE_ID);
            String symbolId = record.ISSUE_SYM_ID;
            String firms = null;
            if(!Strings.isNullOrEmpty(record.RPTG_EXCTN_FIRM_MP_ID))
                firms = record.RPTG_EXCTN_FIRM_MP_ID;
            else if(!Strings.isNullOrEmpty(record.CNTRA_EXCTN_FIRM_MP_ID))
                firms = record.CNTRA_EXCTN_FIRM_MP_ID;
            TestDataRange range = new TestDataRange()
                .withSymbol(symbolId)
                .withFirms(firms)
                .withStartDate(execDate)
                .withEndDate(execDate)
                .withLastOffExchangeTradeId(uniqId)
                .withRelatedFirms(false)
                .withRecordTypes(RecordType.OffExchangeTrade);
            ReferenceData refData = new ReferenceData(range);
            RowContext context = new RowContext(1, range);

            Preconditions.checkArgument(record.RPTG_EXCTN_FIRM_MP_ID!=null || record.CNTRA_EXCTN_FIRM_MP_ID!=null);

            for(Field field : fields) {
                final String fieldName = field.getName();
                TableColumn column = OetDetailRecord.getFieldToColumnMappings().get(fieldName);
                Object value = field.get(record);
                if(value == null) {
                    ColumnDisplayRule displayRule = Iterables.find(displayRules, new Predicate<ColumnDisplayRule>() {
                        @Override
                        public boolean apply(ColumnDisplayRule columnDisplayRule) {
                            return columnDisplayRule.diverFieldName.equalsIgnoreCase("oet_" + fieldName) ||
                            (columnDisplayRule.sourceFieldName!=null &&
                                columnDisplayRule.sourceFieldName.containsKey(RecordType.OffExchangeTrade) &&
                                columnDisplayRule.sourceFieldName.get(RecordType.OffExchangeTrade).equalsIgnoreCase(fieldName));
                        }
                    });
                    if(displayRule!=null) {
                        if(displayRule.recordType==RecordType.Artificial)
                            continue;
                        if(displayRule.diverFieldName.equals("cmn_rec_type"))
                            continue;
                        value = DataGenUtil.getPickListValue(displayRule);
                        if(value!=null){
                            field.set(record, value);
                            continue;
                        }
                        value = DataGenUtil.getRefData(displayRule, refData, context);
                        if(value != null){
                            field.set(record, value);
                            continue;
                        }
                        value = DataGenUtil.applySequenceAndUniqueValues(displayRule, context);
                        if(value != null){
                            field.set(record, value);
                            continue;
                        }
                        value = DataGenUtil.applyFormat(displayRule, range);
                        if(value != null){
                            field.set(record, value);
                            continue;
                        }
                        value = DataGenUtil.applyRandomValue(displayRule, context);
                        if(value != null){
                            field.set(record, value);
                        }
                        else {
                            System.out.println(displayRule.diverFieldName);
                        }
                    }
                }
            }
        }
    }

    @When("^Run DM API query.*")
    public void runDmApiQuery() {

    }

    @Then("^I should get (\\d+) record[s]?.*")
    public void validateDmQueryResult(int expectedRecords) {

    }
}
