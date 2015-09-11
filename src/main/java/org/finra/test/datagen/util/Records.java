package org.finra.test.datagen.util;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.finra.test.datagen.HandleCopyValue;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaodongli on 9/9/15.
 */
public class Records {
    public static String getValue(Map<String, Object> record, String fieldName) {
        Object value = record.containsKey(fieldName)? record.get(fieldName): null;
        return value==null? null: value.toString().trim();
    }


    public static String[] getValues(Map<String, Object> record, final String fieldName) {
        String value = getValue(record, fieldName);
        return value.split("\\s*,\\s*");
    }

    public static Date getDate(Map<String, Object> record, final String fieldName){
        return StringFormat.getDate(getValue(record, fieldName));
    }

    public static long getLong(Map<String, Object> record, final String fieldName){
        return Long.parseLong(getValue(record, fieldName));
    }

    public static boolean getBoolean(Map<String, Object> record, final String fieldName){
        String fieldValue = getValue(record, fieldName);
        return fieldValue.equalsIgnoreCase("Y") || fieldValue.equalsIgnoreCase("T") ||
            fieldValue.equalsIgnoreCase("Yes") || fieldValue.equalsIgnoreCase("True") ||
            fieldValue.equalsIgnoreCase("1");
    }

    public static int getInt(Map<String, Object> record, final String fieldName){
        return Integer.parseInt(getValue(record, fieldName));
    }

    public static Date getDateTime(Map<String, Object> record, final String dateField, final String timeField){
        String date = getValue(record, dateField);
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
        return DateTime.parse(getValue(record, dateField) + " " + getValue(record, timeField), DateTimeFormat.forPattern(datePattern + " HH:mm:ss")).toDate();
    }

    public static <T extends Enum<T>> List<T> getEnumList(Class<T> clazz, Map<String, Object> record, final String fieldName) {
        List<T> enumValues = new ArrayList<>();
        for(String value : getValues(record, fieldName)) {
            T val = Enum.valueOf(clazz, value);
            enumValues.add(val);
        }
        return enumValues;
    }

	public static void applyChanges(Map<String, Object> subject, Map<String, Object> fromMap, Map<String, HandleCopyValue> valueChangeHandlers) {
		for(String key : subject.keySet()){
			if(fromMap.containsKey(key)){
				Object value = fromMap.get(key);
				if(value!=null && value.toString().contains("&gt;"))
					value = value.toString().replace("&gt;",">");
				if(value!=null && value.toString().contains("&lt;"))
					value = value.toString().replace("&lt;","<");
				Object oldValue = subject.get(key);
				if(!oldValue.equals(value)) {
					subject.put(key, value);
					if(valueChangeHandlers.containsKey(key)){
						valueChangeHandlers.get(key).updateDependentFields(subject, value);
					}
				}
			}
		}
	}
}
