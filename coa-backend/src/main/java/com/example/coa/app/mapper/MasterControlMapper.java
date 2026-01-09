
package com.example.coa.app.service;

import com.example.coa.app.dto.TestResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class MasterControlMapper {


    public List<TestResult> mapDataCaptures(Object raw) {
        if (!(raw instanceof Map<?,?> map)) {
            return List.of();
        }

        Object contentObj = map.get("content");
        if (!(contentObj instanceof List<?> contentList)) {
            return List.of();
        }

        List<TestResult> results = new ArrayList<>();

        for (Object o : contentList) {
            if (!(o instanceof Map<?,?> row)) continue;

            String title        = asString(row.get("title"));
            String value        = asString(row.get("value"));
            String unit         = asString(row.get("unitOfMeasure"));
            String dataCapture  = asString(row.get("dataCaptureName"));
            String actionTaken  = asString(row.get("actionTaken"));
            String userName     = asString(row.get("userName"));
            String dateTime     = asString(row.get("dateTime"));

            results.add(new TestResult(
                    title,
                    value,
                    unit,
                    dataCapture,
                    actionTaken,
                    userName,
                    dateTime
            ));
        }

        return results;
    }

    private String asString(Object o) {
        return o == null ? null : o.toString();
    }
}
