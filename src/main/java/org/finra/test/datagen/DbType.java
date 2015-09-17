package org.finra.test.datagen;

import com.google.common.base.Preconditions;
import org.finra.test.datagen.util.StringFormat;

/**
 * Created on 9/1/2015.
 */
public enum DbType {
	Unknown,
	BigInt,
	Date,
	Int,
	Timestamp,
	Decimal,
	Varchar;

	public static DbType fromString(String input) {
		if(input.toLowerCase().startsWith("varchar"))
			return DbType.Varchar;
		if(input.toLowerCase().startsWith("decimal"))
			return DbType.Decimal;
		switch (input.toLowerCase()){
			case "bigint":
				return DbType.BigInt;
			case "date":
				return DbType.Date;
			case "int":
				return DbType.Int;
			case "timestamp":
				return DbType.Timestamp;
			case "decimal":
				return DbType.Decimal;
			case "string":
			default:
				return DbType.Varchar;
		}
	}

    public boolean needsEscape() {
        return this==DbType.Date || this == DbType.Timestamp || this==DbType.Varchar;
    }

    public static boolean assignableFrom(DbType dbType, int size, Object fieldValue) {
        Preconditions.checkNotNull(fieldValue);
        switch (dbType) {
            case BigInt:
                if(fieldValue.toString().matches("\\-?\\d+")) {
                    try{
                        Long.parseLong(fieldValue.toString());
                        return true;
                    }
                    catch (Throwable ignored){}
                }
                return false;
            case Date:
                return StringFormat.getDate(fieldValue)!=null;
            case Int:
                if(fieldValue.toString().matches("\\-?\\d+")){
                    try {
                        Integer.parseInt(fieldValue.toString());
                        return true;
                    }
                    catch (Throwable ignored){}
                }
                return false;
            case Timestamp:
                return StringFormat.getDateTime(fieldValue)!=null;
            case Decimal:
                if(fieldValue.toString().matches("[0-9]{0,10}\\.?[0-9]{0,8}")){
                    try {
                        Double.parseDouble(fieldValue.toString());
                        return true;
                    }catch (Throwable ignored){}
                }
                return false;
            case Varchar:
                return fieldValue.toString().length()<=size;
            default:
                return false;
        }
    }
}
