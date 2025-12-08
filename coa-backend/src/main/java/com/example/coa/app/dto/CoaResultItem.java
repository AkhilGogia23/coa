package com.example.coa.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoaResultItem {
    private String name;
    private String lotNumber;
    private String coaPdfUrl;
    private boolean passed;
}