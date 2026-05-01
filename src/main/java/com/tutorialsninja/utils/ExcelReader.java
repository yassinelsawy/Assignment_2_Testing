package com.tutorialsninja.utils;

import com.tutorialsninja.config.ConfigReader;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader {

    private static Workbook getWorkbook() throws IOException {
        String path = ConfigReader.getTestDataPath();
        FileInputStream fis = new FileInputStream(path);
        return new XSSFWorkbook(fis);
    }

    public static Object[][] getTestData(String sheetName) {
        try (Workbook workbook = getWorkbook()) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet not found: " + sheetName);
            }

            int rowCount = sheet.getLastRowNum();
            int colCount = sheet.getRow(0).getLastCellNum();

            List<Object[]> data = new ArrayList<>();
            for (int i = 1; i <= rowCount; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Object[] rowData = new Object[colCount];
                for (int j = 0; j < colCount; j++) {
                    Cell cell = row.getCell(j);
                    rowData[j] = getCellValue(cell);
                }
                data.add(rowData);
            }

            return data.toArray(new Object[0][]);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel sheet: " + sheetName, e);
        }
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) return "";
        String raw;
        switch (cell.getCellType()) {
            case STRING:
                raw = cell.getStringCellValue().trim();
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    raw = cell.getLocalDateTimeCellValue().toLocalDate().toString();
                } else {
                    double numVal = cell.getNumericCellValue();
                    if (numVal == Math.floor(numVal)) {
                        raw = String.valueOf((long) numVal);
                    } else {
                        raw = String.valueOf(numVal);
                    }
                }
                break;
            case BOOLEAN:
                raw = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA:
                raw = cell.getCachedFormulaResultType() == CellType.STRING
                        ? cell.getStringCellValue().trim()
                        : String.valueOf((long) cell.getNumericCellValue());
                break;
            default:
                raw = "";
        }
        return raw.replace("{timestamp}", String.valueOf(System.currentTimeMillis()));
    }
}
