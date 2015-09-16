package org.finra.test.datagen.schema;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.finra.test.datagen.DataType;
import org.finra.test.datagen.util.TableColumn;

import java.io.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created on 9/16/2015.
 */
public class SeedFileParser {
	public static List<TableColumn> parseSeedFile(String seedFilePath)
		throws IOException {
		Preconditions.checkNotNull(seedFilePath);
		File file = new File(seedFilePath);
		Preconditions.checkState(file.exists() && !file.isDirectory());
		BufferedReader reader = new BufferedReader(new FileReader(seedFilePath));
		boolean startReadColumn=false;
		AtomicInteger ordinal = new AtomicInteger(0);
		List<TableColumn> columns = Lists.newLinkedList();
		String line = reader.readLine();
		while (line!=null) {
			if(line.trim().endsWith("(") || line.trim().startsWith("(")) {
				line = line.substring(line.indexOf("(")+1).trim();
				startReadColumn = true;
			}

			if(startReadColumn){
				if(line.trim().contains(",")){
					String[] lines = line.split("\\s*,\\s*");
					for(String l : lines) {
						readColumnDefinition(l, columns, ordinal);
					}
				}
				else {
					readColumnDefinition(line, columns, ordinal);
				}
			}
			line = reader.readLine();
		}
		reader.close();

		return columns;
	}

	private static void readColumnDefinition(String line, List<TableColumn> columns, AtomicInteger ordinal){
		if(line.trim().length()>0){
			String[] columnExpr = line.trim().split("\\s+");
			if(columnExpr.length>=2) {
				DataType dataType = DataType.parseDataType(columnExpr[1]);
				TableColumn column = new TableColumn();
				column.ordinal= ordinal.addAndGet(1);
				column.name=columnExpr[0];
				column.dbType = dataType.dbType;
				column.size = dataType.size;
				column.precision = dataType.precision;
				column.scale=dataType.scale;
				column.required=false;
				if(columnExpr.length>2 && line.toLowerCase().contains("not null")) {
					column.required=true;
				}
				columns.add(column);
			}
		}
	}
}
