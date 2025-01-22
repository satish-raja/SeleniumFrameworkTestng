package com.orangehrm.testng.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Utility class to handle reading login data from Excel and JSON files.
 */
public class LoginDataUtil {

    private static final Logger logger = LogManager.getLogger(LoginDataUtil.class);

    // Define constants for common keys
    public static final String STORY_KEY = "story";
    public static final String USERNAME_KEY = "username";
    public static final String PASSWORD_KEY = "password";

    /**
     * Reads login credentials from an Excel file.
     *
     * @param filePath Path to the Excel file.
     * @return List of Maps representing test data.
     * @throws IOException If the file cannot be read.
     */
    public static List<Map<String, String>> getDataFromExcel(String filePath) throws IOException {
        List<Map<String, String>> data = new ArrayList<>();
        try (FileInputStream file = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            int colCount = headerRow.getPhysicalNumberOfCells();

            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row currentRow = sheet.getRow(i);
                Map<String, String> rowData = new HashMap<>();
                for (int j = 0; j < colCount; j++) {
                    String key = headerRow.getCell(j).getStringCellValue();
                    String value = getCellValue(currentRow.getCell(j));
                    rowData.put(key, value);
                }
                data.add(rowData);
            }
        }
        return data;
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

    /**
     * Reads login credentials from a JSON file.
     *
     * @param filePath Path to the JSON file.
     * @return List of Maps representing test data.
     * @throws IOException If the file cannot be read.
     */
    public static List<Map<String, String>> getDataFromJSON(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(filePath), new TypeReference<List<Map<String, String>>>() {});
    }

    /**
     * Filters data based on the specified story.
     *
     * @param filePath Path to the file (Excel or JSON).
     * @param story    The story to filter by (e.g., "Valid Credentials").
     * @return Filtered list of test data.
     * @throws IOException If the file cannot be read.
     */
    public static List<Map<String, String>> getFilteredLoginData(String filePath, String story) throws IOException {
        List<Map<String, String>> allData = getTestData(filePath);
        List<Map<String, String>> filteredData = new ArrayList<>();
        for (Map<String, String> data : allData) {
            if (story.equals(data.get(STORY_KEY))) {
                filteredData.add(data);
            }
        }
        if (filteredData.isEmpty()) {
            logger.warn("No data found for story: {}", story);
        }
        return filteredData;
    }

    /**
     * Reads test data from a file (Excel or JSON).
     *
     * @param filePath Path to the file.
     * @return List of test data.
     * @throws IOException If the file cannot be read.
     */
    public static List<Map<String, String>> getTestData(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            logger.error("File not found: {}", filePath);
            throw new FileNotFoundException("File not found: " + filePath);
        }

        if (filePath.endsWith(".json")) {
            logger.info("Loading test data from JSON file: {}", filePath);
            return getDataFromJSON(filePath);
        } else if (filePath.endsWith(".xlsx")) {
            logger.info("Loading test data from Excel file: {}", filePath);
            return getDataFromExcel(filePath);
        } else {
            logger.error("Unsupported file format: {}", filePath);
            throw new IllegalArgumentException("Unsupported file format: " + filePath);
        }
    }
}
