package org.finra.test.datagen;

/**
 * Created on 9/1/2015.
 */
public enum FilterType {
	Unknown,
	SelectList,
	Id,
	String,
	DateField,
	TimeOnly,
	TimeStamp,
	Number,
	Link,
	Blank;

	public static FilterType fromString(String input) {
		switch (input.toLowerCase().trim()) {
			case "datefield":
				return FilterType.DateField;
			case "id":
				return FilterType.Id;
			case "link":
				return FilterType.Link;
			case "number":
				return FilterType.Number;
			case "select list":
				return FilterType.SelectList;
			case "string":
				return FilterType.String;
			case "time only":
				return FilterType.TimeOnly;
			case "timestamp":
				return FilterType.TimeStamp;
			case "(blanks)":
				return FilterType.Blank;
			default:
				return FilterType.Unknown;
		}
	}
}
