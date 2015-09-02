package org.finra.test.datagen;

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
}
