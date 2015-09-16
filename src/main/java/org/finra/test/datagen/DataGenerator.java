package org.finra.test.datagen;

import com.google.common.base.Strings;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.finra.test.datagen.rule.RowContext;
import org.finra.test.datagen.util.DisplayRuleUtil;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

import static org.finra.test.datagen.RecordType.Common;
import static org.finra.test.datagen.RecordType.fromString;

/**
 * Created by xd on 9/2/2015.
 */
public class DataGenerator {
    final static int latestVersion = 8;
    static Random random = new Random(System.currentTimeMillis());

    public static List<Map<String, Object>> generateTestData(TestDataRange range, int totalRecords)
        throws IllegalAccessException, InvalidFormatException, IOException, SQLException {
        return generateTestData(range, totalRecords, latestVersion);
    }

    public static List<Map<String, Object>> generateTestData(TestDataRange range, int totalRecords, int version)
        throws IllegalAccessException, InvalidFormatException, IOException, SQLException {
        RowContext context = new RowContext(totalRecords, range);
        List<ColumnDisplayRule> displayRules = DisplayRuleUtil.readDisplayRules(version);
        Collections.sort(displayRules, new DisplayRuleComparator());
        List<Map<String, Object>> table = new ArrayList<>();
        ReferenceData refData = new ReferenceData(range);
        while (context.next()) {
            Map<String, Object> record = new LinkedHashMap<>();
            for(ColumnDisplayRule displayRule : displayRules){
	            if(displayRule.recordType==RecordType.Artificial)
		            continue;

                record.put(displayRule.diverFieldName, null);
	            if(displayRule.recordType == Common || displayRule.recordType==context.getRecordType()){
<<<<<<< HEAD
                    boolean isValueSet = checkPickListAndReferenceData(record, context, refData, displayRule);
                    if(isValueSet)
                        continue;
                    isValueSet = copyValue(record, displayRule, context);
=======
		            boolean isValueSet = copyValue(record, displayRule, context);
		            if(isValueSet)
			            continue;
                    isValueSet = checkPickListAndReferenceData(record, context, refData, displayRule);
>>>>>>> 034eb2c493cc943c6f71b860ed7003c81563ba74
                    if(isValueSet)
                        continue;
                    isValueSet = applySequenceAndUniqueValues(record, displayRule, context);
                    if(isValueSet)
                        continue;

<<<<<<< HEAD
		            if(displayRule.recordType != null) {
=======
		            if(displayRule.recordType != null && displayRule.recordType!=RecordType.Common) {
>>>>>>> 034eb2c493cc943c6f71b860ed7003c81563ba74
			            boolean shouldFillValue = random.nextDouble() > range.getFillerPercentage();
			            if (!shouldFillValue)
				            continue;
		            }

                    isValueSet = applyFormat(record, displayRule, range);
                    if(isValueSet)
                        continue;
                    isValueSet = applyRandomValue(record, displayRule, context);
                    if(isValueSet)
                        continue;
                    else {
                        System.out.println(displayRule.diverFieldName);
                    }
                }
            }
            table.add(record);
        }
        return table;
    }

    private static boolean checkPickListAndReferenceData(Map<String, Object> record, RowContext rowContext, ReferenceData refData, ColumnDisplayRule displayRule) {
        boolean isValueSet = false;
        if(displayRule.filterType == FilterType.SelectList &&
            displayRule.fieldValueList!=null &&
            displayRule.fieldValueList.size()>0) {
	        int idx = random.nextInt(displayRule.fieldValueList.size());
	        String value = displayRule.fieldValueList.get(idx);
	        value = value.equalsIgnoreCase("blank")? null : value;
	        if(displayRule.diverFieldName.equals("cmn_rec_type")) {
		        RecordType recordType = rowContext.getRecordType();
		        value = RecordType.pickValue(displayRule, recordType);
	        }
            record.put(displayRule.diverFieldName, value);
            isValueSet = true;
        }
        else if(displayRule.diverFieldName.toLowerCase().endsWith("sym_id")){
            List<ReferenceData.SymbolIssueTuple> symbolIssues = refData.getSymbolIssues();
            if(symbolIssues != null && symbolIssues.size()>0) {
                int idx = random.nextInt(symbolIssues.size());
                String symbol = symbolIssues.get(idx).symbol;
                record.put(displayRule.diverFieldName, symbol);
                isValueSet = true;
            }
        }
        else if(displayRule.diverFieldName.toLowerCase().endsWith("issue_id")){
            List<ReferenceData.SymbolIssueTuple> symbolIssues = refData.getSymbolIssues();
            if(symbolIssues != null && symbolIssues.size()>0) {
                String symbol = rowContext.getSymbol();
                if(!Strings.isNullOrEmpty(symbol)){
                    List<ReferenceData.SymbolIssueTuple> symbolIssues2 = new ArrayList<>();
                    for(ReferenceData.SymbolIssueTuple tuple : symbolIssues){
                        if(tuple.symbol.equals(symbol)){
                            symbolIssues2.add(tuple);
                        }
                    }
                    int idx = random.nextInt(symbolIssues2.size());
                    int issueId = symbolIssues2.get(idx).issueId;
                    record.put(displayRule.diverFieldName, issueId);
                    rowContext.setIssueId(issueId);
                    isValueSet = true;
                }
                else {
                    int idx = random.nextInt(symbolIssues.size());
                    int issueId = symbolIssues.get(idx).issueId;
                    record.put(displayRule.diverFieldName, issueId);
                    rowContext.setIssueId(issueId);
                    isValueSet = true;
                }
            }
        }
        else if(displayRule.diverFieldName.toLowerCase().endsWith("mp_id")) {
            switch (rowContext.getRecordType()) {
                case FirmOrder:
                    List<ReferenceData.FirmCrdTuple> firmCrds = refData.getFirmCrds();
                    if(firmCrds!=null && firmCrds.size()>0) {
                        int idx = random.nextInt(firmCrds.size());
                        String firm = firmCrds.get(idx).firm;
                        record.put(displayRule.diverFieldName, firm);
                        rowContext.setFirm(firm);
                        isValueSet = true;
                    }
                    break;
                case ExchangeOrder:
	            case OffExchangeTrade:
                    List<ReferenceData.FirmCrdMemberTuple> firmCrdMembers = refData.getFirmCrdMembers();
                    if(firmCrdMembers!=null && firmCrdMembers.size()>0) {
                        int idx = random.nextInt(firmCrdMembers.size());
                        String firm = firmCrdMembers.get(idx).firm;
                        record.put(displayRule.diverFieldName, firm);
                        rowContext.setFirm(firm);
                        isValueSet = true;
                    }
                    break;
            }
        }
        else if(displayRule.diverFieldName.toLowerCase().endsWith("bd_nb") ||
            displayRule.diverFieldName.toLowerCase().endsWith("cstmr_id")){
            String firm = rowContext.getFirm();
            switch (rowContext.getRecordType()) {
                case FirmOrder:
                    List<ReferenceData.FirmCrdTuple> firmCrds = refData.getFirmCrds();
                    if(firmCrds!=null && firmCrds.size()>0) {
                        if(!Strings.isNullOrEmpty(firm)){
                            List<ReferenceData.FirmCrdTuple> firmCrds2 = new ArrayList<>();
                            for(ReferenceData.FirmCrdTuple tuple : firmCrds) {
                                if(tuple.firm.equals(firm)){
                                    firmCrds2.add(tuple);
                                }
                            }
                            if(firmCrds2.size()>0) {
                                int idx = random.nextInt(firmCrds2.size());
                                long crd = firmCrds2.get(idx).customerId;
                                record.put(displayRule.diverFieldName, crd);
                                rowContext.setCrdNumber(crd);
                                isValueSet = true;
                            }
                        }
                        else {
                            int idx = random.nextInt(firmCrds.size());
                            long crd = firmCrds.get(idx).customerId;
                            record.put(displayRule.diverFieldName, crd);
                            rowContext.setCrdNumber(crd);
                            isValueSet = true;
                        }
                    }
                    break;
                case ExchangeOrder:
	            case OffExchangeTrade:
                    List<ReferenceData.FirmCrdMemberTuple> firmCrdMembers = refData.getFirmCrdMembers();
                    if(firmCrdMembers!=null && firmCrdMembers.size()>0) {
                        if(!Strings.isNullOrEmpty(firm)){
                            List<ReferenceData.FirmCrdMemberTuple> firmCrdMembers2 = new ArrayList<>();
                            for(ReferenceData.FirmCrdMemberTuple tuple : firmCrdMembers) {
                                if(tuple.firm.equals(firm)){
                                    firmCrdMembers2.add(tuple);
                                }
                            }
                            if(firmCrdMembers2.size()>0) {
                                int idx = random.nextInt(firmCrdMembers2.size());
                                long crd = firmCrdMembers2.get(idx).crdNumber;
                                record.put(displayRule.diverFieldName, crd);
                                rowContext.setCrdNumber(crd);
                                isValueSet = true;
                            }
                        }
                        else {
                            int idx = random.nextInt(firmCrdMembers.size());
                            long crd = firmCrdMembers.get(idx).crdNumber;
                            record.put(displayRule.diverFieldName, crd);
                            rowContext.setCrdNumber(crd);
                            isValueSet = true;
                        }
                    }
                    break;
            }
        }
        return isValueSet;
    }

    private static boolean applyFormat(Map<String, Object> record, ColumnDisplayRule displayRule, TestDataRange range) {
        boolean isValueSet = false;
        if(displayRule.formatType == FormatType.Price || (
	        displayRule.diverDataType!=null &&
		        displayRule.diverDataType.dbType == DbType.Decimal &&
		        displayRule.diverFieldName.toLowerCase().endsWith("_pr"))) {
            double value = random.nextDouble() * 1000;
            String price = new DecimalFormat("#.00").format(value);
            record.put(displayRule.diverFieldName, price);
            isValueSet = true;
        }
        else if(displayRule.formatType == FormatType.Number ||
	        (displayRule.diverDataType!=null && displayRule.diverDataType.dbType == DbType.BigInt) ||
	        (displayRule.diverDataType!=null && displayRule.diverDataType.dbType == DbType.Int)) {
            int value = random.nextInt(1000);
            String number = NumberFormat.getInstance().format(value);
            record.put(displayRule.diverFieldName, number);
            isValueSet = true;
        }
        else if(displayRule.formatType == FormatType.Date ||
	        (displayRule.diverDataType!=null && displayRule.diverDataType.dbType == DbType.Date)) {
            DateTime d1 = new DateTime(range.getStartDate());
            DateTime d2 = new DateTime(range.getEndDate());
            int daysInterval = Days.daysBetween(d1.toLocalDate(), d2.toLocalDate()).getDays();
            if(daysInterval>0) {
                int days = random.nextInt(daysInterval);
                DateTime d3 = d1.plusDays(days);
                String value = d3.toString("yyyy-MM-dd");
                record.put(displayRule.diverFieldName, value);
                isValueSet = true;
            }
            else {
                String value = d1.toString("yyyy-MM-dd");
                record.put(displayRule.diverFieldName, value);
                isValueSet = true;
            }
        }
        else if(displayRule.formatType == FormatType.Timestamp ||
	        (displayRule.diverDataType!=null && displayRule.diverDataType.dbType == DbType.Timestamp)) {
<<<<<<< HEAD
            DateTime d1 = new DateTime(range.getStartDate());
            DateTime d2 = new DateTime(range.getEndDate());
            int daysInterval = Days.daysBetween(d1.toLocalDate(), d2.toLocalDate()).getDays();
            if(daysInterval>0) {
                int days = random.nextInt(daysInterval);
                DateTime d3 = d1.plusDays(days)
	                .withHourOfDay(random.nextInt(24))
	                .withMinuteOfHour(random.nextInt(60))
	                .withSecondOfMinute(random.nextInt(60))
	                .withMillisOfSecond(random.nextInt(1000));
	            String value = d3.toString("yyyy-MM-dd HH:mm:ss.SSS");
                record.put(displayRule.diverFieldName, value);
                isValueSet = true;
            }
            else {
	            d1 = d1.withHourOfDay(random.nextInt(24))
		            .withMinuteOfHour(random.nextInt(60))
		            .withSecondOfMinute(random.nextInt(60))
		            .withMillisOfSecond(random.nextInt(1000));
	            String value = d1.toString("yyyy-MM-dd HH:mm:ss.SSS");
                record.put(displayRule.diverFieldName, value);
                isValueSet = true;
            }
=======

	        if(displayRule.diverFieldName.equalsIgnoreCase("cmn_event_tm")){
		        DateTime d = new DateTime().withYear(1970).withMonthOfYear(1).withDayOfMonth(1)
			        .withHourOfDay(random.nextInt(24))
			        .withMinuteOfHour(random.nextInt(60))
			        .withSecondOfMinute(random.nextInt(60))
			        .withMillisOfSecond(random.nextInt(1000));
		        String value = d.toString("yyyy-MM-dd HH:mm:ss.SSS");
		        record.put(displayRule.diverFieldName, value);
		        isValueSet = true;
	        }
	        else {
		        DateTime d1 = new DateTime(range.getStartDate());
		        DateTime d2 = new DateTime(range.getEndDate());
		        int daysInterval = Days.daysBetween(d1.toLocalDate(), d2.toLocalDate()).getDays();
		        if(daysInterval>0) {
			        int days = random.nextInt(daysInterval);
			        DateTime d3 = d1.plusDays(days)
				        .withHourOfDay(random.nextInt(24))
				        .withMinuteOfHour(random.nextInt(60))
				        .withSecondOfMinute(random.nextInt(60))
				        .withMillisOfSecond(random.nextInt(1000));
			        String value = d3.toString("yyyy-MM-dd HH:mm:ss.SSS");
			        record.put(displayRule.diverFieldName, value);
			        isValueSet = true;
		        }
		        else {
			        d1 = d1.withHourOfDay(random.nextInt(24))
				        .withMinuteOfHour(random.nextInt(60))
				        .withSecondOfMinute(random.nextInt(60))
				        .withMillisOfSecond(random.nextInt(1000));
			        String value = d1.toString("yyyy-MM-dd HH:mm:ss.SSS");
			        record.put(displayRule.diverFieldName, value);
			        isValueSet = true;
		        }
	        }
>>>>>>> 034eb2c493cc943c6f71b860ed7003c81563ba74
        }
        return isValueSet;
    }

    private static boolean copyValue(Map<String, Object> record, ColumnDisplayRule displayRule, RowContext context){
        boolean isValueSet = false;
        if(displayRule.sourceFieldName!=null && displayRule.sourceFieldName.size()>0) {
            if(displayRule.sourceFieldName.containsKey(context.getRecordType())){
                String srcField = displayRule.sourceFieldName.get(context.getRecordType());
                if(record.containsKey(srcField)){
                    Object value = record.get(srcField);
                    record.put(displayRule.diverFieldName, value);
                    isValueSet = true;
                }
            }
        }
        return isValueSet;
    }

	private static boolean applySequenceAndUniqueValues(Map<String, Object> record, ColumnDisplayRule displayRule, RowContext context) {
		boolean isValueSet = true;
		int size = displayRule.diverDataType!=null? displayRule.diverDataType.size : 8;
		// unique
		if(displayRule.diverFieldName.equalsIgnoreCase("fo_oats_roe_id")){
			String value = String.valueOf(context.getLastFirmOrderId());
			if(value.length()>size){
				value = value.substring(value.length()-size);
			}
			record.put(displayRule.diverFieldName, value);
		}
		else if(displayRule.diverFieldName.equalsIgnoreCase("eo_rec_unique_id")){
			String value ="J_2015-02-11_1D1KT400NE4Z_NW_" + String.valueOf(context.getLastExchangeOrderId());
			if(value.length()>size){
				value = value.substring(value.length()-size);
			}
			record.put(displayRule.diverFieldName, value);
		}
		else if(displayRule.diverFieldName.equalsIgnoreCase("oet_rec_unique_id")){
			String value = String.valueOf(context.getLastOffExchangeTradeId());
			if(value.length()>size){
				value = value.substring(value.length()-size);
			}
			record.put(displayRule.diverFieldName, value);
		}
		else if(displayRule.diverFieldName.toLowerCase().endsWith("order_id") &&
			displayRule.diverDataType!=null && displayRule.diverDataType.dbType==DbType.Varchar) {
			String value =
				RandomStringUtils.randomAlphabetic(4) +
					RandomStringUtils.randomNumeric(5) +
					RandomStringUtils.randomAlphanumeric(5);
			if(value.length()>size){
				value = value.substring(value.length()-size);
			}
			record.put(displayRule.diverFieldName, value.toUpperCase());
		}
		else if((displayRule.diverFieldName.toLowerCase().endsWith("_nmbr") ||
			displayRule.diverFieldName.toLowerCase().endsWith("_nb")) &&
			displayRule.diverDataType!=null && displayRule.diverDataType.dbType==DbType.Varchar){
			String value = RandomStringUtils.randomNumeric(4);
			if(value.length()>size){
				value = value.substring(value.length()-size);
			}
			record.put(displayRule.diverFieldName, value.toUpperCase());
		}
		else {
			isValueSet = false;
		}
		return isValueSet;
	}

    private static boolean applyRandomValue(Map<String, Object> record, ColumnDisplayRule displayRule, RowContext context) {
	    boolean isValueSet = true;

	    if(displayRule.diverDataType!=null &&
		    displayRule.diverDataType.dbType == DbType.Varchar &&
		    displayRule.diverDataType.size>0) {
		    String value= RandomStringUtils.randomAlphabetic(displayRule.diverDataType.size);
		    record.put(displayRule.diverFieldName, value.toUpperCase());
	    }
	    else if(displayRule.diverDataType!=null &&
		    displayRule.diverDataType.dbType == DbType.Int &&
		    displayRule.diverDataType.size>0) {
		    String value= RandomStringUtils.randomNumeric(8);
		    record.put(displayRule.diverFieldName, value);
	    }
	    else if(displayRule.diverDataType!=null &&
		    displayRule.diverDataType.dbType == DbType.BigInt &&
		    displayRule.diverDataType.size>0) {
		    String value= RandomStringUtils.randomNumeric(14);
		    record.put(displayRule.diverFieldName, value);
	    }
	    else {
		    isValueSet = false;
	    }
        return isValueSet;
    }
}
