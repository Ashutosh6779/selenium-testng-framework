package com.automationframework.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ExcelUtils - Data-Driven Testing (DDT) support via Apache POI.
 *
 * Reads test data from .xlsx files into a List<Map<String, String>> structure
 * where each Map represents one row, keyed by column headers.
 *
 * Usage:
 *   List<Map<String, String>> data = ExcelUtils.readSheet("src/test/resources/testdata/login.xlsx", "LoginData");
 *   String username = data.get(0).get("username");
 */
public class ExcelUtils {

    private ExcelUtils() {}

    /**
     * Reads all rows from a named sheet in an Excel file.
     * First row is treated as headers (column names).
     *
     * @param filePath  path to the .xlsx file
     * @param sheetName name of the sheet to read
     * @return list of row data as column-name → cell-value maps
     */
    public static List<Map<String, String>> readSheet(String filePath, String sheetName) {
        List<Map<String, String>> data = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet '" + sheetName + "' not found in: " + filePath);
            }

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new RuntimeException("Sheet '" + sheetName + "' has no header row.");
            }

            int colCount = headerRow.getLastCellNum();

            for (int rowIdx = 1; rowIdx <= sheet.getLastRowNum(); rowIdx++) {
                Row row = sheet.getRow(rowIdx);
                if (row == null) continue;

                Map<String, String> rowData = new HashMap<>();
                for (int colIdx = 0; colIdx < colCount; colIdx++) {
                    String header = getCellValue(headerRow.getCell(colIdx));
                    String value  = getCellValue(row.getCell(colIdx));
                    rowData.put(header, value);
                }
                data.add(rowData);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel file: " + filePath, e);
        }

        return data;
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING  -> cell.getStringCellValue().trim();
            case NUMERIC -> DateUtil.isCellDateFormatted(cell)
                    ? cell.getLocalDateTimeCellValue().toString()
                    : String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default      -> "";
        };
    }
}
