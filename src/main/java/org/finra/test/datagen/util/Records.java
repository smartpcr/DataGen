package org.finra.test.datagen.util;

import org.finra.test.datagen.HandleCopyValue;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.util.*;

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

    public static List<LinkedCaseInsensitiveMap<Object>> union(List<LinkedCaseInsensitiveMap<Object>> table1, List<LinkedCaseInsensitiveMap<Object>> table2) {
        for(LinkedCaseInsensitiveMap<Object> newRec : table2){
            boolean foundMatch = false;
            for(LinkedCaseInsensitiveMap<Object> existingRec : table1) {
                boolean matchAllField = newRec.size() > 0;
                if(matchAllField){
                    for(String fieldName : newRec.keySet()) {
                        if (!existingRec.containsKey(fieldName)) {
                            matchAllField = false;
                            break;
                        }

                        Object fieldValue = newRec.get(fieldName);
                        if(fieldValue==null || fieldValue.toString().equals("")){
                            if(existingRec.get(fieldName) !=null && !existingRec.get(fieldName).toString().equals("")){
                                matchAllField = false;
                                break;
                            }
                            else {
                                continue;
                            }
                        }
                        if(existingRec.get(fieldName) !=null && !existingRec.get(fieldName).equals(fieldValue)){
                            matchAllField = false;
                            break;
                        }
                    }
                }
                if(matchAllField){
                    foundMatch = true;
                    break;
                }
            }
            if(!foundMatch) {
                table1.add(newRec);
            }
        }
        return table1;
    }

    public static List<LinkedCaseInsensitiveMap<Object>> intercept(List<LinkedCaseInsensitiveMap<Object>> table1, List<LinkedCaseInsensitiveMap<Object>> table2) {
        List<LinkedCaseInsensitiveMap<Object>> commonRecords = new LinkedList<>();
        for(LinkedCaseInsensitiveMap<Object> existingRec : table1) {
            boolean foundMatch = false;
            for(LinkedCaseInsensitiveMap<Object> newRec : table2) {
                boolean matchAllField = newRec.size() > 0;
                if(matchAllField){
                    for(String fieldName : newRec.keySet()) {
                        if (!existingRec.containsKey(fieldName)) {
                            matchAllField = false;
                            break;
                        }

                        Object fieldValue = newRec.get(fieldName);
                        if(fieldValue==null || fieldValue.toString().equals("")){
                            if(existingRec.get(fieldName) !=null && !existingRec.get(fieldName).toString().equals("")){
                                matchAllField = false;
                                break;
                            }
                            else {
                                continue;
                            }
                        }
                        if(existingRec.get(fieldName) !=null && !existingRec.get(fieldName).equals(fieldValue)){
                            matchAllField = false;
                            break;
                        }
                    }
                }
                if(matchAllField){
                    foundMatch = true;
                    break;
                }
            }
            if(foundMatch) {
                commonRecords.add(existingRec);
            }
        }
        return commonRecords;
    }

    public static List<LinkedCaseInsensitiveMap<Object>> except(List<LinkedCaseInsensitiveMap<Object>> table1, List<LinkedCaseInsensitiveMap<Object>> table2) {
        List<LinkedCaseInsensitiveMap<Object>> leftOver = new LinkedList<>();
        for(LinkedCaseInsensitiveMap<Object> existingRec : table1) {
            boolean foundMatch = false;
            for(LinkedCaseInsensitiveMap<Object> newRec : table2) {
                boolean matchAllField = newRec.size() > 0;
                if(matchAllField){
                    for(String fieldName : newRec.keySet()) {
                        if (!existingRec.containsKey(fieldName)) {
                            matchAllField = false;
                            break;
                        }

                        Object fieldValue = newRec.get(fieldName);
                        if(fieldValue==null || fieldValue.toString().equals("")){
                            if(existingRec.get(fieldName) !=null && !existingRec.get(fieldName).toString().equals("")){
                                matchAllField = false;
                                break;
                            }
                            else {
                                continue;
                            }
                        }
                        if(existingRec.get(fieldName) !=null && !existingRec.get(fieldName).equals(fieldValue)){
                            matchAllField = false;
                            break;
                        }
                    }
                }
                if(matchAllField){
                    foundMatch = true;
                    break;
                }
            }
            if(!foundMatch) {
                leftOver.add(existingRec);
            }
        }

        return leftOver;
    }

    public static LinkedCaseInsensitiveMap<Object> toCaseInsensitiveMap(Map<String, Object> map) {
        LinkedCaseInsensitiveMap<Object> newMap = new LinkedCaseInsensitiveMap<>();
        for(String key : map.keySet()){
            newMap.put(key, map.get(key));
        }
        return newMap;
    }

    public static List<LinkedCaseInsensitiveMap<Object>> toCaseInsensitiveRecords(List<Map<String, Object>> records) {
        List<LinkedCaseInsensitiveMap<Object>> newRecords = new LinkedList<>();
        for(Map<String, Object> rec : records) {
            newRecords.add(toCaseInsensitiveMap(rec));
        }
        return newRecords;
    }

    public static Map<String, Object> toMap(LinkedCaseInsensitiveMap<Object> map) {
        Map<String, Object> newMap = new LinkedHashMap<>();
        for(String key : map.keySet()){
            newMap.put(key, map.get(key));
        }
        return newMap;
    }

    public static List<Map<String, Object>> toRecords(List<LinkedCaseInsensitiveMap<Object>> records) {
        List<Map<String, Object>> newRecords = new LinkedList<>();
        for(LinkedCaseInsensitiveMap<Object> rec : records) {
            newRecords.add(toMap(rec));
        }
        return newRecords;
    }
}
