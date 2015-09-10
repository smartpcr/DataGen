package org.finra.test.datagen.util;

import com.google.common.base.Strings;
import com.sun.corba.se.impl.interceptors.CDREncapsCodec;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.finra.test.datagen.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;

/**
 * Created on 9/1/2015.
 */
public class DisplayRuleUtil {
	static final int LatestVersion = 8;
	static final String sheetName = "Display and Mapping Rules";
	private static Map<Integer, List<ColumnDisplayRule>> displayRulesByVersions = new HashMap<>();
	private static Map<String, Field> fieldMappings;

	private static Map<String, Field> getFieldMappings() {
		if(fieldMappings!=null && fieldMappings.size()>0)
			return fieldMappings;

		synchronized (DisplayRuleUtil.class) {
			fieldMappings = new HashMap<>();
			for(Field field : ColumnDisplayRule.class.getFields()) {
				FieldMapping mappingAnnotation = field.getAnnotation(FieldMapping.class);
				if(mappingAnnotation!=null) {
					fieldMappings.put(mappingAnnotation.ColumnHeader(), field);
				}
			}
		}
		return fieldMappings;
	}

	public static List<ColumnDisplayRule> readDisplayRules()
		throws IOException, InvalidFormatException, IllegalAccessException {
		return readDisplayRules(LatestVersion);
	}

	public static List<ColumnDisplayRule> readDisplayRules(int version)
		throws IOException, InvalidFormatException, IllegalAccessException {
		synchronized (DisplayRuleUtil.class){
			if(displayRulesByVersions.containsKey(version)) {
				return displayRulesByVersions.get(version);
			}

			String fileName = String.format("DisplayRules_v%d.xlsx", version);
			URL url = DisplayRuleUtil.class.getClassLoader().getResource(fileName);
			if(url==null)
				throw new IOException("Unable to find display rule excel file: " + fileName);

			String filePath = url.getFile();
			List<Map<String, Object>> table = ExcelUtil.readSheetAsTable(filePath, sheetName);
			List<ColumnDisplayRule> displayRules = new ArrayList<>();
			for(Map<String, Object> row : table) {
				ColumnDisplayRule displayRule = readDisplayRule(row);
				if(displayRule.displayOrder>0) {
					displayRules.add(readDisplayRule(row));
				}
			}
			List<ColumnDisplayRule> txtColumnDisplayRules = getAdditionalTextColumns(version);
			for(ColumnDisplayRule displayRule : txtColumnDisplayRules) {
				boolean exist = false;
				for(ColumnDisplayRule displayRule1 : displayRules) {
					if(displayRule.diverFieldName.equalsIgnoreCase(displayRule1.diverFieldName)){
						exist=true;
						break;
					}
				}
				if(!exist){
					displayRules.add(displayRule);
				}
			}
			displayRulesByVersions.put(version, displayRules);
			return displayRules;
		}
	}

	private static ColumnDisplayRule readDisplayRule(Map<String, Object> row)
		throws IllegalAccessException {
		Map<String, Field> mappings = getFieldMappings();
		ColumnDisplayRule displayRule = new ColumnDisplayRule();
		for(String header : mappings.keySet()) {
			if(row.containsKey(header)){
				Object fieldValue = row.get(header);
				Field field = mappings.get(header);
				if(field.getType() == String.class && fieldValue!=null) {
					field.set(displayRule, fieldValue.toString().trim());
				}
				else if(fieldValue!=null && fieldValue.toString().trim().length()>0){
					switch (field.getName()){
						case "recordType":
							displayRule.recordType = RecordType.fromString(fieldValue.toString());
							break;
						case "sourceFieldName":
							displayRule.sourceFieldName = parseSourceFieldName(fieldValue.toString());
							break;
						case "displayOrder":
							displayRule.displayOrder = parseInt(fieldValue.toString());
							break;
						case "relativeWidth":
							displayRule.relativeWidth = parseInt(fieldValue.toString());
							break;
						case "filterType":
							displayRule.filterType = FilterType.fromString(fieldValue.toString());
							break;
						case "sourceDataType":
							displayRule.sourceDataType = parseSourceDataType(fieldValue.toString());
							break;
						case "diverDataType":
							displayRule.diverDataType = DataType.parseDataType(fieldValue.toString());
							break;
						case "fieldValueList":
							displayRule.fieldValueList = Arrays.asList(fieldValue.toString().split("\\s*,\\s*"));
							break;
						case "derivedField":
							displayRule.derivedField = fieldValue.toString().equals("Y");
							break;
						case "required":
							displayRule.required = fieldValue.toString().equals("Y");
							break;
						case "formatType":
							displayRule.formatType =FormatType.fromString(fieldValue.toString());
							break;
						case "defaultField":
							displayRule.defaultField = fieldValue.toString().equals("Y");
							break;
					}
				}
			}
		}
		return displayRule;
	}

	private static Map<RecordType, String> parseSourceFieldName(String value) {
		Map<RecordType, String> srcFieldNames = new HashMap<>();
		if(!Strings.isNullOrEmpty(value)){
			String[] items = value.split("|");
			for(String item : items) {
				String[] pair = item.split(":");
				if(pair.length==2 && !Strings.isNullOrEmpty(pair[0]) && !Strings.isNullOrEmpty(pair[1])){
					String recordTypeName = pair[0];
					if(recordTypeName.indexOf(".")>0){
						recordTypeName = recordTypeName.substring(0, recordTypeName.indexOf("."));
					}
					RecordType recordType = RecordType.fromString(recordTypeName);
					srcFieldNames.put(recordType, pair[1]);
				}
			}
		}
		return srcFieldNames;
	}

	private static Map<RecordType, DataType> parseSourceDataType(String value) {
		Map<RecordType, DataType> srcDataTypes = new HashMap<>();
		if(!Strings.isNullOrEmpty(value)) {
			String[] items = value.split("|");
			for(String item : items){
				String[] pair = item.split(":");
				if(pair.length==2 && !Strings.isNullOrEmpty(pair[0]) && !Strings.isNullOrEmpty(pair[1])){
					String recordTypeName = pair[0];
					if(recordTypeName.indexOf(".")>0){
						recordTypeName = recordTypeName.substring(0, recordTypeName.indexOf("."));
					}
					RecordType recordType = RecordType.fromString(recordTypeName);
					DataType dataType = DataType.parseDataType(pair[1]);
					srcDataTypes.put(recordType, dataType);
				}
			}
		}
		return srcDataTypes;
	}

	private static int parseInt(String input) {
		return (int)Math.floor(Double.parseDouble(input));
	}

	private static List<ColumnDisplayRule> getAdditionalTextColumns(int version) {
		Integer displayOrder = 1000;
		List<ColumnDisplayRule> textColumns = new ArrayList<>();
		textColumns.add(createTextColumnDisplayRule("cmn_clmn_data_tx", displayOrder));
		textColumns.add(createTextColumnDisplayRule("fo_clmn_data_tx", displayOrder));
		textColumns.add(createTextColumnDisplayRule("eo_clmn_data_tx", displayOrder));
		if(version== LatestVersion) {
			textColumns.add(createTextColumnDisplayRule("oet_clmn_data_tx", displayOrder));
		}
		return textColumns;
	}

	private static ColumnDisplayRule createTextColumnDisplayRule(String fieldName, Integer displayOrder) {
		ColumnDisplayRule rule = new ColumnDisplayRule();
		rule.diverFieldName = fieldName;
		rule.displayOrder = ++displayOrder;
		rule.diverDataType = new DataType();
		rule.diverDataType.size=100;
		rule.recordType = RecordType.Unknown;
		return rule;
	}
}
