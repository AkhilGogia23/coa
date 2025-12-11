package com.example.coa.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestResult {
    private String title;       // e.g. "Assay", "Potency"
    private String value;       // e.g. "99.2"
    private String unit;        // e.g. "%"
    private String dataCapture; // e.g. dataCaptureName
    private String actionTaken; // e.g. "DATA_CAPTURE"
    private String userName;
    private String dateTime;    // ISO string from MC
}
