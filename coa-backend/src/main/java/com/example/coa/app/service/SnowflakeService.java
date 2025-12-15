//package com.example.coa.app.service;
//
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class SnowflakeService {
//
//    private final JdbcTemplate jdbcTemplate;
//
//    public SnowflakeService(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    public String testConnection() {
//        return jdbcTemplate.queryForObject("SELECT CURRENT_WAREHOUSE()", String.class);
//    }
//
//
//    /**
//     * Fetch COA records for a specific lot number from Snowflake database.
//     *
//     * @param lotNumber the lot number to query
//     * @return List of rows from Snowflake where each row is a Map of columnName -> value
//     */
//    public List<Map<String, Object>> fetchCoasByLot(String lotNumber) {
//        // TODO: Update the table & column names once client shares Snowflake schema
//
//        String sql = """
//            SELECT
//                PRODUCT_NAME,
//                LOT_NUMBER,
//                PRODUCT_TYPE,
//                COA_PDF_URL,
//                PASSED
//            FROM COA_TABLE
//            WHERE LOT_NUMBER = ?
//        """;
//
//        return jdbcTemplate.queryForList(sql, lotNumber);
//    }
//}


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

    /**
     * Fetch COA records for a specific lot number from Snowflake database.
     *
     * @param lotNumber the lot number to query
     * @return List of rows from Snowflake where each row is a Map of columnName -> value
     */
    public List<Map<String, Object>> fetchCoasByLot(String lotNumber) {

        String sql = """
        SELECT
            FILE_NAME AS PRODUCT_NAME,
            BATCH_LOT_CODE AS LOT_NUMBER,
            'FINISHED' AS PRODUCT_TYPE,
            FILE_URL AS COA_PDF_URL,
            TRUE AS PASSED
        FROM GOLD_DB.GOLD_MASTER_CONTROL.COA_LIBRARY
        WHERE BATCH_LOT_CODE = ?
    """;

        // --- FULL QUERY WITH RESTRICTED FIELDS (COMMENTED OUT UNTIL ACCESS IS GRANTED) ---

//        String sql = """
// SELECT
//            c.FILE_NAME                       AS PRODUCT_NAME,
//            c.BATCH_LOT_CODE                  AS LOT_NUMBER,
//            'FINISHED'                        AS PRODUCT_TYPE,
//            c.FILE_URL                        AS COA_PDF_URL,
//            TRUE                              AS PASSED,
//            c.LAST_MODIFIED,
//
//            -- mapped fields from PRODUCT_MATL_INVENTORY (alias p)
//            p.FG_INVENTORYID                  AS SKU,
//            p.MATL_DESCRIPTION                AS MANUFACTURER,
//            p.MATL_EXPIRYDATE                 AS EXPIRY,
//            p.MATL_INVENTORYID                AS INGREDIENTS,
//            p.QTYREQUIRED                     AS BLEND,
//            p.FG_DESCRIPTION                  AS FINISHED_DESCRIPTION,
//            p.FG_DESCRIPTION                  AS PRODUCT_DESCRIPTION
//
//        FROM GOLD_DB.GOLD_MASTER_CONTROL.COA_LIBRARY c
//        LEFT JOIN GOLD_DB.GOLD_ACUMATICA.PRODUCT_MATL_INVENTORY p
//            ON c.BATCH_LOT_CODE = p.FG_INVENTORYID
//            OR c.BATCH_LOT_CODE = p.MATL_INVENTORYID
//        WHERE c.BATCH_LOT_CODE = ?
//        ORDER BY c.LAST_MODIFIED DESC
//""";



        return jdbcTemplate.queryForList(sql, lotNumber);
    }

}

