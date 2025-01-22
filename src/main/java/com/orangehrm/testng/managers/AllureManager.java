package com.orangehrm.testng.managers;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class AllureManager {

    private static final Logger logger = LogManager.getLogger(AllureManager.class);
    
    // Static path for Extent Results directory
    private static final String ALLURE_RESULTS_DIR = System.getProperty("user.dir") + File.separator + "allure-results";

    /**
     * Clears the Allure results directory.
     *
     * @param resultsDir Path to the Allure results directory.
     */
    public void clearResults() {
        File directory = new File(ALLURE_RESULTS_DIR);
        if (directory.exists() && directory.isDirectory()) {
            try {
                FileUtils.cleanDirectory(directory);
                logger.info("Allure results cleared successfully.");
            } catch (IOException e) {
                logger.error("Error clearing Allure results: ", e);
            }
        } else {
            directory.mkdirs();
            logger.info("Allure results directory created at: " + directory.getAbsolutePath());
        }
    }
    public String getAllureResultsDir() {
        return ALLURE_RESULTS_DIR;
    }
}
