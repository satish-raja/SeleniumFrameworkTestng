package com.orangehrm.testng.utils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TestHelper {

    public static Object[][] loadTestData(String fileName, String story) throws IOException {
        String filePath = System.getProperty("user.dir") + "/src/test/resources/" + fileName;
        List<Map<String, String>> testData = LoginDataUtil.getFilteredLoginData(filePath, story);
        if (testData.isEmpty()) {
            throw new IllegalArgumentException(story + " test data is missing.");
        }
        return convertToDataProviderFormat(testData);
    }

    private static Object[][] convertToDataProviderFormat(List<Map<String, String>> data) {
        Object[][] dataArray = new Object[data.size()][];
        for (int i = 0; i < data.size(); i++) {
            Map<String, String> map = data.get(i);
            dataArray[i] = new Object[]{map.get("username"), map.get("password"), map.get("story")};
        }
        return dataArray;
    }

    public static void logInfo(String message) {
        LogUtil.logInfo(message);
    }
}
