package com.example.coa.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LotResponse {
    private String lotNumber;
    private String productName;
    private String sku;
    private String status;
    private String manufactureDate;
    private String expiryDate;
    private LotResults results;

}