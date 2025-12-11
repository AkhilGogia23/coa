package com.example.coa.app.service;

import com.example.coa.app.dto.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class LotService {

    private final MasterControlService masterControlService;
    private final SnowflakeService snowflakeService;
    private final MasterControlMapper masterControlMapper;
    public LotService(MasterControlService masterControlService,
                      SnowflakeService snowflakeService,
                      MasterControlMapper masterControlMapper) {
        this.masterControlService = masterControlService;
        this.snowflakeService = snowflakeService;
        this.masterControlMapper = masterControlMapper;
    }

    public LotResponse getLotFromSnowflake(String lotNumber) {
        List<Map<String, Object>> rows = snowflakeService.fetchCoasByLot(lotNumber);

        if (rows == null || rows.isEmpty()) {
            return new LotResponse(
                    lotNumber,
                    "No records found",
                    null,
                    "NOT_FOUND",
                    null,
                    null,
                    new LotResults(List.of(), List.of(), List.of())
            );
        }


        List<CoaResultItem> ingredients = new ArrayList<>();
        List<CoaResultItem> blends = new ArrayList<>();
        List<CoaResultItem> finishedProducts = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            String name = row.get("PRODUCT_NAME") != null ? row.get("PRODUCT_NAME").toString() : null;
            String rowLot = row.get("LOT_NUMBER") != null ? row.get("LOT_NUMBER").toString() : null;
            String type = row.get("PRODUCT_TYPE") != null ? row.get("PRODUCT_TYPE").toString() : null;
            String pdfUrl = row.get("COA_PDF_URL") != null ? row.get("COA_PDF_URL").toString() : null;
            Boolean passed = null;
            Object passedObj = row.get("PASSED");
            if (passedObj instanceof Boolean) passed = (Boolean) passedObj;
            else if (passedObj != null) passed = Boolean.parseBoolean(passedObj.toString());
            if (passed == null) passed = true;

            CoaResultItem item = new CoaResultItem(
                    name,
                    rowLot,
                    pdfUrl,
                    passed
            );

            if (type == null) {
                finishedProducts.add(item);
            } else {
                switch (type.toUpperCase()) {
                    case "INGREDIENT" -> ingredients.add(item);
                    case "BLEND"      -> blends.add(item);
                    case "FINISHED"   -> finishedProducts.add(item);
                    default           -> finishedProducts.add(item);
                }
            }
        }

        LotResults results = new LotResults(ingredients, blends, finishedProducts);


        Map<String, Object> first = rows.get(0);

        String fgInventoryId = first.get("FG_INVENTORYID") != null ? first.get("FG_INVENTORYID").toString() : null;
        String fgDescription = first.get("FG_DESCRIPTION") != null ? first.get("FG_DESCRIPTION").toString() : null;
        String matlExpiry    = first.get("MATL_EXPIRYDATE") != null ? first.get("MATL_EXPIRYDATE").toString() : null;
        String sku           = fgInventoryId != null ? fgInventoryId : "UNKNOWN-SKU";


        Integer productionRecordId = null;
        if (first.get("PRODUCTION_RECORD_ID") != null) {
            try {
                productionRecordId = Integer.parseInt(first.get("PRODUCTION_RECORD_ID").toString());
            } catch (NumberFormatException ignored) { }
        }

        String productName = !finishedProducts.isEmpty()
                ? finishedProducts.get(0).getName()
                : (fgDescription != null ? fgDescription : (first.get("PRODUCT_NAME") != null ? first.get("PRODUCT_NAME").toString() : "Unknown"));


        String manufactureDate = null;
        String expiryDate = matlExpiry;

        return new LotResponse(
                lotNumber,
                productName,
                sku,
                "Released",
                manufactureDate,
                expiryDate,
                results
        );
    }

    public LotFullResponse getFullLot(String lotNumber, Integer productionRecordIdForNow) {


        LotResponse summary = getLotFromSnowflake(lotNumber);

        List<TestResult> tests = List.of();
        if (productionRecordIdForNow != null) {
            Object mcRaw = masterControlService.fetchDataCapturesForProductionRecord(productionRecordIdForNow);
            tests = masterControlMapper.mapDataCaptures(mcRaw);
        }

        return new LotFullResponse(summary, tests);
    }
}
