package com.example.coa.app.service;

import com.example.coa.app.dto.CoaRecord;
import com.example.coa.app.dto.InventoryRecord;
import com.example.coa.app.mapper.CoaRowMapper;
import com.example.coa.app.mapper.InventoryRowMapper;
import com.example.coa.app.util.StringNormalizer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SnowflakeService {

    private final JdbcTemplate jdbcTemplate;

    public SnowflakeService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public CoaRecord getCoaByLot(String lotNumber) {
        String sql = """
            SELECT FILE_NAME, FULL_TEXT, BATCH_LOT_CODE, LAST_MODIFIED
            FROM GOLD_DB.GOLD_MASTER_CONTROL.COA_LIBRARY
            WHERE BATCH_LOT_CODE = ?
            ORDER BY LAST_MODIFIED DESC
            LIMIT 1
        """;

        return jdbcTemplate.queryForObject(
                sql,
                new CoaRowMapper(),
                lotNumber
        );
    }

    public List<InventoryRecord> getInventoryCandidates() {
        String sql = """
    SELECT
    FG_INVENTORYID,
    FG_DESCRIPTION,
    MATL_DESCRIPTION,        -- manufacturer
    MATL_INVENTORYID,        -- ingredient
    QTYREQUIRED,             -- blend qty
    MATL_EXPIRYDATE
FROM GOLD_DB.GOLD_ACUMATICA.PRODUCT_MATL_INVENTORY
        """;

        return jdbcTemplate.query(sql, new InventoryRowMapper());
    }
    public List<InventoryRecord> getBomBySku(String fgInventoryId) {

        String sql = """
        SELECT
            FG_INVENTORYID,
            FG_DESCRIPTION,
            MATL_INVENTORYID,
            MATL_DESCRIPTION,
            QTYREQUIRED,
            MATL_EXPIRYDATE
        FROM GOLD_DB.GOLD_ACUMATICA.PRODUCT_MATL_INVENTORY
        WHERE FG_INVENTORYID = ?
    """;

        return jdbcTemplate.query(
                sql,
                new InventoryRowMapper(),
                fgInventoryId
        );
    }
    public String getPresignedPdfUrl(String lotNumber) {

        String sql = """
        SELECT GET_PRESIGNED_URL(
            '@GOLD_DB.GOLD_MASTER_CONTROL.COA_STAGE_SSE',
            COA_STAGE_PATH,
            86400
        )
        FROM GOLD_DB.GOLD_ACUMATICA.COA_INVENTORY_MAPPING
        WHERE LOT_NUMBER = ?
    """;

        return jdbcTemplate.queryForObject(sql, String.class, lotNumber);
    }


    public String testConnection() {
        return jdbcTemplate.queryForObject(
                "SELECT CURRENT_WAREHOUSE()", String.class
        );
    }
}