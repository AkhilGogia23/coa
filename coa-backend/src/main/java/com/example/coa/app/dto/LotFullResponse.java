package com.example.coa.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LotFullResponse {
    private LotResponse summary;
    private List<TestResult> testResults;
}
