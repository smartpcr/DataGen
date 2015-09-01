package org.finra.test.datagen.util;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created on 9/1/2015.
 */
public class DataSourceManager {
	private static Map<String, DbConnection> dbConnections = new HashMap<>();
	static final String propertyFileName = "settings.properties";

	public static DbConnection getDbConnectionSettings(String dataSourceName) throws IOException {
		if(dbConnections.containsKey(dataSourceName))
			return dbConnections.get(dataSourceName);

		synchronized (DataSourceManager.class){
			Properties th = new Properties();
			String propFilePath = DataSourceManager.class.getClassLoader().getResource(propertyFileName).getFile();
			InputStream fileIn = new FileInputStream(propFilePath);
			th.load(fileIn);
			String driverPropName = String.format("%s.driver", dataSourceName);
			String driverClass = th.getProperty(driverPropName);
			String urlPropName = String.format("%s.url", dataSourceName);
			String url = th.getProperty(urlPropName);
			String userPropName = String.format("%s.user", dataSourceName);
			String user = th.getProperty(userPropName);
			String pwdPropName = String.format("%s.password", dataSourceName);
			String pwd = th.getProperty(pwdPropName);
			DbConnection dbConn=new DbConnection(driverClass, url, user, pwd);
			dbConnections.put(dataSourceName, dbConn);
			fileIn.close();
			return dbConn;
		}
	}
}
