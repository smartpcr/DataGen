package org.finra.test.datagen;

/**
 * Created on 9/1/2015.
 */
public enum FormatType {
	Default,
	Price,
	Date,
	Timestamp,
	Timeonly;

	public static FormatType fromString(String input) {
		switch (input.toLowerCase().trim()) {
			case "price":
				return FormatType.Price;
			default:
				return FormatType.Default;
		}
	}
}
