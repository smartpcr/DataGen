package org.finra.test.datagen;

/**
 * Created on 9/1/2015.
 */
public enum RecordType {
	Unknown,
	Common,
	FirmOrder,
	ExchangeOrder,
	OffExchangeTrade,
	Derived,
	Artificial;

	public static RecordType fromString(String value) {
		switch (value.toLowerCase()) {
			case "artificial":
				return RecordType.Artificial;
			case "common":
				return RecordType.Common;
			case "firm order":
				return RecordType.FirmOrder;
			case "exchange order":
				return RecordType.ExchangeOrder;
			case "off exchange trade":
				return RecordType.OffExchangeTrade;
			case "derived":
				return RecordType.Derived;
			default:
				return RecordType.Unknown;
		}
	}
}
