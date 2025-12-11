package com.example.coa.app.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SnowflakeService {

    private final JdbcTemplate jdbcTemplate;

    public SnowflakeService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String testConnection() {
        return jdbcTemplate.queryForObject("SELECT CURRENT_WAREHOUSE()", String.class);
    }


    public List<Map<String, Object>> fetchCoasByLot(String lotNumber) {

        String sql = """
             SELECT
                                                              c.FILE_NAME                            AS PRODUCT_NAME,
                                                              c.BATCH_LOT_CODE                       AS LOT_NUMBER,
                                                              'FINISHED'                             AS PRODUCT_TYPE,
                                                              c.FILE_URL                             AS COA_PDF_URL,
                                                              TRUE                                   AS PASSED,
                                                              c.PDF_ID,
                                                              p.FG_INVENTORYID,
                                                              p.FG_DESCRIPTION,
                                                              p.FG_QTY_ON_HAND,
                                                              p.FG_TOTAL_VALUE,
                                                              p.MATL_INVENTORYID,
                                                              p.MATL_DESCRIPTION,
                                                              p.MATL_QTY_ON_HAND,
                                                              p.MATL_QTY_AVAILABLE,
                                                              p.QTYREQUIRED,
                                                              p.BUILDQTY_BY_MATERIAL,
                                                              p.BUILDQTY_FG,
                                                              p.MATL_EXPIRYDATE,
                                                              c.PRODUCTION_RECORD_ID,
                                                              c.LAST_MODIFIED
                                                            FROM GOLD_DB.GOLD_MASTER_CONTROL.COA_LIBRARY c
                                                            LEFT JOIN GOLD_DB.GOLD_ACUMATICA.PRODUCT_MATL_INVENTORY p
                                                              -- try matching by FG inventory id OR by lot code if your inventory stores lot as FG_INVENTORYID
                                                              ON (c.BATCH_LOT_CODE = p.FG_INVENTORYID)
                                                                 OR (c.BATCH_LOT_CODE = p.MATL_INVENTORYID)
                                                            WHERE c.BATCH_LOT_CODE = ?
                                                            ORDER BY c.LAST_MODIFIED DESC;
                
        """;

        return jdbcTemplate.queryForList(sql, lotNumber);
    }
}

