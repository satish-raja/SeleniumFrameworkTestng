/**
 * Class Name: TestDataManager
 *
 * Description:
 * This utility class is responsible for managing and processing test data from various file formats.
 * It provides mechanisms to handle Excel and JSON files, reading the data into a list of maps for test execution.
 *
 * Key Features:
 * - Dynamically processes test data files based on file extensions (Excel, JSON).
 * - Uses a factory-based approach to delegate data processing to format-specific processors.
 * - Ensures proper error handling and logging for file reading operations.
 *
 * Dependencies:
 * - DataUtil for reading data from Excel and JSON files.
 * - Log4j for logging error and info messages.
 * - Apache POI for handling Excel file processing.
 * - Jackson ObjectMapper for JSON file processing.
 *
 * Usage:
 * - Use `TestDataManager.getTestData(filePath)` to retrieve test data from a file. The file format (Excel, JSON) is automatically detected.
 * - Extend `DataProcessor` for supporting additional file formats.
 *
 * Extensibility:
 * - New file formats can be supported by creating a new `DataProcessor` implementation and adding it to the `FORMAT_PROCESSORS` map.
 */


package com.orangehrm.testng.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * TestDataManager class for managing and processing test data files.
 */
public class TestDataManager {

    private static final Logger logger = LogManager.getLogger(TestDataManager.class);

    private static final Map<String, DataProcessor> FORMAT_PROCESSORS = new HashMap<>();

    static {
        FORMAT_PROCESSORS.put(".xlsx", new ExcelDataProcessor());
        FORMAT_PROCESSORS.put(".json", new JsonDataProcessor());
    }

    /**
     * Retrieves test data from a specified file based on its format.
     * 
     * @param filePath The path to the test data file.
     * @return A list of maps representing the test data.
     * @throws IOException If there's an error reading the file.
     */
    public static List<Map<String, String>> getTestData(String filePath) throws IOException {
        Path path = Paths.get(filePath).normalize();
        File file = path.toFile();
        String fileName = file.getName();

        logger.info("Processing file: {}", fileName);

        if (!file.exists()) {
            logger.error("The file does not exist: {}", filePath);
            throw new IllegalArgumentException("File does not exist: " + filePath);
        }

        String fileExtension = getFileExtension(fileName);
        DataProcessor processor = FORMAT_PROCESSORS.get(fileExtension);

        if (processor != null) {
            return processor.getData(filePath);
        } else {
            logger.error("Unsupported file format: {}. Supported formats are: {}", fileExtension, FORMAT_PROCESSORS.keySet());
            throw new IllegalArgumentException("Unsupported file format: " + fileExtension);
        }
    }

    /**
     * Helper method to get the file extension from the file name.
     * 
     * @param fileName The name of the file.
     * @return The file extension (e.g., .xlsx, .json).
     */
    private static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0) {
            return fileName.substring(dotIndex).toLowerCase();
        }
        return "";  // Return empty string if no extension is found
    }
}

interface DataProcessor {
    List<Map<String, String>> getData(String filePath) throws IOException;
}

class ExcelDataProcessor implements DataProcessor {
    private static final Logger logger = LogManager.getLogger(ExcelDataProcessor.class);

    @Override
    public List<Map<String, String>> getData(String filePath) throws IOException {
        try {
            return DataUtil.getTestData(filePath); // Now using the DataUtil utility method
        } catch (DataUtil.DataProcessingException e) {
            logger.error("Error reading Excel file: {}. File path: {}", e.getMessage(), filePath, e);
            throw new IOException("Error reading Excel file: " + filePath, e);  // Wrapping DataProcessingException as IOException
        }
    }
}

class JsonDataProcessor implements DataProcessor {
    private static final Logger logger = LogManager.getLogger(JsonDataProcessor.class);

    @Override
    public List<Map<String, String>> getData(String filePath) throws IOException {
        try {
            return DataUtil.getTestData(filePath); // Now using the DataUtil utility method
        } catch (DataUtil.DataProcessingException e) {
            logger.error("Error reading JSON file: {}. File path: {}", e.getMessage(), filePath, e);
            throw new IOException("Error reading JSON file: " + filePath, e);  // Wrapping DataProcessingException as IOException
        }
    }
}
