package org.finra.test.datagen.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import javax.swing.text.NumberFormatter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

/**
 * Created by xiaodongli on 9/9/15.
 */
public class StringFormat {
    //region dateonly
    public static String formatDate(String value) {
        String datePattern= "yyyy-MM-dd";
        if(value.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")){
            datePattern= "yyyy-MM-dd";
        }
        else if(value.matches("^\\d{1,2}-\\d{1,2}-\\d{4}$")){
            datePattern= "MM-dd-yyyy";
        }
        else if(value.matches("^\\d{4}/\\d{1,2}/\\d{1,2}$")){
            datePattern= "yyyy/MM/dd";
        }
        else if(value.matches("^\\d{1,2}/\\d{1,2}/\\d{4}$")){
            datePattern= "MM/dd/yyyy";
        }
        return DateTime.parse(value, DateTimeFormat.forPattern(datePattern)).toString("yyyy-MM-dd");
    }

    public static String formatDate(Date date) {
        return new DateTime(date).toString("yyyy-MM-dd");
    }

    public static String formatDate(DateTime date) {
        return date.toString("yyyy-MM-dd");
    }

    public static Date getDate(String value) {
        String datePattern= "yyyy-MM-dd";
        if(value.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")){
            datePattern= "yyyy-MM-dd";
        }
        else if(value.matches("^\\d{1,2}-\\d{1,2}-\\d{4}$")){
            datePattern= "MM-dd-yyyy";
        }
        else if(value.matches("^\\d{4}/\\d{1,2}/\\d{1,2}$")){
            datePattern= "yyyy/MM/dd";
        }
        else if(value.matches("^\\d{1,2}/\\d{1,2}/\\d{4}$")){
            datePattern= "MM/dd/yyyy";
        }
        try {
            return DateTime.parse(value, DateTimeFormat.forPattern(datePattern)).toDate();
        }
        catch (Throwable ignored) {
            return null;
        }
    }

    public static Date getDate(Object value) {
        return value==null? null: getDate(value.toString());
    }
    //endregion

    //region timeonly
    public static String formatTime(String value) {
        String timePattern= "HH:mm:ss";
        String datePattern= "yyyy/MM/dd";
        if(value.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")){
            datePattern= "yyyy-MM-dd";
        }
        else if(value.matches("^\\d{1,2}-\\d{1,2}-\\d{4}$")){
            datePattern= "MM-dd-yyyy";
        }
        else if(value.matches("^\\d{4}/\\d{1,2}/\\d{1,2}$")){
            datePattern= "yyyy/MM/dd";
        }
        else if(value.matches("^\\d{1,2}/\\d{1,2}/\\d{4}$")){
            datePattern= "MM/dd/yyyy";
        }
        return DateTime.parse(value, DateTimeFormat.forPattern(datePattern+" "+timePattern)).toString(timePattern);
    }

    public static String formatTime(Date date) {
        return new DateTime(date).toString("HH:mm:ss");
    }

    public static String formatTime(DateTime date) {
        return date.toString("HH:mm:ss");
    }

    //endregion

    //region timestamp
    public static String formatTimestamp(String value) {
        String timePattern= "HH:mm:ss";
        String datePattern= "yyyy/MM/dd";
        if(value.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")){
            datePattern= "yyyy-MM-dd";
        }
        else if(value.matches("^\\d{1,2}-\\d{1,2}-\\d{4}$")){
            datePattern= "MM-dd-yyyy";
        }
        else if(value.matches("^\\d{4}/\\d{1,2}/\\d{1,2}$")){
            datePattern= "yyyy/MM/dd";
        }
        else if(value.matches("^\\d{1,2}/\\d{1,2}/\\d{4}$")){
            datePattern= "MM/dd/yyyy";
        }
        return DateTime.parse(value, DateTimeFormat.forPattern(datePattern+" "+timePattern)).toString("yyyy/MM/dd HH:mm:ss");
    }

    public static String formatTimestamp(Date date) {
        return new DateTime(date).toString("yyyy/MM/dd HH:mm:ss");
    }

    public static String formatTimestamp(DateTime date) {
        return date.toString("yyyy/MM/dd HH:mm:ss");
    }
    //endregion

    //region datetime
    public static String formatDateTime(String value) {
        String timePattern= "HH:mm:ss";
        String datePattern= "yyyy-MM-dd";
        if(value.matches("^\\d{4}-\\d{1,2}-\\d{1,2}.*")){
            datePattern= "yyyy-MM-dd";
        }
        else if(value.matches("^\\d{1,2}-\\d{1,2}-\\d{4}.*")){
            datePattern= "MM-dd-yyyy";
        }
        else if(value.matches("^\\d{4}/\\d{1,2}/\\d{1,2}.*")){
            datePattern= "yyyy/MM/dd";
        }
        else if(value.matches("^\\d{1,2}/\\d{1,2}/\\d{4}.*")){
            datePattern= "MM/dd/yyyy";
        }
        return DateTime.parse(value, DateTimeFormat.forPattern(datePattern+" "+timePattern)).toString("yyyy-MM-dd HH:mm:ss");
    }

    public static String formatDateTime(Date date) {
        return new DateTime(date).toString("yyyy-MM-dd HH:mm:ss");
    }

    public static String formatDateTime(DateTime date) {
        return date.toString("yyyy-MM-dd HH:mm:ss");
    }

    public static Date getDateTime(String value) {
        String timePattern= "HH:mm:ss";
        String datePattern= "yyyy-MM-dd";
        if(value.matches("^\\d{4}-\\d{1,2}-\\d{1,2}.*")){
            datePattern= "yyyy-MM-dd";
        }
        else if(value.matches("^\\d{1,2}-\\d{1,2}-\\d{4}.*")){
            datePattern= "MM-dd-yyyy";
        }
        else if(value.matches("^\\d{4}/\\d{1,2}/\\d{1,2}.*")){
            datePattern= "yyyy/MM/dd";
        }
        else if(value.matches("^\\d{1,2}/\\d{1,2}/\\d{4}.*")){
            datePattern= "MM/dd/yyyy";
        }
        try {
            return DateTime.parse(value, DateTimeFormat.forPattern(datePattern + " " + timePattern)).toDate();
        }
        catch (Throwable ignored){
            return null;
        }
    }

    public static Date getDateTime(Object value) {
        return value==null? null : getDateTime(value.toString());
    }
    //endregion

    //region number
    public static String formatNumber(int value) {
        return NumberFormat.getCurrencyInstance().format(value);
    }

    public static String formatNumber(long value) {
        return NumberFormat.getCurrencyInstance().format(value);
    }

    public static String formatNumber(double value, int precision, int scale) {
        if(precision>0 && scale>=0 & precision>scale){
            String pattern = "";
            for(int i=1;i<= precision-scale;i++) {
                pattern = "#" + pattern;
                if(i % 3 ==0 && i< precision-scale) {
                    pattern ="," + pattern;
                }
            }
            if(scale>0) {
                pattern += ".";
            }
            for(int i = 0; i<scale;i++) {
                pattern+="0";
            }
            DecimalFormat format = new DecimalFormat(pattern);
            return format.format(value);
        }
        return NumberFormat.getCurrencyInstance().format(value);
    }
    //endregion
}
