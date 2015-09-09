package org.finra.test.datagen.util;

import org.apache.commons.dbutils.ResultSetHandler;
import org.finra.test.datagen.DataType;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created on 9/9/2015.
 */
public class DbBackup {
	public static List<TableColumn> getTableColumns(String dataSourceName, String schemaName, String tableName) throws IOException, SQLException {
		String sql="SELECT\n" +
			"  ordinal_position,\n" +
			"  column_name,\n" +
			"  column_default,\n" +
			"  is_nullable,\n" +
			"  case\n" +
			"  when data_type='character varying' then 'varchar('||character_maximum_length||')'\n" +
			"  when data_type='numeric' then 'numeric('||numeric_precision||','||numeric_scale||')'\n" +
			"  when data_type='timestamp without time zone' then 'timestamp'\n" +
			"  else data_type END AS data_type\n" +
			"FROM information_schema.columns\n" +
			"--LIMIT 1\n" +
			"WHERE\n" +
			"  table_schema='sawmartowner'\n" +
			"  AND table_name='event_tmplt_v8'\n" +
			"ORDER BY ordinal_position";
		DbConnection dbConn = DataSourceManager.getDbConnectionSettings(dataSourceName);
		return DbReader.readList(dbConn, sql, tableColumnResultSetHandler);
	}

	public static void updateTableSchema(String dataSourceName, String schemaName, String tableName, List<TableColumn> columns) {

	}

	static final ResultSetHandler<TableColumn> tableColumnResultSetHandler = new ResultSetHandler<TableColumn>() {
		@Override
		public TableColumn handle(ResultSet resultSet) throws SQLException {
			TableColumn tuple = new TableColumn();
			tuple.ordinal = resultSet.getInt(0);
			tuple.name = resultSet.getString(1);
			tuple.required = resultSet.getString(3).equals("NO");
			DataType dataType = DataType.parseDataType(resultSet.getString("data_type"));
			tuple.dbType = dataType.dbType;
			tuple.size = dataType.size;
			tuple.precision = dataType.precision;
			tuple.scale = dataType.scale;
			return tuple;
		}
	};
}
