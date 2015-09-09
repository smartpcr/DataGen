package org.finra.test.datagen.util;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.finra.test.datagen.DbType;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * Created on 9/9/2015.
 */
public class TableColumn {
	public int ordinal;
	public String name;
	public DbType dbType;
	public int size;
	public boolean required;
	public int precision;
	public int scale;

    public static TableColumn find(List<TableColumn> columns, final TableColumn subject) {
        return Iterables.find(columns, new Predicate<TableColumn>() {
            @Override
            public boolean apply(TableColumn tableColumn) {
                return tableColumn.name.equalsIgnoreCase(subject.name);
            }
        });
    }

    @Override
    public String toString() {
        String sql;
        switch (this.dbType) {
            case Varchar:
                sql = this.name + " (" + this.size +")";
                break;
            case Decimal:
                sql = "Numeric" + String.format("(%d,%d)", this.precision, this.scale);
                break;
            default:
                sql = this.name;
                break;
        }
        if(this.required) {
            sql += " NOT NULL";
        }
        return sql;
    }

    public Object changeType(String fieldValue) {
        switch (this.dbType){
            case BigInt:
                return BigInteger.valueOf(Long.parseLong(fieldValue));
            case Date:
                return StringFormat.getDate(fieldValue);
            case Int:
                return Integer.parseInt(fieldValue);
            case Timestamp:
                return StringFormat.getDateTime(fieldValue);
            case Decimal:
                return Double.parseDouble(fieldValue);
            case Varchar:
                return fieldValue;
        }
        return fieldValue;
    }

    public String toSqlValue(Object value) {
        switch (this.dbType){
            case BigInt:
                return StringFormat.formatNumber((long)value);
            case Date:
                return "'" + StringFormat.formatDate((Date)value) + "'";
            case Int:
                return StringFormat.formatNumber((int)value);
            case Timestamp:
                return "'" + StringFormat.formatDateTime((Date) value) + "'";
            case Decimal:
                return StringFormat.formatNumber((double) value, this.precision, this.scale);
            case Varchar:
                return "'" + value.toString() + "'";
        }
        return "'" + value.toString() + "'";
    }
}
