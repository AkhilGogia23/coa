package com.example.coa.app.mapper;

import com.example.coa.app.dto.InventoryRecord;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.math.BigDecimal;



public class InventoryRowMapper implements RowMapper<InventoryRecord> {

    @Override
    public InventoryRecord mapRow(ResultSet rs, int rowNum) throws SQLException {

        InventoryRecord record = new InventoryRecord();

        record.setFgInventoryId(rs.getString("FG_INVENTORYID"));
        record.setFgDescription(rs.getString("FG_DESCRIPTION"));
        record.setMatlDescription(rs.getString("MATL_DESCRIPTION"));
        record.setManufacturer(rs.getString("MATL_DESCRIPTION"));
        record.setIngredientInventoryId(rs.getString("MATL_INVENTORYID"));
        record.setBlendQty(
                rs.getBigDecimal("QTYREQUIRED") != null
                        ? rs.getBigDecimal("QTYREQUIRED").toPlainString()
                        : null
        );
        // QTYREQUIRED can be null
        BigDecimal qty = rs.getBigDecimal("QTYREQUIRED");
        record.setQtyRequired(qty);


        // Handle DATE safely
        if (rs.getDate("MATL_EXPIRYDATE") != null) {
            record.setMatlExpiryDate(
                    rs.getDate("MATL_EXPIRYDATE").toLocalDate()
            );
        }

        return record;
    }
}