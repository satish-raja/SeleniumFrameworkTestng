package com.orangehrm.testng.utils;

import java.io.File;
import java.io.IOException;

import org.testng.annotations.DataProvider;

public class TestDataProvider {

    private static final String TEST_DATA_DIR = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources";
    private static final TestDataLoader testDataLoader = new TestDataLoader();

    @DataProvider(name = "loginDataValid")
    public static Object[][] loginDataValidProvider() throws IOException {
        return testDataLoader.loadTestData(TEST_DATA_DIR + File.separator + "loginData.xlsx", "Valid Credentials");
    }

    @DataProvider(name = "loginDataInvalid")
    public static Object[][] loginDataInvalidProvider() throws IOException {
        return testDataLoader.loadTestData(TEST_DATA_DIR + File.separator + "loginData.json", "Invalid Credentials");
    }
}
