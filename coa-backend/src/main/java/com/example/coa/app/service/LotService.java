
package com.example.coa.app.service;

import com.example.coa.app.dto.*;
import com.example.coa.app.mapper.MasterControlMapper;
import com.example.coa.app.util.StringNormalizer;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;

@Service
public class LotService {

    private final SnowflakeService snowflakeService;
    private final MasterControlService masterControlService;
    private final MasterControlMapper masterControlMapper;
    private final JdbcTemplate jdbcTemplate;
    public LotService(
            SnowflakeService snowflakeService,
            MasterControlService masterControlService,
            MasterControlMapper masterControlMapper,
            JdbcTemplate jdbcTemplate
    ) {
        this.snowflakeService = snowflakeService;
        this.masterControlService = masterControlService;
        this.masterControlMapper = masterControlMapper;
        this.jdbcTemplate=jdbcTemplate;
    }


    public LotResponse getLotFromSnowflake(String lotNumber) {

        CoaRecord coa = snowflakeService.getCoaByLot(lotNumber);
        String partNumber = StringNormalizer.extractPartNumber(
                coa.getFileName() + " " + coa.getFullText()
        );
        coa.setPartNumber(partNumber);


        // ✅ FETCH INVENTORY
        List<InventoryRecord> inventoryList =
                snowflakeService.getInventoryCandidates();

        // ✅ MATCH
        InventoryRecord matched =
                matchInventory(coa, inventoryList);

        // ✅ SAVE MAPPING
//        if (matched != null) {
//            saveMapping(
//                    coa.getLotNumber(),
//                    coa.getFileName(),
//                    matched.getFgDescription(),
//                    matched.getFgInventoryId(),
//                    "MEDIUM"
//            );
//        }
        if (matched != null && !mappingExists(coa.getLotNumber())) {
            saveMapping(
                    coa.getLotNumber(),
                    coa.getFileName(),
                    matched.getFgDescription(),
                    matched.getFgInventoryId(),
                    "MEDIUM"
            );
        }

        CoaResultItem finished = new CoaResultItem(
                coa.getFileName(),
                coa.getLotNumber(),
                null,
                true
        );
        List<CoaResultItem> ingredientResults = List.of();
        List<CoaResultItem> blendResults = List.of();

        if (matched != null) {

            ingredientResults = List.of(
                    new CoaResultItem(
                            matched.getMatlDescription(),   // ingredient name
                            coa.getLotNumber(),
                            null,
                            true
                    )
            );

            blendResults = List.of(
                    new CoaResultItem(
                            matched.getFgDescription(),         // quantity
                            coa.getLotNumber(),
                            null,
                            true
                    )
            );
        }

        LotResults results = new LotResults(
                ingredientResults,
                blendResults,
                List.of(finished)
        );

        return new LotResponse(
                lotNumber,
                coa.getFileName(),
                matched != null ? matched.getFgInventoryId() : "UNKNOWN-SKU",
                "Released",

                null, // manufacture date (not available yet)

                matched != null && matched.getMatlExpiryDate() != null
                        ? matched.getMatlExpiryDate().toString()
                        : null,
                results
        );
    }

    public LotFullResponse getFullLot(String lotNumber, Integer productionRecordId) {

        LotResponse summary = getLotFromSnowflake(lotNumber);
        List<TestResult> tests = List.of();

        if (productionRecordId != null) {
            Object raw =
                    masterControlService.fetchDataCapturesForProductionRecord(
                            productionRecordId
                    );
            tests = masterControlMapper.mapDataCaptures(raw);
        }

        return new LotFullResponse(summary, tests);
    }
    private void saveMapping(
            String lotNumber,
            String coaFileName,
            String fgDescription,
            String inventoryId,
            String matchConfidence
    ) {
        String sql = """
        INSERT INTO GOLD_DB.GOLD_ACUMATICA.COA_INVENTORY_MAPPING
        (LOT_NUMBER, COA_FILE_NAME, FG_DESCRIPTION, INVENTORY_ID, MATCH_CONFIDENCE)
        VALUES (?, ?, ?, ?, ?)
    """;

        jdbcTemplate.update(
                sql,
                lotNumber,
                coaFileName,
                fgDescription,
                inventoryId,
                matchConfidence
        );
    }
    private boolean mappingExists(String lotNumber) {
        String sql = """
        SELECT COUNT(*)
        FROM GOLD_DB.GOLD_ACUMATICA.COA_INVENTORY_MAPPING
        WHERE LOT_NUMBER = ?
    """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, lotNumber);
        return count != null && count > 0;
    }


private InventoryRecord matchInventory(
        CoaRecord coa,
        List<InventoryRecord> inventoryList
) {
    // 🔹 1. STRONG MATCH: Part Number
    if (coa.getPartNumber() != null) {
        for (InventoryRecord inv : inventoryList) {
            if (inv.getFgDescription() != null &&
                    inv.getFgDescription().contains(coa.getPartNumber())) {
                return inv;
            }
        }
    }

    // 🔹 2. FALLBACK: Scored fuzzy match
    String coaText = StringNormalizer.normalize(
            coa.getFileName() + " " + coa.getFullText()
    );

    InventoryRecord best = null;
    int bestScore = 0;

    for (InventoryRecord inv : inventoryList) {
        String desc = StringNormalizer.normalize(inv.getFgDescription());
        int score = 0;

        if (desc.contains("ascend") && coaText.contains("ascend")) score++;
        if (desc.contains("descend") && coaText.contains("descend")) score++;
        if (desc.contains("gummy") && coaText.contains("gummy")) score++;
        if (desc.contains("softgel") && coaText.contains("softgel")) score++;
        if (desc.contains("25") && coaText.contains("25")) score++;
        if (desc.contains("50") && coaText.contains("50")) score++;

        if (score > bestScore) {
            bestScore = score;
            best = inv;
        }
    }

    return bestScore >= 2 ? best : null;
}
}