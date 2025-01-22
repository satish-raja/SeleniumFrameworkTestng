package com.orangehrm.testng.managers;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class ExtentManager {

    private static final Logger logger = LogManager.getLogger(ExtentManager.class);

    // Static path for Extent Results directory
    private static final String EXTENT_RESULTS_DIR = System.getProperty("user.dir") + File.separator + "extent-results";

    /**
     * Clears the Extent results directory.
     *
     * @param resultsDir Path to the Extent results directory.
     */
    public void clearResults() {
        File directory = new File(EXTENT_RESULTS_DIR);
        if (directory.exists() && directory.isDirectory()) {
            try {
                FileUtils.cleanDirectory(directory);
                logger.info("Extent results cleared successfully.");
            } catch (IOException e) {
                logger.error("Error clearing Extent results: ", e);
            }
        } else {
            directory.mkdirs();
            logger.info("Extent results directory created at: " + directory.getAbsolutePath());
        }
    }

    public String getExtentResultsDir() {
        return EXTENT_RESULTS_DIR;
    }
}
