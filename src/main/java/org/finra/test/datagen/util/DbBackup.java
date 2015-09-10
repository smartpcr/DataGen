package org.finra.test.datagen.util;

import org.apache.commons.dbutils.ResultSetHandler;
import org.finra.test.datagen.DataType;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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
			"  table_schema='" + schemaName + "'\n" +
			"  AND table_name='"+ tableName + "'\n" +
			"ORDER BY ordinal_position";
		DbConnection dbConn = DataSourceManager.getDbConnectionSettings(dataSourceName);
		return DbUtil.readList(dbConn, sql, tableColumnResultSetHandler);
	}

	public static void updateTableSchema(String dataSourceName, String schemaName, String tableName, List<TableColumn> columns) throws IOException, SQLException {
        DbConnection dbConn = DataSourceManager.getDbConnectionSettings(dataSourceName);
        String checkTableExistSql = "select 1 from information_schema.tables where table_schema='" + schemaName + "' AND table_name='"+tableName +"'";
        int result = DbUtil.readScalar(dbConn, checkTableExistSql);
        if(result>0) {
            List<TableColumn> existingColumns = getTableColumns(dataSourceName, schemaName, tableName);
            List<TableColumn> newColumns = new ArrayList<>();
            List<TableColumn> changedColumns = new ArrayList<>();
            List<TableColumn> droppedColumns = new ArrayList<>();
            for(TableColumn newCol : columns) {
                TableColumn found = TableColumn.find(existingColumns, newCol);
                if(found!=null){
                    if(found.dbType!=newCol.dbType ||
                        found.size!=newCol.size ||
                        found.required!=newCol.required ||
                        found.precision!= newCol.precision ||
                        found.scale != newCol.scale){
                        changedColumns.add(newCol);
                    }
                }
                else {
                    newColumns.add(newCol);
                }
            }
            for(TableColumn oldCol : existingColumns){
                TableColumn found = TableColumn.find(columns, oldCol);
                if(found==null){
                    droppedColumns.add(oldCol);
                }
            }
            if(newColumns.size()>0) {
                for(TableColumn col : newColumns) {
                    String addColumnSql = generateAddColumnSql(schemaName, tableName, col);
                    DbUtil.executeNonQuery(dbConn, addColumnSql);
                }
            }
            if(droppedColumns.size()>0) {
                for(TableColumn col : droppedColumns){
                    String dropColumnSql = generateDropColumnSql(schemaName, tableName, col);
                    DbUtil.executeNonQuery(dbConn, dropColumnSql);
                }
            }
            if(changedColumns.size()>0) {
                for(TableColumn col : changedColumns) {
                    TableColumn tmpColumn = TableColumn.find(existingColumns, col);
                    tmpColumn.name="__temp__";
                    String addColumnSql = generateAddColumnSql(schemaName, tableName, tmpColumn);
                    DbUtil.executeNonQuery(dbConn, addColumnSql);
                    String updateFieldSql = "update "+schemaName+"." +tableName +" set " + tmpColumn.name+"="+col.name;
                    DbUtil.executeNonQuery(dbConn, updateFieldSql);
                    String dropColumnSql = generateDropColumnSql(schemaName, tableName, col);
                    DbUtil.executeNonQuery(dbConn, dropColumnSql);
                    addColumnSql = generateAddColumnSql(schemaName, tableName, col);
                    DbUtil.executeNonQuery(dbConn, addColumnSql);
                    updateFieldSql = "update " + schemaName + "." + tableName +" set " + col.name+"="+tmpColumn.name;
                    DbUtil.executeNonQuery(dbConn, updateFieldSql);
                }
            }
        }
        else {
            String createTableSql = generateCreateTableSql(schemaName, tableName, columns);
            DbUtil.executeNonQuery(dbConn, createTableSql);
        }
	}

    public static void exportTableData(String dataSourceName, String schemaName, String tableName, String filePath) throws IOException, SQLException {
        File file = new File(filePath);
        if(file.exists()) {
            file.delete();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false));
        List<TableColumn> columns = getTableColumns(dataSourceName, schemaName, tableName);
        final ExportRecordResultHandler exportHandler = new ExportRecordResultHandler(columns, writer);
        DbConnection dbConn = DataSourceManager.getDbConnectionSettings(dataSourceName);
        String sql = "select * from " + schemaName + "." + tableName;
        DbUtil.readList(dbConn, sql, exportHandler);
        writer.flush();
        writer.close();
    }

    public static void importTableData(String dataSourceName, String schemaName, String tableName, String filePath) throws IOException, SQLException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        List<Map<String, Object>> records = new LinkedList<>();
        DbConnection dbConn = DataSourceManager.getDbConnectionSettings(dataSourceName);
        List<TableColumn> columns = getTableColumns(dataSourceName, schemaName, tableName);
        int batchSize = 500;
        int recordsRead=0;
        recordsRead = readRecordFromFile(reader, columns, records, batchSize);
        while (recordsRead > 0) {
            String insertSql = generateInsertSql(schemaName, tableName, columns, records);
            DbUtil.executeNonQuery(dbConn, insertSql);
            recordsRead = readRecordFromFile(reader, columns, records, batchSize);
        }
        reader.close();
    }

    private static String generateDropColumnSql(String schemaName, String tableName, TableColumn col) {
        return "alter table " + schemaName + "." + tableName + " drop column " + col.name;
    }

    private static String generateAddColumnSql(String schemaName, String tableName, TableColumn col) {
        return "alter table " + schemaName + "." + tableName + " add column " + col.toString();
    }

    private static String generateCreateTableSql(String schemaName, String tableName, List<TableColumn> columns) {
        StringBuilder sb = new StringBuilder();
        for(TableColumn col : columns) {
            if(sb.length()>0)
                sb.append(",");
            sb.append("\n\t"+col.toString());
        }
        return "create table " + schemaName + "." + tableName + "(" + sb.toString() + ")";
    }

    private static String generateInsertSql(String schemaName, String tableName, List<TableColumn> columns, List<Map<String, Object>> records) {
        StringBuilder fieldList = new StringBuilder();
        StringBuilder valueList = new StringBuilder();
        for(TableColumn col : columns) {
            if(fieldList.length()>0)
                fieldList.append(",");
            fieldList.append(col.name);
        }
        for(Map<String, Object> record : records) {
            if(valueList.length()>0)
                valueList.append(",");
            StringBuilder values = new StringBuilder();
            for(int i = 0; i< columns.size(); i++) {
                TableColumn col = columns.get(i);
                Object value = record.get(col.name);
                if(values.length()>0) {
                    values.append(",");
                }
                if(value==null){
                    values.append("NULL");
                }
                else {
                    values.append(col.toSqlValue(value));
                }
            }
            valueList.append("\n("+values.toString() + ")");
        }
        return "insert into " + schemaName + "." + tableName + "(" +
            fieldList.toString() + ") values (" + valueList.toString() + ")";
    }

    private static int readRecordFromFile(BufferedReader reader, List<TableColumn> columns, List<Map<String, Object>> records, int batchSize) throws IOException {
        int linesRead = 0;
        String line = reader.readLine();
        while (line!=null && records.size()<batchSize) {
            linesRead++;
            String[] fieldValues = line.split("\\t");
            Map<String, Object> record = new LinkedHashMap<>();
            for(int i = 0; i<columns.size(); i++) {
                TableColumn col = columns.get(i);
                String colName = col.name;
                if(fieldValues[i].length()>0) {
                    Object value = col.changeType(fieldValues[i]);
                    record.put(colName, value);
                }
                else {
                    record.put(colName, null);
                }
            }
            records.add(record);
            line = reader.readLine();
        }
        return linesRead;
    }

    static final ResultSetHandler<TableColumn> tableColumnResultSetHandler = new ResultSetHandler<TableColumn>() {
		@Override
		public TableColumn handle(ResultSet resultSet) throws SQLException {
			TableColumn tuple = new TableColumn();
			tuple.ordinal = resultSet.getInt("ordinal_position");
			tuple.name = resultSet.getString("column_name");
			tuple.required = resultSet.getString("is_nullable").equals("NO");
			DataType dataType = DataType.parseDataType(resultSet.getString("data_type"));
			tuple.dbType = dataType.dbType;
			tuple.size = dataType.size;
			tuple.precision = dataType.precision;
			tuple.scale = dataType.scale;
			return tuple;
		}
	};

    static class ExportRecordResultHandler implements ResultSetHandler {
        private List<TableColumn> columns;
        private BufferedWriter fileWriter;

        public ExportRecordResultHandler(List<TableColumn> columns, BufferedWriter fileWriter) {
            this.columns = columns;
            this.fileWriter = fileWriter;
        }

        @Override
        public Object handle(ResultSet resultSet) throws SQLException {
            StringBuilder sb = new StringBuilder();
            for(TableColumn col : this.columns) {
                Object value = resultSet.getObject(col.name);
                if(sb.length()>0)
                    sb.append("\t");
                if(value!=null){
                    sb.append(value.toString());
                }
            }
            sb.append("\n");
            try {
                this.fileWriter.write(sb.toString());
            } catch (IOException e) {
                throw new RuntimeException("Unable to write to file", e);
            }
            return null;
        }
    }
}
