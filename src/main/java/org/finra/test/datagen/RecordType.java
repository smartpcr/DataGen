package org.finra.test.datagen;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import java.util.*;

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
            case "fo":
				return RecordType.FirmOrder;
			case "exchange order":
            case "eo":
				return RecordType.ExchangeOrder;
			case "off exchange trade":
            case "oet":
				return RecordType.OffExchangeTrade;
			case "derived":
				return RecordType.Derived;
			default:
				return RecordType.Unknown;
		}
	}

    public static List<RecordType> valuesOf(String value) {
        Set<RecordType> recordTypes = new HashSet<>();
        for(String item : value.split("\\s*,\\s*")){
            recordTypes.add(fromString(item));
        }
        return Arrays.asList(recordTypes.toArray(new RecordType[recordTypes.size()]));
    }

    public static String join(List<RecordType> recordTypes) {
        StringBuilder sb = new StringBuilder();
        if(recordTypes!=null && recordTypes.size()>0) {
            for(RecordType recType: recordTypes){
                if(sb.length()>0)
                    sb.append(",");
                switch (recType){
                    case FirmOrder:
                        sb.append("fo");
                        break;
                    case ExchangeOrder:
                        sb.append("eo");
                        break;
                    case OffExchangeTrade:
                        sb.append("oet");
                        break;
                }
            }
        }
        return sb.toString();
    }

	public static String pickValue(ColumnDisplayRule displayRule, RecordType recordType) {
		switch (recordType){
			case FirmOrder:
				return Iterables.find(displayRule.fieldValueList, new Predicate<String>() {
					@Override
					public boolean apply(String s) {
						return s.toLowerCase().startsWith("firm");
					}
				});
			case ExchangeOrder:
				return Iterables.find(displayRule.fieldValueList, new Predicate<String>() {
					@Override
					public boolean apply(String s) {
						return s.toLowerCase().startsWith("exchange");
					}
				});
			case OffExchangeTrade:
				return Iterables.find(displayRule.fieldValueList, new Predicate<String>() {
					@Override
					public boolean apply(String s) {
						return s.toLowerCase().startsWith("off");
					}
				});
			default:
				return null;
		}
	}
}
