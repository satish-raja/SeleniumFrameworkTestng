package com.orangehrm.testng.utils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TestDataLoader {
    public Object[][] loadTestData(String filePath, String story) throws IOException {
        List<Map<String, String>> testData = LoginDataUtil.getFilteredLoginData(filePath, story);
        validateTestData(testData, story + " data is missing or improperly formatted.");
        return convertToDataProviderFormat(testData);
    }

    private void validateTestData(List<Map<String, String>> data, String errorMessage) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private Object[][] convertToDataProviderFormat(List<Map<String, String>> data) {
        Object[][] dataArray = new Object[data.size()][];
        for (int i = 0; i < data.size(); i++) {
            Map<String, String> map = data.get(i);
            dataArray[i] = new Object[]{map.get("username"), map.get("password"), map.get("story")};
        }
        return dataArray;
    }
}