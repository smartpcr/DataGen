package org.finra.test.datagen;

import java.util.Random;

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

	static Random random = new Random(System.currentTimeMillis());
	public static RecordType random() {
		int i = random.nextInt() % 3;
		switch (i) {
			case 0:
				return RecordType.FirmOrder;
			case 1:
				return RecordType.ExchangeOrder;
			case 2:
				return RecordType.OffExchangeTrade;
		}
		return RecordType.Unknown;
	}
}
