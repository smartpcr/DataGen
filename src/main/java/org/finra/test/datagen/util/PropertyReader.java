package org.finra.test.datagen.util;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created on 9/16/2015.
 */
public class PropertyReader {
	private static Map<String, String> keyValuePairs;

	static {
		keyValuePairs = new HashMap<>();
		final String propertyFileName = "settings.properties";
		String propFilePath = DataSourceManager.class.getClassLoader().getResource(propertyFileName).getFile();
		File file = new File(propFilePath);
		Preconditions.checkState(file.exists() && !file.isDirectory());
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(propFilePath));
			String line = reader.readLine();
			while (line!=null) {
				String[] pairs = line.trim().split("\\s*=\\s*");
				if(pairs.length==2) {
					keyValuePairs.put(pairs[0], pairs[1]);
				}
				line = reader.readLine();
			}
		} catch (IOException e) {
			Throwables.propagate(e);
		}
		finally {
			if(reader!=null){
				try {
					reader.close();
				} catch (Throwable ignored) {
				}
			}
		}
	}

	public static String get(String key) {
		if(keyValuePairs.containsKey(key))
			return keyValuePairs.get(key);
		return null;
	}
}
