package org.finra.test.datagen.util;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 9/1/2015.
 */
public class DbUtil {
    public static void executeNonQuery(DbConnection dbConn, String sql) throws SQLException {
        Connection connection = getConnection(dbConn);
        try {
            Statement stmt = connection.createStatement();
            stmt.executeQuery(sql);
        }
        catch (SQLException sqlEx){
            throw new RuntimeException("Unable to query DB", sqlEx);
        }
        finally {
            if(connection != null){
                try {
                    connection.close();
                }
                catch (Throwable ignored){}
            }
        }
    }

	public static List<Map<String, Object>> readTable(DbConnection conn, String schemaName, String tableName)
		throws SQLException {
		List<Map<String, Object>> table = new ArrayList<>();
		Connection connection = getConnection(conn);
		try {
			Statement stmt = connection.createStatement();
			ResultSet rset = stmt.executeQuery(String.format("SELECT * FROM %s.%s", schemaName, tableName));
			int numCols = rset.getMetaData().getColumnCount();
			List<String> columns = new ArrayList<>();
			for(int i = 0; i < numCols; i++) {
				columns.add(rset.getMetaData().getColumnName(i));
			}
			while (rset.next()){
				Map<String, Object> row = new HashMap<>();
				for(int i = 0; i < numCols; i++) {
					String colName = columns.get(i);
					row.put(colName, rset.getObject(i));
				}
				table.add(row);
			}
		}
		catch (SQLException sqlEx){
			throw new RuntimeException("Unable to query DB", sqlEx);
		}
		finally {
			if(connection != null){
				try {
					connection.close();
				}
				catch (Throwable ignored){}
			}
		}
		return table;
	}

	public static List<String> readDistinctValues(DbConnection conn, String sql) throws SQLException {
		List<String> distinctValues = new ArrayList<>();
        Connection connection = getConnection(conn);
		try {
			Statement stmt = connection.createStatement();
			ResultSet rset = stmt.executeQuery(sql);
			int numCols = rset.getMetaData().getColumnCount();
			List<String> columns = new ArrayList<>();
			for(int i = 0; i < numCols; i++) {
				columns.add(rset.getMetaData().getColumnName(i));
			}
			while (rset.next()){
				distinctValues.add(rset.getString(0));
			}
		}
		catch (SQLException sqlEx){
			throw new RuntimeException("Unable to query DB", sqlEx);
		}
		finally {
			if(connection != null){
				try {
					connection.close();
				}
				catch (Throwable ignored){}
			}
		}

		return distinctValues;
	}

    public static <T> List<T> readList(DbConnection conn, String sql, ResultSetHandler<T> handler) throws SQLException {
        List<T> list = new ArrayList<>();
        Connection connection = getConnection(conn);
        try {
            Statement stmt = connection.createStatement();
            ResultSet rset = stmt.executeQuery(sql);
            while (rset.next()){
                list.add(handler.handle(rset));
            }
        }
        catch (Exception e){
            throw new RuntimeException("Unable to execute query: " + sql);
        }
        finally {
            if(connection != null){
                try {
                    connection.close();
                }
                catch (Throwable ignored){}
            }
        }
        return list;
    }

    private static Connection getConnection(DbConnection conn) throws SQLException {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(conn.getDriverClassName());
        ds.setUsername(conn.getUser());
        ds.setPassword(conn.getPwd());
        ds.setUrl(conn.getHost());
        Connection connection = ds.getConnection();
        return connection;
    }

    public static int readScalar(DbConnection dbConn, String sql) throws SQLException {
        Connection connection = getConnection(dbConn);
        try {
            Statement stmt = connection.createStatement();
            ResultSet rset = stmt.executeQuery(sql);
            if(rset.next()){
                return rset.getInt(0);
            }
            throw new RuntimeException("No result result from scalar sql " + sql);
        }
        catch (SQLException sqlEx){
            throw new RuntimeException("Unable to query DB", sqlEx);
        }
        finally {
            if(connection != null){
                try {
                    connection.close();
                }
                catch (Throwable ignored){}
            }
        }
    }
}
