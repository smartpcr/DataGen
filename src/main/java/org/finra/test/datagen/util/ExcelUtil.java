package org.finra.test.datagen.util;

import com.google.common.base.Strings;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

/**
 * Created on 9/1/2015.
 */
public class ExcelUtil {
	public static List<Map<String,Object>> readSheetAsTable(String filePath, String sheetName)
		throws IOException, InvalidFormatException {
		File file = new File(filePath);
        if(!file.exists())
            return null;
		InputStream inputStream = new FileInputStream(file);
		Workbook workbook = WorkbookFactory.create(inputStream);
		Sheet sheet = workbook.getSheet(sheetName);
        if(sheet==null)
            return null;
		Map<String, Integer> columnHeaderIndexes = new LinkedHashMap<>();
		Row headerRow = sheet.getRow(0);
		int cellIndex = 0;
		Cell cell = headerRow.getCell(cellIndex);
		while (cell!=null && !Strings.isNullOrEmpty(cell.getStringCellValue())) {
			String columnHeader = cell.getStringCellValue();
			columnHeaderIndexes.put(columnHeader, cellIndex);
			cellIndex++;
			cell=headerRow.getCell(cellIndex);
		}
		List<Map<String, Object>> table = new LinkedList<>();
		int rowCount = sheet.getPhysicalNumberOfRows();
		for(int rowNum = 1; rowNum < rowCount; rowNum++) {
			Row dataRow = sheet.getRow(rowNum);
			Map<String, Object> rowData = new LinkedHashMap<>();
			for(String fieldName : columnHeaderIndexes.keySet()) {
				Cell dataCell = dataRow.getCell(columnHeaderIndexes.get(fieldName), Row.CREATE_NULL_AS_BLANK);
				Object cellValue;
				switch (dataCell.getCellType()) {
					case Cell.CELL_TYPE_STRING:
						cellValue = dataCell.getRichStringCellValue().getString();
						break;
					case Cell.CELL_TYPE_NUMERIC:
						if (DateUtil.isCellDateFormatted(cell)) {
							cellValue=cell.getDateCellValue();
						} else {
							cellValue=dataCell.getNumericCellValue();
						}
						break;
					case Cell.CELL_TYPE_BOOLEAN:
						cellValue=dataCell.getBooleanCellValue();
						break;
					case Cell.CELL_TYPE_FORMULA:
						cellValue = dataCell.getCellFormula();
						break;
					default:
						cellValue = dataCell.getStringCellValue();
						break;
				}
				rowData.put(fieldName, cellValue);
			}
			table.add(rowData);
		}
		return table;
	}

	public static void writeSheet(String filePath, String sheetName, List<Map<String, Object>> table)
		throws IOException, InvalidFormatException {
		Workbook workbook = ensureExcelFile(filePath);
		Sheet sheet = ensureSheet(workbook, sheetName, true);
//        if(sheet.getLastRowNum()>0){
//            for(Row row: sheet){
//                sheet.removeRow(row);
//            }
//        }

		Map<String, Integer> columnIndexes = new HashMap<String, Integer>();
		if(table.size()>0) {
			Map<String, Object> firstRow = table.get(0);
			Row headerRow = sheet.createRow(0);
			int colIndex = 0;
			for(String colHeader : firstRow.keySet()) {
				columnIndexes.put(colHeader, colIndex);
				Cell headerCell = headerRow.createCell(colIndex);
				headerCell.setCellValue(colHeader);
				colIndex++;
			}
			int rowIndex = 1;
			for(Map<String, Object> row : table) {
				Row dataRow = sheet.createRow(rowIndex);
				for(String fieldName : columnIndexes.keySet()) {
					int cellIndex = columnIndexes.get(fieldName);
					Cell dataCell = dataRow.createCell(cellIndex);
					if(dataCell!=null && row.containsKey(fieldName)){
						Object cellValue = row.get(fieldName);
						if(cellValue != null) {
							dataCell.setCellValue(cellValue.toString());
						}
					}
				}
				rowIndex++;
			}
		}
		OutputStream fileOut = new FileOutputStream(filePath);
		workbook.write(fileOut);
		fileOut.close();
	}

    public static void writeSheet2(String filePath, String sheetName, List<Map<String, String>> table)
        throws IOException, InvalidFormatException {
        Workbook workbook = ensureExcelFile(filePath);
        Sheet sheet = ensureSheet(workbook, sheetName, true);
//        if(sheet.getLastRowNum()>0){
//            for(Row row: sheet){
//                sheet.removeRow(row);
//            }
//        }

        Map<String, Integer> columnIndexes = new HashMap<String, Integer>();
        if(table.size()>0) {
            Map<String, String> firstRow = table.get(0);
            Row headerRow = sheet.createRow(0);
            int colIndex = 0;
            for(String colHeader : firstRow.keySet()) {
                columnIndexes.put(colHeader, colIndex);
                Cell headerCell = headerRow.createCell(colIndex);
                headerCell.setCellValue(colHeader);
                colIndex++;
            }
            int rowIndex = 1;
            for(Map<String, String> row : table) {
                Row dataRow = sheet.createRow(rowIndex);
                for(String fieldName : columnIndexes.keySet()) {
                    int cellIndex = columnIndexes.get(fieldName);
                    Cell dataCell = dataRow.createCell(cellIndex);
                    if(dataCell!=null && row.containsKey(fieldName)){
                        String cellValue = row.get(fieldName);
                        if(cellValue != null) {
                            dataCell.setCellValue(cellValue);
                        }
                    }
                }
                rowIndex++;
            }
        }
        OutputStream fileOut = new FileOutputStream(filePath);
        workbook.write(fileOut);
        fileOut.close();
    }

	private static Workbook ensureExcelFile(String filePath)
		throws IOException, InvalidFormatException {
		Workbook workbook;
		File file = new File(filePath);
		if(file.exists() && !file.isDirectory()){
			InputStream inputStream = new FileInputStream(file);
			workbook = WorkbookFactory.create(inputStream);
		}
		else {
			workbook = new XSSFWorkbook();
			OutputStream fileOut = new FileOutputStream(filePath);
			workbook.write(fileOut);
			fileOut.close();
		}
		return workbook;
	}

	private static Sheet ensureSheet(Workbook workbook, String sheetName, boolean createNew) {
		int sheetCount = workbook.getNumberOfSheets();
		for(int i = 0; i < sheetCount; i++) {
			Sheet sheet = workbook.getSheetAt(i);
			if(sheet.getSheetName().equalsIgnoreCase(sheetName)){
				if(createNew){
					workbook.removeSheetAt(i);
					sheet = workbook.createSheet(sheetName);
					return sheet;
				}
				else {
					return sheet;
				}
			}
		}

		Sheet newSheet = workbook.createSheet(sheetName);
		return newSheet;
	}
}
