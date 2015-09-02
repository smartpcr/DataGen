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
import java.text.NumberFormat;
import java.util.*;

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
        RowContext context = new RowContext(totalRecords);
        List<ColumnDisplayRule> displayRules = DisplayRuleUtil.readDisplayRules(version);
        Collections.sort(displayRules, new DisplayRuleComparator());
        List<Map<String, Object>> table = new ArrayList<>();
        ReferenceData refData = new ReferenceData(range);
        while (context.next()) {
            context.reset();
            Map<String, Object> record = new HashMap<>();
            for(ColumnDisplayRule displayRule : displayRules){
                boolean isValueSet = checkPickListAndReferenceData(record, context, refData, displayRule);
                if(isValueSet)
                    continue;
                isValueSet = applyFormat(record, displayRule, range);
                if(isValueSet)
                    continue;
                isValueSet = copyValue(record, displayRule, context);
                if(isValueSet)
                    continue;
                isValueSet = applyRandomValue(record, displayRule);
                if(isValueSet)
                    continue;
                else {
                    System.out.println(displayRule.diverFieldName);
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
            value = value.equals("blank")? null : value;
            record.put(displayRule.diverFieldName, value);
            if(displayRule.diverFieldName.equals("cmn_rec_type")) {
                RecordType recordType = fromString(value);
                rowContext.setRecordType(recordType);
            }
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
            displayRule.diverDataType.dbType == DbType.Decimal && displayRule.diverFieldName.toLowerCase().endsWith("_pr"))) {
            double value = random.nextDouble() * 1000;
            String price = NumberFormat.getCurrencyInstance().format(value);
            record.put(displayRule.diverFieldName, price);
            isValueSet = true;
        }
        else if(displayRule.formatType == FormatType.Number || displayRule.diverDataType.dbType == DbType.BigInt ||
            displayRule.diverDataType.dbType == DbType.Int) {
            int value = random.nextInt(1000);
            String number = NumberFormat.getInstance().format(value);
            record.put(displayRule.diverFieldName, number);
            isValueSet = true;
        }
        else if(displayRule.formatType == FormatType.Date || displayRule.diverDataType.dbType == DbType.Date) {
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
            displayRule.diverDataType.dbType == DbType.Timestamp) {
            DateTime d1 = new DateTime(range.getStartDate());
            DateTime d2 = new DateTime(range.getEndDate());
            int daysInterval = Days.daysBetween(d1.toLocalDate(), d2.toLocalDate()).getDays();
            if(daysInterval>0) {
                int days = random.nextInt(daysInterval);
                DateTime d3 = d1.plusDays(days);
                String value = d3.toString("yyyy/MM/dd HH:mm:ss.fff");
                record.put(displayRule.diverFieldName, value);
                isValueSet = true;
            }
            else {
                String value = d1.toString("yyyy/MM/dd HH:mm:ss.fff");
                record.put(displayRule.diverFieldName, value);
                isValueSet = true;
            }
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

    private static boolean applyRandomValue(Map<String, Object> record, ColumnDisplayRule displayRule) {
        if(displayRule.diverDataType.dbType == DbType.Varchar && displayRule.diverDataType.size>0) {
            String value = RandomStringUtils.random(displayRule.diverDataType.size);
            record.put(displayRule.diverFieldName, value);
            return true;
        }

        return false;
    }
}
