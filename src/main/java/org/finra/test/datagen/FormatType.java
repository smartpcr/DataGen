package org.finra.test.datagen;

/**
 * Created on 9/1/2015.
 */
public enum FormatType {
	/*
	 * no format
	 */
	Default,
	/*
	 * 123,456.90
	 */
	Price,
	/*
	 * 23,456
	 */
	Number,
	/*
	 * 06-04-1989
	 */
	Date,
	/*
	 * 06/04/1989 18:09:12.000
	 */
	Timestamp,
	/*
	 * 12:34:56.000
	 */
	Timeonly;

	public static FormatType fromString(String input) {
		switch (input.toLowerCase().trim()) {
			case "price":
				return FormatType.Price;
			case "date":
				return FormatType.Date;
			case "timeonly":
				return FormatType.Timeonly;
			case "timestamp":
				return FormatType.Timestamp;
			case "number":
				return FormatType.Number;
			default:
				return FormatType.Default;
		}
	}
}
