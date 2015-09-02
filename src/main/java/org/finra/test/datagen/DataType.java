package org.finra.test.datagen;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 9/1/2015.
 */
public class DataType {
	public DbType dbType;
	public int size;
	public int precision;
	public int scale;

	static Pattern sizePattern = Pattern.compile(".+\\((\\d+)\\)$");
	static Pattern decimalPattern = Pattern.compile(".+\\((\\d+),\\s*(\\d+)\\)$");

	public static DataType parseDataType(String input) {
		DataType dataType = new DataType();
		dataType.dbType = DbType.Varchar;
		dataType.size = 255;
		dataType.precision = 18;
		dataType.scale = 8;
		if(input.toLowerCase().startsWith("varchar")) {
			dataType.dbType = DbType.Varchar;
			Matcher m = sizePattern.matcher(input);
			if(m.find()){
				dataType.size = Integer.parseInt(m.group(1));
			}
		}
		else if(input.toLowerCase().startsWith("decimal")) {
			dataType.dbType = DbType.Decimal;
			Matcher m = decimalPattern.matcher(input);
			if(m.find()){
				dataType.precision = Integer.parseInt(m.group(1));
				dataType.scale = Integer.parseInt(m.group(1));
			}
		}
		else {
			switch (input.toLowerCase()){
				case "bigint":
					dataType.dbType =  DbType.BigInt;
					break;
				case "date":
				case "datefield":
					dataType.dbType =  DbType.Date;
					break;
				case "int":
					dataType.dbType =  DbType.Int;
					break;
				case "timestamp":
				case "time only":
					dataType.dbType =  DbType.Timestamp;
					break;
				case "decimal":
					dataType.dbType =  DbType.Decimal;
					break;
				case "string":
				default:
					dataType.dbType =  DbType.Varchar;
					break;
			}
		}
		return dataType;
	}
}
