/**
 * Class Name: DataUtil
 *
 * Description:
 * This utility class provides methods for working with test data, including reading data from Excel or JSON files,
 * filtering data, and validating JSON schemas. It supports extensibility through the `DataReader` interface and
 * includes a factory for dynamic reader creation based on file type.
 *
 * Key Features:
 * - Dynamically reads test data from supported file formats (Excel, JSON).
 * - Filters test data based on specified key-value pairs.
 * - Validates JSON data against basic schema requirements.
 * - Provides utility methods like checking if a cell is date-formatted in Excel files.
 *
 * Dependencies:
 * - Apache POI for reading Excel files.
 * - Jackson ObjectMapper for JSON processing.
 * - Log4j for logging.
 *
 * Usage:
 * - Use `DataUtil.getTestData(filePath)` to retrieve test data.
 * - Extend `DataReader` for additional file format support.
 */

package com.orangehrm.testng.utils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DataUtil {
    private static final Logger logger = LogManager.getLogger(DataUtil.class);

    /**
     * Custom exception class for handling errors during data processing.
     */
    public static class DataProcessingException extends Exception {
        private static final long serialVersionUID = 1L;

        public DataProcessingException(String message) {
            super(message);
        }

        public DataProcessingException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Retrieves test data from a specified file (Excel or JSON).
     *
     * @param filePath The path to the test data file.
     * @return A list of maps representing the test data.
     * @throws DataProcessingException If there's an error reading the file.
     */
    public static List<Map<String, String>> getTestData(String filePath) throws DataProcessingException {
        DataReader reader = DataReaderFactory.getReader(filePath);
        return reader.readData(filePath);
    }

    /**
     * Filters test data based on a key-value pair.
     *
     * @param data       The list of test data to filter.
     * @param filterKey  The key to filter by.
     * @param filterValue The value to match for the filterKey.
     * @return A filtered list of test data.
     */
    public static List<Map<String, String>> filterTestData(List<Map<String, String>> data, String filterKey, String filterValue) {
        return data.stream()
                .filter(map -> filterValue.equals(map.get(filterKey)))
                .collect(Collectors.toList());
    }

    /**
     * Validates a JSON string against a predefined schema.
     *
     * @param jsonData The JSON data as a string.
     * @throws DataProcessingException If the JSON data is invalid or missing required fields.
     */
    public static void validateJsonSchema(String jsonData) throws DataProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(jsonData);
            if (!jsonNode.has("requiredField")) {
                throw new DataProcessingException("Missing required field in JSON data.");
            }
        } catch (IOException e) {
            logger.error("Invalid JSON format. Exception: {}", e.getMessage(), e);
            throw new DataProcessingException("Invalid JSON format.", e);
        }
    }

    /**
     * Utility method to check if a cell is formatted as a date.
     *
     * @param cell The cell to check.
     * @return True if the cell contains a date.
     */
    public static boolean isCellDateFormatted(Cell cell) {
        return DateUtil.isCellDateFormatted(cell);
    }
}

/**
 * Interface: DataReader
 *
 * Description:
 * This interface defines the contract for reading data from files. It supports extensibility for different file formats.
 */
interface DataReader {
    List<Map<String, String>> readData(String filePath) throws DataUtil.DataProcessingException;
}

/**
 * Class Name: ExcelDataReader
 *
 * Description:
 * Implementation of `DataReader` for reading data from Excel files. Uses Apache POI to parse Excel sheets and
 * converts the data into a list of maps.
 */
class ExcelDataReader implements DataReader {
//    private static final Logger logger = LogManager.getLogger(ExcelDataReader.class);
//    private static final String DEFAULT_EMPTY_CELL_VALUE = System.getProperty("default.empty.cell.value", "N/A");

    @Override
    public List<Map<String, String>> readData(String filePath) throws DataUtil.DataProcessingException {
        // Implement reading Excel data
        return null;  // Replace with actual implementation
    }
}

/**
 * Class Name: JsonDataReader
 *
 * Description:
 * Implementation of `DataReader` for reading data from JSON files. Uses Jackson ObjectMapper to parse JSON data
 * into a list of maps.
 */
class JsonDataReader implements DataReader {
//    private static final Logger logger = LogManager.getLogger(JsonDataReader.class);

    @Override
    public List<Map<String, String>> readData(String filePath) throws DataUtil.DataProcessingException {
        // Implement reading JSON data
        return null;  // Replace with actual implementation
    }
}

/**
 * Class Name: DataReaderFactory
 *
 * Description:
 * Factory class for creating instances of `DataReader` based on the file type. Supports extensibility for adding
 * new file format readers.
 */
class DataReaderFactory {
    public static DataReader getReader(String filePath) throws DataUtil.DataProcessingException {
        if (filePath.endsWith(".json")) {
            return new JsonDataReader();
        } else if (filePath.endsWith(".xlsx")) {
            return new ExcelDataReader();
        } else {
            throw new DataUtil.DataProcessingException("Unsupported file format: " + filePath);
        }
    }
}
