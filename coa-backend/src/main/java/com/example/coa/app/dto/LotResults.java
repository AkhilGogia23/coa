package com.example.coa.app.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LotResults {
    private List<CoaResultItem> ingredients;
    private List<CoaResultItem> blend;
    private List<CoaResultItem> finishedProduct;
}