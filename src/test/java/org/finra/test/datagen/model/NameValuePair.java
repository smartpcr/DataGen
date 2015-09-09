package org.finra.test.datagen.model;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xiaodongli on 9/8/15.
 */
public class NameValuePair {
	private String field;
	public String getField() {
	    return this.field;
	}

	private String value;
	public String getValue() {
	    return this.value;
	}

	public NameValuePair(String field, String value) {
		this.field = field;
		this.value = value;
	}

	public static String getValue(List<NameValuePair> pairs, final String fieldName) {
		NameValuePair found = Iterables.find(pairs, new Predicate<NameValuePair>() {
			@Override
			public boolean apply(NameValuePair nameValuePair) {
				return nameValuePair.getField().equalsIgnoreCase(fieldName);
			}
		});
		return found.getValue();
	}

	public static String[] getValues(List<NameValuePair> pairs, final String fieldName) {
		NameValuePair found = Iterables.find(pairs, new Predicate<NameValuePair>() {
			@Override
			public boolean apply(NameValuePair nameValuePair) {
				return nameValuePair.getField().equalsIgnoreCase(fieldName);
			}
		});
		return found.getValue().split("\\s*,\\s*");
	}

	public static Date getDate(List<NameValuePair> pairs, final String fieldName){
		return DateTime.parse(getValue(pairs, fieldName)).toDate();
	}

    public static long getLong(List<NameValuePair> pairs, final String fieldName){
        return Long.parseLong(getValue(pairs, fieldName));
    }

	public static boolean getBoolean(List<NameValuePair> pairs, final String fieldName){
		String fieldValue = getValue(pairs, fieldName);
		return fieldValue.equalsIgnoreCase("Y") || fieldValue.equalsIgnoreCase("T") ||
			fieldValue.equalsIgnoreCase("Yes") || fieldValue.equalsIgnoreCase("True") ||
			fieldValue.equalsIgnoreCase("1");
	}

	public static int getInt(List<NameValuePair> pairs, final String fieldName){
		return Integer.parseInt(getValue(pairs, fieldName));
	}

    public static Date getDateTime(List<NameValuePair> pairs, final String dateField, final String timeField){
        String date = getValue(pairs, dateField);
        String datePattern= "yyyy-MM-dd";
        if(date.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")){
            datePattern= "yyyy-MM-dd";
        }
        else if(date.matches("^\\d{1,2}-\\d{1,2}-\\d{4}$")){
            datePattern= "MM-dd-yyyy";
        }
        else if(date.matches("^\\d{4}/\\d{1,2}/\\d{1,2}$")){
            datePattern= "yyyy/MM/dd";
        }
        else if(date.matches("^\\d{1,2}/\\d{1,2}/\\d{4}$")){
            datePattern= "MM/dd/yyyy";
        }
        return DateTime.parse(getValue(pairs, dateField) + " " + getValue(pairs, timeField), DateTimeFormat.forPattern(datePattern + " HH:mm:ss")).toDate();
    }

    public static <T extends Enum<T>> List<T> getEnumList(Class<T> clazz, List<NameValuePair> pairs, final String fieldName) {
        List<T> enumValues = new ArrayList<>();
        for(String value : getValues(pairs, fieldName)) {
            T val = Enum.valueOf(clazz, value);
            enumValues.add(val);
        }
        return enumValues;
    }
}
