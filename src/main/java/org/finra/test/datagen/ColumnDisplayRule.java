package org.finra.test.datagen;


import java.util.List;
import java.util.Map;

/**
 * Created on 9/1/2015.
 */
public class ColumnDisplayRule {
	//region display rules
	@FieldMapping(ColumnHeader = "Field Type")
	public RecordType recordType;
	@FieldMapping(ColumnHeader = "Source Table Name")
	public String sourceTableName;
	@FieldMapping(ColumnHeader = "Source Field Name")
	public Map<RecordType, String> sourceFieldName;
	@FieldMapping(ColumnHeader = "DIVER Field Name")
	public String diverFieldName;
	@FieldMapping(ColumnHeader = "Diver Short Field Name")
	public String diverShortFieldName;
	@FieldMapping(ColumnHeader = "Description")
	public String description;
	@FieldMapping(ColumnHeader = "Display Order")
	public int displayOrder;
	@FieldMapping(ColumnHeader = "Relative width for Horizontal Headers View")
	public int relativeWidth;
	@FieldMapping(ColumnHeader = "Filter Type")
	public FilterType filterType;
	@FieldMapping(ColumnHeader = "Source Data Type")
	public Map<RecordType, DataType> sourceDataType;
	@FieldMapping(ColumnHeader = "DIVER Data Type")
	public DataType diverDataType;
	@FieldMapping(ColumnHeader = "Field Value List")
	public List<String> fieldValueList;
	@FieldMapping(ColumnHeader = "Derived Field")
	public boolean derivedField;
	@FieldMapping(ColumnHeader = "Required (NOT Null)")
	public boolean required;
	@FieldMapping(ColumnHeader ="Format Type")
	public FormatType formatType;
	@FieldMapping(ColumnHeader ="Default Field")
	public boolean defaultField;
	@FieldMapping(ColumnHeader ="Comments")
	public String comments;
	@FieldMapping(ColumnHeader ="Help Text")
	public String helpText;
	//endregion



}
