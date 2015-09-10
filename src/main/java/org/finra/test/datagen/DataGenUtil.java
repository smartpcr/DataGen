package org.finra.test.datagen;

import com.google.common.base.Strings;
import org.apache.commons.lang.RandomStringUtils;
import org.finra.test.datagen.rule.RowContext;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by xiaodongli on 9/9/15.
 */
public class DataGenUtil {
    static Random random = new Random(System.currentTimeMillis());

    public static Object getPickListValue(ColumnDisplayRule displayRule) {
        if(displayRule.filterType == FilterType.SelectList &&
            displayRule.fieldValueList!=null &&
            displayRule.fieldValueList.size()>0) {
            int idx = random.nextInt(displayRule.fieldValueList.size());
            String value = displayRule.fieldValueList.get(idx);
            value = value.equalsIgnoreCase("blank")? null : value;
            return value;
        }
        return null;


    }

    public static Object getRefData(ColumnDisplayRule displayRule, ReferenceData refData, RowContext rowContext) {
        Object value = null;
        if(displayRule.diverFieldName.toLowerCase().endsWith("sym_id")){
            List<ReferenceData.SymbolIssueTuple> symbolIssues = refData.getSymbolIssues();
            if(symbolIssues != null && symbolIssues.size()>0) {
                int idx = random.nextInt(symbolIssues.size());
                value = symbolIssues.get(idx).symbol;
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
                    value = issueId;
                    rowContext.setIssueId(issueId);
                }
                else {
                    int idx = random.nextInt(symbolIssues.size());
                    int issueId = symbolIssues.get(idx).issueId;
                    value = issueId;
                    rowContext.setIssueId(issueId);
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
                        value = firm;
                        rowContext.setFirm(firm);
                    }
                    break;
                case ExchangeOrder:
                case OffExchangeTrade:
                    List<ReferenceData.FirmCrdMemberTuple> firmCrdMembers = refData.getFirmCrdMembers();
                    if(firmCrdMembers!=null && firmCrdMembers.size()>0) {
                        int idx = random.nextInt(firmCrdMembers.size());
                        String firm = firmCrdMembers.get(idx).firm;
                        value = firm;
                        rowContext.setFirm(firm);
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
                                value = crd;
                                rowContext.setCrdNumber(crd);
                            }
                        }
                        else {
                            int idx = random.nextInt(firmCrds.size());
                            long crd = firmCrds.get(idx).customerId;
                            value = crd;
                            rowContext.setCrdNumber(crd);
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
                                value = crd;
                                rowContext.setCrdNumber(crd);
                            }
                        }
                        else {
                            int idx = random.nextInt(firmCrdMembers.size());
                            long crd = firmCrdMembers.get(idx).crdNumber;
                            value = crd;
                            rowContext.setCrdNumber(crd);
                        }
                    }
                    break;
            }
        }
        return value;
    }

    public static Object applyFormat(ColumnDisplayRule displayRule, TestDataRange range) {
        Object returnValue = null;
        if(displayRule.formatType == FormatType.Price || (
            displayRule.diverDataType!=null &&
                displayRule.diverDataType.dbType == DbType.Decimal &&
                displayRule.diverFieldName.toLowerCase().endsWith("_pr"))) {
            double value = random.nextDouble() * 1000;
            String price = new DecimalFormat("#.00").format(value);
            returnValue = price;
        }
        else if(displayRule.formatType == FormatType.Number ||
            (displayRule.diverDataType!=null && displayRule.diverDataType.dbType == DbType.BigInt) ||
            (displayRule.diverDataType!=null && displayRule.diverDataType.dbType == DbType.Int)) {
            int value = random.nextInt(1000);
            String number = NumberFormat.getInstance().format(value);
            returnValue = number;
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
                returnValue = value;
            }
            else {
                String value = d1.toString("yyyy-MM-dd");
                returnValue = value;
            }
        }
        else if(displayRule.formatType == FormatType.Timestamp ||
            (displayRule.diverDataType!=null && displayRule.diverDataType.dbType == DbType.Timestamp)) {
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
                returnValue = value;
            }
            else {
                d1 = d1.withHourOfDay(random.nextInt(24))
                    .withMinuteOfHour(random.nextInt(60))
                    .withSecondOfMinute(random.nextInt(60))
                    .withMillisOfSecond(random.nextInt(1000));
                String value = d1.toString("yyyy-MM-dd HH:mm:ss.SSS");
                returnValue = value;
            }
        }
        return returnValue;
    }

    public static String getSourceFieldName(ColumnDisplayRule displayRule, RowContext context){
        if(displayRule.sourceFieldName!=null && displayRule.sourceFieldName.size()>0) {
            if(displayRule.sourceFieldName.containsKey(context.getRecordType())){
                return displayRule.sourceFieldName.get(context.getRecordType());
            }
        }
        return null;
    }

    public static Object applySequenceAndUniqueValues(ColumnDisplayRule displayRule, RowContext context) {
        int size = displayRule.diverDataType!=null? displayRule.diverDataType.size : 8;
        // unique
        if(displayRule.diverFieldName.equalsIgnoreCase("fo_oats_roe_id")){
            String value = String.valueOf(context.getLastFirmOrderId());
            if(value.length()>size){
                value = value.substring(value.length()-size);
            }
            return value;
        }
        else if(displayRule.diverFieldName.equalsIgnoreCase("eo_rec_unique_id")){
            String value ="J_2015-02-11_1D1KT400NE4Z_NW_" + String.valueOf(context.getLastExchangeOrderId());
            if(value.length()>size){
                value = value.substring(value.length()-size);
            }
            return value;
        }
        else if(displayRule.diverFieldName.equalsIgnoreCase("oet_rec_unique_id")){
            String value = String.valueOf(context.getLastOffExchangeTradeId());
            if(value.length()>size){
                value = value.substring(value.length()-size);
            }
            return value;
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
            return value.toUpperCase();
        }
        else if((displayRule.diverFieldName.toLowerCase().endsWith("_nmbr") ||
            displayRule.diverFieldName.toLowerCase().endsWith("_nb")) &&
            displayRule.diverDataType!=null && displayRule.diverDataType.dbType==DbType.Varchar){
            String value = RandomStringUtils.randomNumeric(4);
            if(value.length()>size){
                value = value.substring(value.length()-size);
            }
            return value.toUpperCase();
        }
        return null;
    }

    public static Object applyRandomValue(ColumnDisplayRule displayRule, RowContext context) {
        if(displayRule.diverDataType!=null &&
            displayRule.diverDataType.dbType == DbType.Varchar &&
            displayRule.diverDataType.size>0) {
            String value= RandomStringUtils.randomAlphabetic(displayRule.diverDataType.size);
            return value.toUpperCase();
        }
        else if(displayRule.diverDataType!=null &&
            displayRule.diverDataType.dbType == DbType.Int &&
            displayRule.diverDataType.size>0) {
            return RandomStringUtils.randomNumeric(8);
        }
        else if(displayRule.diverDataType!=null &&
            displayRule.diverDataType.dbType == DbType.BigInt &&
            displayRule.diverDataType.size>0) {
            return RandomStringUtils.randomNumeric(14);
        }
        return null;
    }
}
